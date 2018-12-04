package com.hust.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ThreadUtils {
	
	public static Thread thread(Object container, final String name, Class<?>[] paramTypes, Object[] params) {
		Thread later = new Thread() {
			@Override
			public void run() {
				method(container, name, paramTypes, params);
			}
		};
		later.start();
		return later;
	}

	public static void method(Object container, String name, Class<?>[] paramTypes, Object[] params) {
		try {
			Method method = container.getClass().getMethod(name, paramTypes);
			method.invoke(container, params);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.getTargetException().printStackTrace();
		} catch (NoSuchMethodException nsme) {
			String error = "There is no public " + name + "(";
			for (Class<?> paramType : paramTypes) {
				error += paramType.getSimpleName() + ", ";
			}
			if (paramTypes.length > 0) {
				error = error.substring(0, error.length() - 2);
			}
			error += ") method " + "in the class " + container.getClass().getName();
			System.err.println(error);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
