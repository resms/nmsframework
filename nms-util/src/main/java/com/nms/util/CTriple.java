package com.nms.util;

import java.io.Serializable;

/**
 * Created by sam on 16-5-6.
 */
public class CTriple<T1 extends Serializable,T2 extends Serializable,T3 extends Serializable> implements Serializable,Cloneable {
    protected T1 one;
    protected T2 tow;
    protected T3 three;

    public CTriple(){}

    public CTriple(T1 one, T2 tow, T3 three) {
        this.one = one;
        this.tow = tow;
        this.three = three;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CTriple)) return false;

        CTriple<?, ?, ?> cTriple = (CTriple<?, ?, ?>) o;

        if (!one.equals(cTriple.one)) return false;
        if (!tow.equals(cTriple.tow)) return false;
        return three.equals(cTriple.three);

    }

    @Override
    public int hashCode() {
        int result = one.hashCode();
        result = 31 * result + tow.hashCode();
        result = 31 * result + three.hashCode();
        return result;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public T1 getOne() {
        return one;
    }

    public void setOne(T1 one) {
        this.one = one;
    }

    public T2 getTow() {
        return tow;
    }

    public void setTow(T2 tow) {
        this.tow = tow;
    }

    public T3 getThree() {
        return three;
    }

    public void setThree(T3 three) {
        this.three = three;
    }
}
