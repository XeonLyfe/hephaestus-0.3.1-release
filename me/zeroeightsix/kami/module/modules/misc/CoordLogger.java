//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketEffect
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.Vec3d
 */
package me.zeroeightsix.kami.module.modules.misc;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.FileHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;

@Module.Info(name="CoordLogger", description="Coord Logger", category=Module.Category.MISC)
public class CoordLogger
extends Module {
    private static final String fileName = "Hephaestus_CoordLogger.txt";
    private Setting<Boolean> tp = this.register(Settings.b("TpExploit", false));
    private Setting<Boolean> lightning = this.register(Settings.b("Thunder", false));
    private Setting<Boolean> portal = this.register(Settings.b("EndPortal", false));
    private Setting<Boolean> wither = this.register(Settings.b("Wither", false));
    private Setting<Boolean> dragon = this.register(Settings.b("Dragon", false));
    private Setting<Boolean> savetofile = this.register(Settings.b("SaveToFile", false));
    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        SPacketSoundEffect packet;
        if (this.lightning.getValue().booleanValue() && event.getPacket() instanceof SPacketSoundEffect && (packet = (SPacketSoundEffect)event.getPacket()).getCategory() == SoundCategory.WEATHER && packet.getSound() == SoundEvents.ENTITY_LIGHTNING_THUNDER) {
            this.sendNotification("\u00a7cCoordLogger: Lightning spawned at X" + packet.getX() + " Z" + packet.getZ());
        }
        if (event.getPacket() instanceof SPacketEffect) {
            packet = (SPacketEffect)event.getPacket();
            if (this.portal.getValue().booleanValue() && packet.getSoundType() == 1038) {
                this.sendNotification("\u00a7cCoordLogger: End Portal activated at X" + packet.getSoundPos().getX() + " Y" + packet.getSoundPos().getY() + " Z" + packet.getSoundPos().getZ());
            }
            if (this.wither.getValue().booleanValue() && packet.getSoundType() == 1023) {
                this.sendNotification("\u00a7cCoordLogger: Wither spawned at X" + packet.getSoundPos().getX() + " Y" + packet.getSoundPos().getY() + " Z" + packet.getSoundPos().getZ());
            }
            if (this.dragon.getValue().booleanValue() && packet.getSoundType() == 1028) {
                this.sendNotification("\u00a7cCoordLogger: Dragon killed at X" + packet.getSoundPos().getX() + " Y" + packet.getSoundPos().getY() + " Z" + packet.getSoundPos().getZ());
            }
        }
    }, new Predicate[0]);
    private HashMap<Entity, Vec3d> knownPlayers = new HashMap();

    @Override
    public void onUpdate() {
        if (!this.tp.getValue().booleanValue()) {
            return;
        }
        List tickEntityList = CoordLogger.mc.world.getLoadedEntityList();
        for (Entity entity : tickEntityList) {
            if (!(entity instanceof EntityPlayer) || entity.getName().equals(CoordLogger.mc.player.getName())) continue;
            Vec3d targetPos = new Vec3d(entity.posX, entity.posY, entity.posZ);
            if (this.knownPlayers.containsKey(entity)) {
                if (Math.abs(CoordLogger.mc.player.getPositionVector().distanceTo(targetPos)) >= 128.0 && this.knownPlayers.get(entity).distanceTo(targetPos) >= 64.0) {
                    this.sendNotification("\u00a7cCoordLogger: Player " + entity.getName() + " moved to Position " + targetPos.toString());
                }
                this.knownPlayers.put(entity, targetPos);
                continue;
            }
            this.sendNotification("CoordLogger: Watching Player " + entity.getName());
            this.knownPlayers.put(entity, targetPos);
        }
    }

    private void sendNotification(String s) {
        Command.sendChatMessage(s);
        if (this.savetofile.getValue().booleanValue()) {
            FileHelper.appendTextFile(s, fileName);
        }
    }
}

