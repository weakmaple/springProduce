package org.litespring.test.v5;

import java.lang.reflect.Method;
import org.junit.Test;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.NoOp;

/**
 * @objective : 5
 * @date :2019/12/25- 19:56
 */
public class CGLibTest {

    // 测试cglib动态代理，是对代理对象类的class文件加载进来，通过修改其字节码生成子类来处理
    // 该代理会对被代理类任何方法被调用时都执行拦截
    @Test
    public void testCallBack(){
        // 增强器 类似于factory
        Enhancer enhancer = new Enhancer();
        // 由于cglib是生成子类进行强化的，所以这里要设置父类
        enhancer.setSuperclass(PetStoreService.class);
        //设置拦截器
        enhancer.setCallback(new TransactionInterceptor());
        // 这里生成的类已经是代理对象了，即PetStoreService的子类
        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();
        petStore.toString();
    }

    // 拦截器
    public static class TransactionInterceptor implements MethodInterceptor {
        TransactionManager txManager = new TransactionManager();
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            txManager.start();
            //真正的业务方法
            Object result = proxy.invokeSuper(obj, args);
            txManager.commit();
            return result;
        }
    }

    // 测试只对需要进行拦截的方法进行拦截
    // 这里测试只对place开头的函数进行拦截
    // 所以对toString函数不拦截
    @Test
    public void  testFilter(){
        // 增强器 类似于factory
        Enhancer enhancer = new Enhancer();
        // 由于cglib是生成子类进行强化的，所以这里要设置父类
        enhancer.setSuperclass(PetStoreService.class);

        enhancer.setInterceptDuringConstruction(false);

        // 设置两个代理类
        Callback[] callbacks = new Callback[]{new TransactionInterceptor(),NoOp.INSTANCE};
        Class<?>[] types = new Class<?>[callbacks.length];
        for (int x = 0; x < types.length; x++) {
            types[x] = callbacks[x].getClass();
        }
        // 设置过滤器
        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(types);

        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();
        System.out.println(petStore.toString());
    }
    // 过滤器
    private static class ProxyCallbackFilter implements CallbackFilter {
        public ProxyCallbackFilter() {

        }
        public int accept(Method method) {
            if(method.getName().startsWith("place")){
                //为0则调用第0个拦截器TransactionInterceptor
                return 0;
            } else{
                //为1则调用第1个拦截器NoOp.INSTANCE
                return 1;
            }
        }
    }
}