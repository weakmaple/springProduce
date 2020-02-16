package org.litespring.core.type.classreading;

import org.litespring.core.type.ClassMetadata;
import org.litespring.util.ClassUtils;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.SpringAsmInfo;

/**
 * @objective : 判断类对象的属性（是否为接口，是否为抽象类，是否有继承父类，是否有实现接口）
 * @date :2019/11/22- 13:15
 */
public class ClassMetadataReadingVisitor extends ClassVisitor implements ClassMetadata {

    private String className;
    private boolean isInterface;
    private boolean isAbstract;
    private boolean isFinal;
    private String superClassName;
    private String[] interfaces;

    public ClassMetadataReadingVisitor(){
        super(SpringAsmInfo.ASM_VERSION);
    }

    public void visit(int version,int access,String name,String signature,String supername,String[] interfaces){
        /*
        * 参数说明
        * version：class的版本
        * access：用于计算class是否为接口，抽象类这类的
        * name：全类名
        * signature：
        * supername：存储父类的名字
        * interfaces：存放接口
        * */
        // 获得转化为.的class类路径
        this.className = ClassUtils.convertResourcePathToClassName(name);
        // 判断是否为接口
        this.isInterface = ((access & Opcodes.ACC_INTERFACE) != 0);
        // 判断是否为抽象类
        this.isAbstract = ((access & Opcodes.ACC_ABSTRACT) != 0);
        // 判断是否有final
        this.isFinal = ((access & Opcodes.ACC_FINAL) != 0);
        // 判断是否有继承父类
        if (supername != null){
            this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
        }
        // 判断是否有实现接口
        this.interfaces = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
        }
    }
    public String getClassName() {
        return this.className;
    }
    public boolean isInterface() {
        return this.isInterface;
    }

    public boolean isAbstract() {
        return this.isAbstract;
    }

    public boolean isConcrete() {
        return !(this.isInterface || this.isAbstract);
    }

    public boolean isFinal() {
        return this.isFinal;
    }


    public boolean hasSuperClass() {
        return (this.superClassName != null);
    }

    public String getSuperClassName() {
        return this.superClassName;
    }

    public String[] getInterfaceNames() {
        return this.interfaces;
    }
}
