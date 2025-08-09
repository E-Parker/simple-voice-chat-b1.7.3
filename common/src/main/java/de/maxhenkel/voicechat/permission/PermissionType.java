package de.maxhenkel.voicechat.permission;

import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public enum PermissionType {

    EVERYONE, NOONE, OPS;

    boolean hasPermission(@Nullable PlayerEntity player) {
        switch (this) {
            case EVERYONE:
                return true;
            default:
            case NOONE:
                return false;
            case OPS:
                return false;
        }
    }

}
