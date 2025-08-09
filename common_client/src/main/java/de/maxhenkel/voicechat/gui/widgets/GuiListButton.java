package de.maxhenkel.voicechat.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;

public class GuiListButton extends ButtonWidget {
    private boolean value;
    private final String localizationStr;
    private final GuiPageButtonList.GuiResponder guiResponder;

    public GuiListButton(GuiPageButtonList.GuiResponder responder, int buttonId, int x, int y, String localizationStrIn, boolean valueIn)
    {
        super(buttonId, x, y, 150, 20, "");
        this.localizationStr = localizationStrIn;
        this.value = valueIn;
        this.text = this.buildDisplayString();
        this.guiResponder = responder;
    }

    private String buildDisplayString()
    {
        return TranslationStorage.getInstance().get(this.localizationStr) + ": " + TranslationStorage.getInstance().get(this.value ? "gui.yes" : "gui.no");
    }

    public void setValue(boolean valueIn)
    {
        this.value = valueIn;
        this.text = this.buildDisplayString();
        this.guiResponder.setEntryValue(this.id, valueIn);
    }

    public boolean isMouseOver(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.isMouseOver(mc, mouseX, mouseY))
        {
            this.value = !this.value;
            this.text = this.buildDisplayString();
            this.guiResponder.setEntryValue(this.id, this.value);
            return true;
        }
        else
        {
            return false;
        }
    }
}
