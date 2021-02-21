//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

@Module.Info(name="NoEntityTrace", category=Module.Category.MISC, description="Blocks entities from stopping you from mining")
public class NoEntityTrace
extends Module {
    private Setting<TraceMode> mode = this.register(Settings.e("Mode", TraceMode.DYNAMIC));
    private static NoEntityTrace INSTANCE;

    public NoEntityTrace() {
        INSTANCE = this;
    }

    public static boolean shouldBlock() {
        return INSTANCE.isEnabled() && (NoEntityTrace.INSTANCE.mode.getValue() == TraceMode.STATIC || NoEntityTrace.mc.playerController.isHittingBlock);
    }

    private static enum TraceMode {
        STATIC,
        DYNAMIC;

    }
}

