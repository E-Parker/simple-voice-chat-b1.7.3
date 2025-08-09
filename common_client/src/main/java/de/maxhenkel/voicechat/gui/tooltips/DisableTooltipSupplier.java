package de.maxhenkel.voicechat.gui.tooltips;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.gui.widgets.ImageButton;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.ArrayList;
import java.util.List;

public class DisableTooltipSupplier implements ImageButton.TooltipSupplier {

    private final Screen screen;
    private final ClientPlayerStateManager stateManager;

    public DisableTooltipSupplier(Screen screen, ClientPlayerStateManager stateManager) {
        this.screen = screen;
        this.stateManager = stateManager;
    }

    @Override
    public void onTooltip(ImageButton button, int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();

        if (!stateManager.canEnable()) {
            tooltip.add(TranslationStorage.getInstance().get("message.voicechat.disable.no_speaker"));
        } else if (stateManager.isDisabled()) {
            tooltip.add(TranslationStorage.getInstance().get("message.voicechat.disable.enabled"));
        } else {
            tooltip.add(TranslationStorage.getInstance().get("message.voicechat.disable.disabled"));
        }

        Minecraft mc = MinecraftAccessor.getMinecraft();
        for (int i = 0; i < tooltip.size(); i++) {
            String s = tooltip.get(i);
            mc.textRenderer.drawWithShadow(s, mouseX, (int) (i * TextureHelper.FONT_HEIGHT + 2) + mouseY, 16777215);
        }
    }

}
