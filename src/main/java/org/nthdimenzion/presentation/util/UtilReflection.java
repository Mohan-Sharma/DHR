package org.nthdimenzion.presentation.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.nthdimenzion.object.utils.UtilValidator;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UtilReflection {

	public final static Class<?>[] EMPTY_CLASS_ARRAY = new Class[] {};
	public final static Object[] EMPTY_OBJECT_ARRAY = new Object[] {};

	public static Method getGetterMethod(String fieldName, Class<?> klass) throws SecurityException,NoSuchMethodException {
//	return ReflectionUtils.findMethod(klass, "get" + StringUtils.capitalize(fieldName));
	return ReflectionUtils.findMethod(klass, "get" + StringUtils.capitalize(fieldName));
	}

	public static Object getFieldValue(Object object, String fieldName) {
	Object returnValue = null;
	try {
		returnValue = getGetterMethod(fieldName, object.getClass()).invoke(object, EMPTY_OBJECT_ARRAY);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	return returnValue;
	}

	public static Annotation getAnnotationOnGetter(Class<?> klass, Class<? extends Annotation> annotationClass,String fieldName) {
	try {
		return getGetterMethod(fieldName, klass).getAnnotation(annotationClass);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	}
	
	public static boolean areEqual(Object obj1, Object obj2, String... fields) {
		if(obj1==null || obj2==null || UtilValidator.isEmpty(fields))
			return false;
		if (!obj1.getClass().equals(obj2.getClass()))
			return false;
		try {
			for (String field : fields) {
				Object obj1Value = UtilReflection.getFieldValue(obj1, field);
				Object obj2Value = UtilReflection.getFieldValue(obj2, field);
				if (!EqualsBuilder.reflectionEquals(obj1Value, obj2Value))
					return false;
			}
		} catch (Throwable th) {
			th.printStackTrace();
			return false;
		}
		return true;
		}

    public static Field[] getDeclaredFields(Class clazz, boolean recursively) {
        List<Field> fields = new LinkedList<Field>();
        Field[] declaredFields = clazz.getDeclaredFields();
        Collections.addAll(fields, declaredFields);

        Class superClass = clazz.getSuperclass();

        if(superClass != null && recursively) {
            Field[] declaredFieldsOfSuper = getDeclaredFields(superClass, recursively);
            if(declaredFieldsOfSuper.length > 0)
                Collections.addAll(fields, declaredFieldsOfSuper);
        }

        return fields.toArray(new Field[fields.size()]);
    }

    public static Field[] getAnnotatedDeclaredFields(Class clazz,
                                                     Class<? extends Annotation> annotationClass,
                                                     boolean recursively) {
        Field[] allFields = getDeclaredFields(clazz, recursively);
        List<Field> annotatedFields = new LinkedList<Field>();

        for (Field field : allFields) {
            if(field.isAnnotationPresent(annotationClass))
                annotatedFields.add(field);
        }

        return annotatedFields.toArray(new Field[annotatedFields.size()]);
    }

}