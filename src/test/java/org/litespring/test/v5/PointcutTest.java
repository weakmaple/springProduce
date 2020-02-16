package org.litespring.test.v5;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.aspectj.AspectJExpressionPointcut;
import org.litespring.service.v5.PetStoreService;

import java.lang.reflect.Method;

/**
 * @objective : 2
 * @date :2019/12/23- 23:14
 */
public class PointcutTest {

    // 测试 将表达式转化为结构化的表达式 然后判断输入的方法是否在这个表达式的表示范围之内
    @Test
    public void testPointCut() throws Exception{
        String expression = "execution(* org.litespring.service.v5.*.placeOrder(..))";
        AspectJExpressionPointcut pc = new AspectJExpressionPointcut();
        pc.setExpression(expression);

        MethodMatcher mm = pc.getMethodMatcher();
        {
            Class<?> targetClass = PetStoreService.class;

            Method method1 = targetClass.getMethod("placeOrder");
            Assert.assertTrue(mm.matches(method1));

            Method method2 = targetClass.getMethod("getAccountDao");
            Assert.assertFalse(mm.matches(method2));
        }
        {
            Class<?> targetClass = org.litespring.service.v4.PetStoreService.class;
            Method method = targetClass.getMethod("getAccountDao");
            Assert.assertFalse(mm.matches(method));
        }
    }
}
