package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.intercompatibility.FabricClientCompatibilityManager;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "tickInput", at = @At("HEAD"))
    public void mouseTick(CallbackInfo ci) {
        FabricClientCompatibilityManager.getInstance().onTickMouse();
    }

    @Inject(method = "tickInput", at = @At("TAIL"))
    public void keyboardTick(CallbackInfo ci) {
        FabricClientCompatibilityManager.getInstance().onTickKey();
    }
}
