package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.util.MathHelper2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.lwjgl.opengl.GL11;

public abstract class Slider extends ButtonWidget {

    protected double value;
    private boolean dragging;

    public Slider(int buttonId, int x, int y, int width, int height, double value) {
        super(buttonId, x, y, width, height, "");
        this.value = value;
    }

    @Override
    protected int getYImage(boolean mouseOver) {
        return 0;
    }

    @Override
    protected void renderBackground(Minecraft mc, int mouseX, int mouseY) {
        if (!visible) {
            return;
        }
        if (dragging) {
            updateSliderValue(mouseX, mouseY);
        }

        GL11.glBindTexture(3553, mc.textureManager.getTextureId("/gui/gui.png"));
        GL11.glColor4f(1F, 1F, 1F, 1F);
        drawTexture(x + (int) (value * (float) (width - 8)), y, 0, 66, 4, 20);
        drawTexture(x + (int) (value * (float) (width - 8)) + 4, y, 196, 66, 4, 20);
    }

    @Override
    public boolean isMouseOver(Minecraft mc, int mouseX, int mouseY) {
        if (super.isMouseOver(mc, mouseX, mouseY)) {
            updateSliderValue(mouseX, mouseY);
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    private void updateSliderValue(int mouseX, int mouseY) {
        value = (double) (mouseX - (x + 4)) / (double) (width - 8);
        value = MathHelper2.clamp(value, 0D, 1D);
        updateMessage();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }

    protected abstract void updateMessage();

}
