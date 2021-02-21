//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.KamiTessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@Module.Info(name="EyeFinder", description="Draw lines from entity's heads to where they are looking", category=Module.Category.RENDER)
public class EyeFinder
extends Module {
    private Setting<Boolean> players = this.register(Settings.b("Players", true));
    private Setting<Boolean> mobs = this.register(Settings.b("Mobs", false));
    private Setting<Boolean> animals = this.register(Settings.b("Animals", false));

    @Override
    public void onWorldRender(RenderEvent event) {
        EyeFinder.mc.world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> EyeFinder.mc.player != entity).map(entity -> (EntityLivingBase)entity).filter(entityLivingBase -> !entityLivingBase.isDead).filter(entity -> this.players.getValue() != false && entity instanceof EntityPlayer || (EntityUtil.isPassive((Entity)entity) ? this.animals.getValue() : this.mobs.getValue()) != false).forEach(this::drawLine);
    }

    private void drawLine(EntityLivingBase e) {
        RayTraceResult result = e.rayTrace(6.0, Minecraft.getMinecraft().getRenderPartialTicks());
        if (result == null) {
            return;
        }
        Vec3d eyes = e.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks());
        GlStateManager.enableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        double posx = eyes.x - EyeFinder.mc.getRenderManager().renderPosX;
        double posy = eyes.y - EyeFinder.mc.getRenderManager().renderPosY;
        double posz = eyes.z - EyeFinder.mc.getRenderManager().renderPosZ;
        double posx2 = result.hitVec.x - EyeFinder.mc.getRenderManager().renderPosX;
        double posy2 = result.hitVec.y - EyeFinder.mc.getRenderManager().renderPosY;
        double posz2 = result.hitVec.z - EyeFinder.mc.getRenderManager().renderPosZ;
        GL11.glColor4f((float)0.752f, (float)0.086f, (float)0.709f, (float)0.8f);
        GlStateManager.glLineWidth((float)1.5f);
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)posx, (double)posy, (double)posz);
        GL11.glVertex3d((double)posx2, (double)posy2, (double)posz2);
        GL11.glVertex3d((double)posx2, (double)posy2, (double)posz2);
        GL11.glVertex3d((double)posx2, (double)posy2, (double)posz2);
        GL11.glEnd();
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            KamiTessellator.prepare(7);
            GL11.glEnable((int)2929);
            BlockPos b = result.getBlockPos();
            float x = b.x;
            float y = b.y;
            float z = b.z;
            KamiTessellator.drawBox(KamiTessellator.getBufferBuilder(), x, y, z, 1.01f, 1.01f, 1.01f, 192, 22, 181, 204, 63);
            KamiTessellator.release();
        }
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
    }
}

