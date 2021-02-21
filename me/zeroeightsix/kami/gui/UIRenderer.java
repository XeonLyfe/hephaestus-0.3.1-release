//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package me.zeroeightsix.kami.gui;

import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.gui.kami.DisplayGuiScreen;
import me.zeroeightsix.kami.gui.kami.KamiGUI;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Frame;
import me.zeroeightsix.kami.gui.rgui.component.listen.RenderListener;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class UIRenderer {
    public static void renderAndUpdateFrames() {
        if (Wrapper.getMinecraft().currentScreen instanceof DisplayGuiScreen || Wrapper.getMinecraft().gameSettings.showDebugInfo) {
            return;
        }
        KamiGUI gui = KamiMod.getInstance().getGuiManager();
        GL11.glDisable((int)3553);
        for (Component c : gui.getChildren()) {
            if (!(c instanceof Frame)) continue;
            GlStateManager.pushMatrix();
            Frame child = (Frame)c;
            if (child.isPinned() && child.isVisible()) {
                boolean slide = child.getOpacity() != 0.0f;
                GL11.glTranslated((double)child.getX(), (double)child.getY(), (double)0.0);
                child.getRenderListeners().forEach(renderListener -> renderListener.onPreRender());
                child.getTheme().getUIForComponent(child).renderComponent(child, child.getTheme().getFontRenderer());
                int translateX = 0;
                int translateY = 0;
                if (slide) {
                    translateX += child.getOriginOffsetX();
                    translateY += child.getOriginOffsetY();
                } else {
                    if (child.getDocking().isBottom()) {
                        translateY += child.getOriginOffsetY();
                    }
                    if (child.getDocking().isRight()) {
                        translateX += child.getOriginOffsetX();
                        if (child.getChildren().size() > 0) {
                            translateX += (child.getWidth() - child.getChildren().get(0).getX() - child.getChildren().get(0).getWidth()) / DisplayGuiScreen.getScale();
                        }
                    }
                    if (child.getDocking().isLeft() && child.getChildren().size() > 0) {
                        translateX -= child.getChildren().get(0).getX();
                    }
                    if (child.getDocking().isTop() && child.getChildren().size() > 0) {
                        translateY -= child.getChildren().get(0).getY();
                    }
                }
                GL11.glTranslated((double)translateX, (double)translateY, (double)0.0);
                child.getRenderListeners().forEach(RenderListener::onPostRender);
                child.renderChildren();
                GL11.glTranslated((double)(-translateX), (double)(-translateY), (double)0.0);
                GL11.glTranslated((double)(-child.getX()), (double)(-child.getY()), (double)0.0);
            }
            GlStateManager.popMatrix();
        }
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3042);
        GlStateManager.enableBlend();
    }
}

