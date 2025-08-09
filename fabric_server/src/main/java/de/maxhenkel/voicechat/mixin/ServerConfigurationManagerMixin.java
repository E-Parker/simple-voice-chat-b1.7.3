package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.intercompatibility.FabricCommonCompatibilityManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class ServerConfigurationManagerMixin {
    @Inject(method = "addPlayer", at = @At("HEAD"))
    public void playerLogIn(ServerPlayerEntity par1, CallbackInfo ci) {
        FabricCommonCompatibilityManager.instance.onPlayerLogIn(par1);
    }

    @Inject(method = "disconnect", at = @At("HEAD"))
    public void playerLogOut(ServerPlayerEntity par1, CallbackInfo ci) {
        FabricCommonCompatibilityManager.instance.onPlayerLogOut(par1);
    }

    @Inject(method = "respawnPlayer", at = @At("HEAD"))
    public void onPlayerDeath(ServerPlayerEntity par1, int dimensionId, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        FabricCommonCompatibilityManager.instance.onPlayerLogOut(par1);
    }

    @Inject(method = "respawnPlayer", at = @At("RETURN"))
    public void onPlayerRespawn(ServerPlayerEntity par1, int dimensionId, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        FabricCommonCompatibilityManager.instance.onPlayerLogIn(cir.getReturnValue());
    }
}
