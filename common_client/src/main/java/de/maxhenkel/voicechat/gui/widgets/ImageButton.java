package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.extensions.DrawContextExtension;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.Minecraft;

public class ImageButton extends ButtonBase {

    protected Minecraft mc;
    protected String texture;
    protected PressAction onPress;
    protected TooltipSupplier tooltipSupplier;

    public ImageButton(int id, int x, int y, String texture, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(id, x, y, 20, 20, "");
        mc = MinecraftAccessor.getMinecraft();
        this.texture = texture;
        this.onPress = onPress;
        this.tooltipSupplier = tooltipSupplier;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    protected void renderImage(int mouseX, int mouseY) {
        // oh.

        TextureHelper.bindTexture(texture);
        ((DrawContextExtension) this).drawModalRectWithCustomSizedTexture(x + 2, y + 2, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public void render(Minecraft minecraft, int mouseX, int mouseY) {
        super.render(minecraft, mouseX, mouseY);
        renderImage(mouseX, mouseY);
    }

    @Override
    public void renderTooltips(int mouseX, int mouseY, float delta) {
        if (false) {
            tooltipSupplier.onTooltip(this, mouseX, mouseY);
        }
    }

    public interface TooltipSupplier {
        void onTooltip(ImageButton button, int mouseX, int mouseY);
    }

    public interface PressAction {
        void onPress(ImageButton button);
    }

}
