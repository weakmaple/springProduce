package org.litespring.beans.factory.support;

import org.litespring.beans.factory.config.SingletonBeanRegistry;
import org.litespring.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @objective : 单例模式注册的类
 * @date :2019/11/14- 13:18
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    // 记录bean为单例模式的map
    private final Map<String,Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);

    // 注册单例模式的bean
    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        // 断言 beanName不为空
        Assert.notNull(beanName,"'beanName' must not be null");
        // 判断这个bean是否以及存在，由于单例模式只能存在一个完全相同的bean，所以报错
        Object oldObject = this.singletonObjects.get(beanName);
        if(oldObject != null){
            throw new IllegalStateException("Could not register object {" + singletonObject +
                    "} under bean name '"+beanName+"': there is already object {"+oldObject+"}");
        }
        // 注册的bean是不存在的，添加到map中
        this.singletonObjects.put(beanName,singletonObject);
    }

    // 获得单例模式的bean
    @Override
    public Object getSingleton(String beanName) {
        // 从map中获取单例模式的bean
        return this.singletonObjects.get(beanName);
    }
}
