package com.mom.app.model;

/**
 * Created by vaibhavsinha on 10/13/14.
 */
public class Operator {
    public String code;
    public String name;

    public Operator(){}
    public Operator(String code, String name){
        this.code   = code;
        this.name   = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        return name;
    }
}
