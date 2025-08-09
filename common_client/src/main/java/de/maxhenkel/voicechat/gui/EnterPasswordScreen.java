package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.gui.widgets.ButtonBase;
import de.maxhenkel.voicechat.gui.widgets.GuiTextField;
import de.maxhenkel.voicechat.net.ClientNetManager;
import de.maxhenkel.voicechat.net.JoinGroupPacket;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.common.ClientGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.input.Keyboard;

public class EnterPasswordScreen extends VoiceChatScreenBase {

    private static final String TEXTURE = TextureHelper.format(Voicechat.MODID, "textures/gui/gui_enter_password.png");
    private static final String TITLE = TranslationStorage.getInstance().get("gui.voicechat.enter_password.title");
    private static final String JOIN_GROUP = TranslationStorage.getInstance().get("message.voicechat.join_group");
    private static final String ENTER_GROUP_PASSWORD = TranslationStorage.getInstance().get("message.voicechat.enter_group_password");
    private static final String PASSWORD = TranslationStorage.getInstance().get("message.voicechat.password");

    private GuiTextField password;
    private ButtonBase joinGroup;
    private ClientGroup group;

    public EnterPasswordScreen(ClientGroup group) {
        super(TITLE, 195, 74);
        this.group = group;
    }

    @Override
    public void init() {
        super.init();
        hoverAreas.clear();
        buttons.clear();

        Keyboard.enableRepeatEvents(true);

        password = new GuiTextField(0, textRenderer, guiLeft + 7, guiTop + 7 + ((int)TextureHelper.FONT_HEIGHT + 5) * 2 - 5 + 2, xSize - 7 * 2, 10);
        password.setMaxStringLength(32);
        password.setValidator(s -> s.isEmpty() || Voicechat.GROUP_REGEX.matcher(s).matches());

        joinGroup = new ButtonBase(1, guiLeft + 7, guiTop + ySize - 20 - 7, xSize - 7 * 2, 20, JOIN_GROUP) {
            @Override
            public void onPress() {
                joinGroup();
            }
        };
        buttons.add(joinGroup);
    }

    private void joinGroup() {
        if (!password.getText().isEmpty()) {
            ClientNetManager.sendToServer(new JoinGroupPacket(group.getId(), password.getText()));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (password == null) {
            return;
        }
        password.updateCursorCounter();
        joinGroup.active = !password.getText().isEmpty();
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
        if (password != null) {
            password.drawTextBox();
        }
        textRenderer.draw(ENTER_GROUP_PASSWORD, guiLeft + xSize / 2 - textRenderer.getWidth(ENTER_GROUP_PASSWORD) / 2, guiTop + 7, FONT_COLOR);
        textRenderer.draw(PASSWORD, guiLeft + 8, guiTop + 7 + (int) TextureHelper.FONT_HEIGHT + 5, FONT_COLOR);
    }

    @Override
    protected void keyPressed(char typedChar, int keyCode) {
        super.keyPressed(typedChar, keyCode);
        if (password == null) {
            return;
        }
        if (password.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }

        if (keyCode == Keyboard.KEY_RETURN) {
            joinGroup();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (password == null) {
            return;
        }
        password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        if (password == null) {
            super.init(minecraft, width, height);
            return;
        }

        String passwordText = password.getText();
        super.init(minecraft, width, height);
        password.setText(passwordText);
    }
}
