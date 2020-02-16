package org.litespring.beans.factory.support;

import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypeStringValue;

/**
 * @objective : 获取封装为（RuntimeBeanReference或者TypeStringValue）的实例
 *               并从其中的（ref或者value）获取到bean或者String类型数值
 *               转化为Object类型
 * @date :2019/11/17- 10:50
 */
public class BeanDefinitionValueResolver {
    private final ConfigurableBeanFactory beanFactory;

    public BeanDefinitionValueResolver(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    // 解析封装的内容，提取属性，获取相应的值
    public Object resolverValueIfNecessary(Object value) {
        // 封装为RuntimeBeanReference，表示为ref属性提取出来的
        if(value instanceof RuntimeBeanReference){
            // 获取到ref的值
            RuntimeBeanReference ref = (RuntimeBeanReference)value;
            // 从ioc容器中直接获取到bean
            String refName = ref.getBeanName();
            Object bean = this.beanFactory.getBean(refName);
            return bean;
        }else if(value instanceof TypeStringValue){
            // 封装为TypeStringValue，表示为value属性提取出来的
            // 直接返回string类型的值
           return ((TypeStringValue)value).getValue();
        }else{
            throw new RuntimeException("the value "+value+" has not implemented");
        }
    }
}
