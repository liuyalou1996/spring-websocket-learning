package com.universe.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * FastJson字符串与Bean转换工具类
 * @author Nick Liu
 * @date 2022/8/5
 */
public class FastJsonUtils {

  public static String toJsonString(Object obj) {
    return toJsonString(obj, null, false, false);
  }

  public static String toJsonString(Object obj, SerializeFilter... filters) {
    return toJsonString(obj, null, false, false, filters);
  }

  public static String toJsonStringWithNullValue(Object obj, SerializeFilter... filters) {
    return toJsonString(obj, null, true, false, filters);
  }

  public static String toPrettyJsonString(Object obj, SerializeFilter... filters) {
    return toJsonString(obj, null, false, true, filters);
  }

  public static String toPrettyJsonStringWithNullValue(Object obj, SerializeFilter... filters) {
    return toJsonString(obj, null, true, true, filters);
  }

  public static String toJsonStringWithDateFormat(Object obj, String dateFormat, SerializeFilter... filters) {
    return toJsonString(obj, dateFormat, false, false, filters);
  }

  public static String toJsonStringWithDateFormatAndNullValue(Object obj, String dateFormat, SerializeFilter... filters) {
    return toJsonString(obj, dateFormat, true, false, filters);
  }

  public static String toPrettyJsonStringWithDateFormat(Object obj, String dateFormat, SerializeFilter... filters) {
    return toJsonString(obj, dateFormat, false, true, filters);
  }

  public static String toPrettyJsonStringWithDateFormatAndNullValue(Object obj, String dateFormat, SerializeFilter... filters) {
    return toJsonString(obj, dateFormat, true, true, filters);
  }

  /**
   *
   * 对象所有的key，包括嵌套对象的key都会按照自然顺序排序
   * @param obj
   * @return
   */
  public static String toKeyOrderedJsonString(Object obj) {
    return toJsonString(beanToTreeMap(obj));
  }

  public static String toJsonString(Object obj, String dateFormat, boolean writeNullValue, boolean prettyFormat,
      SerializeFilter... filters) {
    if (obj == null) {
      return null;
    }

    int defaultFeature = JSON.DEFAULT_GENERATE_FEATURE;
    if (writeNullValue) {
      return prettyFormat
          ? JSON.toJSONString(obj, SerializeConfig.globalInstance, filters, dateFormat, defaultFeature, SerializerFeature.WriteMapNullValue,
              SerializerFeature.PrettyFormat)
          : JSON.toJSONString(obj, SerializeConfig.globalInstance, filters, dateFormat, defaultFeature,
              SerializerFeature.WriteMapNullValue);
    }

    return prettyFormat
        ? JSON.toJSONString(obj, SerializeConfig.globalInstance, filters, dateFormat, defaultFeature, SerializerFeature.PrettyFormat)
        : JSON.toJSONString(obj, SerializeConfig.globalInstance, filters, dateFormat, defaultFeature);

  }

  public static <T> T toJavaBean(String jsonStr, Class<T> clazz) {
    if (StringUtils.isBlank(jsonStr)) {
      return null;
    }

    return JSON.parseObject(jsonStr, clazz);
  }

  public static <T> List<T> toList(String jsonStr, Class<T> clazz) {
    if (StringUtils.isBlank(jsonStr)) {
      return null;
    }

    return JSON.parseArray(jsonStr, clazz);
  }

  public static Map<String, Object> toMap(String jsonStr) {
    if (StringUtils.isBlank(jsonStr)) {
      return null;
    }

    return JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {});
  }

  public static <T> Map<String, T> toMap(String jsonStr, TypeReference<Map<String, T>> typeReference) {
    if (StringUtils.isBlank(jsonStr)) {
      return null;
    }

    return JSON.parseObject(jsonStr, typeReference);
  }

  public static Map<String, Object> toTreeMap(String jsonStr) {
    if (StringUtils.isBlank(jsonStr)) {
      return null;
    }

    JSONObject jsonObject = JSON.parseObject(jsonStr);
    Map<String, Object> treeMap = new TreeMap<>();
    addJsonObjectToTreeMap(treeMap, jsonObject);
    return treeMap;
  }

  private static void addJsonObjectToTreeMap(Map<String, Object> treeMap, JSONObject jsonObject) {
    jsonObject.forEach((key, value) -> {
      if (value instanceof JSONObject) {
        // 递归处理嵌套的 JSONObject
        Map<String, Object> nestedSortedMap = new TreeMap<>(Comparator.naturalOrder());
        addJsonObjectToTreeMap(nestedSortedMap, (JSONObject) value);
        treeMap.put(key, nestedSortedMap);
      } else {
        treeMap.put(key, value);
      }
    });
  }

  public static Map<String, Object> javaBeanToMap(Object obj) {
    if (Objects.isNull(obj)) {
      return null;
    }

    return toMap(toJsonString(obj));
  }

  public static Map<String, Object> beanToTreeMap(Object obj) {
    if (Objects.isNull(obj)) {
      return null;
    }

    return toTreeMap(toJsonString(obj));
  }


  public static <T> T mapToJavaBean(Map<String, ? extends Object> map, Class<T> clazz) {
    if (CollectionUtils.isEmpty(map)) {
      return null;
    }

    String jsonStr = JSON.toJSONString(map);
    return JSON.parseObject(jsonStr, clazz);
  }

}
