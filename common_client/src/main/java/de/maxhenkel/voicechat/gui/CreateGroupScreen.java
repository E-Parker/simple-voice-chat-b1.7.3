package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.TextRendererExtension;
import de.maxhenkel.voicechat.extensions.ButtonWidgetExtension;
import de.maxhenkel.voicechat.gui.widgets.ButtonBase;
import de.maxhenkel.voicechat.gui.widgets.GuiTextField;
import de.maxhenkel.voicechat.net.ClientNetManager;
import de.maxhenkel.voicechat.net.CreateGroupPacket;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.input.Keyboard;

public class CreateGroupScreen extends VoiceChatScreenBase {

    private static final String TEXTURE = TextureHelper.format(Voicechat.MODID, "textures/gui/gui_create_group.png");
    private static final String TITLE = TranslationStorage.getInstance().get("gui.voicechat.create_group.title");
    private static final String CREATE = TranslationStorage.getInstance().get("message.voicechat.create");
    private static final String CREATE_GROUP = TranslationStorage.getInstance().get("message.voicechat.create_group");
    private static final String GROUP_NAME = TranslationStorage.getInstance().get("message.voicechat.group_name");
    private static final String OPTIONAL_PASSWORD = TranslationStorage.getInstance().get("message.voicechat.optional_password");
    private static final String GROUP_TYPE = TranslationStorage.getInstance().get("message.voicechat.group_type");

    private GuiTextField groupName;
    private GuiTextField password;
    private GroupType groupType;
    private ButtonBase groupTypeButton;
    private ButtonBase createGroup;

    public CreateGroupScreen() {
        super(TITLE, 195, 124);
        groupType = GroupType.NORMAL;
    }

    @Override
    public void init() {
        super.init();
        hoverAreas.clear();
        buttons.clear();

        Keyboard.enableRepeatEvents(true);

        groupName = new GuiTextField(0, textRenderer, guiLeft + 7, guiTop + 32, xSize - 7 * 2, 10);
        groupName.setMaxStringLength(24);
        groupName.setValidator(s -> s.isEmpty() || Voicechat.GROUP_REGEX.matcher(s).matches());

        password = new GuiTextField(1, textRenderer, guiLeft + 7, guiTop + 58, xSize - 7 * 2, 10);
        password.setMaxStringLength(32);
        password.setValidator(s -> s.isEmpty() || Voicechat.GROUP_REGEX.matcher(s).matches());

        groupTypeButton = new ButtonBase(2, guiLeft + 6, guiTop + 71, xSize - 12, 20, GROUP_TYPE + ": " + groupType.getTranslation()) {
            @Override
            public void onPress() {
                groupType = GroupType.values()[(groupType.ordinal() + 1) % GroupType.values().length];
                text = GROUP_TYPE + ": " + groupType.getTranslation();
            }
        };
        buttons.add(groupTypeButton);

        createGroup = new ButtonBase(3, guiLeft + 6, guiTop + ySize - 27, xSize - 12, 20, CREATE) {
            @Override
            public void onPress() {
                createGroup();
            }
        };
        buttons.add(createGroup);
    }

    private void createGroup() {
        if (!groupName.getText().isEmpty()) {
            ClientNetManager.sendToServer(new CreateGroupPacket(groupName.getText(), password.getText().isEmpty() ? null : password.getText(), groupType.getType()));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (groupName == null) {
            return;
        }
        groupName.updateCursorCounter();
        password.updateCursorCounter();
        createGroup.active = !groupName.getText().isEmpty();
    }

    @Override
    public void removed() {
        super.removed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void renderBackground(int mouseX, int mouseY, float delta) {
        TextureHelper.bindTexture(TEXTURE);
        drawTexture(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, float delta) {
        if (groupName == null) {
            return;
        }
        groupName.drawTextBox();
        password.drawTextBox();
        textRenderer.draw(CREATE_GROUP, guiLeft + xSize / 2 - textRenderer.getWidth(CREATE_GROUP) / 2, guiTop + 7, FONT_COLOR);
        textRenderer.draw(GROUP_NAME, guiLeft + 8, guiTop + 7 + (int)TextureHelper.FONT_HEIGHT + 5, FONT_COLOR);
        textRenderer.draw(OPTIONAL_PASSWORD, guiLeft + 8, guiTop + 7 + ((int)TextureHelper.FONT_HEIGHT + 5) * 2 + 10 + 2, FONT_COLOR);

        if (mouseX >= groupTypeButton.x && mouseY >= groupTypeButton.y && mouseX < groupTypeButton.x + ((ButtonWidgetExtension) groupTypeButton).getWidth() && mouseY < groupTypeButton.y + ((ButtonWidgetExtension) groupTypeButton).getHeight()) {
            drawHoveringText(((TextRendererExtension) minecraft.textRenderer).listFormattedStringToWidth(groupType.getDescription(), 200), mouseX, mouseY);
        }
    }

    @Override
    protected void keyPressed(char typedChar, int keyCode) {
        super.keyPressed(typedChar, keyCode);
        if (groupName == null) {
            return;
        }
        if (groupName.textboxKeyTyped(typedChar, keyCode) | password.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }

        if (keyCode == Keyboard.KEY_RETURN) {
            createGroup();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (groupName == null) {
            return;
        }
        groupName.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        if (groupName == null || password == null) {
            super.init(minecraft, width, height);
            return;
        }

        String groupNameText = groupName.getText();
        String passwordText = password.getText();
        super.init(minecraft, width, height);
        groupName.setText(groupNameText);
        password.setText(passwordText);
    }

}
