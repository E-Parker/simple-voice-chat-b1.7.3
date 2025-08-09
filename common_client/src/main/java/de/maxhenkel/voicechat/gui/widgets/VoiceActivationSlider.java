package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.common.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class VoiceActivationSlider extends DebouncedSlider implements MicTestButton.MicListener {

    private static final String SLIDER = TextureHelper.format(Voicechat.MODID, "textures/gui/voice_activation_slider.png");
    private static final String NO_ACTIVATION = "§c" + TranslationStorage.getInstance().get("message.voicechat.voice_activation.disabled");

    private double micValue;

    public VoiceActivationSlider(int id, int x, int y, int width, int height) {
        super(id, x, y, width, height, Utils.dbToPerc(VoicechatClient.CLIENT_CONFIG.voiceActivationThreshold.get().floatValue()));
        updateMessage();
    }

    @Override
    public void renderBackground(Minecraft mc, int mouseX, int mouseY) {
        super.renderBackground(mc, mouseX, mouseY);
        TextureHelper.bindTexture(SLIDER);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        int width = (int) (226D * micValue);
        drawTexture(x + 1, y + 1, 0, 0, width, 18);
    }

    @Override
    protected void updateMessage() {
        long db = Math.round(Utils.percToDb(value));
        String component = String.format(TranslationStorage.getInstance().get("message.voicechat.voice_activation"), db);

        if (db >= -10L) {
            component = "§c" + component;
        }

        text = component;
    }

    @Nullable
    public String getTooltip() {
        if (value >= 1D) {
            return NO_ACTIVATION;
        }
        return null;
    }

    @Override
    public void applyDebounced() {
        VoicechatClient.CLIENT_CONFIG.voiceActivationThreshold.set(Utils.percToDb(value)).save();
    }

    @Override
    public void onMicValue(double percentage) {
        this.micValue = percentage;
    }
}
