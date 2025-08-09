package de.maxhenkel.voicechat.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ActiveRenderInfo {
    private static final IntBuffer VIEWPORT = GlAllocationUtils.allocateIntBuffer(16);
    private static final FloatBuffer MODELVIEW = GlAllocationUtils.allocateFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GlAllocationUtils.allocateFloatBuffer(16);
    private static final FloatBuffer OBJECTCOORDS = GlAllocationUtils.allocateFloatBuffer(3);
    private static Vec3d position = Vec3d.createCached(0.0D, 0.0D, 0.0D);
    private static float rotationX;
    private static float rotationXZ;
    private static float rotationZ;
    private static float rotationYZ;
    private static float rotationXY;

    public static void updateRenderInfo(PlayerEntity entityplayerIn, boolean p_74583_1_)
    {
        updateRenderInfo((Entity) entityplayerIn, p_74583_1_);
    }

    public static void updateRenderInfo(Entity entityplayerIn, boolean p_74583_1_)
    {
        GL11.glGetFloat(GL11.GL_MODELVIEW, MODELVIEW);
        GL11.glGetFloat(GL11.GL_PROJECTION, PROJECTION);
        GL11.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT);
        float f = (float)((VIEWPORT.get(0) + VIEWPORT.get(2)) / 2);
        float f1 = (float)((VIEWPORT.get(1) + VIEWPORT.get(3)) / 2);
        GLU.gluUnProject(f, f1, 0.0F, MODELVIEW, PROJECTION, VIEWPORT, OBJECTCOORDS);
        position = Vec3d.createCached((double)OBJECTCOORDS.get(0), (double)OBJECTCOORDS.get(1), (double)OBJECTCOORDS.get(2));
        int i = p_74583_1_ ? 1 : 0;
        float f2 = entityplayerIn.pitch;
        float f3 = entityplayerIn.yaw;
        rotationX = MathHelper.cos(f3 * 0.017453292F) * (float)(1 - i * 2);
        rotationZ = MathHelper.sin(f3 * 0.017453292F) * (float)(1 - i * 2);
        rotationYZ = -rotationZ * MathHelper.sin(f2 * 0.017453292F) * (float)(1 - i * 2);
        rotationXY = rotationX * MathHelper.sin(f2 * 0.017453292F) * (float)(1 - i * 2);
        rotationXZ = MathHelper.cos(f2 * 0.017453292F);
    }

    public static Vec3d projectViewFromEntity(Entity entityIn, double p_178806_1_)
    {
        double d0 = entityIn.prevX + (entityIn.x - entityIn.prevX) * p_178806_1_;
        double d1 = entityIn.prevY + (entityIn.y - entityIn.prevY) * p_178806_1_;
        double d2 = entityIn.prevZ + (entityIn.z - entityIn.prevZ) * p_178806_1_;
        double d3 = d0 + position.x;
        double d4 = d1 + position.y;
        double d5 = d2 + position.z;
        return Vec3d.createCached(d3, d4, d5);
    }

    public static float getRotationX()
    {
        return rotationX;
    }

    public static float getRotationXZ()
    {
        return rotationXZ;
    }

    public static float getRotationZ()
    {
        return rotationZ;
    }

    public static float getRotationYZ()
    {
        return rotationYZ;
    }

    public static float getRotationXY()
    {
        return rotationXY;
    }

    /* ======================================== FORGE START =====================================*/

    /**
     * Vector from render view entity position (corrected for partialTickTime) to the middle of screen
     */
    public static Vec3d getCameraPosition()
    {
        return position;
    }
}
