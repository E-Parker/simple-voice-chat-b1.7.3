package de.maxhenkel.voicechat;

import de.maxhenkel.voicechat.extensions.PlayerEntityExtension;
import de.maxhenkel.voicechat.mixin.ConnectionListenerAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class VoicechatServerImpl implements VoicechatServer {
    public static VoicechatServerImpl instance;

    private final MinecraftServer server;
    private final HashMap<UUID, String> uuidToUsernameMap = new HashMap<>();

    public VoicechatServerImpl(MinecraftServer server) {
        instance = this;
        this.server = server;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    @Override
    public void sendToPlayer(PlayerEntity player, Packet packet) {
        server.playerManager.sendPacket(player.name, packet);
    }

    @Override
    public PlayerEntity getPlayerByName(String name) {
        return server.playerManager.getPlayer(name);
    }

    @Override
    public PlayerEntity getPlayerByUuid(UUID uuid) {
        if (uuidToUsernameMap.containsKey(uuid))
            return getPlayerByName(uuidToUsernameMap.get(uuid));

        PlayerEntity player = null;
        for (PlayerEntity playerEntity : (List<PlayerEntity>) server.playerManager.players) {
            if (((PlayerEntityExtension) playerEntity).getUniqueID().equals(uuid)) {
                player = playerEntity;
                break;
            }
        }

        if (player == null)
            return null;

        uuidToUsernameMap.put(uuid, player.name);
        return player;
    }

    @Override
    public List<PlayerEntity> getPlayerList() {
        return server.playerManager.players;
    }

    @Override
    public String getServerIp() {
        return ((ConnectionListenerAccessor) server.connections).getSocket().getInetAddress().getHostAddress();
    }
}
