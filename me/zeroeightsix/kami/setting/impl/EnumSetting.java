/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Converter
 */
package me.zeroeightsix.kami.setting.impl;

import com.google.common.base.Converter;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.AbstractSetting;
import me.zeroeightsix.kami.setting.converter.EnumConverter;

public class EnumSetting<T extends Enum>
extends AbstractSetting<T> {
    private EnumConverter converter;
    public final Class<? extends Enum> clazz;

    public EnumSetting(T value, Predicate<T> restriction, BiConsumer<T, T> consumer, String name, Predicate<T> visibilityPredicate, Class<? extends Enum> clazz) {
        super(value, restriction, consumer, name, visibilityPredicate);
        this.converter = new EnumConverter(clazz);
        this.clazz = clazz;
    }

    public Converter converter() {
        return this.converter;
    }
}

