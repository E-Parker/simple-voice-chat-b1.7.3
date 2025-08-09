package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.gui.GameProfileUtils;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
public class TextureManagerMixin {
    @Inject(method = "reload", at = @At("HEAD"))
    public void resetTextureHelper(CallbackInfo ci) {
        TextureHelper.refreshTextures();
        GameProfileUtils.refreshTextures();
    }
}
