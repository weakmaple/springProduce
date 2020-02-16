package org.litespring.context.annotation;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.BeanNameGenerator;
import org.litespring.core.io.Resource;
import org.litespring.core.io.support.PackageResourceLoader;
import org.litespring.core.type.classreading.MetadataReader;
import org.litespring.core.type.classreading.SimpleMetadataReader;
import org.litespring.stereotype.Component;
import org.litespring.util.StringUtils;

/**
 * @objective : 对context:component-scan指定的包进行扫描，并注入factory中
 * @author Administrator
 *
 */
public class ClassPathBeanDefinitionScanner {

	// bean定义接口
	private final BeanDefinitionRegistry registry;
	// 扫描包内的bean（dao，service，controller）
	private PackageResourceLoader resourceLoader = new PackageResourceLoader();
	// 日志
	protected final Log logger = LogFactory.getLog(getClass());
    // context:component-scan扫描出来的包没有id，需要我们给他造一个
	private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

	public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {		
		this.registry = registry;		
	}
	// 解析传入地址下的文件是否有component注解，有的话，将其生成为BeanDefinition，
	// 设置好bean的id，再将其放入factory中存储BeanDefinition的Map中
	public Set<BeanDefinition> doScan(String packagesToScan) {
		//将传进来的字符串用,分割成数组。比如"org.litespring.service.v4,org.litespring.dao.v4";
		String[] basePackages = StringUtils.tokenizeToStringArray(packagesToScan,",");
		// 用于存储bean
		Set<BeanDefinition> beanDefinitions = new LinkedHashSet<BeanDefinition>();
		for (String basePackage : basePackages) {
			//拿到可成为候选的Component（用于存储context:component-scan生成的bean）
			Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
			for (BeanDefinition candidate : candidates) {
				beanDefinitions.add(candidate);
				//注册BeanDefinition
				registry.registerBeanDefinition(candidate.getID(),candidate);
			}
		}
		return beanDefinitions;
	}

	// 解析传输过来的地址（basePackage）下的类，解析类中的注解
	public Set<BeanDefinition> findCandidateComponents(String basePackage) {
	    // 存储BeanDefinition的set
		Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
		try {
			Resource[] resources = this.resourceLoader.getResources(basePackage);
			for (Resource resource : resources) {
				try {
					//读取Resource中的注解，并将注解的相关信息存放在ClassMetadata和AnnotationMetadata中
					MetadataReader metadataReader = new SimpleMetadataReader(resource);
				    // 如果存在Component注解
					if(metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())){
						//ScannedGenericBeanDefinition用于存放注解元信息，这里设置bean的id为全类名
						ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader.getAnnotationMetadata());
						//生成beanName,beanName作为BeanDefinition的ID
						String beanName = this.beanNameGenerator.generateBeanName(sbd, this.registry);
						sbd.setId(beanName);
						candidates.add(sbd);					
					}
				}
				catch (Throwable ex) {
					throw new BeanDefinitionStoreException(
							"Failed to read candidate component class: " + resource, ex);
				}
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		}
		return candidates;
	}
}
