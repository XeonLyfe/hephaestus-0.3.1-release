//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.zeroeightsix.kami.util;

import java.util.Arrays;
import java.util.List;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class BlockInteractionHelper {
    public static final List<Block> blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean hotbarSlotCheckEmpty(ItemStack stack) {
        return stack != ItemStack.EMPTY;
    }

    public static boolean blockCheckNonBlock(ItemStack stack) {
        return stack.getItem() instanceof ItemBlock;
    }

    public static void placeBlockScaffold(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ);
        for (EnumFacing side : EnumFacing.values()) {
            Vec3d hitVec;
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (!BlockInteractionHelper.canBeClicked(neighbor) || eyesPos.squareDistanceTo(hitVec = new Vec3d((Vec3i)neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5))) > 18.0625) continue;
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
            BlockInteractionHelper.processRightClickBlock(neighbor, side2, hitVec);
            Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
            BlockInteractionHelper.mc.rightClickDelayTimer = 4;
            return;
        }
    }

    private static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = BlockInteractionHelper.getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{Wrapper.getPlayer().rotationYaw + MathHelper.wrapDegrees((float)(yaw - Wrapper.getPlayer().rotationYaw)), Wrapper.getPlayer().rotationPitch + MathHelper.wrapDegrees((float)(pitch - Wrapper.getPlayer().rotationPitch))};
    }

    private static Vec3d getEyesPos() {
        return new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ);
    }

    private static void faceVectorPacketInstant(Vec3d vec) {
        float[] rotations = BlockInteractionHelper.getLegitRotations(vec);
        Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], Wrapper.getPlayer().onGround));
    }

    private static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec) {
        BlockInteractionHelper.getPlayerController().processRightClickBlock(Wrapper.getPlayer(), BlockInteractionHelper.mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
    }

    private static boolean canBeClicked(BlockPos pos) {
        return BlockInteractionHelper.getBlock(pos).canCollideCheck(BlockInteractionHelper.getState(pos), false);
    }

    private static Block getBlock(BlockPos pos) {
        return BlockInteractionHelper.getState(pos).getBlock();
    }

    private static PlayerControllerMP getPlayerController() {
        return Minecraft.getMinecraft().playerController;
    }

    private static IBlockState getState(BlockPos pos) {
        return Wrapper.getWorld().getBlockState(pos);
    }

    public static boolean checkForNeighbours(BlockPos blockPos) {
        if (!BlockInteractionHelper.hasNeighbour(blockPos)) {
            for (EnumFacing side : EnumFacing.values()) {
                BlockPos neighbour = blockPos.offset(side);
                if (!BlockInteractionHelper.hasNeighbour(neighbour)) continue;
                return true;
            }
            return false;
        }
        return true;
    }

    private static boolean hasNeighbour(BlockPos blockPos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = blockPos.offset(side);
            if (Wrapper.getWorld().getBlockState(neighbour).getMaterial().isReplaceable()) continue;
            return true;
        }
        return false;
    }
}

