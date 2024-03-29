/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.kami.RenderHelper;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.use.InputField;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootInputFieldUI<T extends InputField>
extends AbstractComponentUI<InputField> {
    @Override
    public void renderComponent(InputField component, FontRenderer fontRenderer) {
        GL11.glColor3f((float)0.33f, (float)0.22f, (float)0.22f);
        RenderHelper.drawFilledRectangle(0.0f, 0.0f, component.getWidth(), component.getHeight());
        GL11.glLineWidth((float)1.5f);
        GL11.glColor4f((float)1.0f, (float)0.33f, (float)0.33f, (float)0.6f);
        RenderHelper.drawRectangle(0.0f, 0.0f, component.getWidth(), component.getHeight());
    }

    @Override
    public void handleAddComponent(InputField component, Container container) {
        component.setWidth(200);
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
    }
}

