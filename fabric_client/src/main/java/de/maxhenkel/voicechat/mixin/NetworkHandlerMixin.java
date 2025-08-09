package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.NetworkHandlerExtension;
import de.maxhenkel.voicechat.net.FabricNetworkEvents;
import de.maxhenkel.voicechat.net.Packet136ServerCustomPayload;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetworkHandler.class)
public class NetworkHandlerMixin implements NetworkHandlerExtension {

    @Override
    public void processCustomPayload(Packet packet) {
        if (packet instanceof Packet136ServerCustomPayload) {
            FabricNetworkEvents.onCustomPayloadClient((Packet136ServerCustomPayload) packet);
        }
    }
}
