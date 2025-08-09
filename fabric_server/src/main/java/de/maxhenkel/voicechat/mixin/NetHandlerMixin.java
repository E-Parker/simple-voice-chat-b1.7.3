package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.NetworkHandlerExtension;
import de.maxhenkel.voicechat.net.FabricNetworkEvents;
import de.maxhenkel.voicechat.net.Packet135ClientCustomPayload;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetworkHandler.class)
public class NetHandlerMixin implements NetworkHandlerExtension {
    @Override
    public void processCustomPayload(Packet packet) {
        if (packet instanceof Packet135ClientCustomPayload) {
            FabricNetworkEvents.onCustomPayloadServer((Packet135ClientCustomPayload) packet, Voicechat.serverInstance.getPlayerByName(((ServerPlayNetworkHandler) (Object) this).getName()));
        }
    }
}
