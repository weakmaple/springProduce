package org.litespring.beans.factory.support;

import org.apache.commons.beanutils.BeanUtils;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.NoSuchBeanDefinitionException;
import org.litespring.beans.factory.config.BeanPostProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @objective : 默认bean的实现
 * @date :2019/11/12- 9:41
 */
public class DefaultBeanFactory extends DefaultSingletonBeanRegistry
        implements ConfigurableBeanFactory,BeanDefinitionRegistry {

    // 用于存储BeanPostProcessor的List
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    // BeanDefinition存放的map
    private final Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,BeanDefinition>();
    // 类加载器
    private ClassLoader classLoader;

    public DefaultBeanFactory() {

    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor postProcessor) {
        this.beanPostProcessors.add(postProcessor);
    }

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    // 在map中添加bean
    @Override
    public void registerBeanDefinition(String beanID, BeanDefinition bd) {
        this.beanDefinitionMap.put(beanID,bd);
    }

    // 在map中获取bean
    @Override
    public BeanDefinition getBeanDefinition(String beanId) {
        return this.beanDefinitionMap.get(beanId);
    }

    // 给用户调用的获取bean
    @Override
    public Object getBean(String beanId) {
        // 在map中获取BeanDefinition（要有需要实现目标类的id和类路径）
        BeanDefinition bd = this.getBeanDefinition(beanId);
        // bean未注册，不存在
        if(bd == null){
            throw new BeanCreationException("Bean Definition does not exist");
        }
        // 判断是否为单例模式
        if(bd.isSingleton()){
            // 到这里表示以及在单例模式的map中注册过了，直接获取反射后实例化的bean
            Object bean = this.getSingleton(beanId);
            // bean不存在，进行创建
            if(bean == null){
                bean = createBean(bd);
                // 在单例模式的map中注册
                this.registerSingleton(beanId,bean);
            }
            return bean;
        }
        // 不为单例模式，创建一个新的bean返回
        return createBean(bd);

//        ClassLoader cl = this.getClassLoader();
//        String beanClassName = bd.getBeanClassName();
//        try{
//            Class<?> clz = cl.loadClass(beanClassName);
//            return clz.newInstance();
//        } catch (Exception e) {
//            throw new BeanCreationException("Bean Definition does not exist");
//        }
    }
    // 创建bean实例并设置属性
    private Object createBean(BeanDefinition bd){
        // 创建实例
        Object bean = instantiateBean(bd);
        // 设置属性
        populateBean(bd, bean);
//        populateBeanUseCommonBeanUtils(bd, bean);
        return bean;
    }

    // 创建bean
    private Object instantiateBean(BeanDefinition bd) {
        // 存在构造器
        if(bd.hasConstructorArgumentValues()){
            ConstructorResolver resolver = new ConstructorResolver(this);
            return resolver.autowireConstructor(bd);
        }else {
            // 加载类加载器
            ClassLoader cl = this.getClassLoader();
            // 获得类路径
            String beanClassName = bd.getBeanClassName();
            try{
                // 反射生成类
                Class<?> clz = cl.loadClass(beanClassName);
                return clz.newInstance();
            } catch (Exception e) {
                // bean的类路径错误或者不存在
                throw new BeanCreationException("create bean for "+beanClassName+" failed",e);
            }
        }
    }

    // 设置属性
    private void populateBean(BeanDefinition bd, Object bean) {
        // 遍历所有BeanPostProcessor，完成@Autowired注入
        for (BeanPostProcessor processor : beanPostProcessors) {
            if(processor instanceof InstantiationAwareBeanPostProcessor){
                ((InstantiationAwareBeanPostProcessor)processor).postProcessPropertyValues(bean,bd.getID());
            }
        }

        // 获取该bean的所有属性设置
        List<PropertyValue> pvs = bd.getPropertyValues();
        // 无属性，直接返回
        if(pvs == null || pvs.isEmpty()){
            return;
        }
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter converter = new SimpleTypeConverter();
        try{
            // 遍历属性
            for (PropertyValue pv : pvs) {
                // 获取属性的名字和类路径[ref]（或者值[value]）
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                // 获取到确切的值
                // 如果是ref类型的话，获取到是实例化后的类
                // 如果是value类型的话，获取到的是string类型的值
                Object resolvedValue = valueResolver.resolverValueIfNecessary(originalValue);
                // 反射执行set方法
                // 进行内省，了解其所有属性、公开的方法和事件
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                // 获取属性
                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    // 若查找的属性存在
                    if(pd.getName().equals(propertyName)){
                        // 转换为正确的类型
                        Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                        // 获取到属性的Setting方法并使用反射执行
                        pd.getWriteMethod().invoke(bean,convertedValue);
                        break;
                    }
                }
            }
        }catch (Exception ex){
            throw new BeanCreationException("Failed to obtain Beaninfo for class ["+bd.getBeanClassName()+"]");
        }
    }

    // 使用beanUtils设置属性（可用，暂时不用）
    private void populateBeanUseCommonBeanUtils(BeanDefinition bd, Object bean) {
        // 获取该bean的所有属性设置
        List<PropertyValue> pvs = bd.getPropertyValues();
        // 无属性，直接返回
        if(pvs == null || pvs.isEmpty()){
            return;
        }
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
        try{
            // 遍历属性
            for (PropertyValue pv : pvs) {
                // 获取属性的名字和类路径[ref]（或者值[value]）
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                // 获取到确切的值
                // 如果是ref类型的话，获取到是实例化后的类
                // 如果是value类型的话，获取到的是string类型的值
                Object resolvedValue = valueResolver.resolverValueIfNecessary(originalValue);
                BeanUtils.setProperty(bean,propertyName,resolvedValue);
            }
        }catch (Exception ex){
            throw new BeanCreationException("Failed to obtain Beaninfo for class ["+bd.getBeanClassName()+"]");
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public ClassLoader getClassLoader() {
        // 如果为null则选择默认的classLoader
        return (this.classLoader != null?this.classLoader: ClassUtils.getDefaultClassLoader());
    }

    // 对比factory中的bean的classloader，确定两者的类路径是否相同，并创建bean
    @Override
    public Object resolveDependency(DependencyDescriptor descriptor) {
        // 获取需要Autowired的属性
        Class<?> typeToMatch = descriptor.getDependencyType();
        // 从存放BeanDefinition的map中获取BeanDefinition
        for (BeanDefinition bd : beanDefinitionMap.values()) {
            // 让bean具备classloader
            resolveBeanClass(bd);
            Class<?> beanClass = bd.getBeanClass();
            // 判断两者的类路径是否相同
            if (typeToMatch.isAssignableFrom(beanClass)){
                return this.getBean(bd.getID());
            }
        }
        return null;
    }

    // 让bean一定有classLoader
    public void resolveBeanClass(BeanDefinition bd) {
        if(bd.hasBeanClass()){
            return;
        }else{
            try{
                bd.resolveBeanClass(this.getClassLoader());
            }catch (ClassNotFoundException e){
                throw new RuntimeException("can't load class:"+bd.getBeanClassName());
            }
        }
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        BeanDefinition bd = this.getBeanDefinition(name);
        if (bd == null){
            throw new NoSuchBeanDefinitionException(name);
        }
        resolveBeanClass(bd);
        return bd.getBeanClass();
    }
}
