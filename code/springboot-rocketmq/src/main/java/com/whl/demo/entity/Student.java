package com.whl.demo.entity;

/**
 * @program: springboot-rocketmq
 * @description:
 * @author: Mr.Wang
 * @create: 2018-11-27 09:53
 **/
public class Student {

    private Integer sNo;
    private String sName;
    private Integer sAge;
    private String sSex;

    public Integer getsNo() {
        return sNo;
    }

    public void setsNo(Integer sNo) {
        this.sNo = sNo;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public Integer getsAge() {
        return sAge;
    }

    public void setsAge(Integer sAge) {
        this.sAge = sAge;
    }

    public String getsSex() {
        return sSex;
    }

    public void setsSex(String sSex) {
        this.sSex = sSex;
    }
}
