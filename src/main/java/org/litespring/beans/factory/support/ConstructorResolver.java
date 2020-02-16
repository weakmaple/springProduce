package org.litespring.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @objective :
 * @date :2019/11/20- 12:53
 */
public class ConstructorResolver {
    protected final Log logger = LogFactory.getLog(getClass());
    private final ConfigurableBeanFactory beanFactory;

    public ConstructorResolver(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    public Object autowireConstructor(final BeanDefinition bd) {
        // 适合的构造器
        Constructor<?> constructorToUse = null;
        // 构造器的参数
        Object[] argsToUse = null;
        // 需要创建的bean
        Class<?> beanClass = null;

        try {
            beanClass = this.beanFactory.getClassLoader().loadClass(bd.getBeanClassName());
        }catch (ClassNotFoundException e){
            throw new BeanCreationException(bd.getID(),"Instantiation of bean failed, can't");
        }
        // 获得所有构造器
        Constructor<?>[] candidates = beanClass.getConstructors();
        // 获得参数的解析器
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory);
        // 获得这个bean的<constructor-arg/>标签
        ConstructorArgument cargs = bd.getConstructorArgument();
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();

        for (int i = 0; i < candidates.length; i++) {
            Class<?>[] parameterTypes = candidates[i].getParameterTypes();
            // 判断构造器参数个数和xml文件中<constructor-arg/>标签是否可以对上
            if (parameterTypes.length != cargs.getArgumentCount()){
                continue;
            }
            argsToUse = new Object[parameterTypes.length];
            boolean result = this.valuesMatchTypes(parameterTypes,
                    cargs.getArgumentValues(),
                    argsToUse,
                    valueResolver,
                    typeConverter);
            if(result){
                constructorToUse = candidates[i];
                break;
            }
        }
        if(constructorToUse == null){
            throw new BeanCreationException(bd.getID(),"can't find a apporiate constructor");
        }
        try{
            return constructorToUse.newInstance(argsToUse);
        } catch (Exception e) {
            throw new BeanCreationException(bd.getID(),"can't find a create instance using "+ e);
        }
    }

    private boolean valuesMatchTypes(Class<?>[] parameterTypes,
                                     List<ConstructorArgument.ValueHolder> valueHolders,
                                     Object[] argsToUse,
                                     BeanDefinitionValueResolver valueResolver,
                                     SimpleTypeConverter typeConverter) {

        for (int i = 0; i < parameterTypes.length; i++) {
            // 获取到之前保存在factory中的constructorArgument（保存<constructor-arg/>标签的Map）
            ConstructorArgument.ValueHolder valueHolder = valueHolders.get(i);
            Object originalValue = valueHolder.getValue();
            try{
                // 解析封装的内容，提取属性，获取相应的值
                Object resolverValue = valueResolver.resolverValueIfNecessary(originalValue);
                // 把resolverValue（此时为object）的bean或者string转化为对应类型
                Object convertedValue = typeConverter.convertIfNecessary(resolverValue,parameterTypes[i]);
                argsToUse[i] = convertedValue;
            }catch (Exception e){
                logger.error(e);
                return false;
            }
        }
        return true;
    }
}
