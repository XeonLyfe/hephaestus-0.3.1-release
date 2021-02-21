/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.setting.impl;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.AbstractSetting;
import me.zeroeightsix.kami.setting.converter.BooleanConverter;

public class BooleanSetting
extends AbstractSetting<Boolean> {
    private static final BooleanConverter converter = new BooleanConverter();

    public BooleanSetting(Boolean value, Predicate<Boolean> restriction, BiConsumer<Boolean, Boolean> consumer, String name, Predicate<Boolean> visibilityPredicate) {
        super(value, restriction, consumer, name, visibilityPredicate);
    }

    public BooleanConverter converter() {
        return converter;
    }
}

