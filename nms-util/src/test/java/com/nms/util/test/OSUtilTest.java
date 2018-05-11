package com.nms.util.test;

import com.nms.util.OSUtil;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by sam on 17-3-19.
 */
public class OSUtilTest {
    @Test
    public void getOSName()
    {
        System.out.println(OSUtil.getOSName());
    }

    @Test
    public void getProperty()
    {
        try {
            Map<String, String> map = System.getenv();
            for(Iterator<String> itr = map.keySet().iterator(); itr.hasNext();){
                String key = itr.next();
                System.out.println(key + "=" + map.get(key));
            }
            String s = System.getProperty("java.io.tmpdir");
            System.out.println(s);
        } catch (final SecurityException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
