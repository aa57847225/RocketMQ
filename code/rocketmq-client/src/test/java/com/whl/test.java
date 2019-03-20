package com.whl;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: rocketmq-client
 * @description:
 * @author: Mr.Wang
 * @create: 2018-11-26 11:05
 **/
public class test {

    public static void main(String[] args) {
        List<String> s = new ArrayList();
        s.add("1");
        s.add("2");
        s.add("3");
        s.add("4");
        s.add("2");
        s.add("11");

//        s.removeIf(e->"2".equals(e));
//        for(String l:s){
//            if(l.equals("2")){s.remove(l);}
//        }
        System.out.println(JSON.toJSONString(s));
    }
}
