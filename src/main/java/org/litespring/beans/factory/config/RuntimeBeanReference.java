package org.litespring.beans.factory.config;

/**
 * @objective :封装<property>标签/<constructor-arg>标签的ref属性
 * @date :2019/11/15- 12:46
 */
public class RuntimeBeanReference {
    private final String beanName;

    public RuntimeBeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
