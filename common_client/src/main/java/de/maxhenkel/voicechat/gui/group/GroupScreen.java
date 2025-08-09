package de.maxhenkel.voicechat.gui.group;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.gui.GroupType;
import de.maxhenkel.voicechat.gui.tooltips.DisableTooltipSupplier;
import de.maxhenkel.voicechat.gui.tooltips.HideGroupHudTooltipSupplier;
import de.maxhenkel.voicechat.gui.tooltips.MuteTooltipSupplier;
import de.maxhenkel.voicechat.gui.widgets.ImageButton;
import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
import de.maxhenkel.voicechat.gui.widgets.ToggleImageButton;
import de.maxhenkel.voicechat.net.ClientNetManager;
import de.maxhenkel.voicechat.net.LeaveGroupPacket;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import de.maxhenkel.voicechat.voice.client.MicrophoneActivationType;
import de.maxhenkel.voicechat.voice.common.ClientGroup;
import net.minecraft.client.resource.language.TranslationStorage;

public class GroupScreen extends ListScreenBase {

    protected static final String TEXTURE = TextureHelper.format(Voicechat.MODID, "textures/gui/gui_group.png");
    protected static final String LEAVE = TextureHelper.format(Voicechat.MODID, "textures/icons/leave.png");
    protected static final String MICROPHONE = TextureHelper.format(Voicechat.MODID, "textures/icons/microphone_button.png");
    protected static final String SPEAKER = TextureHelper.format(Voicechat.MODID, "textures/icons/speaker_button.png");
    protected static final String GROUP_HUD = TextureHelper.format(Voicechat.MODID, "textures/icons/group_hud_button.png");
    protected static final String TITLE = TranslationStorage.getInstance().get("gui.voicechat.group.title");
    protected static final String LEAVE_GROUP = TranslationStorage.getInstance().get("message.voicechat.leave_group");

    protected static final int HEADER_SIZE = 16;
    protected static final int FOOTER_SIZE = 32;
    protected static final int UNIT_SIZE = 18;
    protected static final int CELL_HEIGHT = 36;

    protected GroupList groupList;
    protected int units;

    protected final ClientGroup group;
    protected ToggleImageButton mute;
    protected ToggleImageButton disable;
    protected ToggleImageButton showHUD;
    protected ImageButton leave;

    public GroupScreen(ClientGroup group) {
        super(TITLE, 236, 0);
        this.group = group;
    }

    @Override
    public void init() {
        super.init();
        guiLeft = guiLeft + 2;
        guiTop = 32;
        int minUnits = (int) Math.ceil((float) (CELL_HEIGHT + 4) / (float) UNIT_SIZE);
        units = Math.max(minUnits, (height - HEADER_SIZE - FOOTER_SIZE - guiTop * 2) / UNIT_SIZE);
        ySize = HEADER_SIZE + units * UNIT_SIZE + FOOTER_SIZE;

        ClientPlayerStateManager stateManager = ClientManager.getPlayerStateManager();

        groupList = new GroupList(this, width, height, guiTop + HEADER_SIZE, guiTop + HEADER_SIZE + units * UNIT_SIZE, CELL_HEIGHT);
        setList(groupList);

        int buttonY = guiTop + ySize - 20 - 7;
        int buttonSize = 20;

        mute = new ToggleImageButton(0, guiLeft + 7, buttonY, MICROPHONE, stateManager::isMuted, button -> {
            stateManager.setMuted(!stateManager.isMuted());
        }, new MuteTooltipSupplier(this, stateManager));
        buttons.add(mute);

        disable = new ToggleImageButton(1, guiLeft + 7 + buttonSize + 3, buttonY, SPEAKER, stateManager::isDisabled, button -> {
            stateManager.setDisabled(!stateManager.isDisabled());
        }, new DisableTooltipSupplier(this, stateManager));
        buttons.add(disable);

        showHUD = new ToggleImageButton(2, guiLeft + 7 + (buttonSize + 3) * 2, buttonY, GROUP_HUD, VoicechatClient.CLIENT_CONFIG.showGroupHUD::get, button -> {
            VoicechatClient.CLIENT_CONFIG.showGroupHUD.set(!VoicechatClient.CLIENT_CONFIG.showGroupHUD.get()).save();
        }, new HideGroupHudTooltipSupplier(this));
        buttons.add(showHUD);

        leave = new ImageButton(3, guiLeft + xSize - buttonSize - 7, buttonY, LEAVE, button -> {
            ClientNetManager.sendToServer(new LeaveGroupPacket());
            minecraft.setScreen(new JoinGroupScreen());
        }, (button, mouseX, mouseY) -> {
            minecraft.textRenderer.drawWithShadow(LEAVE_GROUP, mouseX, mouseY, 16777215);
        });
        buttons.add(leave);

        checkButtons();
    }

    @Override
    public void tick() {
        super.tick();
        checkButtons();
    }

    private void checkButtons() {
        if (mute != null) {
            mute.active = VoicechatClient.CLIENT_CONFIG.microphoneActivationType.get().equals(MicrophoneActivationType.VOICE);
        }
        if (showHUD != null) {
            showHUD.active = !VoicechatClient.CLIENT_CONFIG.hideIcons.get();
        }
    }

    @Override
    public void renderBackground(int mouseX, int mouseY, float delta) {
        TextureHelper.bindTexture(TEXTURE);
        drawTexture(guiLeft, guiTop, 0, 0, xSize, HEADER_SIZE);
        for (int i = 0; i < units; i++) {
            drawTexture(guiLeft, guiTop + HEADER_SIZE + UNIT_SIZE * i, 0, HEADER_SIZE, xSize, UNIT_SIZE);
        }
        drawTexture(guiLeft, guiTop + HEADER_SIZE + UNIT_SIZE * units, 0, HEADER_SIZE + UNIT_SIZE, xSize, FOOTER_SIZE);
        drawTexture(guiLeft + 10, guiTop + HEADER_SIZE + 6 - 2, xSize, 0, 12, 12);
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, float delta) {
        String title;
        if (group.getType().equals(Group.Type.NORMAL)) {
            title = String.format(TranslationStorage.getInstance().get("message.voicechat.group_title"), group.getName());
        } else {
            title = String.format(TranslationStorage.getInstance().get("message.voicechat.group_type_title"), group.getName(), GroupType.fromType(group.getType()).getTranslation());
        }

        textRenderer.draw(title, guiLeft + xSize / 2 - textRenderer.getWidth(title) / 2, guiTop + 5, FONT_COLOR);
    }

}
