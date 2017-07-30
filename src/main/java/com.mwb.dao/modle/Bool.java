package com.mwb.dao.modle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MengWeiBo on 2017-03-29
 */
public enum Bool {
    Y(true, "Y", "Yes"),
    N(false, "N", "No");

    private static final Map<Boolean, Bool> value2Bool;
    private static final Map<String, Bool> code2Bool;

    private boolean value;
    private String code;
    private String description;

    static {
        value2Bool = new HashMap<Boolean, Bool>();
        code2Bool = new HashMap<String, Bool>();

        for (Bool bool : Bool.values()) {
            value2Bool.put(bool.getValue(), bool);
            code2Bool.put(bool.getCode(), bool);
        }
    }

    public static Bool fromValue(Boolean value) {
        if (value == null) {
            return null;
        }

        return value2Bool.get(value);
    }

    public static Bool fromCode(String code) {
        if (code == null) {
            return null;
        } else {
            code = code.toUpperCase();
        }

        return code2Bool.get(code);
    }

    Bool(boolean value, String code, String description) {
        this.value = value;
        this.code = code;
        this.description = description;
    }

    public boolean getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}

