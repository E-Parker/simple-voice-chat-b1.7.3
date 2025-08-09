package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.ClientNetworkHandlerExtension;
import net.minecraft.network.Connection;
import net.minecraft.client.network.ClientNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientNetworkHandler.class)
public abstract class ClientNetworkHandlerMixin implements ClientNetworkHandlerExtension {
    @Accessor
    public abstract Connection getConnection();

    @Override
    public Connection getNetworkManager() {
        return getConnection();
    }
}
