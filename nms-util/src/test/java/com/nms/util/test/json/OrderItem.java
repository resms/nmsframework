package com.nms.util.test.json;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItem implements Serializable {
    private BigDecimal price = new BigDecimal(0.1000000000000000055511151231257827021181583404541015625);
    private BigDecimal price1 = new BigDecimal("2000000000000000.11");
    private BigDecimal bd = new BigDecimal(2000000000000000.11);

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getBd() {
        return bd;
    }

    public void setBd(BigDecimal bd) {
        this.bd = bd;
    }
}
