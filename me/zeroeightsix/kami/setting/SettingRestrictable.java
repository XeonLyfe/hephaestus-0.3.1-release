/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.setting;

import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.Setting;

public abstract class SettingRestrictable<T>
extends Setting<T> {
    private Predicate<T> restriction;

    public SettingRestrictable(T value, Predicate<T> restriction) {
        super(value);
        this.restriction = restriction;
    }

    @Override
    public boolean setValue(T value) {
        return this.restriction.test(value) && super.setValue(value);
    }
}

