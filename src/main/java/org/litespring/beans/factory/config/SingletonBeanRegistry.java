package org.litespring.beans.factory.config;

/**
 * @objective : 单例模式注册接口
 * @date :2019/11/14- 13:16
 */
public interface SingletonBeanRegistry {
    // 注册单例模式的bean
    void registerSingleton(String beanName, Object singletonObject);
    // 获得单例模式的bean
    Object getSingleton(String beanName);
}
