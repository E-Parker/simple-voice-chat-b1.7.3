package de.maxhenkel.voicechat.plugins.impl;

import de.maxhenkel.voicechat.api.Entity;
import de.maxhenkel.voicechat.api.Position;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class EntityImpl implements Entity {
    private final UUID uuid = UUID.randomUUID();


    protected net.minecraft.entity.Entity entity;

    public EntityImpl(net.minecraft.entity.Entity entity) {
        this.entity = entity;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public Position getPosition() {
        return new PositionImpl(Vec3d.createCached(entity.x, entity.y, entity.z));
    }

    public net.minecraft.entity.Entity getRealEntity() {
        return entity;
    }

}
