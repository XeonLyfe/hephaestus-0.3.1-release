/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.setting;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.ListeningNamedSettingRestrictable;
import me.zeroeightsix.kami.setting.converter.Convertable;

public abstract class SavableListeningNamedSettingRestrictable<T>
extends ListeningNamedSettingRestrictable<T>
implements Convertable {
    public SavableListeningNamedSettingRestrictable(T value, Predicate<T> restriction, BiConsumer<T, T> consumer, String name) {
        super(value, restriction, consumer, name);
    }
}

