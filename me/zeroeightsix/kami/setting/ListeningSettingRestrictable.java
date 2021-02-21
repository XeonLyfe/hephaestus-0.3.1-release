/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.setting;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.SettingRestrictable;

public class ListeningSettingRestrictable<T>
extends SettingRestrictable<T> {
    private BiConsumer<T, T> consumer;

    public ListeningSettingRestrictable(T value, Predicate<T> restriction, BiConsumer<T, T> consumer) {
        super(value, restriction);
        this.consumer = consumer;
    }

    @Override
    public BiConsumer<T, T> changeListener() {
        return this.consumer;
    }

    @Override
    public boolean setValue(T value) {
        Object old = this.getValue();
        boolean b = super.setValue(value);
        if (b) {
            this.consumer.accept(old, value);
        }
        return b;
    }
}

