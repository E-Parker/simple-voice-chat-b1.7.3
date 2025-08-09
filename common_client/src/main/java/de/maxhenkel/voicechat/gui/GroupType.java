package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.api.Group;
import net.minecraft.client.resource.language.TranslationStorage;

public enum GroupType {
    NORMAL(TranslationStorage.getInstance().get("message.voicechat.group_type.normal"), TranslationStorage.getInstance().get("message.voicechat.group_type.normal.description"), Group.Type.NORMAL),
    OPEN(TranslationStorage.getInstance().get("message.voicechat.group_type.open"), TranslationStorage.getInstance().get("message.voicechat.group_type.open.description"), Group.Type.OPEN),
    ISOLATED(TranslationStorage.getInstance().get("message.voicechat.group_type.isolated"), TranslationStorage.getInstance().get("message.voicechat.group_type.isolated.description"), Group.Type.ISOLATED);

    private final String translation;
    private final String description;
    private final Group.Type type;

    GroupType(String translation, String description, Group.Type type) {
        this.translation = translation;
        this.description = description;
        this.type = type;
    }

    public String getTranslation() {
        return translation;
    }

    public String getDescription() {
        return description;
    }

    public Group.Type getType() {
        return type;
    }

    public static GroupType fromType(Group.Type type) {
        for (GroupType groupType : values()) {
            if (groupType.getType() == type) {
                return groupType;
            }
        }
        return NORMAL;
    }

}
