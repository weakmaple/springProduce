package org.litespring.util;

/**
 * @objective :
 * @date :2019/11/14- 8:03
 */
public class Assert {
    public static void notNull(Object object,String message){
        if (object == null){
            throw new IllegalArgumentException(message);
        }
    }
}
