package de.maxhenkel.voicechat.gui.widgets;

import com.google.common.collect.Lists;
import de.maxhenkel.voicechat.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.List;

public abstract class ListScreenEntryBase implements GuiListExtended.IGuiListEntry {

    protected final Minecraft mc;
    protected final List<ButtonWidget> children;
    protected boolean selected;

    public ListScreenEntryBase() {
        this.children = Lists.newArrayList();
        mc = MinecraftAccessor.getMinecraft();
    }

    public List<? extends ButtonWidget> children() {
        return children;
    }

    @Override
    public void updatePosition(int slotIndex, int x, int y, float partialTicks) {

    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        selected = isSelected;
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        for (ButtonWidget button : children) {
            if (button.isMouseOver(mc, mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        for (ButtonWidget button : children) {
            button.mouseReleased(x, y);
        }
    }

    public boolean isSelected() {
        return selected;
    }
}
