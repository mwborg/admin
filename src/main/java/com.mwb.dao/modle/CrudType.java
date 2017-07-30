package com.mwb.dao.modle;

import java.util.HashMap;
import java.util.Map;

public enum CrudType implements IdInterface {

    CREATE(1, "CREATE", "Create"), 
    READ(2, "READ", "Read"), 
    UPDATE(3, "UPDATE", "Update"), 
    DELETE(4, "DELETE", "Delete"), 
    COPY(5, "COPY", "Copy");

    private static final Map<String, CrudType> code2Bool;
    private static Map<String, CrudType> name2Bool;

    private int id;
    private String code;
    private String description;

    static {
        code2Bool = new HashMap<String, CrudType>();

        for (CrudType type : CrudType.values()) {
            code2Bool.put(type.getCode(), type);
        }
    }

    public static CrudType fromCode(String code) {
        return code2Bool.get(code);
    }

    CrudType(int id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    @Override
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
