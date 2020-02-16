package org.litespring.beans;

import org.litespring.beans.propertyeditors.CustomBooleanEditor;
import org.litespring.beans.propertyeditors.CustomerNumberEditor;
import org.litespring.util.ClassUtils;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

/**
 * @objective : 进行类型转换
 * @date :2019/11/19- 7:37
 */
public class SimpleTypeConverter implements TypeConverter {
    private Map<Class<?>, PropertyEditor> defaultEditors;

    public SimpleTypeConverter() {
    }

    // 将Object类型的属性转化为他所对应类型的属性
    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
        // 如果可以直接转化，那么直接强制类型转化（包含是bean的类型或者是String类型）
        if(ClassUtils.isAssignableValue(requiredType,value)){
            return (T)value;
        }else{
            // 传入的值为String类型（但是需要的是别的类型，如int类型）
            if(value instanceof String){
                // 查看map中是否有可以转化的类型
                PropertyEditor editor = findDefaultEditor(requiredType);
                try{
                    // 进行类型转换
                    editor.setAsText((String)value);
                }catch (IllegalArgumentException e){
                    throw new TypeMismatchException(value,requiredType);
                }
                return (T)editor.getValue();
            }else{
                throw new RuntimeException("Todo: can't convert value for "+value+" class "+requiredType);
            }
        }
    }
    // 获取默认转化类型的类
    private PropertyEditor findDefaultEditor(Class<?> requiredType){
        PropertyEditor editor = this.getDefaultEditor(requiredType);
        // 要求转化的类型不存在或者还么有存入map中
        if(editor == null){
            throw new RuntimeException("Editor for "+requiredType+" has not been implements");
        }
        return editor;
    }

    // 获取到可转化类型
    private PropertyEditor getDefaultEditor(Class<?> requiredType) {
        if(this.defaultEditors == null){
            createDefaultEditors();
        }
        return this.defaultEditors.get(requiredType);
    }

    // 创建默认的可转化类型
    private void createDefaultEditors() {
        this.defaultEditors = new HashMap<Class<?>,PropertyEditor>(64);

        this.defaultEditors.put(boolean.class,new CustomBooleanEditor(false));
        this.defaultEditors.put(Boolean.class,new CustomBooleanEditor(true));

        this.defaultEditors.put(int.class,new CustomerNumberEditor(Integer.class,false));
        this.defaultEditors.put(Integer.class,new CustomerNumberEditor(Integer.class,true));
    }
}
