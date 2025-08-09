package de.maxhenkel.voicechat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;

import java.util.List;
import java.util.UUID;

public interface VoicechatServer {
    void sendToPlayer(PlayerEntity player, Packet packet);
    PlayerEntity getPlayerByName(String name);
    PlayerEntity getPlayerByUuid(UUID uuid);
    List<PlayerEntity> getPlayerList();
    String getServerIp();
}
