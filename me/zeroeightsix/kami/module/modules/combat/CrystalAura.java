//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 */
package me.zeroeightsix.kami.module.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.modules.render.Tracers;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.KamiTessellator;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

@Module.Info(name="CrystalAura", category=Module.Category.COMBAT)
public class CrystalAura
extends Module {
    private Setting<Boolean> autoSwitch = this.register(Settings.b("Auto Switch"));
    private Setting<Boolean> players = this.register(Settings.b("Players"));
    private Setting<Boolean> place = this.register(Settings.b("Place", false));
    private Setting<Boolean> explode = this.register(Settings.b("Explode", false));
    private Setting<Boolean> mobs = this.register(Settings.b("Mobs", false));
    private Setting<Boolean> animals = this.register(Settings.b("Animals", false));
    private Setting<Double> range = this.register(Settings.d("Range", 4.0));
    private Setting<Boolean> antiweakness = this.register(Settings.b("Anti Weakness", false));
    private BlockPos render;
    private Entity renderEnt;
    private long systemTime = -1L;
    private static boolean togglePitch = false;
    private boolean switchCooldown = false;
    private int newSlot;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener = new Listener<PacketEvent.Send>(event -> {
        Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer)packet).yaw = (float)yaw;
            ((CPacketPlayer)packet).pitch = (float)pitch;
        }
    }, new Predicate[0]);

    @Override
    public void onUpdate() {
        int crystalSlot;
        EntityEnderCrystal crystal = CrystalAura.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> (EntityEnderCrystal)entity).min(Comparator.comparing(c -> Float.valueOf(CrystalAura.mc.player.getDistance((Entity)c)))).orElse(null);
        if (this.explode.getValue().booleanValue() && crystal != null && (double)CrystalAura.mc.player.getDistance((Entity)crystal) <= this.range.getValue()) {
            if (System.nanoTime() / 1000000L - this.systemTime >= 250L) {
                if (this.antiweakness.getValue().booleanValue() && CrystalAura.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    this.newSlot = -1;
                    for (int i = 0; i < 9; ++i) {
                        ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
                        if (stack == ItemStack.EMPTY) continue;
                        if (stack.getItem() instanceof ItemSword) {
                            this.newSlot = i;
                            break;
                        }
                        if (!(stack.getItem() instanceof ItemTool)) continue;
                        this.newSlot = i;
                        break;
                    }
                    if (this.newSlot != -1) {
                        Wrapper.getPlayer().inventory.currentItem = this.newSlot;
                        this.switchCooldown = true;
                    }
                }
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)CrystalAura.mc.player);
                CrystalAura.mc.playerController.attackEntity((EntityPlayer)CrystalAura.mc.player, (Entity)crystal);
                CrystalAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.systemTime = System.nanoTime() / 1000000L;
            }
            return;
        }
        CrystalAura.resetRotation();
        int n = crystalSlot = CrystalAura.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? CrystalAura.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (CrystalAura.mc.player.inventory.getStackInSlot(l).getItem() != Items.END_CRYSTAL) continue;
                crystalSlot = l;
                break;
            }
        }
        boolean offhand = false;
        if (CrystalAura.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        } else if (crystalSlot == -1) {
            return;
        }
        List<BlockPos> blocks = this.findCrystalBlocks();
        ArrayList entities = new ArrayList();
        if (this.players.getValue().booleanValue()) {
            entities.addAll(CrystalAura.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        }
        entities.addAll(CrystalAura.mc.world.loadedEntityList.stream().filter(entity -> EntityUtil.isLiving(entity) && (EntityUtil.isPassive(entity) ? this.animals.getValue() : this.mobs.getValue()) != false).collect(Collectors.toList()));
        BlockPos q = null;
        double damage = 0.5;
        for (Entity entity2 : entities) {
            if (entity2 == CrystalAura.mc.player || ((EntityLivingBase)entity2).getHealth() <= 0.0f) continue;
            for (BlockPos blockPos : blocks) {
                double self;
                double d;
                double b = entity2.getDistanceSq(blockPos);
                if (b >= 169.0 || !((d = (double)CrystalAura.calculateDamage((double)blockPos.x + 0.5, blockPos.y + 1, (double)blockPos.z + 0.5, entity2)) > damage) || (self = (double)CrystalAura.calculateDamage((double)blockPos.x + 0.5, blockPos.y + 1, (double)blockPos.z + 0.5, (Entity)CrystalAura.mc.player)) > d && !(d < (double)((EntityLivingBase)entity2).getHealth()) || self - 0.5 > (double)CrystalAura.mc.player.getHealth()) continue;
                damage = d;
                q = blockPos;
                this.renderEnt = entity2;
            }
        }
        if (damage == 0.5) {
            this.render = null;
            this.renderEnt = null;
            CrystalAura.resetRotation();
            return;
        }
        this.render = q;
        if (this.place.getValue().booleanValue()) {
            if (!offhand && CrystalAura.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue().booleanValue()) {
                    CrystalAura.mc.player.inventory.currentItem = crystalSlot;
                    CrystalAura.resetRotation();
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket((double)q.x + 0.5, (double)q.y - 0.5, (double)q.z + 0.5, (EntityPlayer)CrystalAura.mc.player);
            RayTraceResult result = CrystalAura.mc.world.rayTraceBlocks(new Vec3d(CrystalAura.mc.player.posX, CrystalAura.mc.player.posY + (double)CrystalAura.mc.player.getEyeHeight(), CrystalAura.mc.player.posZ), new Vec3d((double)q.x + 0.5, (double)q.y - 0.5, (double)q.z + 0.5));
            EnumFacing f = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
            if (this.switchCooldown) {
                this.switchCooldown = false;
                return;
            }
            CrystalAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
        if (isSpoofingAngles) {
            if (togglePitch) {
                CrystalAura.mc.player.rotationPitch = (float)((double)CrystalAura.mc.player.rotationPitch + 4.0E-4);
                togglePitch = false;
            } else {
                CrystalAura.mc.player.rotationPitch = (float)((double)CrystalAura.mc.player.rotationPitch - 4.0E-4);
                togglePitch = true;
            }
        }
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (this.render != null) {
            KamiTessellator.prepare(7);
            KamiTessellator.drawBox(this.render, 0x44FFFFFF, 63);
            KamiTessellator.release();
            if (this.renderEnt != null) {
                Vec3d p = EntityUtil.getInterpolatedRenderPos(this.renderEnt, mc.getRenderPartialTicks());
                Tracers.drawLineFromPosToPos((double)this.render.x - CrystalAura.mc.getRenderManager().renderPosX + 0.5, (double)this.render.y - CrystalAura.mc.getRenderManager().renderPosY + 1.0, (double)this.render.z - CrystalAura.mc.getRenderManager().renderPosZ + 0.5, p.x, p.y, p.z, this.renderEnt.getEyeHeight(), 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        CrystalAura.setYawAndPitch((float)v[0], (float)v[1]);
    }

    private boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        return (CrystalAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || CrystalAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && CrystalAura.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && CrystalAura.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && CrystalAura.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(CrystalAura.mc.player.posX), Math.floor(CrystalAura.mc.player.posY), Math.floor(CrystalAura.mc.player.posZ));
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList positions = NonNullList.create();
        positions.addAll((Collection)this.getSphere(CrystalAura.getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f = y;
                    float f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double)doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = CrystalAura.getBlastReduction((EntityLivingBase)entity, CrystalAura.getDamageMultiplied(damage), new Explosion((World)CrystalAura.mc.world, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.causeExplosionDamage((Explosion)explosion);
            damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)ep.getTotalArmorValue(), (float)((float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
            int k = EnchantmentHelper.getEnchantmentModifierDamage((Iterable)ep.getArmorInventoryList(), (DamageSource)ds);
            float f = MathHelper.clamp((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(Potion.getPotionById((int)11))) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage - ep.getAbsorptionAmount(), 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)entity.getTotalArmorValue(), (float)((float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = CrystalAura.mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(EntityEnderCrystal crystal, Entity entity) {
        return CrystalAura.calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = CrystalAura.mc.player.rotationYaw;
            pitch = CrystalAura.mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    @Override
    public void onDisable() {
        this.render = null;
        this.renderEnt = null;
        CrystalAura.resetRotation();
    }
}

