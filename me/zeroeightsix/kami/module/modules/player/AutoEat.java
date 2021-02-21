//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.FoodStats
 */
package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;

@Module.Info(name="AutoEat", description="Automatically eat when hungry", category=Module.Category.PLAYER)
public class AutoEat
extends Module {
    private int lastSlot = -1;
    private boolean eating = false;

    private boolean isValid(ItemStack stack, int food) {
        return stack.getItem() instanceof ItemFood && 20 - food >= ((ItemFood)stack.getItem()).getHealAmount(stack);
    }

    @Override
    public void onUpdate() {
        if (this.eating && !AutoEat.mc.player.isHandActive()) {
            if (this.lastSlot != -1) {
                AutoEat.mc.player.inventory.currentItem = this.lastSlot;
                this.lastSlot = -1;
            }
            this.eating = false;
            KeyBinding.setKeyBindState((int)AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), (boolean)false);
            return;
        }
        if (this.eating) {
            return;
        }
        FoodStats stats = AutoEat.mc.player.getFoodStats();
        if (this.isValid(AutoEat.mc.player.getHeldItemOffhand(), stats.getFoodLevel())) {
            AutoEat.mc.player.setActiveHand(EnumHand.OFF_HAND);
            this.eating = true;
            KeyBinding.setKeyBindState((int)AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), (boolean)true);
            mc.rightClickMouse();
        } else {
            for (int i = 0; i < 9; ++i) {
                if (!this.isValid(AutoEat.mc.player.inventory.getStackInSlot(i), stats.getFoodLevel())) continue;
                this.lastSlot = AutoEat.mc.player.inventory.currentItem;
                AutoEat.mc.player.inventory.currentItem = i;
                this.eating = true;
                KeyBinding.setKeyBindState((int)AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), (boolean)true);
                mc.rightClickMouse();
                return;
            }
        }
    }
}

