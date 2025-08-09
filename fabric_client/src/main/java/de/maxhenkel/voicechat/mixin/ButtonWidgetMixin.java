package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.ButtonWidgetExtension;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ButtonWidget.class)
public abstract class ButtonWidgetMixin implements ButtonWidgetExtension {
    @Accessor
    public abstract void setWidth(int width);

    @Accessor
    public abstract void setHeight(int height);

    @Accessor
    public abstract int getWidth();

    @Accessor
    public abstract int getHeight();
}
