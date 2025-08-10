package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.gui.GuiEnhancedControls;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin {
    @Accessor
    public abstract GameOptions getOptions();

    @Redirect(method = "buttonClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 1))
    public void useEnhancedControls(Minecraft instance, Screen guiScreen, ButtonWidget button) {
        instance.setScreen(new GuiEnhancedControls((OptionsScreen) (Object) this, this.getOptions()));
    }
}
