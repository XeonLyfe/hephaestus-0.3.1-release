//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer
 */
package me.zeroeightsix.kami.module.modules.movement;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Info(name="AntiHunger", category=Module.Category.MOVEMENT, description="Lose hunger less fast. Might cause ghostblocks.")
public class AntiHunger
extends Module {
    @EventHandler
    public Listener<PacketEvent.Send> packetListener = new Listener<PacketEvent.Send>(event -> {
        if (event.getPacket() instanceof CPacketPlayer) {
            ((CPacketPlayer)event.getPacket()).onGround = false;
        }
    }, new Predicate[0]);
}

