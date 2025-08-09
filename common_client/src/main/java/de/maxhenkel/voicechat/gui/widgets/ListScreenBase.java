package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.gui.VoiceChatScreenBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;

public abstract class ListScreenBase extends VoiceChatScreenBase {

    private Runnable postRender;
    private ListScreenListBase<?> list;

    public ListScreenBase(String title, int xSize, int ySize) {
        super(title, xSize, ySize);
    }

    @Override
    public void onMouseEvent() {
        super.onMouseEvent();
        if (list != null) {
            list.handleMouseInput();
        }
    }

    protected void buttonClicked(ButtonWidget button) {
        super.buttonClicked(button);
        if (list != null) {
            list.buttonClicked(button);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
        if (list != null) {
            list.drawScreen(mouseX, mouseY, delta);
        }
        if (postRender != null) {
            postRender.run();
            postRender = null;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (list != null) {
            list.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (list != null) {
            list.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void init(Minecraft mcIn, int w, int h) {
        super.init(mcIn, w, h);
    }

    public void setList(ListScreenListBase<?> list) {
        this.list = list;
    }

    public void removeList() {
        this.list = null;
    }

    public void postRender(Runnable postRender) {
        this.postRender = postRender;
    }

}
