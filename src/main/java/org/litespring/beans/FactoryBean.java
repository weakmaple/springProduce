package org.litespring.beans;

/**
 * @objective :
 * @date :2020/1/2- 18:29
 */
public interface FactoryBean<T> {
    T getObject() throws Exception;
    Class<?> getObjectType();
}
