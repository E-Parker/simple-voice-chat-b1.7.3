package de.maxhenkel.voicechat.extensions;

import net.minecraft.network.packet.Packet;

public interface NetworkHandlerExtension {
    void processCustomPayload(Packet packet);
}
