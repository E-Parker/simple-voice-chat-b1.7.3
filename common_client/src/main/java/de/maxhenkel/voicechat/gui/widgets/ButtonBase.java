package de.maxhenkel.voicechat.gui.widgets;

import net.minecraft.client.gui.widget.ButtonWidget;

public abstract class ButtonBase extends ButtonWidget {
    public ButtonBase(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    public abstract void onPress();

    public void renderTooltips(int mouseX, int mouseY, float delta) {

    }

}
