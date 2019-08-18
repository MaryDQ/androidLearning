package com.mlx.administrator.myapplication;

import java.util.Arrays;
import java.util.Collections;

public class Test {
    int a=1000,b=600;
    @org.junit.Test
    public void main(){

        testReverse();
    }

    private void testReverse() {
        System.out.println(System.currentTimeMillis());
        System.out.println(Arrays.toString(tempString));
        System.out.println(System.currentTimeMillis());
        Collections.reverse(Arrays.asList(tempString));
        System.out.println(System.currentTimeMillis());
        System.out.println(Arrays.toString(tempString));
        System.out.println(System.currentTimeMillis());
    }

    String[] tempString={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};


    private void swap(int a,int b ){
        int temp;
        temp=a;
        a=b;
        b=temp;
        System.out.println("a:"+a+",b:"+b);
    }
}
