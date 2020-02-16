package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.Pointcut;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * @objective : 用于存储切面的Throwing方法
 * @date :2019/12/25- 19:27
 */
public class AspectJAfterThrowingAdvice extends AbstractAspectJAdvice {

    public AspectJAfterThrowingAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
    }

    // 由于是Throwing方法，所以应该try别的连接器方法，再执行
    // 执行拦截器中的方法
    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        try{
            return mi.proceed();
        }
        catch (Throwable t){
            invokeAdviceMethod();
            throw t;
        }
    }
}
