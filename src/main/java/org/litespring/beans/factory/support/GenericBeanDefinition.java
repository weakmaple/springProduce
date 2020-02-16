package org.litespring.beans.factory.support;


import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @objective : bean定义接口的实现类
 * @date :2019/11/12- 9:55
 */
public class GenericBeanDefinition implements BeanDefinition {

    private String id;
    private String beanClassName;
    private Class<?> beanClass;
    // bean默认为单例模式
    private boolean singleton = true;
    private boolean prototype = false;
    private String scope = SCOPE_DEFAULT;
    // 获取属性列表 （即<property/>列表）
    List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
    // ConstructorArgument类中有个list来 获得构造器列表（即<constructor-arg/>列表）
    private ConstructorArgument constructorArgument = new ConstructorArgument();

    // 表明这个Bean定义是不是我们litespring合成的
    private boolean isSynthetic = false;


    public GenericBeanDefinition() {

    }

    public GenericBeanDefinition(Class<?> clz){
        this.beanClass = clz;
        this.beanClassName = clz.getName();
    }

    public GenericBeanDefinition(String id, String beanClassName){
        this.id = id;
        this.beanClassName = beanClassName;
    }

    @Override
    public boolean isSynthetic() {
        return isSynthetic;
    }

    public void setSynthetic(boolean synthetic) {
        isSynthetic = synthetic;
    }

    @Override
    public String getBeanClassName(){
        return this.beanClassName;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    @Override
    public boolean isPrototype() {
        return this.prototype;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
        // 如果是默认或者单例模式，设置为单例模式
        this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return this.propertyValues;
    }

    @Override
    public ConstructorArgument getConstructorArgument() {
        return this.constructorArgument;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgument.isEmpty();
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        String className = getBeanClassName();
        if(className == null){
            return null;
        }
        Class<?> resolvedClass = classLoader.loadClass(className);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    @Override
    public Class<?> getBeanClass() throws IllegalStateException {
        if(this.beanClass == null){
            throw new IllegalStateException("Bean Class name ["+this.getBeanClassName()+"] has not been resolved into");
        }
        return this.beanClass;
    }

    @Override
    public boolean hasBeanClass() {
        return this.beanClass != null;
    }
}
