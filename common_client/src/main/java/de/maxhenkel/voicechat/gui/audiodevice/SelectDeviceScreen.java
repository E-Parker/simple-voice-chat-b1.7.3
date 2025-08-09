package de.maxhenkel.voicechat.gui.audiodevice;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.gui.VoiceChatScreenBase;
import de.maxhenkel.voicechat.gui.widgets.ButtonBase;
import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.TranslationStorage;


import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SelectDeviceScreen extends ListScreenBase {

    protected static final String TEXTURE = TextureHelper.format(Voicechat.MODID, "textures/gui/gui_audio_devices.png");
    protected static final String BACK = TranslationStorage.getInstance().get("message.voicechat.back");

    protected static final int HEADER_SIZE = 16;
    protected static final int FOOTER_SIZE = 32;
    protected static final int UNIT_SIZE = 18;
    protected static final int CELL_HEIGHT = 36;

    @Nullable
    protected Screen parent;
    protected AudioDeviceList deviceList;
    protected ButtonBase back;
    protected int units;

    public SelectDeviceScreen(String title, @Nullable Screen parent) {
        super(title, 236, 0);
        this.parent = parent;
    }

    public abstract List<String> getDevices();

    public abstract String getSelectedDevice();

    public abstract void onSelect(String device);

    public abstract String getIcon(String device);

    public abstract String getEmptyListComponent();

    public abstract String getVisibleName(String device);

    @Override
    public void init() {
        super.init();
        guiLeft = guiLeft + 2;
        guiTop = 32;
        int minUnits = (int) Math.ceil((float) (CELL_HEIGHT + 4) / (float) UNIT_SIZE);
        units = Math.max(minUnits, (height - HEADER_SIZE - FOOTER_SIZE - guiTop * 2) / UNIT_SIZE);
        ySize = HEADER_SIZE + units * UNIT_SIZE + FOOTER_SIZE;

        deviceList = new AudioDeviceList(width, height, guiTop + HEADER_SIZE, guiTop + HEADER_SIZE + units * UNIT_SIZE, CELL_HEIGHT);
        setList(deviceList);

        back = new ButtonBase(0, guiLeft + 7, guiTop + ySize - 20 - 7, xSize - 14, 20, BACK) {
            @Override
            public void onPress() {
                minecraft.setScreen(parent);
            }
        };
        buttons.add(back);

        deviceList.replaceEntries(getDevices().stream().map(s -> new AudioDeviceEntry(this, s)).collect(Collectors.toList()));
    }

    @Override
    public void renderBackground(int mouseX, int mouseY, float delta) {
        if (isIngame()) {
            TextureHelper.bindTexture(TEXTURE);
            drawTexture(guiLeft, guiTop, 0, 0, xSize, HEADER_SIZE);
            for (int i = 0; i < units; i++) {
                drawTexture(guiLeft, guiTop + HEADER_SIZE + UNIT_SIZE * i, 0, HEADER_SIZE, xSize, UNIT_SIZE);
            }
            drawTexture(guiLeft, guiTop + HEADER_SIZE + UNIT_SIZE * units, 0, HEADER_SIZE + UNIT_SIZE, xSize, FOOTER_SIZE);
            drawTexture(guiLeft + 10, guiTop + HEADER_SIZE + 6 - 2, xSize, 0, 12, 12);
        }
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, float delta) {
        textRenderer.draw(title, width / 2 - textRenderer.getWidth(title) / 2, guiTop + 5, isIngame() ? VoiceChatScreenBase.FONT_COLOR : 0xFFFFFF);
        if (deviceList == null || deviceList.isEmpty()) {
            drawCenteredTextWithShadow(textRenderer, getEmptyListComponent(), width / 2, guiTop + HEADER_SIZE + (units * UNIT_SIZE) / 2 - (int) TextureHelper.FONT_HEIGHT / 2, -1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (deviceList == null) {
            return;
        }
        for (AudioDeviceEntry entry : deviceList.children()) {
            if (entry.isSelected()) {
                if (!getSelectedDevice().equals(entry.getDevice())) {
                    minecraft.soundManager.playSound("random.click", 1F, 1F);
                    onSelect(entry.getDevice());
                    return;
                }
            }
        }
    }

}
