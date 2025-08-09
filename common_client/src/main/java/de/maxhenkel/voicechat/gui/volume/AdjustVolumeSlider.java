package de.maxhenkel.voicechat.gui.volume;

import de.maxhenkel.voicechat.gui.widgets.DebouncedSlider;
import net.minecraft.client.resource.language.TranslationStorage;

public class AdjustVolumeSlider extends DebouncedSlider {

    protected static final String MUTED = TranslationStorage.getInstance().get("message.voicechat.muted");

    protected static final float MAXIMUM = 4F;

    protected final VolumeConfigEntry volumeConfigEntry;

    public AdjustVolumeSlider(int id, int xIn, int yIn, int widthIn, int heightIn, VolumeConfigEntry volumeConfigEntry) {
        super(id, xIn, yIn, widthIn, heightIn, volumeConfigEntry.get() / MAXIMUM);
        this.volumeConfigEntry = volumeConfigEntry;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        if (value <= 0D) {
            text = MUTED;
            return;
        }
        long amp = Math.round(value * MAXIMUM * 100F - 100F);
        text = String.format(TranslationStorage.getInstance().get("message.voicechat.volume_amplification"), (amp > 0F ? "+" : "") + amp + "%");
    }

    @Override
    public void applyDebounced() {
        volumeConfigEntry.save(value * MAXIMUM);
    }

    public interface VolumeConfigEntry {
        void save(double value);

        double get();
    }

}
