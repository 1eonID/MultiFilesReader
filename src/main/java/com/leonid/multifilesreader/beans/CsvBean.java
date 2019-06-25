package com.leonid.multifilesreader.beans;

public class CsvBean {
    private Integer productId;
    private String name;
    private String condition;
    private String state;
    private Float price;

    public CsvBean(Integer productId, String name, String condition, String state, Float price) {
        this.productId = productId;
        this.name = name;
        this.condition = condition;
        this.state = state;
        this.price = price;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCondition() {
        return condition;
    }

    public String getState() {
        return state;
    }

    public Float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "CsvBean{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", condition='" + condition + '\'' +
                ", state='" + state + '\'' +
                ", price=" + price +
                '}';
    }
}
