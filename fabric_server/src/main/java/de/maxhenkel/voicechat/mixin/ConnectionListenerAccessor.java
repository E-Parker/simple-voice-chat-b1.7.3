package de.maxhenkel.voicechat.mixin;

import net.minecraft.server.network.ConnectionListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.net.ServerSocket;

@Mixin(ConnectionListener.class)
public interface ConnectionListenerAccessor {
    @Accessor
    ServerSocket getSocket();
}
