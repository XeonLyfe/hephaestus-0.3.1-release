//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraftforge.event.world.ChunkEvent$Unload
 *  org.lwjgl.opengl.GL11
 */
package me.zeroeightsix.kami.module.modules.render;

import java.util.ArrayList;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.ChunkEvent;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import org.lwjgl.opengl.GL11;

@Module.Info(name="ChunkFinder", description="Highlights newly generated chunks", category=Module.Category.RENDER)
public class ChunkFinder
extends Module {
    private Setting<Integer> yOffset = this.register(Settings.i("Y Offset", 0));
    private Setting<Boolean> relative = this.register(Settings.b("Relative", true));
    static ArrayList<Chunk> chunks = new ArrayList();
    private static boolean dirty = true;
    private int list = GL11.glGenLists((int)1);
    @EventHandler
    public Listener<ChunkEvent> listener = new Listener<ChunkEvent>(event -> {
        if (!event.getPacket().isFullChunk()) {
            chunks.add(event.getChunk());
            dirty = true;
        }
    }, new Predicate[0]);
    @EventHandler
    private Listener<ChunkEvent.Unload> unloadListener = new Listener<ChunkEvent.Unload>(event -> {
        dirty = chunks.remove(event.getChunk());
    }, new Predicate[0]);

    @Override
    public void onWorldRender(RenderEvent event) {
        if (dirty) {
            GL11.glNewList((int)this.list, (int)4864);
            GL11.glPushMatrix();
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3553);
            GL11.glDepthMask((boolean)false);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)3042);
            GL11.glLineWidth((float)1.0f);
            for (Chunk chunk : chunks) {
                double posX = chunk.x * 16;
                double posY = 0.0;
                double posZ = chunk.z * 16;
                GL11.glColor3f((float)0.6f, (float)0.1f, (float)0.2f);
                GL11.glBegin((int)2);
                GL11.glVertex3d((double)posX, (double)posY, (double)posZ);
                GL11.glVertex3d((double)(posX + 16.0), (double)posY, (double)posZ);
                GL11.glVertex3d((double)(posX + 16.0), (double)posY, (double)(posZ + 16.0));
                GL11.glVertex3d((double)posX, (double)posY, (double)(posZ + 16.0));
                GL11.glVertex3d((double)posX, (double)posY, (double)posZ);
                GL11.glEnd();
            }
            GL11.glDisable((int)3042);
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glEndList();
            dirty = false;
        }
        double x = ChunkFinder.mc.getRenderManager().renderPosX;
        double y = this.relative.getValue() != false ? 0.0 : -ChunkFinder.mc.getRenderManager().renderPosY;
        double z = ChunkFinder.mc.getRenderManager().renderPosZ;
        GL11.glTranslated((double)(-x), (double)(y + (double)this.yOffset.getValue().intValue()), (double)(-z));
        GL11.glCallList((int)this.list);
        GL11.glTranslated((double)x, (double)(-(y + (double)this.yOffset.getValue().intValue())), (double)z);
    }

    @Override
    public void destroy() {
        GL11.glDeleteLists((int)1, (int)1);
    }
}

