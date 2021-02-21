//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@Module.Info(name="ESP", category=Module.Category.RENDER)
public class ESP
extends Module {
    private Setting<ESPMode> mode = this.register(Settings.e("Mode", ESPMode.RECTANGLE));
    private Setting<Boolean> players = this.register(Settings.b("Players", true));
    private Setting<Boolean> animals = this.register(Settings.b("Animals", false));
    private Setting<Boolean> mobs = this.register(Settings.b("Mobs", false));

    @Override
    public void onWorldRender(RenderEvent event) {
        if (Wrapper.getMinecraft().getRenderManager().options == null) {
            return;
        }
        switch (this.mode.getValue()) {
            case RECTANGLE: {
                boolean isThirdPersonFrontal = Wrapper.getMinecraft().getRenderManager().options.thirdPersonView == 2;
                float viewerYaw = Wrapper.getMinecraft().getRenderManager().playerViewY;
                ESP.mc.world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> ESP.mc.player != entity).map(entity -> (EntityLivingBase)entity).filter(entityLivingBase -> !entityLivingBase.isDead).filter(entity -> this.players.getValue() != false && entity instanceof EntityPlayer || (EntityUtil.isPassive((Entity)entity) ? this.animals.getValue() : this.mobs.getValue()) != false).forEach(e -> {
                    GlStateManager.pushMatrix();
                    Vec3d pos = EntityUtil.getInterpolatedPos((Entity)e, event.getPartialTicks());
                    GlStateManager.translate((double)(pos.x - ESP.mc.getRenderManager().renderPosX), (double)(pos.y - ESP.mc.getRenderManager().renderPosY), (double)(pos.z - ESP.mc.getRenderManager().renderPosZ));
                    GlStateManager.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.rotate((float)(-viewerYaw), (float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.rotate((float)(isThirdPersonFrontal ? -1 : 1), (float)1.0f, (float)0.0f, (float)0.0f);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask((boolean)false);
                    GlStateManager.disableDepth();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
                    if (e instanceof EntityPlayer) {
                        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
                    } else if (EntityUtil.isPassive((Entity)e)) {
                        GL11.glColor3f((float)0.11f, (float)0.9f, (float)0.11f);
                    } else {
                        GL11.glColor3f((float)0.9f, (float)0.1f, (float)0.1f);
                    }
                    GlStateManager.disableTexture2D();
                    GL11.glLineWidth((float)2.0f);
                    GL11.glEnable((int)2848);
                    GL11.glBegin((int)2);
                    GL11.glVertex2d((double)(-e.width / 2.0f), (double)0.0);
                    GL11.glVertex2d((double)(-e.width / 2.0f), (double)e.height);
                    GL11.glVertex2d((double)(e.width / 2.0f), (double)e.height);
                    GL11.glVertex2d((double)(e.width / 2.0f), (double)0.0);
                    GL11.glEnd();
                    GlStateManager.popMatrix();
                });
                GlStateManager.enableDepth();
                GlStateManager.depthMask((boolean)true);
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
                GlStateManager.shadeModel((int)7425);
                GlStateManager.disableDepth();
                GlStateManager.enableCull();
                GlStateManager.glLineWidth((float)1.0f);
                GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
                break;
            }
        }
    }

    public static enum ESPMode {
        RECTANGLE;

    }
}

