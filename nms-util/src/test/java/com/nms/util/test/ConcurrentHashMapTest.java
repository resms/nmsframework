package com.nms.util.test;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sam on 17-6-1.
 */
public class ConcurrentHashMapTest {

    private ConcurrentHashMap<String,Object> map;

    @Before
    public void init()
    {
        map = new ConcurrentHashMap<String,Object>();
        map.put("name","sam");
        map.put("age",33);
        map.put("title","architect");
    }

    @Test
    public void forEach()
    {
        map.forEach((k,v) ->{System.out.println(k+","+v.toString());});
    }

    @Test
    public void foreachKey()
    {
        map.forEachKey(2l,(k)-> System.out.println(k));
    }

    @Test
    public void foreachValue()
    {
        map.forEachValue(2l,(v)-> System.out.println(v));
    }

    @Test
    public void foreachEntry()
    {
        map.forEachEntry(2l,(entry)-> System.out.println(entry.getKey()+","+entry.getValue()));
    }

    @Test
    public void search()
    {
        //map.search(1l,(k,v)-> System.out.println(k));
    }
}
