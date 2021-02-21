/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.setting;

import java.util.function.BiConsumer;

public interface ISetting<T> {
    public T getValue();

    public boolean setValue(T var1);

    public boolean isVisible();

    public BiConsumer<T, T> changeListener();
}

