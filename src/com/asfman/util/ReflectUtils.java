package com.asfman.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class ReflectUtils {
	/*
	 * 遍历设置对象成员变量值
	 * 
	 * @param object 目标对象
	 * 
	 * @param jsonObject api读取过来的JSON对象
	 */
	public static Object setFieldsValue(Object object, JSONObject jsonObject)
			throws IllegalArgumentException, IllegalAccessException,
			JSONException, InstantiationException {
		Field[] fields = object.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			if (!Modifier.isStatic(f.getModifiers())) {// 过滤static成员变量
				String field = f.getName();
				if (!jsonObject.isNull(field)) {
					f.setAccessible(true);
					String classType = f.getType().getSimpleName().toLowerCase();
					Log.i("info", "class type: " + classType);
					if (classType.equals("integer") || classType.equals("int")) {
						f.set(object, jsonObject.getInt(field));
					} else if (classType.equals("double")) {
						f.set(object, jsonObject.getDouble(field));
					} else if (classType.equals("boolean")) {
						f.set(object, jsonObject.getBoolean(field));
					} else if (classType.equals("long")) {
						f.set(object, jsonObject.getLong(field));
					} else if(classType.equals("string")){
						f.set(object, jsonObject.getString(field));
					} else {
						if(!jsonObject.isNull(field)) {
							JSONObject subJsonObject = jsonObject.optJSONObject(field);
							Log.i("info", field + ": " + subJsonObject.toString());
							if(subJsonObject != null)
								f.set(object, setFieldsValue(f.getType().newInstance(), subJsonObject));
						}
					}
				}
			}
		}
		return object;
	}

	/*
	 * 根据提供的key调用对应的set方法 setValueByField(obj, "id", "13333"); 最终调用
	 * setId("13333");
	 */
	public static void setValueByField(Object object, String key, String value) {
		try {
			Method[] methods = object.getClass().getDeclaredMethods();// 获得类的方法集合
			// 遍历方法集合
			for (int i = 0; i < methods.length; i++) {
				// 获取所有setXX()的返回值
				// methods[i].getName()方法返回方法名
				String methodName = methods[i].getName();
				if (methodName.startsWith("set")
						&& methodName.toLowerCase().contains(key.toLowerCase())) {
					methods[i].setAccessible(true);
					methods[i].invoke(object, new Object[] { value });
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, String> toHashMap(Object object) {
		try {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			Field[] fields = object.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				String field = fields[i].getName();
				String value = (String) fields[i].get(object);
				hashMap.put(field, value);
			}
			return hashMap;
		} catch (Exception e) {
			return null;
		}
	}

	public static String toString(Object object) {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			Field[] fields = object.getClass().getDeclaredFields();
			stringBuilder.append("[");
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				String field = fields[i].getName();
				Object value = fields[i].get(object);
				stringBuilder.append(field);
				stringBuilder.append(": ");
				stringBuilder.append(value);
				if (i != fields.length - 1)
					stringBuilder.append(", ");
			}
			stringBuilder.append("]");
			return stringBuilder.toString();
		} catch (Exception e) {
			return "[]";
		}
	}

}
