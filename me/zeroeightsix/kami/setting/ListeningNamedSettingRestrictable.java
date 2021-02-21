/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.setting;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.ListeningSettingRestrictable;
import me.zeroeightsix.kami.setting.Named;

public abstract class ListeningNamedSettingRestrictable<T>
extends ListeningSettingRestrictable<T>
implements Named {
    String name;

    public ListeningNamedSettingRestrictable(T value, Predicate<T> restriction, BiConsumer<T, T> consumer, String name) {
        super(value, restriction, consumer);
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}

