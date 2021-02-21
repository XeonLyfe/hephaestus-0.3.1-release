//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockPortal
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraft.world.chunk.storage.ExtendedBlockStorage
 */
package me.zeroeightsix.kami.module.modules.render;

import java.util.ArrayList;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.ChunkEvent;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.modules.render.Tracers;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.block.BlockPortal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

@Module.Info(name="PortalTracers", category=Module.Category.RENDER)
public class PortalTracers
extends Module {
    private Setting<Integer> range = this.register(Settings.i("Range", 5000));
    private ArrayList<BlockPos> portals = new ArrayList();
    @EventHandler
    private Listener<ChunkEvent> loadListener = new Listener<ChunkEvent>(event -> {
        Chunk chunk = event.getChunk();
        this.portals.removeIf(blockPos -> blockPos.getX() / 16 == chunk.x && blockPos.getZ() / 16 == chunk.z);
        for (ExtendedBlockStorage storage : chunk.getBlockStorageArray()) {
            if (storage == null) continue;
            for (int x = 0; x < 16; ++x) {
                for (int y = 0; y < 16; ++y) {
                    for (int z = 0; z < 16; ++z) {
                        if (!(storage.get(x, y, z).getBlock() instanceof BlockPortal)) continue;
                        int px = chunk.x * 16 + x;
                        int py = storage.yBase + y;
                        int pz = chunk.z * 16 + z;
                        this.portals.add(new BlockPos(px, py, pz));
                        y += 6;
                    }
                }
            }
        }
    }, new Predicate[0]);

    @Override
    public void onWorldRender(RenderEvent event) {
        this.portals.stream().filter(blockPos -> PortalTracers.mc.player.getDistance((double)blockPos.x, (double)blockPos.y, (double)blockPos.z) <= (double)this.range.getValue().intValue()).forEach(blockPos -> Tracers.drawLine((double)blockPos.x - PortalTracers.mc.getRenderManager().renderPosX, (double)blockPos.y - PortalTracers.mc.getRenderManager().renderPosY, (double)blockPos.z - PortalTracers.mc.getRenderManager().renderPosZ, 0.0, 0.6f, 0.3f, 0.8f, 1.0f));
    }
}

