package cn.edu.bjfu.dualchoice.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {
    public static JSONObject mergeJson(JSONObject target, JSONObject source){
        if (target == null) {
            return source;
        }

        for (String key : source.keySet()) {
            Object value = source.get(key);
            if (!target.containsKey(key)) {
                target.put(key, value);
            } else {
                if (value instanceof JSONObject) {
                    JSONObject valueJson = (JSONObject) value;
                    JSONObject targetValue = mergeJson(valueJson, target.getJSONObject(key));
                    target.put(key, targetValue);
                } else if (value instanceof JSONArray) {
                    JSONArray valueArray = (JSONArray) value;
                    for (int i = 0; i < valueArray.size(); i++) {
                        JSONObject obj = (JSONObject) valueArray.get(i);
                        JSONObject targetValue = mergeJson(obj, (JSONObject) target.getJSONArray(key).get(i));
                        target.getJSONArray(key).set(i, targetValue);
                    }
                } else {
                    target.put(key, value);
                }
            }
        }
        return target;
    }
}
