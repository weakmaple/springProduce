package org.litespring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.aop.config.ConfigBeanDefinitionParser;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypeStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.context.annotation.ClassPathBeanDefinitionScanner;
import org.litespring.core.io.Resource;
import org.litespring.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @objective : 解析xml配置文件，并将bean放入存储bean的map，将bean的property放入存储property的map中
 *               之后叫交给DefaultBeanFactory处理
 * @date :2019/11/13- 12:52
 */
public class XmlBeanDefinitionReader {
    public static final String ID_ATTRIBUTE = "id";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String SCOPE_ATTRIBUTE = "scope";

    public static final String PROPERTY_ELEMENT = "property";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String NAME_ATTRIBUTE = "name";

    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
    public static final String TYPE_ATTRIBUTE = "type";
    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";
    public static final String AOP_NAMESPACE_URI = "http://www.springframework.org/schema/aop";
    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";


    BeanDefinitionRegistry registry;

    protected final Log logger = LogFactory.getLog(getClass());


    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry){
        this.registry = registry;
    }

    // 真正解析配置文件
    public void loadBeanDefinitions(Resource resource) {
        InputStream is = null;
        try{
            // 根据多态，resource可能获得的是ClassPathResource，或者是FileSystemResource
            is = resource.getInputStream();
            SAXReader reader = new SAXReader();
            // 读取xml文件为树形结构
            Document doc = reader.read(is);
            //获取根标签 （获得<beans>标签或者<context:component-scan>标签）
            Element root = doc.getRootElement();
            // 获取beans内标签的集合
            Iterator<Element> iter = root.elementIterator();
            // 遍历beans中（<bean>的内容/<context:component-scan>的内容）
            while (iter.hasNext()){
                Element ele = iter.next();
                String namespaceUri = ele.getNamespaceURI();
                if(this.isDefaultNamespace(namespaceUri)){
                    parserDefaultElement(ele);// 普通的bean
                }else if (this.isContextNamespace(namespaceUri)){
                    parseComponentElement(ele);// 例如<context:component-scan>
                }else if (this.isAOPNamespace(namespaceUri)){
                    parseAOPElement(ele); // 例如 <aop:config>
                }

            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document",e);
        } finally {
            // 关闭流
            if(is != null){
                try {
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    // 解析<aop:config>标签的内容
    private void parseAOPElement(Element ele){
        ConfigBeanDefinitionParser parser = new ConfigBeanDefinitionParser();
        parser.parse(ele,this.registry);
    }


    // 解析<context:component-scan>标签扫描到的bean
    private void parseComponentElement(Element ele) {
        String basePackages = ele.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.doScan(basePackages);
    }

    // 解析普通的bean标签
    private void parserDefaultElement(Element ele) {
        // 获取bean的id
        String id = ele.attributeValue(ID_ATTRIBUTE);
        // 获取bean的class
        String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
        // 创建BeanDefinition
        BeanDefinition bd = new GenericBeanDefinition(id,beanClassName);
        // 查看是否设置单例
        if(ele.attribute(SCOPE_ATTRIBUTE) != null){
            bd.setScope(ele.attributeValue(SCOPE_ATTRIBUTE));
        }
        // 解析构造器注入内容,即解析<constructor-arg/>标签
        parseConstructorArgElements(ele,bd);
        // 解析property，注入属性
        parsePropertyElement(ele,bd);
        // 将bean添加到存储bean的map中
        this.registry.registerBeanDefinition(id,bd);
    }

    // 解析property，注入属性
    public void parsePropertyElement(Element beanElem,BeanDefinition bd){
        // 获得所有property标签
        Iterator iter= beanElem.elementIterator(PROPERTY_ELEMENT);
        while (iter.hasNext()){
            Element propElem = (Element) iter.next();
            // 获取到name属性
            String propertyName = propElem.attributeValue(NAME_ATTRIBUTE);
            if (!StringUtils.hasLength(propertyName)){
                logger.fatal("Tag 'property' must have a 'name' attribute");
                return;
            }
            // 获取到value或者ref属性
            Object val = parsePropertyValue(propElem,bd,propertyName);
            PropertyValue pv = new PropertyValue(propertyName,val);
            bd.getPropertyValues().add(pv);
        }
    }
    // 获取到value或者ref属性
    private Object parsePropertyValue(Element ele, BeanDefinition bd, String propertyName) {
        String elementName = (propertyName != null)?"<property> element for property '"+propertyName+"'"
                :"<constructor-arg> element";
        // 检查是否有ref属性
        boolean hasRefAttribute = (ele.attribute(REF_ATTRIBUTE) != null);
        // 检查是否有value属性
        boolean hasValueAttribute = (ele.attribute(VALUE_ATTRIBUTE) != null);
        // 有ref属性的情况
        if(hasRefAttribute){
            // 获取到ref的值
            String refName = ele.attributeValue(REF_ATTRIBUTE);
            if(!StringUtils.hasText(refName)){
                logger.error(elementName+" contains empty 'ref' attribute");
            }
            // 封装成RuntimeBeanReference类型
            RuntimeBeanReference ref = new RuntimeBeanReference(refName);
            return ref;
        }else if(hasValueAttribute){
            // 有value属性的情况
            // 获取到value的值
            String valName = ele.attributeValue(VALUE_ATTRIBUTE);
            // 封装成TypeStringValue类型
            TypeStringValue valueHolder = new TypeStringValue(valName);
            return valueHolder;
        }else{
            throw new RuntimeException(elementName+" must specify a ref or value");
        }
    }

    // 解析构造器注入内容,即解析<constructor-arg/>标签
    private void parseConstructorArgElements(Element beanEle, BeanDefinition bd) {
        // beanEle.elementIterator(String name) 返回与name相匹配的内容
        Iterator iter = beanEle.elementIterator(CONSTRUCTOR_ARG_ELEMENT);
        // 存在<constructor-arg/>标签，进行解析
        while(iter.hasNext()){
            Element ele = (Element) iter.next();
            parseConstructorArgElement(ele,bd);
        }
    }

    // 真正解析<constructor-arg/>标签
    private void parseConstructorArgElement(Element ele,BeanDefinition bd){
        String typeAttr = ele.attributeValue(TYPE_ATTRIBUTE);
        String nameAttr = ele.attributeValue(NAME_ATTRIBUTE);
        Object value = parsePropertyValue(ele,bd,null);
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);

        // 获得type和name，在此项目中不涉及type和name，只涉及value
        if(StringUtils.hasLength(typeAttr)){
            valueHolder.setType(typeAttr);
        }
        if(StringUtils.hasLength(nameAttr)){
            valueHolder.setType(nameAttr);
        }
        // 装载进存储构造器的list中
        bd.getConstructorArgument().addArgumentValue(valueHolder);
    }
    // 判断是bean标签的命名空间
    public boolean isDefaultNamespace(String namespaceUri){
        return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
    }
    // 判断是<constructor-arg/>标签的命名空间
    public boolean isContextNamespace(String namespaceUri){
        return (!StringUtils.hasLength(namespaceUri) || CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }
    // 判断是<aop:config>标签的命名空间
    public boolean isAOPNamespace(String namespaceUri){
        return (!StringUtils.hasLength(namespaceUri) || AOP_NAMESPACE_URI.equals(namespaceUri));
    }
}
