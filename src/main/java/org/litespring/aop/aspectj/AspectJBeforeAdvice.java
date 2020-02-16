package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * @objective : 用于存储切面的before方法
 * @date :2019/12/25- 18:08
 */
public class AspectJBeforeAdvice extends AbstractAspectJAdvice {

    public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPointcut pc, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod,pc,adviceObjectFactory);
    }
    // 由于是Before方法，所以应该执行，再查看别的连接器方法
    // 执行拦截器中的方法
    @Override
    public Object invoke(MethodInvocation mi) throws Throwable{
        // 例如:调用TransactionManager的start方法
//        adviceMethod.invoke(adviceObject);
        this.invokeAdviceMethod();
        Object o = mi.proceed();
        return o;
    }
}
