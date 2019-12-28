
package com.zp.mobile91q.category;

/**
 * 用于搜索中title的业务bean类
 */
public class QuickSearchItem {
    // eg:最近更新
    private String name;

    // eg:2015
    private String code;

    // eg:year,搜索条件由type和code拼接year=2015
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "name: " + name + " code: " + code + " type: " + type;
    }
}
