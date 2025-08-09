package de.maxhenkel.voicechat.plugins.impl;

import de.maxhenkel.voicechat.api.ServerLevel;
import de.maxhenkel.voicechat.api.ServerPlayer;
import net.minecraft.entity.player.PlayerEntity;

public class ServerPlayerImpl extends PlayerImpl implements ServerPlayer {

    public ServerPlayerImpl(PlayerEntity entity) {
        super(entity);
    }

    public PlayerEntity getRealServerPlayer() {
        return (PlayerEntity) entity;
    }

    @Override
    public ServerLevel getServerLevel() {
        return new ServerLevelImpl(entity.world);
    }
}
