package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.intercompatibility.FabricClientCompatibilityManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "renderNameTag(Lnet/minecraft/entity/LivingEntity;Ljava/lang/String;DDDI)V", at = @At("HEAD"))
    private void renderNameTag(LivingEntity name, String dx, double dy, double dz, double range, int maxDistance, CallbackInfo ci) {
        if (ci.isCancelled()) {
            return;
        }
        if (name == null) {
            return;
        }

        FabricClientCompatibilityManager.getInstance().onRenderName(name, dx, dy, dz, range, maxDistance);
    }
}
