
package com.zp.mobile91q.category;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 搜索字段列表
 */
public class SearchCondition implements Comparable<SearchCondition> {

    // 字段名 传给服务器的参数名
    private String fieldValue;

    // 字段名称 显示用
    private String fieldName;

    // 筛选条件显示顺序
    private int showNo;

    // 字段值列表
    private List<Item> ItemList;

    public SearchCondition(String fieldName, String fieldValue, int showNo) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.showNo = showNo;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public List<Item> getItemList() {
        return ItemList;
    }

    public int getShowNo() {
        return showNo;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setShowNo(int showNo) {
        this.showNo = showNo;
    }

    @Override
    public String toString() {
        return "SearchCondition [fieldValue=" + fieldValue + ", fieldName="
                + fieldName + ", ItemList="
                + Arrays.toString(ItemList.toArray()) + "]";
    }

    public void setItemList(List<Item> itemList) {
        ItemList = itemList;
    }

    public static class Item {
        // id
        private String id;

        // 名称
        private String name;

        //
        private String code;

        public Item(String id, String name, String code) {
            this.id = id;
            this.name = name;
            this.code = code;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return "Item [id=" + id + ", name=" + name + ", code=" + code + "]";
        }

    }

    @Override
    public int compareTo(SearchCondition another) {
        return ((Integer) this.getShowNo()).compareTo(another.getShowNo());
    }

}
