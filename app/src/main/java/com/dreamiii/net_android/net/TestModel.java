package com.dreamiii.net_android.net;

/**
 * Created by bbx on 2016/11/7.
 */

public class TestModel {

    private String a;
    private String b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                '}';
    }
}
