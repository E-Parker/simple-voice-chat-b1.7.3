package de.maxhenkel.voicechat.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.opengl.GL11;

public class GuiSliderModern extends ButtonWidget {
    private float sliderPosition = 1.0F;
    public boolean isMouseDown;
    private final String name;
    private final float min;
    private final float max;
    private final GuiPageButtonList.GuiResponder responder;
    private FormatHelper formatHelper;

    public GuiSliderModern(GuiPageButtonList.GuiResponder guiResponder, int idIn, int x, int y, String nameIn, float minIn, float maxIn, float defaultValue, FormatHelper formatter)
    {
        super(idIn, x, y, 150, 20, "");
        this.name = nameIn;
        this.min = minIn;
        this.max = maxIn;
        this.sliderPosition = (defaultValue - minIn) / (maxIn - minIn);
        this.formatHelper = formatter;
        this.responder = guiResponder;
        this.text = this.getDisplayString();
    }

    public float getSliderValue()
    {
        return this.min + (this.max - this.min) * this.sliderPosition;
    }

    public void setSliderValue(float value, boolean notifyResponder)
    {
        this.sliderPosition = (value - this.min) / (this.max - this.min);
        this.text = this.getDisplayString();

        if (notifyResponder)
        {
            this.responder.setEntryValue(this.id, this.getSliderValue());
        }
    }

    public float getSliderPosition()
    {
        return this.sliderPosition;
    }

    private String getDisplayString()
    {
        return this.formatHelper == null ? TranslationStorage.getInstance().get(this.name) + ": " + this.getSliderValue() : this.formatHelper.getText(this.id, TranslationStorage.getInstance().get(this.name), this.getSliderValue());
    }

    protected int getYImage(boolean mouseOver)
    {
        return 0;
    }

    protected void renderBackground(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.isMouseDown)
            {
                this.sliderPosition = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);

                if (this.sliderPosition < 0.0F)
                {
                    this.sliderPosition = 0.0F;
                }

                if (this.sliderPosition > 1.0F)
                {
                    this.sliderPosition = 1.0F;
                }

                this.text = this.getDisplayString();
                this.responder.setEntryValue(this.id, this.getSliderValue());
            }

            GL11.glColor4f(1F, 1F, 1F, 1F);
            this.drawTexture(this.x + (int)(this.sliderPosition * (float)(this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexture(this.x + (int)(this.sliderPosition * (float)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    public void setSliderPosition(float position)
    {
        this.sliderPosition = position;
        this.text = this.getDisplayString();
        this.responder.setEntryValue(this.id, this.getSliderValue());
    }

    public boolean isMouseOver(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.isMouseOver(mc, mouseX, mouseY))
        {
            this.sliderPosition = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);

            if (this.sliderPosition < 0.0F)
            {
                this.sliderPosition = 0.0F;
            }

            if (this.sliderPosition > 1.0F)
            {
                this.sliderPosition = 1.0F;
            }

            this.text = this.getDisplayString();
            this.responder.setEntryValue(this.id, this.getSliderValue());
            this.isMouseDown = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void mouseReleased(int mouseX, int mouseY)
    {
        this.isMouseDown = false;
    }

    public interface FormatHelper
    {
        String getText(int id, String name, float value);
    }
}
