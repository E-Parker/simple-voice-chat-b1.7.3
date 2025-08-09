package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.TextRendererExtension;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TextRenderer.class)
public class TextRendererMixin implements TextRendererExtension {
}
