package org.litespring.beans.factory.annotation;

import org.litespring.beans.BeansException;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.litespring.core.annotation.AnnotationUtils;
import org.litespring.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @objective : 读取有Autowired注解的属性，将其保存在InjectionMetadata类中
 * @date :2019/12/15- 21:43
 */
public class AutowiredAnnotationProcessor implements InstantiationAwareBeanPostProcessor {

    private AutowireCapableBeanFactory beanFactory;
    private String requiredParameterName = "required";
    private boolean requiredParameterValue = true;

    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes =
            new LinkedHashSet<Class<? extends Annotation>>();

    public AutowiredAnnotationProcessor() {
        this.autowiredAnnotationTypes.add(Autowired.class);
    }

    // 读取有Autowired注解的属性，将其保存在InjectionMetadata类中
    public InjectionMetadata buildAutowiringMetadata(Class<?> clazz){
        // 用于存放需要Autowired（自动注入）的属性的List
        LinkedList<InjectionElement> elements = new LinkedList<InjectionElement>();
        Class<?> targetClass = clazz;
        do{
            // 用于存放需要Autowired（自动注入）的属性的List
            LinkedList<InjectionElement> currElements = new LinkedList<InjectionElement>();
            // 遍历类的属性（Filed）查看他们是否存在Autowired注解，
            for (Field field : targetClass.getDeclaredFields()) {
                Annotation ann = findAutowiredAnnotation(field);
                if(ann != null){
                    // 该属性不能是静态的
                    if(Modifier.isStatic(field.getModifiers())){
                        continue;
                    }
                    // 检查Autowired注解的属性（request=true）是什么状态
                    boolean required = determineRequiredStatus(ann);
                    currElements.add(new AutowiredFieldElement(field,required,beanFactory));
                }
            }
            // 方法注入暂不支持
            for (Method method : targetClass.getDeclaredMethods()) {
                // 处理方法载入
            }
            elements.addAll(0,currElements);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);
        return new InjectionMetadata(clazz,elements);
    }
    // 查看传入的Field是否存在Autowired注解
    private Annotation findAutowiredAnnotation(AccessibleObject ao){
        for (Class<? extends Annotation> type: this.autowiredAnnotationTypes){
            Annotation ann = AnnotationUtils.getAnnotation(ao,type);
            if (ann != null){
                return ann;
            }
        }
        return null;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    // 检查Autowired注解的属性（request=true）是什么状态
    protected boolean determineRequiredStatus(Annotation ann){
        try{
            Method method = ReflectionUtils.findMethod(ann.annotationType(),this.requiredParameterName);
            if (method == null){
                return true;
            }
            return (this.requiredParameterValue == (Boolean)ReflectionUtils.invokeMethod(method,ann));
        }catch (Exception ex){
            return true;
        }
    }

    @Override
    public Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object afterInstantiation(Object bean, String beanName) throws BeansException {
        return null;
    }
    // 完成属性注入
    @Override
    public void postProcessPropertyValues(Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = buildAutowiringMetadata(bean.getClass());
        try {
            metadata.inject(bean);
        }
        catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
        }
    }

    @Override
    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object afterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
