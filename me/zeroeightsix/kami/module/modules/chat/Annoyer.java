//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUpdateSign
 *  net.minecraft.network.play.server.SPacketUseBed
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.event.entity.living.LivingEntityUseItemEvent$Finish
 */
package me.zeroeightsix.kami.module.modules.chat;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.GuiScreenEvent;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

@Module.Info(name="Annoyer", category=Module.Category.CHAT, description="Annoyer")
public class Annoyer
extends Module {
    private static boolean isFirstRun = true;
    private static Queue<String> messageQueue = new ConcurrentLinkedQueue<String>();
    private static Map<String, Integer> minedBlocks = new ConcurrentHashMap<String, Integer>();
    private static Map<String, Integer> placedBlocks = new ConcurrentHashMap<String, Integer>();
    private static Map<String, Integer> droppedItems = new ConcurrentHashMap<String, Integer>();
    private static Map<String, Integer> consumedItems = new ConcurrentHashMap<String, Integer>();
    private static DecimalFormat df = new DecimalFormat();
    private static TimerTask timerTask;
    private static Timer timer;
    private static PacketEvent.Receive lastEventReceive;
    private static PacketEvent.Send lastEventSend;
    private static LivingEntityUseItemEvent.Finish lastLivingEntityUseFinishEvent;
    private static GuiScreenEvent.Displayed lastGuiScreenDisplayedEvent;
    private static String lastmessage;
    private static Vec3d thisTickPos;
    private static Vec3d lastTickPos;
    private static double distanceTraveled;
    private static float thisTickHealth;
    private static float lastTickHealth;
    private static float gainedHealth;
    private static float lostHealth;
    private Setting<Boolean> distance = this.register(Settings.b("Distance", true));
    private Setting<Integer> mindistance = this.register(Settings.integerBuilder("Min Distance").withRange(1, 100).withValue(10).build());
    private Setting<Integer> maxdistance = this.register(Settings.integerBuilder("Max Distance").withRange(100, 10000).withValue(150).build());
    private Setting<Boolean> blocks = this.register(Settings.b("Blocks", true));
    private Setting<Boolean> items = this.register(Settings.b("Items", true));
    private Setting<Boolean> playerheal = this.register(Settings.b("Player Heal", true));
    private Setting<Boolean> playerdamage = this.register(Settings.b("Player Damage", true));
    private Setting<Boolean> playerdeath = this.register(Settings.b("Death", true));
    private Setting<Boolean> greentext = this.register(Settings.b("Greentext", false));
    private Setting<Integer> delay = this.register(Settings.integerBuilder("Send Delay").withRange(1, 10).withValue(2).build());
    private Setting<Integer> queuesize = this.register(Settings.integerBuilder("Queue Size").withRange(1, 100).withValue(5).build());
    private Setting<Boolean> clearqueue = this.register(Settings.b("Clear Queue", false));
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> guiScreenEventDisplayedlistener = new Listener<GuiScreenEvent.Displayed>(event -> {
        if (this.isDisabled() || Annoyer.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (lastGuiScreenDisplayedEvent != null && lastGuiScreenDisplayedEvent.equals(event)) {
            return;
        }
        if (this.playerdeath.getValue().booleanValue() && event.getScreen() instanceof GuiGameOver) {
            this.queueMessage("I ded ;(");
            return;
        }
        lastGuiScreenDisplayedEvent = event;
    }, new Predicate[0]);
    @EventHandler
    private Listener<PacketEvent.Receive> packetEventReceiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (this.isDisabled() || Annoyer.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (lastEventReceive != null && lastEventReceive.equals(event)) {
            return;
        }
        if (event.getPacket() instanceof SPacketUseBed) {
            this.queueMessage("I used a Bed!");
            lastEventReceive = event;
            return;
        }
    }, new Predicate[0]);
    @EventHandler
    private Listener<PacketEvent.Send> packetEventSendListener = new Listener<PacketEvent.Send>(event -> {
        if (this.isDisabled() || Annoyer.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (lastEventSend != null && lastEventSend.equals(event)) {
            return;
        }
        if ((this.items.getValue().booleanValue() || this.blocks.getValue().booleanValue()) && event.getPacket() instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging p = (CPacketPlayerDigging)event.getPacket();
            if (this.items.getValue().booleanValue() && (p.getAction().equals((Object)CPacketPlayerDigging.Action.DROP_ITEM) || p.getAction().equals((Object)CPacketPlayerDigging.Action.DROP_ALL_ITEMS))) {
                String name = Annoyer.mc.player.inventory.getCurrentItem().getDisplayName();
                if (droppedItems.containsKey(name)) {
                    droppedItems.put(name, droppedItems.get(name) + 1);
                } else {
                    droppedItems.put(name, 1);
                }
                lastEventSend = event;
                return;
            }
            if (this.blocks.getValue().booleanValue() && p.getAction().equals((Object)CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                String name = Annoyer.mc.world.getBlockState(p.getPosition()).getBlock().getLocalizedName();
                if (minedBlocks.containsKey(name)) {
                    minedBlocks.put(name, minedBlocks.get(name) + 1);
                } else {
                    minedBlocks.put(name, 1);
                }
                lastEventSend = event;
                return;
            }
        } else {
            if (this.items.getValue().booleanValue() && event.getPacket() instanceof CPacketUpdateSign) {
                CPacketUpdateSign p = (CPacketUpdateSign)event.getPacket();
                this.queueMessage("I placed a Sign!");
                lastEventSend = event;
                return;
            }
            if (this.blocks.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                CPacketPlayerTryUseItemOnBlock p = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
                ItemStack itemStack = Annoyer.mc.player.inventory.getCurrentItem();
                if (itemStack.isEmpty) {
                    lastEventSend = event;
                    return;
                }
                if (itemStack.getItem() instanceof ItemBlock) {
                    String name = Annoyer.mc.player.inventory.getCurrentItem().getDisplayName();
                    if (placedBlocks.containsKey(name)) {
                        placedBlocks.put(name, placedBlocks.get(name) + 1);
                    } else {
                        placedBlocks.put(name, 1);
                    }
                    lastEventSend = event;
                    return;
                }
            }
        }
    }, new Predicate[0]);
    @EventHandler
    public Listener<LivingEntityUseItemEvent.Finish> listener = new Listener<LivingEntityUseItemEvent.Finish>(event -> {
        if (event.getEntity().equals((Object)Annoyer.mc.player) && event.getItem().getItem() instanceof ItemFood) {
            String name = event.getItem().getDisplayName();
            if (consumedItems.containsKey(name)) {
                consumedItems.put(name, consumedItems.get(name) + 1);
            } else {
                consumedItems.put(name, 1);
            }
            lastLivingEntityUseFinishEvent = event;
            return;
        }
    }, new Predicate[0]);

    @Override
    public void onEnable() {
        timer = new Timer();
        if (Annoyer.mc.player == null) {
            this.disable();
            return;
        }
        df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        timerTask = new TimerTask(){

            @Override
            public void run() {
                Annoyer.this.sendMessageCycle();
            }
        };
        timer.schedule(timerTask, 0L, (long)(this.delay.getValue() * 1000));
    }

    @Override
    public void onDisable() {
        timer.cancel();
        timer.purge();
        messageQueue.clear();
    }

    @Override
    public void onUpdate() {
        if (this.isDisabled() || Annoyer.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (this.clearqueue.getValue().booleanValue()) {
            this.clearqueue.setValue(false);
            messageQueue.clear();
        }
        this.getGameTickData();
    }

    private void getGameTickData() {
        if (this.distance.getValue().booleanValue()) {
            lastTickPos = thisTickPos;
            thisTickPos = Annoyer.mc.player.getPositionVector();
            distanceTraveled += thisTickPos.distanceTo(lastTickPos);
        }
        if (this.playerheal.getValue().booleanValue() || this.playerdamage.getValue().booleanValue()) {
            lastTickHealth = thisTickHealth;
            thisTickHealth = Annoyer.mc.player.getHealth() + Annoyer.mc.player.getAbsorptionAmount();
            float healthDiff = thisTickHealth - lastTickHealth;
            if (healthDiff < 0.0f) {
                lostHealth += healthDiff * -1.0f;
            } else {
                gainedHealth += healthDiff;
            }
        }
    }

    private void composeGameTickData() {
        CharSequence sb;
        if (isFirstRun) {
            isFirstRun = false;
            this.clearTickData();
            return;
        }
        if (this.distance.getValue().booleanValue() && distanceTraveled >= 1.0) {
            if (distanceTraveled < (double)(this.delay.getValue() * this.mindistance.getValue())) {
                return;
            }
            if (distanceTraveled > (double)(this.delay.getValue() * this.maxdistance.getValue())) {
                distanceTraveled = 0.0;
                return;
            }
            sb = new StringBuilder();
            ((StringBuilder)sb).append("I moved ");
            ((StringBuilder)sb).append((int)distanceTraveled);
            if ((int)distanceTraveled == 1) {
                ((StringBuilder)sb).append(" Block!");
            } else {
                ((StringBuilder)sb).append(" Blocks!");
            }
            this.queueMessage(((StringBuilder)sb).toString());
            distanceTraveled = 0.0;
        }
        if (this.playerdamage.getValue().booleanValue() && lostHealth != 0.0f) {
            sb = "I lost " + df.format(lostHealth) + " Health!";
            this.queueMessage((String)sb);
            lostHealth = 0.0f;
        }
        if (this.playerheal.getValue().booleanValue() && gainedHealth != 0.0f) {
            sb = "I gained " + df.format(gainedHealth) + " Health!";
            this.queueMessage((String)sb);
            gainedHealth = 0.0f;
        }
    }

    private void composeEventData() {
        for (Map.Entry<String, Integer> kv : minedBlocks.entrySet()) {
            this.queueMessage("I mined " + kv.getValue() + " " + kv.getKey() + "!");
            minedBlocks.remove(kv.getKey());
        }
        for (Map.Entry<String, Integer> kv : placedBlocks.entrySet()) {
            this.queueMessage("I placed " + kv.getValue() + " " + kv.getKey() + "!");
            placedBlocks.remove(kv.getKey());
        }
        for (Map.Entry<String, Integer> kv : droppedItems.entrySet()) {
            this.queueMessage("I dropped " + kv.getValue() + " " + kv.getKey() + "!");
            droppedItems.remove(kv.getKey());
        }
        for (Map.Entry<String, Integer> kv : consumedItems.entrySet()) {
            this.queueMessage("I ate " + kv.getValue() + " " + kv.getKey() + "!");
            consumedItems.remove(kv.getKey());
        }
    }

    private void sendMessageCycle() {
        if (this.isDisabled() || Annoyer.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        this.composeGameTickData();
        this.composeEventData();
        Iterator iterator = messageQueue.iterator();
        if (iterator.hasNext()) {
            String message = (String)iterator.next();
            this.sendMessage(message);
            messageQueue.remove(message);
            return;
        }
    }

    private void sendMessage(String s) {
        StringBuilder sb = new StringBuilder();
        if (this.greentext.getValue().booleanValue()) {
            sb.append("> ");
        }
        sb.append(s);
        Annoyer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(sb.toString().replaceAll("\u00a7", "")));
    }

    private void clearTickData() {
        float health;
        Vec3d pos;
        lastTickPos = pos = Annoyer.mc.player.getPositionVector();
        thisTickPos = pos;
        distanceTraveled = 0.0;
        lastTickHealth = health = Annoyer.mc.player.getHealth() + Annoyer.mc.player.getAbsorptionAmount();
        thisTickHealth = health;
        lostHealth = 0.0f;
        gainedHealth = 0.0f;
    }

    private Block getBlock(BlockPos pos) {
        return Annoyer.mc.world.getBlockState(pos).getBlock();
    }

    private void queueMessage(String message) {
        if (messageQueue.size() > this.queuesize.getValue()) {
            return;
        }
        messageQueue.add(message);
    }

    static {
        lastmessage = "";
    }
}

