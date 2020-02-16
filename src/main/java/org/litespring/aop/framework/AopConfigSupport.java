package org.litespring.aop.framework;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;
import org.litespring.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @objective : aop的配置类，实现 存储切面的方法
 * @date :2019/12/26- 13:51
 */
public class AopConfigSupport implements AopConfig {
    private boolean proxyTargetClass = false;
    // 目标对象（需要实现切面的类）
    private Object targetObject = null;
    // 用于存储切面的方法（例如：before，AfterReturning，AfterThrowing）
    private List<Advice> advices = new ArrayList<Advice>();
    private List<Class> interfaces = new ArrayList<Class>();

    public AopConfigSupport() {

    }

    @Override
    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }
    @Override
    public Object getTargetObject() {
        return this.targetObject;
    }

    @Override
    public Class<?> getTargetClass() {
        return this.targetObject.getClass();
    }

    public void addInterface(Class<?> intf){
        Assert.notNull(intf,"interface must not be null");
        if(!intf.isInterface()){
            throw new IllegalArgumentException("["+intf.getName()+"] is not an interface");
        }
        if(!this.interfaces.contains(intf)){
            this.interfaces.add(intf);
        }
    }

    @Override
    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class[this.interfaces.size()]);
    }

    @Override
    public boolean isInterfaceProxied(Class<?> intf) {
        for (Class proxyIntf : this.interfaces){
            if (intf.isAssignableFrom(proxyIntf)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void addAdvice(Advice advice) {
        this.advices.add(advice);
    }

    @Override
    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    @Override
    public List<Advice> getAdvices() {
        return this.advices;
    }

    // 检查给定方法是否符合各个切面方法的切面表达式
    @Override
    public List<Advice> getAdvices(Method method) {
        List<Advice> result = new ArrayList<Advice>();
        for(Advice advice : this.getAdvices()){
            Pointcut pc = advice.getPointcut();
            if(pc.getMethodMatcher().matches(method)){
                result.add(advice);
            }
        }
        return result;
    }
}
