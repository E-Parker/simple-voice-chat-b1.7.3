package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameProfileUtils {

    private static final Minecraft mc = MinecraftAccessor.getMinecraft();

    /*
    private static final Map<String, String> skinUrlCache = new ConcurrentHashMap<>();

    public static String getSkinUrl(String username) {
        if (skinUrlCache.containsKey(username))
            return skinUrlCache.get(username);

        String data = ConnectionUtil.readText("https://api.mojang.com/users/profiles/minecraft/" + username);
        if (data == null)
            return null;

        JsonObject uuidData = JsonParser.parseString(data).getAsJsonObject();

        String skinData = ConnectionUtil.readText("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidData.get("id").getAsString());
        if (skinData == null)
            return null;

        JsonObject skinJson = JsonParser.parseString(skinData).getAsJsonObject();

        if (!skinJson.has("properties"))
            return null;

        JsonArray skinProperties = skinJson.getAsJsonArray("properties");

        if (skinProperties.isEmpty())
            return null;

        String b64str = skinProperties.get(0).getAsJsonObject().get("value").getAsString();
        String decoded = new String(Base64.getDecoder().decode(b64str));

        JsonObject propertyJson = JsonParser.parseString(decoded).getAsJsonObject();

        if (!propertyJson.has("textures"))
            return null;

        JsonObject texturesJson = propertyJson.getAsJsonObject("textures");

        if (!texturesJson.has("SKIN"))
            return null;

        String skinUrl = texturesJson.getAsJsonObject("SKIN").get("url").getAsString();
        skinUrlCache.put(username, skinUrl);
        return skinUrl;
    }*/

    private static final Map<String, Integer> usernameToIdMap = new ConcurrentHashMap<>();

    public static void bindSkinTexture(String username) {
        /* I feel like fetching the user's skin is not voicechat's job
        // Messy but working modern skin fetching
        int skinTextureId = -1;
        if (!usernameToIdMap.containsKey(username)) {
            String skinUrl = getSkinUrl(username);
            if(skinUrl == null) {
                Voicechat.LOGGER.info("Skin for " + username + " is null!");
                usernameToIdMap.put(username, -1);
            } else {
                Voicechat.LOGGER.info("Queuing skin for download: " + username + ": " + skinUrl);
                // Queue the image for download
                mc.textureManager.downloadImage(skinUrl, null);
                usernameToIdMap.put(username, -2);
            }
        } else if (usernameToIdMap.get(username) == -2) {
            // Get the texture
            skinTextureId = mc.textureManager.downloadTexture(getSkinUrl(username), null);
            Voicechat.LOGGER.info(skinTextureId);

            if(skinTextureId >= 0) {
                usernameToIdMap.put(username, skinTextureId);
            }
        } else {
            skinTextureId = usernameToIdMap.get(username);
        }*/
        // Requires use of a proper skin fix mod
        int skinTextureId;
        if (!usernameToIdMap.containsKey(username)) {
            PlayerEntity player = MinecraftAccessor.getMinecraft().world.getPlayer(username);

            skinTextureId = mc.textureManager.downloadTexture(player.skinUrl, null);
            usernameToIdMap.put(username, skinTextureId);
        } else {
            skinTextureId = usernameToIdMap.get(username);
        }

        if (skinTextureId >= 0) {
            mc.textureManager.bindTexture(skinTextureId);
        } else {
            mc.textureManager.bindTexture(mc.textureManager.getTextureId("/mob/char.png"));
        }
    }

    public static void refreshTextures() {
        usernameToIdMap.clear();
    }

    /*public static ResourceLocation getSkin(UUID uuid) {
        NetHandlerPlayClient connection = mc.getConnection();
        if (connection == null) {
            return DefaultPlayerSkin.getDefaultSkin(uuid);
        }
        NetworkPlayerInfo playerInfo = connection.getPlayerInfo(uuid);
        if (playerInfo == null) {
            return DefaultPlayerSkin.getDefaultSkin(uuid);
        }
        return playerInfo.getLocationSkin();
    }*/

}
