package de.maxhenkel.voicechat.gui.group;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.gui.CreateGroupScreen;
import de.maxhenkel.voicechat.gui.EnterPasswordScreen;
import de.maxhenkel.voicechat.gui.widgets.ButtonBase;
import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
import de.maxhenkel.voicechat.net.ClientNetManager;
import de.maxhenkel.voicechat.net.JoinGroupPacket;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.common.ClientGroup;
import net.minecraft.client.resource.language.TranslationStorage;

public class JoinGroupScreen extends ListScreenBase {

    protected static final String TEXTURE = TextureHelper.format(Voicechat.MODID, "textures/gui/gui_join_group.png");
    protected static final String TITLE = TranslationStorage.getInstance().get("gui.voicechat.join_create_group.title");
    protected static final String CREATE_GROUP = TranslationStorage.getInstance().get("message.voicechat.create_group_button");
    protected static final String JOIN_CREATE_GROUP = TranslationStorage.getInstance().get("message.voicechat.join_create_group");
    protected static final String NO_GROUPS = TranslationStorage.getInstance().get("message.voicechat.no_groups");

    protected static final int HEADER_SIZE = 16;
    protected static final int FOOTER_SIZE = 32;
    protected static final int UNIT_SIZE = 18;
    protected static final int CELL_HEIGHT = 36;

    protected JoinGroupList groupList;
    protected ButtonBase createGroup;
    protected int units;

    public JoinGroupScreen() {
        super(TITLE, 236, 0);
    }

    @Override
    public void init() {
        super.init();
        guiLeft = guiLeft + 2;
        guiTop = 32;
        int minUnits = (int) Math.ceil((float) (CELL_HEIGHT + 4) / (float) UNIT_SIZE);
        units = Math.max(minUnits, (height - HEADER_SIZE - FOOTER_SIZE - guiTop * 2) / UNIT_SIZE);
        ySize = HEADER_SIZE + units * UNIT_SIZE + FOOTER_SIZE;

        groupList = new JoinGroupList(this, width, height, guiTop + HEADER_SIZE, guiTop + HEADER_SIZE + units * UNIT_SIZE, CELL_HEIGHT);
        setList(groupList);

        createGroup = new ButtonBase(0, guiLeft + 7, guiTop + ySize - 20 - 7, xSize - 14, 20, CREATE_GROUP) {
            @Override
            public void onPress() {
                minecraft.setScreen(new CreateGroupScreen());
            }
        };
        buttons.add(createGroup);
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
        textRenderer.draw(JOIN_CREATE_GROUP, guiLeft + xSize / 2 - textRenderer.getWidth(JOIN_CREATE_GROUP) / 2, guiTop + 5, FONT_COLOR);

        if (groupList != null && !groupList.isEmpty()) {
            groupList.drawScreen(mouseX, mouseY, delta);
        } else {
            drawCenteredTextWithShadow(textRenderer, NO_GROUPS, width / 2, guiTop + HEADER_SIZE + (units * UNIT_SIZE) / 2 - (int)TextureHelper.FONT_HEIGHT / 2, -1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (groupList == null) {
            return;
        }
        for (JoinGroupEntry entry : groupList.children()) {
            if (entry.isSelected()) {
                ClientGroup group = entry.getGroup().getGroup();
                minecraft.soundManager.playSound("random.click", 1F, 1F);
                if (group.hasPassword()) {
                    minecraft.setScreen(new EnterPasswordScreen(group));
                } else {
                    ClientNetManager.sendToServer(new JoinGroupPacket(group.getId(), null));
                }
                return;
            }
        }
    }

}
