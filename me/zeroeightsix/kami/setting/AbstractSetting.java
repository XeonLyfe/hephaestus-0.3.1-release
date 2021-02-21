/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.setting;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.SavableListeningNamedSettingRestrictable;

public abstract class AbstractSetting<T>
extends SavableListeningNamedSettingRestrictable<T> {
    private Predicate<T> visibilityPredicate;

    public AbstractSetting(T value, Predicate<T> restriction, BiConsumer<T, T> consumer, String name, Predicate<T> visibilityPredicate) {
        super(value, restriction, consumer, name);
        this.visibilityPredicate = visibilityPredicate;
    }

    @Override
    public boolean isVisible() {
        return this.visibilityPredicate.test(this.getValue());
    }
}

