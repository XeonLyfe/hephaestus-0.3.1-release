/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.setting;

import me.zeroeightsix.kami.setting.ISetting;

public abstract class Setting<T>
implements ISetting<T> {
    T value;

    public Setting(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean setValue(T value) {
        this.value = value;
        return true;
    }
}

