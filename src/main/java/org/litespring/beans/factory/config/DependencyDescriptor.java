package org.litespring.beans.factory.config;

import org.litespring.util.Assert;

import java.lang.reflect.Field;

/**
 * @objective : 用于@Autowired，其中field为需要自动导入的属性，required为是否必要
 * @date :2019/11/29- 8:02
 */
public class DependencyDescriptor {
    private Field field;
    private boolean required;

    public DependencyDescriptor(Field field, boolean required) {
        Assert.notNull(field,"field must not be null");
        this.field = field;
        this.required = required;
    }
    public Class<?> getDependencyType(){
        if(this.field != null){
            return field.getType();
        }
        throw new RuntimeException("only support field dependency");
    }
    public boolean isRequired(){
        return this.required;
    }
}
