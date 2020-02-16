package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * @objective : 用于存储切面的Returning方法
 * @date :2019/12/25- 18:15
 */
public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice {

    public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPointcut pc, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod,pc,adviceObjectFactory);
    }
    // 由于是Returning方法，所以应该查看别的连接器方法，再执行
    // 执行拦截器中的方法
    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object o = mi.proceed();
        // 例如:调用TransactionManager的commit方法
//        adviceMethod.invoke(adviceObject);
        this.invokeAdviceMethod();
        return o;
    }
}
