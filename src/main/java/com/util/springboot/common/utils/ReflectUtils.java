package com.util.springboot.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public abstract class ReflectUtils {

    public static final Class<?>[] EMPTY_PARAM_TYPES = new Class<?>[0];
    public static final Object[] EMPTY_PARAMS = new Object[0];

	/* ************************************************** 字段相关的方法 ******************************************************* */

    /**
     * 从指定的类中获取指定的字段
     *
     * @param sourceClazz         指定的类
     * @param fieldName           要获取的字段的名字
     * @param isFindDeclaredField 是否查找Declared字段
     * @param isUpwardFind        是否向上去其父类中寻找
     * @return Field
     */
    public static Field getField(Class<?> sourceClazz, String fieldName, boolean isFindDeclaredField, boolean isUpwardFind) {
        Field field = null;
        try {
            field = isFindDeclaredField ? sourceClazz.getDeclaredField(fieldName) : sourceClazz.getField(fieldName);
        } catch (NoSuchFieldException e1) {
            if (isUpwardFind) {
                Class<?> clazz = sourceClazz.getSuperclass();
                while (field == null && clazz != null) {
                    try {
                        field = isFindDeclaredField ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
                    } catch (NoSuchFieldException e11) {
                        clazz = clazz.getSuperclass();
                    }
                }
            }
        }
        return field;
    }

    /**
     * 从指定的类中获取指定的字段，默认获取Declared类型的字段、向上查找
     *
     * @param sourceClazz 指定的类
     * @param fieldName   要获取的字段的名字
     * @return Field
     */
    public static Field getField(Class<?> sourceClazz, String fieldName) {
        return getField(sourceClazz, fieldName, true, true);
    }

    /**
     * 获取给定类的所有字段
     *
     * @param sourceClazz         给定的类
     * @param isGetDeclaredField  是否需要获取Declared字段
     * @param isGetParentField    是否需要把其父类中的字段也取出
     * @param isGetAllParentField 是否需要把所有父类中的字段全取出
     * @param isDESCGet           在最终获取的列表里，父类的字段是否需要排在子类的前面。只有需要把其父类中的字段也取出时此参数才有效
     * @return 给定类的所有字段
     */
    public static List<Field> getFields(Class<?> sourceClazz, boolean isGetDeclaredField, boolean isGetParentField, boolean isGetAllParentField, boolean isDESCGet) {
        List<Field> fieldList = new ArrayList<>();
        //如果需要从父类中获取
        if (isGetParentField) {
            //获取当前类的所有父类
            List<Class<?>> clazzList;
            if (isGetAllParentField) {
                clazzList = getSuperClass(sourceClazz, true);
            } else {
                clazzList = new ArrayList<>(2);
                clazzList.add(sourceClazz);
                Class<?> superClass = sourceClazz.getSuperclass();
                if (superClass != null) {
                    clazzList.add(superClass);
                }
            }

            //如果是降序获取
            if (isDESCGet) {
                for (int w = clazzList.size() - 1; w > -1; w--) {
                    Collections.addAll(fieldList, isGetDeclaredField ? clazzList.get(w).getDeclaredFields() : clazzList.get(w).getFields());
                }
            } else {
                for (Class<?> clazz : clazzList) {
                    Collections.addAll(fieldList, isGetDeclaredField ? clazz.getDeclaredFields() : clazz.getFields());
                }
            }
        } else {
            Collections.addAll(fieldList, isGetDeclaredField ? sourceClazz.getDeclaredFields() : sourceClazz.getFields());
        }
        return fieldList;
    }

    /**
     * 获取给定类的所有字段
     *
     * @param sourceClazz 给定的类
     * @return 给定类的所有字段
     */
    public static List<Field> getFields(Class<?> sourceClazz) {
        return getFields(sourceClazz, true, true, true, true);
    }

    /**
     * 设置给定的对象中给定名称的字段的值
     *
     * @param object              给定的对象
     * @param fieldName           要设置的字段的名称
     * @param newValue            要设置的字段的值
     * @param isFindDeclaredField 是否查找Declared字段
     * @param isUpwardFind        如果在当前类中找不到的话，是否取其父类中查找
     * @return 设置是否成功。false：字段不存在或新的值与字段的类型不一样，导致转型失败
     */
    public static boolean setField(Object object, String fieldName, Object newValue, boolean isFindDeclaredField, boolean isUpwardFind) {
        boolean result = false;
        Field field = getField(object.getClass(), fieldName, isFindDeclaredField, isUpwardFind);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(object, newValue);
                result = true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }


	/* ************************************************** 方法相关的方法 ******************************************************* */

    /**
     * 从指定的类中获取指定的方法
     *
     * @param sourceClazz          给定的类
     * @param isFindDeclaredMethod 是否查找Declared字段
     * @param isUpwardFind         是否向上去其父类中寻找
     * @param methodName           要获取的方法的名字
     * @param methodParameterTypes 方法参数类型
     * @return 给定的类中给定名称以及给定参数类型的方法
     */
    public static Method getMethod(Class<?> sourceClazz, boolean isFindDeclaredMethod, boolean isUpwardFind, String methodName, Class<?>... methodParameterTypes) {
        Method method = null;
        try {
            method = isFindDeclaredMethod ? sourceClazz.getDeclaredMethod(methodName, methodParameterTypes) : sourceClazz.getMethod(methodName, methodParameterTypes);
        } catch (NoSuchMethodException e1) {
            if (isUpwardFind) {
                Class<?> clazz = sourceClazz.getSuperclass();
                while (method == null && clazz != null) {
                    try {
                        method = isFindDeclaredMethod ? clazz.getDeclaredMethod(methodName, methodParameterTypes) : clazz.getMethod(methodName, methodParameterTypes);
                    } catch (NoSuchMethodException e11) {
                        clazz = clazz.getSuperclass();
                    }
                }
            }
        }
        return method;
    }

    /**
     * 从指定的类中获取指定的方法，默认获取Declared类型的方法、向上查找
     *
     * @param sourceClazz          指定的类
     * @param methodName           方法名
     * @param methodParameterTypes 方法参数类型
     * @return Method
     */
    public static Method getMethod(Class<?> sourceClazz, String methodName, Class<?>... methodParameterTypes) {
        return getMethod(sourceClazz, true, true, methodName, methodParameterTypes);
    }

    /**
     * 从指定的类中获取指定名称的不带任何参数的方法，默认获取Declared类型的方法并且向上查找
     *
     * @param sourceClazz 指定的类
     * @param methodName  方法名
     * @return Method
     */
    public static Method getMethod(Class<?> sourceClazz, String methodName) {
        return getMethod(sourceClazz, methodName, EMPTY_PARAM_TYPES);
    }

    /**
     * 获取给定类的所有方法
     *
     * @param sourceClazz               给定的类
     * @param isGetDeclaredMethod 是否需要获取Declared方法
     * @param isFromSuperClassGet 是否需要把其父类中的方法也取出
     * @param isDESCGet           在最终获取的列表里，父类的方法是否需要排在子类的前面。只有需要把其父类中的方法也取出时此参数才有效
     * @return 给定类的所有方法
     */
    public static List<Method> getMethods(Class<?> sourceClazz, boolean isGetDeclaredMethod, boolean isFromSuperClassGet, boolean isDESCGet) {
        List<Method> methodList = new ArrayList<>();
        //如果需要从父类中获取
        if (isFromSuperClassGet) {
            //获取当前类的所有父类
            List<Class<?>> clazzList = getSuperClass(sourceClazz, true);

            //如果是降序获取
            if (isDESCGet) {
                for (int w = clazzList.size() - 1; w > -1; w--) {
                    Collections.addAll(methodList, isGetDeclaredMethod ? clazzList.get(w).getDeclaredMethods() : clazzList.get(w).getMethods());
                }
            } else {
                for (Class<?> clazz : clazzList) {
                    Collections.addAll(methodList, isGetDeclaredMethod ? clazz.getDeclaredMethods() : clazz.getMethods());
                }
            }
        } else {
            for (Method method : isGetDeclaredMethod ? sourceClazz.getDeclaredMethods() : sourceClazz.getMethods()) {
                methodList.add(method);
            }
        }
        return methodList;
    }

    /**
     * 获取给定类的所有方法
     *
     * @param sourceClazz 给定的类
     * @return 给定类的所有方法
     */
    public static List<Method> getMethods(Class<?> sourceClazz) {
        return getMethods(sourceClazz, true, true, true);
    }

    /**
     * 获取给定的类中指定参数类型的ValueOf方法
     *
     * @param sourceClazz          给定的类
     * @param methodParameterTypes 方法参数类型
     * @return 给定的类中给定名称的字段的GET方法
     */
    public static Method getValueOfMethod(Class<?> sourceClazz, Class<?>... methodParameterTypes) {
        return getMethod(sourceClazz, true, true, "valueOf", methodParameterTypes);
    }

    /**
     * 调用不带参数的方法
     *
     * @param method 方法
     * @param object 对象
     * @return Object
     * @throws Exception
     */
    public static Object invokeMethod(Method method, Object object) throws
            Exception {
        return method.invoke(object, EMPTY_PARAMS);
    }

	/* ************************************************** 构造函数相关的方法 ******************************************************* */

    /**
     * 获取给定的类中给定参数类型的构造函数
     *
     * @param sourceClazz               给定的类
     * @param isFindDeclaredConstructor 是否查找Declared构造函数
     * @param isUpwardFind              是否向上去其父类中寻找
     * @param constructorParameterTypes 构造函数的参数类型
     * @return 给定的类中给定参数类型的构造函数
     */
    public static Constructor<?> getConstructor(Class<?> sourceClazz, boolean isFindDeclaredConstructor, boolean isUpwardFind, Class<?>... constructorParameterTypes) {
        Constructor<?> method = null;
        try {
            method = isFindDeclaredConstructor ? sourceClazz.getDeclaredConstructor(constructorParameterTypes) : sourceClazz.getConstructor(constructorParameterTypes);
        } catch (NoSuchMethodException e1) {
            if (isUpwardFind) {
                Class<?> clazz = sourceClazz.getSuperclass();
                while (method == null && clazz != null) {
                    try {
                        method = isFindDeclaredConstructor ? sourceClazz.getDeclaredConstructor(constructorParameterTypes) : sourceClazz.getConstructor(constructorParameterTypes);
                    } catch (NoSuchMethodException e11) {
                        clazz = clazz.getSuperclass();
                    }
                }
            }
        }
        return method;
    }

    /**
     * 获取给定的类中所有的构造函数
     *
     * @param sourceClazz               给定的类
     * @param isFindDeclaredConstructor 是否需要获取Declared构造函数
     * @param isFromSuperClassGet       是否需要把其父类中的构造函数也取出
     * @param isDESCGet                 在最终获取的列表里，父类的构造函数是否需要排在子类的前面。只有需要把其父类中的构造函数也取出时此参数才有效
     * @return 给定的类中所有的构造函数
     */
    public static List<Constructor<?>> getConstructors(Class<?> sourceClazz, boolean isFindDeclaredConstructor, boolean isFromSuperClassGet, boolean isDESCGet) {
        List<Constructor<?>> constructorList = new ArrayList<>();
        //如果需要从父类中获取
        if (isFromSuperClassGet) {
            //获取当前类的所有父类
            List<Class<?>> clazzList = getSuperClass(sourceClazz, true);

            //如果是降序获取
            if (isDESCGet) {
                for (int w = clazzList.size() - 1; w > -1; w--) {
                    Collections.addAll(constructorList, isFindDeclaredConstructor ? clazzList.get(w).getDeclaredConstructors() : clazzList.get(w).getConstructors());
                }
            } else {
                for (Class<?> clazz : clazzList) {
                    Collections.addAll(constructorList, isFindDeclaredConstructor ? clazz.getDeclaredConstructors() : clazz.getConstructors());
                }
            }
        } else {
            Collections.addAll(constructorList, isFindDeclaredConstructor ? sourceClazz.getDeclaredConstructors() : sourceClazz.getConstructors());
        }
        return constructorList;
    }


	/* ************************************************** 父类相关的方法 ******************************************************* */

    /**
     * 获取给定的类所有的父类
     *
     * @param sourceClazz       给定的类
     * @param isAddCurrentClass 是否将当年类放在最终返回的父类列表的首位
     * @return 给定的类所有的父类
     */
    public static List<Class<?>> getSuperClass(Class<?> sourceClazz, boolean isAddCurrentClass) {
        List<Class<?>> clazzList = new ArrayList<>();
        Class<?> clazz;
        if (isAddCurrentClass) {
            clazz = sourceClazz;
        } else {
            clazz = sourceClazz.getSuperclass();
        }
        while (clazz != null) {
            clazzList.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return clazzList;
    }


	/* ************************************************** 其它的辅助方法 ******************************************************* */

    /**
     * 获取给定的类的名字
     *
     * @param sourceClazz 给定的类
     * @return 给定的类的名字
     */
    public static String getClassName(Class<?> sourceClazz) {
        String classPath = sourceClazz.getName();
        return classPath.substring(classPath.lastIndexOf('.') + 1);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObjectByFieldName(Object object, String fieldName, Class<T> clazz) {
        if (object != null && StringUtils.isNotEmpty(fieldName) && clazz != null) {
            try {
                Field field = ReflectUtils.getField(object.getClass(), fieldName, true, true);
                if (field != null) {
                    field.setAccessible(true);
                    return (T) field.get(object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断给定字段是否是type类型的数组
     *
     * @param field 字段
     * @param type  类型
     * @return boolean
     */
    public static boolean isArrayByType(Field field, Class<?> type) {
        Class<?> fieldType = field.getType();
        return fieldType.isArray() && type.isAssignableFrom(fieldType.getComponentType());
    }

    /**
     * 判断给定字段是否是type类型的collectionType集合，
     * 例如collectionType=List.class，type=Date.class就是要判断给定字段是否是Date类型的List
     *
     * @param field          字段
     * @param collectionType 集合类型
     * @param type           类型
     * @return boolean
     */
    @SuppressWarnings("rawtypes")
    public static boolean isCollectionByType(Field field, Class<? extends Collection> collectionType, Class<?> type) {
        Class<?> fieldType = field.getType();
        if (collectionType.isAssignableFrom(fieldType)) {
            Class<?> first = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            return type.isAssignableFrom(first);
        } else {
            return false;
        }
    }

}
