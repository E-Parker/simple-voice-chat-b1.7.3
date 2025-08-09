package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.FabricVoicechatClientMod;
import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.intercompatibility.FabricClientCompatibilityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "init", at = @At("TAIL"))
    public void assignMinecraft(CallbackInfo ci) {
        MinecraftAccessor.setInstance((Minecraft) (Object) this);
        FabricVoicechatClientMod.instance.initializeClient();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void postTick(CallbackInfo ci) {
        FabricClientCompatibilityManager.getInstance().onInput();
    }

    @Inject(method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ClientPlayerEntity;teleportTop()V", ordinal = 0))
    public void joinWorld(World string, String entityPlayer, PlayerEntity par3, CallbackInfo ci) {
        FabricClientCompatibilityManager.getInstance().onJoinServer();
    }

    @Inject(method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"))
    public void disconnectEvent(World world, String entityPlayer, PlayerEntity par3, CallbackInfo ci) {
        if (world == null) {
            FabricClientCompatibilityManager.getInstance().onDisconnect();
        }
    }
}
