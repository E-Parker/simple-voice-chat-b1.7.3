package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.Voicechat;
import net.minecraft.client.resource.pack.ZippedTexturePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;

@Mixin(ZippedTexturePack.class)
public class TexturePackCustomMixin {
    @Inject(method = "getResource", at = @At("RETURN"), cancellable = true)
    public void checkVoiceChatTexturesIfNull(String par1, CallbackInfoReturnable<InputStream> cir) {
        if (cir.getReturnValue() == null) {
            cir.setReturnValue(Voicechat.class.getResourceAsStream(par1));
        }
    }
}
