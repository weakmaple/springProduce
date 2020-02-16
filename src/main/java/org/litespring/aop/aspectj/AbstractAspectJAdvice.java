package org.litespring.aop.aspectj;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;
import org.litespring.aop.config.AspectInstanceFactory;
import java.lang.reflect.Method;

/**
 * @objective : 将AspectJBeforeAdvice和AspectJExpressionPointcut相似的部分抽象出来
 * @date :2019/12/25- 19:14
 */
public abstract class AbstractAspectJAdvice implements Advice{
    //所通知/增强的方法
    protected Method adviceMethod;
    protected AspectJExpressionPointcut pointcut;
    //所通知/增强的方法所在的对象
    protected AspectInstanceFactory adviceObjectFactory;

    public AbstractAspectJAdvice(Method adviceMethod,
                                 AspectJExpressionPointcut pointcut,
                                 AspectInstanceFactory adviceObjectFactory){

        this.adviceMethod = adviceMethod;
        this.pointcut = pointcut;
        this.adviceObjectFactory = adviceObjectFactory;
    }
    //调用通知/增强代码
    public void invokeAdviceMethod() throws  Throwable{
        adviceMethod.invoke(adviceObjectFactory.getAspectInstance());
    }
    public Pointcut getPointcut(){
        return this.pointcut;
    }
    public Method getAdviceMethod() {
        return adviceMethod;
    }
}
