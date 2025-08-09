package de.maxhenkel.voicechat.gui.tooltips;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.gui.widgets.ImageButton;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.ArrayList;
import java.util.List;

public class RecordingTooltipSupplier implements ImageButton.TooltipSupplier {

    private final Screen screen;

    public RecordingTooltipSupplier(Screen screen) {
        this.screen = screen;
    }

    @Override
    public void onTooltip(ImageButton button, int mouseX, int mouseY) {
        ClientVoicechat client = ClientManager.getClient();
        if (client == null) {
            return;
        }

        List<String> tooltip = new ArrayList<>();

        if (client.getRecorder() == null) {
            tooltip.add(TranslationStorage.getInstance().get("message.voicechat.recording.disabled"));
        } else {
            tooltip.add(TranslationStorage.getInstance().get("message.voicechat.recording.enabled"));
        }

        Minecraft mc = MinecraftAccessor.getMinecraft();
        for (int i = 0; i < tooltip.size(); i++) {
            String s = tooltip.get(i);
            mc.textRenderer.drawWithShadow(s, mouseX, (int) (i * TextureHelper.FONT_HEIGHT + 2) + mouseY, 16777215);
        }
    }

}
