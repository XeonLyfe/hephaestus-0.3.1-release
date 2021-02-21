//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EnumPlayerModelParts
 */
package me.zeroeightsix.kami.module.modules.misc;

import java.util.Random;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.entity.player.EnumPlayerModelParts;

@Module.Info(name="SkinFlicker", description="Toggle the jacket layer rapidly for a cool skin effect", category=Module.Category.MISC)
public class SkinFlicker
extends Module {
    private Setting<FlickerMode> mode = this.register(Settings.e("Mode", FlickerMode.HORIZONTAL));
    private Setting<Integer> slowness = this.register(Settings.integerBuilder().withName("Slowness").withValue(2).withMinimum(1).build());
    private static final EnumPlayerModelParts[] PARTS_HORIZONTAL = new EnumPlayerModelParts[]{EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.HAT, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG, EnumPlayerModelParts.RIGHT_SLEEVE};
    private static final EnumPlayerModelParts[] PARTS_VERTICAL = new EnumPlayerModelParts[]{EnumPlayerModelParts.HAT, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.RIGHT_SLEEVE, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG};
    private Random r = new Random();
    private int len = EnumPlayerModelParts.values().length;

    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case RANDOM: {
                if (SkinFlicker.mc.player.ticksExisted % this.slowness.getValue() != 0) {
                    return;
                }
                SkinFlicker.mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.values()[this.r.nextInt(this.len)]);
                break;
            }
            case VERTICAL: 
            case HORIZONTAL: {
                int i = SkinFlicker.mc.player.ticksExisted / this.slowness.getValue() % (PARTS_HORIZONTAL.length * 2);
                boolean on = false;
                if (i >= PARTS_HORIZONTAL.length) {
                    on = true;
                    i -= PARTS_HORIZONTAL.length;
                }
                SkinFlicker.mc.gameSettings.setModelPartEnabled(this.mode.getValue() == FlickerMode.VERTICAL ? PARTS_VERTICAL[i] : PARTS_HORIZONTAL[i], on);
            }
        }
    }

    public static enum FlickerMode {
        HORIZONTAL,
        VERTICAL,
        RANDOM;

    }
}

