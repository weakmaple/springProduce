package org.litespring.beans;

/**
 * @objective : 封装RuntimeBeanReference和TypeStringValue类型，PropertyValue只有两个属性，
 * 第一个是这个<property>标签的name属性，第二个是RuntimeBeanReference和TypeStringValue类型
 * @date :2019/11/15- 12:39
 */
public class PropertyValue {
    /*
    * 这里的converted和convertedValue
    * 表示的是否可转换
    * 在我们获得property的时候，如果是ref，那么应该把它转换为真实的bean，而不是类路径
    * 所以这个时候converted应该为true，而convertedValue为真实的bean
    * 如果是value属性，那么converted应该为false
    * */
    private final String name;
    private final Object value;
    private boolean converted = false;
    private Object convertedValue;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public synchronized boolean isConverted(){
        return this.converted;
    }

    public synchronized void setConvertedValue(Object value){
        this.converted = true;
        this.convertedValue = value;
    }

    public synchronized Object getConvertedValue(){
        return this.convertedValue;
    }
}
