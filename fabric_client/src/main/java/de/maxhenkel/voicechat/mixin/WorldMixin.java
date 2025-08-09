package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.intercompatibility.FabricClientCompatibilityManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
    @Inject(method = "spawnEntity", at = @At("HEAD"))
    public void spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        FabricClientCompatibilityManager.getInstance().spawnEntity(entity);
    }
}
