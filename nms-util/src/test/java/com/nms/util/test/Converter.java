package com.nms.util.test;

/**
 * Created by sam on 17-2-22.
 */
@FunctionalInterface
public interface Converter<F, T> {
    T convert(F from);
}
