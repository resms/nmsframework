package com.nms.util.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by sam on 17-4-7.
 */
public class TupleTest {
    @Test
    public void testTuple2()
    {
    }

    @Test
    public void testStream()
    {
        Map<String,String> cacheMap = new HashMap<>();
        cacheMap.put("1","1");
        cacheMap.put("2","2");
        cacheMap.put("3","3");
        cacheMap.put("4","4");

        Map<String,String> map = new HashMap<>();
        map.put("1","1");
        map.put("2","2");
        map.put("5","5");
        map.put("6","6");
        map.put("7","7");
        map.put("8","8");

        cacheMap.keySet()
                .stream()
                .filter(k -> !map.containsKey(k))
                .collect(Collectors.toSet()).forEach(s -> cacheMap.remove(s));

        map.entrySet().forEach(entry ->cacheMap.put(entry.getKey(),entry.getValue()));

        cacheMap.entrySet().forEach(s->System.out.println(s));
    }
}
