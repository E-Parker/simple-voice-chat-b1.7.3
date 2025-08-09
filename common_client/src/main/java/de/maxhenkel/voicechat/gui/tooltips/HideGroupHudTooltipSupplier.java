package de.maxhenkel.voicechat.gui.tooltips;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.gui.widgets.ImageButton;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.ArrayList;
import java.util.List;

public class HideGroupHudTooltipSupplier implements ImageButton.TooltipSupplier {

    private final Screen screen;

    public HideGroupHudTooltipSupplier(Screen screen) {
        this.screen = screen;
    }

    @Override
    public void onTooltip(ImageButton button, int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();

        if (VoicechatClient.CLIENT_CONFIG.showGroupHUD.get()) {
            tooltip.add(TranslationStorage.getInstance().get("message.voicechat.show_group_hud.enabled"));
        } else {
            tooltip.add(TranslationStorage.getInstance().get("message.voicechat.show_group_hud.disabled"));
        }

        Minecraft mc = MinecraftAccessor.getMinecraft();
        for (int i = 0; i < tooltip.size(); i++) {
            String s = tooltip.get(i);
            mc.textRenderer.drawWithShadow(s, mouseX, (int) (i * TextureHelper.FONT_HEIGHT + 2) + mouseY, 16777215);
        }
    }

}
