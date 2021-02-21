//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockContainer
 *  net.minecraft.block.BlockFalling
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.zeroeightsix.kami.module.modules.combat;

import java.util.Collections;
import java.util.List;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.BlockInteractionHelper;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Info(name="AutoBarrier2", category=Module.Category.COMBAT)
public class AutoBarrier2
extends Module {
    private final Vec3d[] surroundList = new Vec3d[]{new Vec3d(0.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 1.0, -1.0)};
    private final Vec3d[] surroundListFull = new Vec3d[]{new Vec3d(0.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(1.0, 1.0, -1.0)};
    private final List<Block> obsidian = Collections.singletonList(Blocks.OBSIDIAN);
    private Setting<Boolean> toggleable = this.register(Settings.b("Toggleable", true));
    private Setting<Boolean> slowmode = this.register(Settings.b("Slow", false));
    private Setting<Boolean> full = this.register(Settings.b("Full", false));
    private Vec3d[] surroundTargets;
    private BlockPos basePos;
    private boolean slowModeSwitch = false;
    private int blocksPerTick = 3;
    private int offsetStep = 0;
    private int oldSlot = 0;

    @Override
    public void onUpdate() {
        if (this.isDisabled() || AutoBarrier2.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (this.slowModeSwitch) {
            this.slowModeSwitch = false;
            return;
        }
        if (this.offsetStep == 0) {
            this.init();
        }
        for (int i = 0; i < this.blocksPerTick; ++i) {
            if (this.offsetStep >= this.surroundTargets.length) {
                this.end();
                return;
            }
            Vec3d offset = this.surroundTargets[this.offsetStep];
            this.placeBlock(new BlockPos((Vec3i)this.basePos.add(offset.x, offset.y, offset.z)));
            ++this.offsetStep;
        }
        this.slowModeSwitch = true;
    }

    private void placeBlock(BlockPos blockPos) {
        if (!Wrapper.getWorld().getBlockState(blockPos).getMaterial().isReplaceable()) {
            return;
        }
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock) || BlockInteractionHelper.blackList.contains(block = ((ItemBlock)stack.getItem()).getBlock()) || block instanceof BlockContainer || !Block.getBlockFromItem((Item)stack.getItem()).getDefaultState().isFullBlock() || ((ItemBlock)stack.getItem()).getBlock() instanceof BlockFalling && Wrapper.getWorld().getBlockState(blockPos.down()).getMaterial().isReplaceable() || !this.obsidian.contains(block)) continue;
            newSlot = i;
            break;
        }
        if (newSlot == -1) {
            if (!this.toggleable.getValue().booleanValue()) {
                Command.sendChatMessage("AutoBarrier: No Obsidian in Hotbar!");
            }
            this.end();
            return;
        }
        Wrapper.getPlayer().inventory.currentItem = newSlot;
        if (!BlockInteractionHelper.checkForNeighbours(blockPos)) {
            return;
        }
        BlockInteractionHelper.placeBlockScaffold(blockPos);
    }

    private void init() {
        this.basePos = new BlockPos(AutoBarrier2.mc.player.getPositionVector()).down();
        if (this.slowmode.getValue().booleanValue()) {
            this.blocksPerTick = 1;
        }
        this.surroundTargets = this.full.getValue() != false ? this.surroundListFull : this.surroundList;
    }

    private void end() {
        this.offsetStep = 0;
        if (!this.toggleable.getValue().booleanValue()) {
            this.disable();
        }
    }

    @Override
    protected void onEnable() {
        AutoBarrier2.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoBarrier2.mc.player, CPacketEntityAction.Action.START_SNEAKING));
        this.oldSlot = Wrapper.getPlayer().inventory.currentItem;
    }

    @Override
    protected void onDisable() {
        AutoBarrier2.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoBarrier2.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        Wrapper.getPlayer().inventory.currentItem = this.oldSlot;
    }
}

