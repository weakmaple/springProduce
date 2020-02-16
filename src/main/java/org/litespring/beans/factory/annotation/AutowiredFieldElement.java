package org.litespring.beans.factory.annotation;

import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @objective : 真正进行注入的函数，利用反射完成注入
 * @date :2019/12/15- 20:54
 */
public class AutowiredFieldElement extends InjectionElement {
    boolean required;

    public AutowiredFieldElement(Field f, boolean required,AutowireCapableBeanFactory factory){
        super(f, factory);
        this.required = required;
    }

    public Field getField(){
        return (Field) this.member;
    }

    // 真正进行注入的函数
    @Override
    public void inject(Object target) {
        Field field = this.getField();
        try{
            // 创建出需要注入的类
            DependencyDescriptor desc = new DependencyDescriptor(field,this.required);
            Object value = factory.resolveDependency(desc);
            if(value != null){
                // 利用反射完成setting注入
                ReflectionUtils.makeAccessible(field);
                field.set(target,value);
            }
        }catch (Throwable ex){
            throw new BeanCreationException("Could not autowire field: "+field,ex);
        }
    }
}
