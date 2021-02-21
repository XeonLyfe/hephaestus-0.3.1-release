//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.Vec3d
 */
package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.module.modules.misc.AutoTool;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.LagCompensator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

@Module.Info(name="Aura", category=Module.Category.COMBAT, description="Hits entities around you")
public class Aura
extends Module {
    private Setting<Boolean> players = this.register(Settings.b("Players", true));
    private Setting<Boolean> animals = this.register(Settings.b("Animals", false));
    private Setting<Boolean> mobs = this.register(Settings.b("Mobs", false));
    private Setting<Double> range = this.register(Settings.d("Range", 5.5));
    private Setting<Boolean> wait = this.register(Settings.b("Wait", true));
    private Setting<Boolean> walls = this.register(Settings.b("Walls", false));
    private Setting<Boolean> sharpness = this.register(Settings.b("32k Switch", false));
    private Setting<Boolean> doublehit = this.register(Settings.b("SquidLauncer Attack", false));

    @Override
    public void onUpdate() {
        boolean shield;
        if (Aura.mc.player.isDead) {
            return;
        }
        boolean bl = shield = Aura.mc.player.getHeldItemOffhand().getItem().equals(Items.SHIELD) && Aura.mc.player.getActiveHand() == EnumHand.OFF_HAND;
        if (Aura.mc.player.isHandActive() && !shield) {
            return;
        }
        if (this.wait.getValue().booleanValue()) {
            if (Aura.mc.player.getCooledAttackStrength(this.getLagComp()) < 1.0f) {
                return;
            }
            if (Aura.mc.player.ticksExisted % 2 != 0) {
                return;
            }
        }
        for (Entity target : Minecraft.getMinecraft().world.loadedEntityList) {
            if (!EntityUtil.isLiving(target) || target == Aura.mc.player || (double)Aura.mc.player.getDistance(target) > this.range.getValue() || ((EntityLivingBase)target).getHealth() <= 0.0f || ((EntityLivingBase)target).hurtTime != 0 && this.wait.getValue().booleanValue() || !this.walls.getValue().booleanValue() && !Aura.mc.player.canEntityBeSeen(target) && !this.canEntityFeetBeSeen(target)) continue;
            if (this.players.getValue().booleanValue() && target instanceof EntityPlayer && !Friends.isFriend(target.getName())) {
                this.attack(target);
                return;
            }
            if (!(EntityUtil.isPassive(target) ? this.animals.getValue() != false : EntityUtil.isMobAggressive(target) && this.mobs.getValue() != false)) continue;
            if (ModuleManager.isModuleEnabled("AutoTool")) {
                AutoTool.equipBestWeapon();
            }
            this.attack(target);
            return;
        }
    }

    private boolean checkSharpness(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            return false;
        }
        NBTTagList enchants = (NBTTagList)stack.getTagCompound().getTag("ench");
        for (int i = 0; i < enchants.tagCount(); ++i) {
            NBTTagCompound enchant = enchants.getCompoundTagAt(i);
            if (enchant.getInteger("id") != 16) continue;
            int lvl = enchant.getInteger("lvl");
            if (lvl < 16) break;
            return true;
        }
        return false;
    }

    private void attack(Entity e) {
        if (this.sharpness.getValue().booleanValue() && !this.checkSharpness(Aura.mc.player.getHeldItemMainhand())) {
            int newSlot = -1;
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = Aura.mc.player.inventory.getStackInSlot(i);
                if (stack == ItemStack.EMPTY || !this.checkSharpness(stack)) continue;
                newSlot = i;
                break;
            }
            if (newSlot == -1) {
                return;
            }
            Aura.mc.player.inventory.currentItem = newSlot;
        }
        Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.player, e);
        Aura.mc.player.swingArm(EnumHand.MAIN_HAND);
        if (this.doublehit.getValue().booleanValue()) {
            Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.player, e);
            Aura.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    private float getLagComp() {
        if (this.wait.getValue().booleanValue()) {
            return -(20.0f - LagCompensator.INSTANCE.getTickRate());
        }
        return 0.0f;
    }

    private boolean canEntityFeetBeSeen(Entity entityIn) {
        return Aura.mc.world.rayTraceBlocks(new Vec3d(Aura.mc.player.posX, Aura.mc.player.posX + (double)Aura.mc.player.getEyeHeight(), Aura.mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null;
    }
}

