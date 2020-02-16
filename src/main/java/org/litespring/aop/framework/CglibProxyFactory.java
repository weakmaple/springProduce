package org.litespring.aop.framework;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.aop.Advice;
import org.litespring.util.Assert;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @objective : 生成目标对象的代理子类
 * @date :2019/12/26- 14:09
 */
@SuppressWarnings("serial")
public class CglibProxyFactory implements AopProxyFactory {

    // CGLIB回调数组索引的常量
    private static final int AOP_PROXY = 0;
    private static final int INVOKE_TARGET = 1;
    private static final int NO_OVERRIDE = 2;
    private static final int DISPATCH_TARGET = 3;
    private static final int DISPATCH_ADVISED = 4;
    private static final int INVOKE_EQUALS = 5;
    private static final int INVOKE_HASHCODE = 6;

    protected static final Log logger = LogFactory.getLog(CglibProxyFactory.class);
    protected final AopConfig config;
    private Object[] constructorArgs;
    private Class<?>[] constructorArgTypes;
    // 需要存在切面方法
    public CglibProxyFactory(AopConfig config) throws AopConfigException {
        Assert.notNull(config, "AdvisedSupport must not be null");
        if (config.getAdvices().size() == 0 /*&& config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE*/) {
            throw new AopConfigException("No advisors and no TargetSource specified");
        }
        this.config = config;
    }
	/*public void setConstructorArguments(Object[] constructorArgs, Class<?>[] constructorArgTypes) {
		if (constructorArgs == null || constructorArgTypes == null) {
			throw new IllegalArgumentException("Both 'constructorArgs' and 'constructorArgTypes' need to be specified");
		}
		if (constructorArgs.length != constructorArgTypes.length) {
			throw new IllegalArgumentException("Number of 'constructorArgs' (" + constructorArgs.length +
					") must match number of 'constructorArgTypes' (" + constructorArgTypes.length + ")");
		}
		this.constructorArgs = constructorArgs;
		this.constructorArgTypes = constructorArgTypes;
	}*/
    // 生成目标对象的代理子类
    public Object getProxy() {
        return getProxy(null);
    }
    // 生成目标对象的代理子类
    public Object getProxy(ClassLoader classLoader) {
        /*
        if (logger.isDebugEnabled()) {
            logger.debug("Creating CGLIB proxy: target source is " + this.config.getTargetClass());
        }
        */
        try {
            // 获取到目标对象
            Class<?> rootClass = this.config.getTargetClass();

            // 增强器 类似于factory
            Enhancer enhancer = new Enhancer();
            if (classLoader != null) {
                enhancer.setClassLoader(classLoader);
            }
            // 由于cglib是生成子类进行强化的，所以这里要设置父类
            enhancer.setSuperclass(rootClass);
            // 设置代理子类的命名规则
            enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE); //"BySpringCGLIB"
            enhancer.setInterceptDuringConstruction(false);
            // 获取到拦截器们
            Callback[] callbacks = getCallbacks(rootClass);
            Class<?>[] types = new Class<?>[callbacks.length];
            for (int x = 0; x < types.length; x++) {
                types[x] = callbacks[x].getClass();
            }
            // 设置过滤器
            enhancer.setCallbackFilter(new ProxyCallbackFilter(this.config));
            enhancer.setCallbackTypes(types);
            enhancer.setCallbacks(callbacks);

            // Generate the proxy class and create a proxy instance.
            Object proxy = enhancer.create();
			/*if (this.constructorArgs != null) {
				proxy = enhancer.create(this.constructorArgTypes, this.constructorArgs);
			}
			else {*/
            //proxy = enhancer.create();
            /*}*/
            return proxy;
        }
        catch (CodeGenerationException ex) {
            throw new AopConfigException("Could not generate CGLIB subclass of class [" +
                    this.config.getTargetClass() + "]: " +
                    "Common causes of this problem include using a final class or a non-visible class",
                    ex);
        }
        catch (IllegalArgumentException ex) {
            throw new AopConfigException("Could not generate CGLIB subclass of class [" +
                    this.config.getTargetClass() + "]: " +
                    "Common causes of this problem include using a final class or a non-visible class",
                    ex);
        }
        catch (Exception ex) {
            // TargetSource.getTarget() failed
            throw new AopConfigException("Unexpected AOP exception", ex);
        }
    }
	/*protected Enhancer createEnhancer() {
		return new Enhancer();
	}*/
    // 获取到拦截器们
    private Callback[] getCallbacks(Class<?> rootClass) throws Exception {
        // 拦截器
        Callback aopInterceptor = new DynamicAdvisedInterceptor(this.config);
        /*
        Callback targetInterceptor = new StaticUnadvisedExposedInterceptor(this.advised.getTargetObject());
        Callback targetDispatcher = new StaticDispatcher(this.advised.getTargetObject());
        */
        Callback[] callbacks = new Callback[] {
                aopInterceptor,  // AOP_PROXY for normal advice
                /*targetInterceptor,  // INVOKE_TARGET invoke target without considering advice, if optimized
                new SerializableNoOp(),  // NO_OVERRIDE  no override for methods mapped to this
                targetDispatcher,        //DISPATCH_TARGET
                this.advisedDispatcher,  //DISPATCH_ADVISED
                new EqualsInterceptor(this.advised),
                new HashCodeInterceptor(this.advised)*/
        };
        return callbacks;
    }
	/*private static Object processReturnType(Object proxy, Object target, Method method, Object retVal) {

		if (retVal != null && retVal == target && !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {

			retVal = proxy;
		}
		Class<?> returnType = method.getReturnType();
		if (retVal == null && returnType != Void.TYPE && returnType.isPrimitive()) {
			throw new AopInvocationException(
					"Null return value from advice does not match primitive return type for: " + method);
		}
		return retVal;
	}*/

    // 拦截器
    private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {

        private final AopConfig config;

        public DynamicAdvisedInterceptor(AopConfig advised) {
            this.config = advised;
        }
        // 在目标对象运行方法时发挥作用
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object target = this.config.getTargetObject();
            // 检查给定方法是否符合各个切面方法的切面表达式
            List<Advice> chain = this.config.getAdvices(method/*, targetClass*/);
            Object retVal;
            // 检查我们是否没有一个InvokerInterceptor：也就是说，没有真正的advice，
            // 只是对目标的反射调用。
            if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
                // 直接调用目标方法
                retVal = methodProxy.invoke(target, args);
            }
            else {
                List<org.aopalliance.intercept.MethodInterceptor> interceptors =
                        new ArrayList<org.aopalliance.intercept.MethodInterceptor>();
                interceptors.addAll(chain);
                // proceed方法开始执行切面方法
                retVal = new ReflectiveMethodInvocation(target, method, args, interceptors).proceed();
            }
            //retVal = processReturnType(proxy, target, method, retVal);
            return retVal;
        }
    }

    // 过滤器 ProxyCallbackFilter将回调分配给方法
    private static class ProxyCallbackFilter implements CallbackFilter {
        private final AopConfig config;

        public ProxyCallbackFilter(AopConfig advised) {
            this.config = advised;
        }
        // 这里是调用了第0个拦截器，因为这里做了简化
        // 拦截器列表之后一个拦截器（在第0个位置）
        public int accept(Method method) {
            // 注意，这里做了简化
            return AOP_PROXY;
        }
    }
}
