package me.micartey.webhookly;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSONObject {

    private final HashMap<String, Object> map = new HashMap<>();

    void put(String key, Object value) {
        if (value == null)
            return;

        map.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        builder.append("{");

        int i = 0;
        for (Map.Entry<String, Object> entry : entrySet) {
            Object val = entry.getValue();
            builder.append(escape(entry.getKey())).append(":");

            if (val instanceof String) {
                builder.append(escape(String.valueOf(val)));
            } else if (val instanceof Integer) {
                builder.append(Integer.valueOf(String.valueOf(val)));
            } else if (val instanceof Boolean) {
                builder.append(val);
            } else if (val instanceof JSONObject) {
                builder.append(val);
            } else if (val.getClass().isArray()) {
                builder.append("[");
                int len = Array.getLength(val);
                for (int j = 0; j < len; j++) {
                    builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                }
                builder.append("]");
            }

            builder.append(++i == entrySet.size() ? "}" : ",");
        }

        return builder.toString();
    }

    private String escape(String string) {
        StringBuilder builder = new StringBuilder("\"");
        for (char c : string.toCharArray()) {
            if (c == '\\' || c == '"' || c == '/') {
                builder.append("\\").append(c);
            } else if (c == '\b') {
                builder.append("\\b");
            } else if (c == '\t') {
                builder.append("\\t");
            } else if (c == '\n') {
                builder.append("\\n");
            } else if (c == '\f') {
                builder.append("\\f");
            } else if (c == '\r') {
                builder.append("\\r");
            } else if (c < ' ' || c > 0x7f) {
                builder.append(String.format("\\u%04x", (int) c));
            } else {
                builder.append(c);
            }
        }
        return builder.append('"').toString();
    }
}
