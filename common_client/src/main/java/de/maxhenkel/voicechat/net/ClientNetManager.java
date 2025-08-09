package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.network.NetworkHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public abstract class ClientNetManager extends NetManager {
    public static void sendToServer(Packet<?> packet) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream buffer = new DataOutputStream(outputStream);
        try {
            packet.toBytes(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClientNetworkHandler connection = MinecraftAccessor.getMinecraft().getNetworkHandler();
        if (connection != null) {
            connection.sendPacket(new Packet135ClientCustomPayload(packet.getIdentifier().toString(), outputStream.toByteArray()));
        }
    }

    public interface ClientReceiver<T extends Packet<T>> {
        void onPacket(Minecraft client, NetworkHandler handler, T packet);
    }
}
