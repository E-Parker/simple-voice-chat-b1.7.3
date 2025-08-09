package de.maxhenkel.voicechat.extensions;


import net.minecraft.client.render.Tessellator;

public interface DrawContextExtension {
    void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight);
    void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight);

    static void staticDrawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float textureU = 1F / textureWidth;
        float textureV = 1F / textureHeight;

        Tessellator tessellator = Tessellator.INSTANCE;

        tessellator.start(7);
        tessellator.vertex(x, y + height, 0f, u * textureU, (v + height) * textureV);
        tessellator.vertex(x + width, y + height, 0f, (u + width) * textureU, (v + height) * textureV);
        tessellator.vertex(x + width, y, 0f, (u + width) * textureU, v * textureV);
        tessellator.vertex(x, y, 0f, u * textureU, v * textureV);
        tessellator.draw();
    }

    static void staticDrawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float tileU = 1F / tileWidth;
        float tileV = 1F / tileHeight;

        Tessellator tessellator = Tessellator.INSTANCE;

        tessellator.start(7);

        tessellator.vertex(x, y + height, 0f, u * tileU, (v + vHeight) * tileV);
        tessellator.vertex(x + width, y + height, 0f, (u + uWidth) * tileU, (v + vHeight) * tileV);
        tessellator.vertex(x + width, y, 0f, (u + uWidth) * tileU, v * tileV);
        tessellator.vertex(x, y, 0f, u * tileU, v * tileV);

        tessellator.draw();
    }
}
