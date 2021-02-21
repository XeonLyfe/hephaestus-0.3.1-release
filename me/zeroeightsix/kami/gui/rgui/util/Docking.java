/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.gui.rgui.util;

public enum Docking {
    TOPLEFT(true, true, false, false),
    TOP(true, false, false, false),
    TOPRIGHT(true, false, false, true),
    RIGHT(false, false, false, true),
    BOTTOMRIGHT(false, false, true, true),
    BOTTOM(false, false, true, false),
    BOTTOMLEFT(false, true, true, false),
    LEFT(false, true, false, false),
    CENTER(false, false, false, false),
    NONE(false, false, false, false);

    boolean isTop;
    boolean isLeft;
    boolean isBottom;
    boolean isRight;

    private Docking(boolean isTop, boolean isLeft, boolean isBottom, boolean isRight) {
        this.isTop = isTop;
        this.isLeft = isLeft;
        this.isBottom = isBottom;
        this.isRight = isRight;
    }

    public boolean isBottom() {
        return this.isBottom;
    }

    public boolean isLeft() {
        return this.isLeft;
    }

    public boolean isRight() {
        return this.isRight;
    }

    public boolean isTop() {
        return this.isTop;
    }
}

