package com.crawler.model.market;

public enum Type {
    X12("1X2"),
    DC("DC"),
    OU("OU"),
    BTS("BTS"),
    DNB("DNB"),
    CS("CS");

    private final String name;

    private Type(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}