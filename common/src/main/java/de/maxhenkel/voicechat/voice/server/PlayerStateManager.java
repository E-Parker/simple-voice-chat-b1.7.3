package de.maxhenkel.voicechat.voice.server;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.PlayerEntityExtension;
import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
import de.maxhenkel.voicechat.net.NetManager;
import de.maxhenkel.voicechat.net.PlayerStatePacket;
import de.maxhenkel.voicechat.net.PlayerStatesPacket;
import de.maxhenkel.voicechat.plugins.PluginManager;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerStateManager {

    private final ConcurrentHashMap<UUID, PlayerState> states;
    private final Server voicechatServer;

    public PlayerStateManager(Server voicechatServer) {
        this.voicechatServer = voicechatServer;
        this.states = new ConcurrentHashMap<>();
        CommonCompatibilityManager.INSTANCE.onPlayerLoggedIn(this::onPlayerLoggedIn);
        CommonCompatibilityManager.INSTANCE.onPlayerLoggedOut(this::onPlayerLoggedOut);
        CommonCompatibilityManager.INSTANCE.onServerVoiceChatConnected(this::onPlayerVoicechatConnect);
        CommonCompatibilityManager.INSTANCE.onServerVoiceChatDisconnected(this::onPlayerVoicechatDisconnect);
        CommonCompatibilityManager.INSTANCE.onPlayerCompatibilityCheckSucceeded(this::onPlayerCompatibilityCheckSucceeded);

        CommonCompatibilityManager.INSTANCE.getNetManager().updateStateChannel.setServerListener((player, handler, packet) -> {
            PlayerState state = states.get(((PlayerEntityExtension) player).getUniqueID());

            if (state == null) {
                state = defaultDisconnectedState(player);
            }

            state.setDisabled(packet.isDisabled());

            states.put(((PlayerEntityExtension) player).getUniqueID(), state);

            broadcastState(state);
            Voicechat.logDebug("Got state of {}: {}", player.name, state);
        });
    }

    public void broadcastState(PlayerState state) {
        PlayerStatePacket packet = new PlayerStatePacket(state);
        Voicechat.serverInstance.getPlayerList().forEach(p -> NetManager.sendToClient(p, packet));
        PluginManager.instance().onPlayerStateChanged(state);
    }

    private void onPlayerCompatibilityCheckSucceeded(PlayerEntity player) {
        PlayerStatesPacket packet = new PlayerStatesPacket(states);
        NetManager.sendToClient(player, packet);
        Voicechat.logDebug("Sending initial states to {}", player.name);
    }

    private void onPlayerLoggedIn(PlayerEntity player) {
        PlayerState state = defaultDisconnectedState(player);
        states.put(((PlayerEntityExtension) player).getUniqueID(), state);
        broadcastState(state);
        Voicechat.logDebug("Setting default state of {}: {}", player.name, state);
    }

    private void onPlayerLoggedOut(PlayerEntity player) {
        states.remove(((PlayerEntityExtension) player).getUniqueID());
        broadcastState(new PlayerState(((PlayerEntityExtension) player).getUniqueID(), player.name, false, true));
        Voicechat.logDebug("Removing state of {}", player.name);
    }

    private void onPlayerVoicechatDisconnect(UUID uuid) {
        PlayerState state = states.get(uuid);
        if (state == null) {
            return;
        }

        state.setDisconnected(true);

        broadcastState(state);
        Voicechat.logDebug("Set state of {} to disconnected: {}", uuid, state);
    }

    private void onPlayerVoicechatConnect(PlayerEntity player) {
        PlayerState state = states.get(((PlayerEntityExtension) player).getUniqueID());

        if (state == null) {
            state = defaultDisconnectedState(player);
        }

        state.setDisconnected(false);

        states.put(((PlayerEntityExtension) player).getUniqueID(), state);

        broadcastState(state);
        Voicechat.logDebug("Set state of {} to connected: {}", player.name, state);
    }

    @Nullable
    public PlayerState getState(UUID playerUUID) {
        return states.get(playerUUID);
    }

    public static PlayerState defaultDisconnectedState(PlayerEntity player) {
        return new PlayerState(((PlayerEntityExtension) player).getUniqueID(), player.name, false, true);
    }

    public void setGroup(PlayerEntity player, @Nullable UUID group) {
        PlayerState state = states.get(((PlayerEntityExtension) player).getUniqueID());
        if (state == null) {
            state = PlayerStateManager.defaultDisconnectedState(player);
            Voicechat.logDebug("Defaulting to default state for {}: {}", player.name, state);
        }
        state.setGroup(group);
        states.put(((PlayerEntityExtension) player).getUniqueID(), state);
        broadcastState(state);
        Voicechat.logDebug("Setting group of {}: {}", player.name, state);
    }

    public Collection<PlayerState> getStates() {
        return states.values();
    }

}
