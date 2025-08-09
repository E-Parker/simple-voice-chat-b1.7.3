package de.maxhenkel.voicechat.voice.server;

import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class ServerWorldUtils {

    public static Collection<PlayerEntity> getPlayersInRange(World level, Vec3d pos, double range, Predicate<PlayerEntity> filter) {
        List<PlayerEntity> nearbyPlayers = new ArrayList<>();
        List<PlayerEntity> players = level.players;
        for (int i = 0; i < players.size(); i++) {
            PlayerEntity player = (PlayerEntity) players.get(i);
            if (isInRange(Vec3d.createCached(player.x, player.y, player.z), pos, range) && filter.test(player)) {
                nearbyPlayers.add(player);
            }
        }
        return nearbyPlayers;
    }

    public static boolean isInRange(Vec3d pos1, Vec3d pos2, double range) {
        return Math.abs(pos1.x - pos2.x) <= range && Math.abs(pos1.y - pos2.y) <= range && Math.abs(pos1.z - pos2.z) <= range;
    }

}
