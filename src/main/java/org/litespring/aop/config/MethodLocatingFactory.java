package org.litespring.aop.config;

import org.litespring.beans.BeanUtils;
import org.litespring.beans.FactoryBean;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.BeanFactoryAware;
import org.litespring.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @objective : 从factory中获取到bean的class，再查看bean中是否有我们需要的方法
 * @date :2019/12/24- 19:18
 */
public class MethodLocatingFactory implements FactoryBean<Method>, BeanFactoryAware {
    // 目标bean的名字
    private String targetBeanName;
    // 目标方法的名字
    private String methodName;
    // 目标方法
    private Method method;

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    // 从factory中获取到bean的class，再查看bean中是否有我们需要的方法
    public void setBeanFactory(BeanFactory beanFactory) {
        // 判断targetBeanName和methodName是否已经存在
        if (!StringUtils.hasText(this.targetBeanName)) {
            throw new IllegalArgumentException("Property 'targetBeanName' is required");
        }
        if (!StringUtils.hasText(this.methodName)) {
            throw new IllegalArgumentException("Property 'methodName' is required");
        }
        // 获取到目标bean的class
        Class<?> beanClass = beanFactory.getType(this.targetBeanName);
        if (beanClass == null) {
            throw new IllegalArgumentException("Can't determine type of bean with name '" + this.targetBeanName + "'");
        }

        // 获取到目标方法
        this.method = BeanUtils.resolveSignature(this.methodName, beanClass);
        if (this.method == null) {
            throw new IllegalArgumentException("Unable to locate method [" + this.methodName +
                    "] on bean [" + this.targetBeanName + "]");
        }
    }

    public Method getObject() throws Exception {
        return this.method;
    }

    @Override
    public Class<?> getObjectType() {
        return Method.class;
    }
}
