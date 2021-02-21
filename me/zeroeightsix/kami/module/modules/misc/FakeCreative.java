//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.GameType
 */
package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.Module;
import net.minecraft.world.GameType;

@Module.Info(name="FakeCreative", description="Fake GMC", category=Module.Category.MISC)
public class FakeCreative
extends Module {
    @Override
    public void onEnable() {
        FakeCreative.mc.playerController.setGameType(GameType.CREATIVE);
    }

    @Override
    public void onDisable() {
        FakeCreative.mc.playerController.setGameType(GameType.SURVIVAL);
    }
}

