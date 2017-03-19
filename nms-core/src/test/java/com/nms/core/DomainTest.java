package com.nms.core;

import com.nms.core.bean.College;
import org.junit.Test;

/**
 * Created by sam on 16-10-30.
 */
public class DomainTest {
    @Test
    public void testJsonDomain()
    {
        College c = new College();
        c.setName("sam");
        c.setAge(33);
        c.setClassRoom("3106");
        c.setSpecialty("software");

        System.out.println(c.toJson());
    }
}
