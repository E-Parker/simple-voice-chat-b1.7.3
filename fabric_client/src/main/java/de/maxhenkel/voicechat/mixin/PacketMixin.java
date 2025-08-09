package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.net.Packet135ClientCustomPayload;
import de.maxhenkel.voicechat.net.Packet136ServerCustomPayload;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Packet.class)
public abstract class PacketMixin {
    @Invoker
    private static void invokeRegister(int rawId, boolean clientBound, boolean serverBound, Class type) {}

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerCustomPayloadPackets(CallbackInfo ci) {
        // TODO CHECK THESE ARE CORRECT DIRECTIONS
        invokeRegister(135, false, true, Packet135ClientCustomPayload.class);
        invokeRegister(136, true, false, Packet136ServerCustomPayload.class);
    }
}
