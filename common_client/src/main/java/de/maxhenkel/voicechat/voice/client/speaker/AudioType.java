package de.maxhenkel.voicechat.voice.client.speaker;

import net.minecraft.client.resource.language.TranslationStorage;

public enum AudioType {

    NORMAL(TranslationStorage.getInstance().get("message.voicechat.audio_type.normal")), REDUCED(TranslationStorage.getInstance().get("message.voicechat.audio_type.reduced")), OFF(TranslationStorage.getInstance().get("message.voicechat.audio_type.off"));

    private final String component;

    AudioType(String component) {
        this.component = component;
    }

    public String getText() {
        return component;
    }
}
