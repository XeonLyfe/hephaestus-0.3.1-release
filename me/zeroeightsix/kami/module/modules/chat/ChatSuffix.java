//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package me.zeroeightsix.kami.module.modules.chat;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.ChatTextUtils;
import net.minecraft.network.play.client.CPacketChatMessage;

@Module.Info(name="ChatSuffix", category=Module.Category.CHAT, description="Fancy Suffix")
public class ChatSuffix
extends Module {
    private Setting<Boolean> commands = this.register(Settings.b("Ignore Commands", true));
    private Setting<Boolean> ignorespecial = this.register(Settings.b("Ignore Special", true));
    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage)event.getPacket()).getMessage();
            if (this.commands.getValue().booleanValue() && s.startsWith("/")) {
                return;
            }
            if (this.ignorespecial.getValue().booleanValue() && !Character.isLetter(s.charAt(0)) && !Character.isDigit(s.charAt(0)) && s.charAt(0) != '>' && s.charAt(0) != '^') {
                return;
            }
            ((CPacketChatMessage)event.getPacket()).message = s = ChatTextUtils.appendChatSuffix(s);
        }
    }, new Predicate[0]);
}

