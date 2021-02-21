/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketSpawnExperienceOrb
 *  net.minecraft.network.play.server.SPacketSpawnGlobalEntity
 *  net.minecraft.network.play.server.SPacketSpawnMob
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.network.play.server.SPacketSpawnPainting
 */
package me.zeroeightsix.kami.module.modules.render;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;

@Module.Info(name="NoRender", category=Module.Category.RENDER, description="Ignore entity spawn packets")
public class NoRender
extends Module {
    private Setting<Boolean> mob = this.register(Settings.b("Mob"));
    private Setting<Boolean> gentity = this.register(Settings.b("GEntity"));
    private Setting<Boolean> object = this.register(Settings.b("Object"));
    private Setting<Boolean> xp = this.register(Settings.b("XP"));
    private Setting<Boolean> paint = this.register(Settings.b("Paintings"));
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        Packet packet = event.getPacket();
        if (packet instanceof SPacketSpawnMob && this.mob.getValue() != false || packet instanceof SPacketSpawnGlobalEntity && this.gentity.getValue() != false || packet instanceof SPacketSpawnObject && this.object.getValue() != false || packet instanceof SPacketSpawnExperienceOrb && this.xp.getValue() != false || packet instanceof SPacketSpawnPainting && this.paint.getValue().booleanValue()) {
            event.cancel();
        }
    }, new Predicate[0]);
}

