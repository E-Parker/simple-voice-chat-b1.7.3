package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.DrawContextExtension;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DrawContext.class)
public class DrawContextMixin implements DrawContextExtension {
    @Override
    public void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        DrawContextExtension.staticDrawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
    }

    @Override
    public void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        DrawContextExtension.staticDrawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
    }
}
