package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.gui.widgets.ButtonBase;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class VoiceChatScreenBase extends Screen {

    public static final int FONT_COLOR = 4210752;

    protected List<HoverArea> hoverAreas;
    protected int guiLeft;
    protected int guiTop;
    protected int xSize;
    protected int ySize;
    protected String title;

    protected VoiceChatScreenBase(String title, int xSize, int ySize) {
        this.title = title;
        this.xSize = xSize;
        this.ySize = ySize;
        this.hoverAreas = new ArrayList<>();
    }

    @Override
    public void init() {
        buttons.clear();
        super.init();

        this.guiLeft = (width - this.xSize) / 2;
        this.guiTop = (height - this.ySize) / 2;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        renderBackground();
        GL11.glColor4f(1F, 1F, 1F, 1F);
        renderBackground(mouseX, mouseY, delta);
        super.render(mouseX, mouseY, delta);
        renderForeground(mouseX, mouseY, delta);
        for (ButtonWidget button : (List<ButtonWidget>) buttons) {
            if (button instanceof ButtonBase) {
                ((ButtonBase) button).renderTooltips(mouseX, mouseY, delta);
            }
        }
    }

    public void renderBackground(int mouseX, int mouseY, float delta) {

    }

    public void renderForeground(int mouseX, int mouseY, float delta) {

    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        super.buttonClicked(button);

        if (!(button instanceof ButtonBase)) {
            return;
        }

        ButtonBase b = (ButtonBase) button;
        b.onPress();
    }

    public int getGuiLeft() {
        return guiLeft;
    }

    public int getGuiTop() {
        return guiTop;
    }

    protected boolean isIngame() {
        return minecraft.world != null;
    }

    protected int getFontColor() {
        return isIngame() ? FONT_COLOR : 0xFFFFFF;
    }

    public void drawHoverAreas(int mouseX, int mouseY) {
        for (HoverArea hoverArea : hoverAreas) {
            if (hoverArea.tooltip != null && hoverArea.isHovered(guiLeft, guiTop, mouseX, mouseY)) {
                drawHoveringText(hoverArea.tooltip.get(), mouseX - guiLeft, mouseY - guiTop);
            }
        }
    }

    public void drawHoveringText(String tooltip, int x, int y) {
        minecraft.textRenderer.drawWithShadow(tooltip, x, y, 16777215);
    }

    public void drawHoveringText(List<String> list, int x, int y) {
        for (int i = 0; i < list.size(); i++) {
            minecraft.textRenderer.drawWithShadow(list.get(i), x, y + (i * (int) TextureHelper.FONT_HEIGHT), 16777215);
        }
    }

    public static int color(int alpha, int red, int green, int blue) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    public static class HoverArea {
        private final int posX, posY;
        private final int width, height;
        @Nullable
        private final Supplier<List<String>> tooltip;

        public HoverArea(int posX, int posY, int width, int height) {
            this(posX, posY, width, height, null);
        }

        public HoverArea(int posX, int posY, int width, int height, Supplier<List<String>> tooltip) {
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.tooltip = tooltip;
        }

        public int getPosX() {
            return posX;
        }

        public int getPosY() {
            return posY;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        @Nullable
        public Supplier<List<String>> getTooltip() {
            return tooltip;
        }

        public boolean isHovered(int guiLeft, int guiTop, int mouseX, int mouseY) {
            if (mouseX >= guiLeft + posX && mouseX < guiLeft + posX + width) {
                if (mouseY >= guiTop + posY && mouseY < guiTop + posY + height) {
                    return true;
                }
            }
            return false;
        }
    }

}
