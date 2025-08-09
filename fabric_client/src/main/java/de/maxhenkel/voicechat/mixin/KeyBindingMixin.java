package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.extensions.KeyBindingExtension;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements KeyBindingExtension {
    @Accessor
    public abstract int getCode();

    private boolean wasPressed = false;

    @Override
    public boolean isPressed() {
        if (MinecraftAccessor.getMinecraft().currentScreen != null)
            return false;

        if (this.getCode() == Keyboard.KEY_NONE)
            return false;

        boolean isPressed = Keyboard.getEventKey() == this.getCode();

        if (isPressed && !wasPressed) {
            wasPressed = true;
            return true;
        } else if (!isPressed && wasPressed) {
            wasPressed = false;
            return false;
        }

        return false;
    }

    @Override
    public boolean isHeld() {
        if (MinecraftAccessor.getMinecraft().currentScreen != null)
            return false;

        return Keyboard.getEventKey() == this.getCode();
    }
}
