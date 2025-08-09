package de.maxhenkel.voicechat.voice.client;

import net.minecraft.client.resource.language.TranslationStorage;

public enum MicrophoneActivationType {

    PTT(TranslationStorage.getInstance().get("message.voicechat.activation_type.ptt")), VOICE(TranslationStorage.getInstance().get("message.voicechat.activation_type.voice"));

    private final String component;

    MicrophoneActivationType(String component) {
        this.component = component;
    }

    public String getText() {
        return component;
    }
}
