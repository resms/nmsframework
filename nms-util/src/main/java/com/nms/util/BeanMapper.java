package com.nms.util;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sam on 16-4-5.
 */
public class BeanMapper {

    private final static Logger logger = LoggerFactory.getLogger(BeanMapper.class);

    protected final static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * use Dozer copy object A to object B
     *
     * @param source            source object
     * @param destinationObject target object
     */
    public static void copy(Object source, Object destinationObject) {

        dozer.map(source, destinationObject);
    }

    /**
     * use Dozer convert object
     *
     * @param source
     * @param destinationClass
     * @return
     */
    public static <T> T map(Object source, Class<T> destinationClass) {

        return dozer.map(source, destinationClass);
    }

    /**
     * use Dozer convert objects in Collection
     *
     * @param sourceList
     * @param destinationClass
     * @return
     */
    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {

        final List<T> destinationList = new ArrayList<T>();
        for (final Object sourceObject : sourceList) {
            final T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }
}
