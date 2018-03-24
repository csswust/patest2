package com.csswust.patest2.utils;

/**
 * @author 杨顺丰
 */
public class SystemInfo {
    private String code;
    private String desc;
    private String value;

    public SystemInfo() {
        // TODO Auto-generated constructor stub
    }

    public SystemInfo(String code, String desc, Object value) {
        super();
        this.code = code;
        this.desc = desc;
        this.value = value.toString();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SystemInfo [code=" + code + ", desc=" + desc + ", value=" + value + "]";
    }
}
