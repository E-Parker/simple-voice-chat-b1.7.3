package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.intercompatibility.FabricClientCompatibilityManager;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void renderVoiceChatOverlays(float bl, boolean i, int j, int par4, CallbackInfo ci) {
        FabricClientCompatibilityManager.getInstance().onRenderHUD(bl);
    }
}
