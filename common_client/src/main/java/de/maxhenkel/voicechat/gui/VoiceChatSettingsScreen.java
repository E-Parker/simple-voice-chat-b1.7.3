package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.gui.audiodevice.SelectMicrophoneScreen;
import de.maxhenkel.voicechat.gui.audiodevice.SelectSpeakerScreen;
import de.maxhenkel.voicechat.gui.volume.AdjustVolumesScreen;
import de.maxhenkel.voicechat.gui.widgets.*;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.Denoiser;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;
import de.maxhenkel.voicechat.voice.client.speaker.AudioType;

import javax.annotation.Nullable;

public class VoiceChatSettingsScreen extends VoiceChatScreenBase {

    private static final String TEXTURE = TextureHelper.format(Voicechat.MODID, "textures/gui/gui_voicechat_settings.png");
    private static final String TITLE = TranslationStorage.getInstance().get("gui.voicechat.voice_chat_settings.title");
    private static final String ENABLED = TranslationStorage.getInstance().get("message.voicechat.enabled");
    private static final String DISABLED = TranslationStorage.getInstance().get("message.voicechat.disabled");
    private static final String ADJUST_VOLUMES = TranslationStorage.getInstance().get("message.voicechat.adjust_volumes");
    private static final String SELECT_MICROPHONE = TranslationStorage.getInstance().get("message.voicechat.select_microphone");
    private static final String SELECT_SPEAKER = TranslationStorage.getInstance().get("message.voicechat.select_speaker");
    private static final String BACK = TranslationStorage.getInstance().get("message.voicechat.back");

    @Nullable
    private final Screen parent;
    private VoiceActivationSlider voiceActivationSlider;

    public VoiceChatSettingsScreen(@Nullable Screen parent) {
        super(TITLE, 248, 219);
        this.parent = parent;
    }

    public VoiceChatSettingsScreen() {
        this(null);
    }

    @Override
    public void init() {
        super.init();

        int y = guiTop + 20;

        buttons.add(new VoiceSoundSlider(0, guiLeft + 10, y, xSize - 20, 20));
        y += 21;
        buttons.add(new MicAmplificationSlider(1, guiLeft + 10, y, xSize - 20, 20));
        y += 21;
        BooleanConfigButton denoiser = new BooleanConfigButton(2, guiLeft + 10, y, xSize - 20, 20, VoicechatClient.CLIENT_CONFIG.denoiser, enabled -> {
            return String.format(TranslationStorage.getInstance().get("message.voicechat.denoiser"), enabled ? ENABLED : DISABLED);
        });
        buttons.add(denoiser);

        if (Denoiser.createDenoiser() == null) {
            denoiser.active = false;
        }
        y += 21;

        voiceActivationSlider = new VoiceActivationSlider(3, guiLeft + 10, y + 21, xSize - 20, 20);

        buttons.add(new MicActivationButton(4, guiLeft + 10, y, xSize - 20, 20, voiceActivationSlider));
        y += 21;

        buttons.add(voiceActivationSlider);
        y += 21;

        MicTestButton micTestButton = new MicTestButton(5, guiLeft + 10, y, xSize - 20, 20, voiceActivationSlider);
        buttons.add(micTestButton);
        y += 21;

        buttons.add(new EnumButton<AudioType>(6, guiLeft + 10, y, xSize - 20, 20, VoicechatClient.CLIENT_CONFIG.audioType) {

            @Override
            protected String getText(AudioType type) {
                return String.format(TranslationStorage.getInstance().get("message.voicechat.audio_type"), type.getText());
            }

            @Override
            protected void onUpdate(AudioType type) {
                ClientVoicechat client = ClientManager.getClient();
                if (client != null) {
                    micTestButton.stop();
                    client.reloadAudio();
                }
            }
        });
        y += 21;
        if (isIngame()) {
            buttons.add(new ButtonBase(7, guiLeft + 10, y, xSize - 20, 20, ADJUST_VOLUMES) {
                @Override
                public void onPress() {
                    minecraft.setScreen(new AdjustVolumesScreen());
                }
            });
            y += 21;
        }
        buttons.add(new ButtonBase(8, guiLeft + 10, y, xSize / 2 - 15, 20, SELECT_MICROPHONE) {
            @Override
            public void onPress() {
                minecraft.setScreen(new SelectMicrophoneScreen(VoiceChatSettingsScreen.this));
            }
        });
        buttons.add(new ButtonBase(9, guiLeft + xSize / 2 + 6, y, xSize / 2 - 15, 20, SELECT_SPEAKER) {
            @Override
            public void onPress() {
                minecraft.setScreen(new SelectSpeakerScreen(VoiceChatSettingsScreen.this));
            }
        });
        y += 21;
        if (!isIngame() && parent != null) {
            buttons.add(new ButtonBase(10, guiLeft + 10, y, xSize - 20, 20, BACK) {
                @Override
                public void onPress() {
                    minecraft.setScreen(parent);
                }
            });
        }
    }

    @Override
    public void renderBackground(int mouseX, int mouseY, float delta) {
        TextureHelper.bindTexture(TEXTURE);
        if (isIngame()) {
            drawTexture(guiLeft, guiTop, 0, 0, xSize, ySize);
        }
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, float delta) {
        int titleWidth = textRenderer.getWidth(TITLE);
        textRenderer.draw(TITLE, guiLeft + (xSize - titleWidth) / 2, guiTop + 7, getFontColor());

        if (voiceActivationSlider == null) {
            return;
        }

        /*String tooltip = voiceActivationSlider.getTooltip();
        if (tooltip != null && voiceActivationSlider.isMouseOver()) {
            drawHoveringText(tooltip, mouseX, mouseY);
        }*/
    }
}
