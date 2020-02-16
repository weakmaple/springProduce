package org.litespring.beans;

/**
 * @objective :
 * @date :2019/11/19- 7:37
 */
public interface TypeConverter {
        <T> T convertIfNecessary(Object o, Class<T> requiredType) throws TypeMismatchException;
}
