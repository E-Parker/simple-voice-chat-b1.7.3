package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.util.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GuiEnhancedControls extends Screen {
    private Screen parent;
    private GameOptions options;
    private int buttonId = -1;

    public GuiEnhancedControls(Screen parent, GameOptions options) {
        this.parent = parent;
        this.options = options;
    }

    private final List<ButtonWidget> scrollableList = new LinkedList<>();
    private int currentPage = 0;
    private int maxPage = 0;

    private int splitEvery = 10;

    private final List<KeyBinding> mergedKeyBindings = new LinkedList<>();

    public void init() {
        mergedKeyBindings.clear();

        mergedKeyBindings.addAll(Arrays.asList(this.options.allKeys));
        mergedKeyBindings.addAll(KeyBindingHelper.getKeyBindings());

        maxPage = 0;
        currentPage = 0;

        TranslationStorage translate = TranslationStorage.getInstance();
        int buttonWidth = this.width / 2 - 155;

        int baseY = this.height / 6;
        int j = 0;
        for (int i = 0; i < this.mergedKeyBindings.size(); ++i) {
            int y = baseY + 24 * (j >> 1);

            if (splitEvery == j) {
                j = 0;
                y = baseY;
                maxPage++;
            }

            this.scrollableList.add(new OptionButtonWidget(i, buttonWidth + j % 2 * 160, y, 70, 20, this.getOptionDisplayString(i)));
            j++;
        }

        this.buttons.add(new ButtonWidget(200, this.width / 2 - 100, this.height / 6 + 168, translate.get("gui.done")));

        this.buttons.add(new OptionButtonWidget(201, this.width / 2 - 80, this.height / 6 + 138, 20, 20, "<"));
        this.buttons.add(new OptionButtonWidget(203, this.width / 2 + 60, this.height / 6 + 138, 20, 20, ">"));
    }

    private String getOptionDisplayString(int id) {
        return Keyboard.getKeyName(this.mergedKeyBindings.get(id).code);
    }

    private String getKeyBindingDescription(int id) {
        TranslationStorage translate = TranslationStorage.getInstance();
        return translate.get(this.mergedKeyBindings.get(id).translationKey);
    }

    @Override
    public void removed() {
        KeyBindingHelper.saveKeyBindings();
    }

    protected void buttonClicked(ButtonWidget guiButton) {
        for(int var2 = 0; var2 < this.mergedKeyBindings.size(); ++var2) {
            (this.scrollableList.get(var2)).text = this.getOptionDisplayString(var2);
        }

        if (guiButton.id == 200) { // Ok
            this.minecraft.setScreen(this.parent);
            KeyBindingHelper.saveKeyBindings();
        } else if (guiButton.id == 201) { // Back
            if (currentPage > 0) {
                currentPage--;
            }
        } else if (guiButton.id == 203) { // Front
            if (currentPage < maxPage) {
                currentPage++;
            }
        } else {
            this.buttonId = guiButton.id;
            guiButton.text = "> " + this.getOptionDisplayString(guiButton.id) + " <";
        }

    }

    protected void keyPressed(char c, int i) {
        if (this.buttonId >= 0) {
            if (i == Keyboard.KEY_ESCAPE) // add the ability to not assign keys
                i = Keyboard.KEY_NONE;

            this.setKeyBinding(this.buttonId, i);
            (this.scrollableList.get(this.buttonId)).text = this.getOptionDisplayString(this.buttonId);
            this.buttonId = -1;
        } else {
            super.keyPressed(c, i);
        }
    }

    private void setKeyBinding(int id, int keyCode) {
        this.mergedKeyBindings.get(id).code = keyCode;

        KeyBindingHelper.saveKeyBindings();
        this.options.save();
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        if (k == 0) {
            for(int var4 = 0; var4 < this.scrollableList.size(); ++var4) {
                ButtonWidget var5 = this.scrollableList.get(var4);
                if (this.isItemVisible(var4) && var5.isMouseOver(this.minecraft, i, j)) {
                    this.minecraft.soundManager.playSound("random.click", 1.0F, 1.0F);
                    this.buttonClicked(var5);
                }
            }
        }

        super.mouseClicked(i, j, k);
    }

    public boolean isItemVisible(int i) {
        return i <= ((currentPage + 1) * splitEvery) - 1 && i >= (currentPage * splitEvery);
    }

    public void render(int i, int j, float f) {
        this.renderBackground();
        this.drawCenteredTextWithShadow(this.textRenderer, "Controls", this.width / 2, 20, 16777215);
        int buttonWidth = this.width / 2 - 155;

        for(int var5 = 0; var5 < this.mergedKeyBindings.size(); ++var5) {
            if (!isItemVisible(var5))
                continue;

            ButtonWidget button = this.scrollableList.get(var5);
            button.render(minecraft, i, j);
            this.drawTextWithShadow(this.textRenderer, this.getKeyBindingDescription(var5), buttonWidth + var5 % 2 * 160 + 70 + 6, button.y + 7, -1);
        }

        this.drawCenteredTextWithShadow(this.textRenderer, String.format("%d / %d", currentPage + 1, maxPage + 1), this.width / 2, this.height / 6 + 148 - 4, 16777215);

        super.render(i, j, f);
    }
}
