package com.crawler.model;

import com.crawler.model.market.Type;

public class Ratio {
    private final String betVariation;
    private final Double value;
    private final Double koef;
    private final transient Type type;

    public Ratio(String betVariation, Double value, Double koef, Type type) {
        this.betVariation = betVariation;
        this.value = value;
        this.koef = koef;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ratio ratio = (Ratio) o;

        if (!betVariation.equals(ratio.betVariation)) return false;
        if (value != null ? !value.equals(ratio.value) : ratio.value != null) return false;
        if (!koef.equals(ratio.koef)) return false;
        return type == ratio.type;
    }

    @Override
    public int hashCode() {
        int result = betVariation.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + koef.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
