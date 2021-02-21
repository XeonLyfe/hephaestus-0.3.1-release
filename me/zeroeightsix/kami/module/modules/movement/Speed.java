//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

@Module.Info(name="Speed", description="Modify player speed on ground.", category=Module.Category.MOVEMENT)
public class Speed
extends Module {
    private Setting<Float> speed = this.register(Settings.f("Speed", 0.0f));

    @Override
    public void onUpdate() {
        if ((Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f) && !Speed.mc.player.isSneaking() && Speed.mc.player.onGround) {
            Speed.mc.player.jump();
            Speed.mc.player.motionX *= 1.4;
            Speed.mc.player.motionY *= 0.4;
            Speed.mc.player.motionZ *= 1.4;
        }
    }
}

