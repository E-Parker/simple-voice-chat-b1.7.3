package de.maxhenkel.voicechat.voice.server;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.PlayerEntityExtension;
import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
import de.maxhenkel.voicechat.net.NetManager;
import de.maxhenkel.voicechat.net.SecretPacket;
import de.maxhenkel.voicechat.plugins.PluginManager;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerVoiceEvents {

    private final Map<UUID, Integer> clientCompatibilities;
    private Server server;

    public ServerVoiceEvents() {
        clientCompatibilities = new ConcurrentHashMap<>();
        CommonCompatibilityManager.INSTANCE.onServerStarting(this::serverStarting);
        CommonCompatibilityManager.INSTANCE.onPlayerLoggedIn(this::playerLoggedIn);
        CommonCompatibilityManager.INSTANCE.onPlayerLoggedOut(this::playerLoggedOut);
        CommonCompatibilityManager.INSTANCE.onServerStopping(this::serverStopping);

        CommonCompatibilityManager.INSTANCE.getNetManager().requestSecretChannel.setServerListener((player, handler, packet) -> {
            Voicechat.LOGGER.info("Received secret request of {} ({})", player.name, packet.getCompatibilityVersion());
            clientCompatibilities.put(((PlayerEntityExtension) player).getUniqueID(), packet.getCompatibilityVersion());
            if (packet.getCompatibilityVersion() != Voicechat.COMPATIBILITY_VERSION) {
                Voicechat.LOGGER.warn("Connected client {} has incompatible voice chat version (server={}, client={})", player.name, Voicechat.COMPATIBILITY_VERSION, packet.getCompatibilityVersion());
                //player.sendMessage(getIncompatibleMessage(packet.getCompatibilityVersion()));
            } else {
                initializePlayerConnection(player);
            }
        });
    }

    public boolean isCompatible(PlayerEntity player) {
        return isCompatible(((PlayerEntityExtension) player).getUniqueID());
    }

    public boolean isCompatible(UUID playerUuid) {
        return clientCompatibilities.getOrDefault(playerUuid, -1) == Voicechat.COMPATIBILITY_VERSION;
    }

    public void serverStarting(Object no) {
        if (server != null) {
            server.close();
            server = null;
        }

        if (Voicechat.clientInstance != null) {
            Voicechat.LOGGER.info("Disabling voice chat in singleplayer");
            return;
        }

        /*if (mcServer.isDedicatedServer()) {
            if (!mcServer.isServerInOnlineMode()) {
                Voicechat.LOGGER.warn("Running in offline mode - Voice chat encryption is not secure!");
            }
        }*/

        try {
            server = new Server();
            server.start();
            PluginManager.instance().onServerStarted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializePlayerConnection(PlayerEntity player) {
        if (server == null) {
            return;
        }
        CommonCompatibilityManager.INSTANCE.emitPlayerCompatibilityCheckSucceeded(player);

        UUID secret = server.getSecret(((PlayerEntityExtension) player).getUniqueID());
        NetManager.sendToClient(player, new SecretPacket(player, secret, server.getPort(), Voicechat.SERVER_CONFIG));
        Voicechat.LOGGER.info("Sent secret to {}", player.name);
    }

    public void playerLoggedIn(PlayerEntity serverPlayer) {
        if (!Voicechat.SERVER_CONFIG.forceVoiceChat.get()) {
            return;
        }
    }

    public void playerLoggedOut(PlayerEntity player) {
        clientCompatibilities.remove(((PlayerEntityExtension) player).getUniqueID());
        if (server == null) {
            return;
        }

        server.disconnectClient(((PlayerEntityExtension) player).getUniqueID());
        Voicechat.LOGGER.info("Disconnecting client {}", player.name);
    }

    @Nullable
    public Server getServer() {
        return server;
    }

    public void serverStopping(Object no) {
        if (server != null) {
            server.close();
            server = null;
        }
    }

}
