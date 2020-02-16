package org.litespring.beans.factory.annotation;

import java.util.List;

/**
 * @objective : 总体控制注入的函数，循环遍历每个需要注入的field
 * @date :2019/12/15- 20:46
 */
public class InjectionMetadata {

    private final Class<?> targetClass;
    private List<InjectionElement> injectionElements;

    public InjectionMetadata(Class<?> targetClass,List<InjectionElement> injectionElements){
        this.targetClass = targetClass;
        this.injectionElements = injectionElements;
    }

    public List<InjectionElement> getInjectionElements() {
        return injectionElements;
    }

    // 进行各个Field的注入
    public void inject(Object target){
        if(injectionElements == null || injectionElements.isEmpty()){
            return;
        }
        for (InjectionElement ele : injectionElements) {
            ele.inject(target);
        }
    }
}
