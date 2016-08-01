package com.coamctech.xlsunit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class CaseInsensitiveHashMap  extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 9178606903603606031L;
	
	private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

    @Override
    public boolean containsKey(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return super.containsKey(realKey);
    }

    @Override
    public Object get(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return super.get(realKey);
    }

    @Override
    public Object put(String key, Object value) {
    	
        /*
         * 保持map和lowerCaseMap同步
         * 在put新值之前remove旧的映射关系
         */
        Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
        Object oldValue = super.remove(oldKey);
        super.put(key, value);
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            this.put(key, value);
        }
    }

    @Override
    public Object remove(Object key) {
        Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
        return super.remove(realKey);
    }
}
