package de.maxhenkel.voicechat.permission;

import net.minecraft.entity.player.PlayerEntity;

public interface Permission {

    boolean hasPermission(PlayerEntity player);

    PermissionType getPermissionType();

}
