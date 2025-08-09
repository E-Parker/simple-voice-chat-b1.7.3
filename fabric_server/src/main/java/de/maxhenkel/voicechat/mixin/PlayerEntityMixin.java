package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.PlayerEntityExtension;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityExtension {
    @Accessor
    public abstract String getName();

    private UUID uuid = null;

    @Override
    public UUID getUniqueID() {
        if (uuid == null)
            uuid = UUID.nameUUIDFromBytes(this.getName().getBytes(StandardCharsets.UTF_8));
        return uuid;
    }
}

