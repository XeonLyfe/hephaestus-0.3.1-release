//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package me.zeroeightsix.kami.module.modules.misc;

import java.util.List;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.util.ColourUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

@Module.Info(name="Carpenter", category=Module.Category.MISC)
public class Carpenter
extends Module {
    private int displayList = -1;

    public class Shape {
        final BlockPos[] blocks;
        private final int colour;

        Shape(List<BlockPos> blocks) {
            this.blocks = blocks.toArray(new BlockPos[0]);
            this.colour = ColourUtils.toRGBA(0.5 + Math.random() * 0.5, 0.5 + Math.random() * 0.5, 0.5 + Math.random() * 0.5, 1.0);
        }

        public BlockPos[] getBlocks() {
            return this.blocks;
        }

        public int getColour() {
            return this.colour;
        }
    }

    public static class Selection {
        private BlockPos first;
        private BlockPos second;

        public Selection(BlockPos pos1, BlockPos pos2) {
            this.first = pos1;
            this.second = pos2;
        }

        public BlockPos getFirst() {
            return this.first;
        }

        public BlockPos getSecond() {
            return this.second;
        }

        public void setFirst(BlockPos first) {
            this.first = first;
        }

        public void setSecond(BlockPos second) {
            this.second = second;
        }

        public boolean isInvalid() {
            return this.first == null || this.second == null;
        }

        public int getWidth() {
            int x1 = Math.min(this.first.getX(), this.second.getX());
            int x2 = Math.max(this.first.getX(), this.second.getX()) + 1;
            return Math.abs(x1 - x2);
        }

        public int getLength() {
            int z1 = Math.min(this.first.getZ(), this.second.getZ());
            int z2 = Math.max(this.first.getZ(), this.second.getZ()) + 1;
            return Math.abs(z1 - z2);
        }

        public int getHeight() {
            int y1 = Math.min(this.first.getY(), this.second.getY()) + 1;
            int y2 = Math.max(this.first.getY(), this.second.getY());
            return Math.abs(y1 - y2);
        }

        public int getSize() {
            return this.getWidth() * this.getLength() * this.getHeight();
        }

        public BlockPos getMinimum() {
            int x1 = Math.min(this.first.getX(), this.second.getX());
            int y1 = Math.min(this.first.getY(), this.second.getY());
            int z1 = Math.min(this.first.getZ(), this.second.getZ());
            return new BlockPos(x1, y1, z1);
        }

        public BlockPos getMaximum() {
            int x1 = Math.min(this.first.getX(), this.second.getX()) + 1;
            int y1 = Math.min(this.first.getY(), this.second.getY());
            int z1 = Math.min(this.first.getZ(), this.second.getZ()) + 1;
            return new BlockPos(x1, y1, z1);
        }

        public BlockPos getFurthest(int x, int y, int z) {
            if (x > 0) {
                if (this.first.getX() > this.second.getX()) {
                    return this.first;
                }
                return this.second;
            }
            if (x < 0) {
                if (this.first.getX() < this.second.getX()) {
                    return this.first;
                }
                return this.second;
            }
            if (y > 0) {
                if (this.first.getX() > this.second.getX()) {
                    return this.first;
                }
                return this.second;
            }
            if (y < 0) {
                if (this.first.getY() < this.second.getY()) {
                    return this.first;
                }
                return this.second;
            }
            if (z > 0) {
                if (this.first.getZ() > this.second.getZ()) {
                    return this.first;
                }
                return this.second;
            }
            if (z < 0) {
                if (this.first.getZ() < this.second.getZ()) {
                    return this.first;
                }
                return this.second;
            }
            return null;
        }

        public void moveSelection(int x, int y, int z) {
            this.first = this.first.add(x, y, z);
            this.second = this.second.add(x, y, z);
        }

        public void expand(int amount, EnumFacing facing) {
            BlockPos affect = this.second;
            switch (facing) {
                case DOWN: {
                    affect = this.second.getY() < this.first.getY() ? (this.second = this.second.add(0, -amount, 0)) : (this.first = this.first.add(0, -amount, 0));
                    break;
                }
                case UP: {
                    affect = this.second.getY() > this.first.getY() ? (this.second = this.second.add(0, amount, 0)) : (this.first = this.first.add(0, amount, 0));
                    break;
                }
                case NORTH: {
                    affect = this.second.getZ() < this.first.getZ() ? (this.second = this.second.add(0, 0, -amount)) : (this.first = this.first.add(0, 0, -amount));
                    break;
                }
                case SOUTH: {
                    affect = this.second.getZ() > this.first.getZ() ? (this.second = this.second.add(0, 0, amount)) : (this.first = this.first.add(0, 0, amount));
                    break;
                }
                case WEST: {
                    affect = this.second.getX() < this.first.getX() ? (this.second = this.second.add(-amount, 0, 0)) : (this.first = this.first.add(-amount, 0, 0));
                    break;
                }
                case EAST: {
                    affect = this.second.getX() > this.first.getX() ? (this.second = this.second.add(amount, 0, 0)) : (this.first = this.first.add(amount, 0, 0));
                }
            }
        }
    }

    public static class ShapeBuilder {
        private static BlockPos from(double x, double y, double z) {
            return new BlockPos(Math.floor(x), Math.floor(y), Math.floor(z));
        }

        public static Shape oval(BlockPos origin, double width, double length) {
            return null;
        }
    }
}

