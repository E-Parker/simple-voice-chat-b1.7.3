package de.maxhenkel.voicechat.extensions;

import net.minecraft.network.Connection;
public interface ClientNetworkHandlerExtension {
    Connection getNetworkManager();
}
