//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.zeroeightsix.kami.gui.kami.theme.kami;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.gui.kami.component.ActiveModules;
import me.zeroeightsix.kami.gui.rgui.component.AlignedComponent;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.util.Wrapper;
import org.lwjgl.opengl.GL11;

public class KamiActiveModulesUI
extends AbstractComponentUI<ActiveModules> {
    @Override
    public void renderComponent(ActiveModules component, FontRenderer f) {
        GL11.glDisable((int)2884);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3553);
        FontRenderer renderer = Wrapper.getFontRenderer();
        List mods = ModuleManager.getModules().stream().filter(Module::isEnabled).sorted(Comparator.comparing(module -> renderer.getStringWidth(module.getName() + (module.getHudInfo() == null ? "" : module.getHudInfo() + " ")) * (component.sort_up ? -1 : 1))).collect(Collectors.toList());
        int[] y = new int[]{2};
        if (component.getParent().getY() < 26 && Wrapper.getPlayer().getActivePotionEffects().size() > 0 && component.getParent().getOpacity() == 0.0f) {
            y[0] = Math.max(component.getParent().getY(), 26 - component.getParent().getY());
        }
        float[] hue = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        boolean lAlign = component.getAlignment() == AlignedComponent.Alignment.LEFT;
        mods.stream().forEach(module -> {
            int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
            String s = module.getHudInfo();
            String text = module.getName() + (s == null ? "" : " " + Command.SECTIONSIGN() + "7" + s);
            int textwidth = renderer.getStringWidth(text);
            int textheight = renderer.getFontHeight() + 1;
            int red = rgb >> 16 & 0xFF;
            int green = rgb >> 8 & 0xFF;
            int blue = rgb & 0xFF;
            renderer.drawStringWithShadow(!lAlign ? component.getWidth() - textwidth : 0, y[0], red, green, blue, text);
            hue[0] = hue[0] + 0.02f;
            y[0] = y[0] + textheight;
        });
        component.setHeight(y[0]);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)3042);
    }

    @Override
    public void handleSizeComponent(ActiveModules component) {
        component.setWidth(100);
        component.setHeight(100);
    }
}

