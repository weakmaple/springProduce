package org.litespring.beans.factory.config;

/**
 * @objective : 封装<property>标签/<constructor-arg>标签的value属性
 * @date :2019/11/15- 12:52
 */
public class TypeStringValue {
    private String value;

    public TypeStringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
