package com.mlx.administrator.myapplication;

public class Test {
    int a=1000,b=600;
    @org.junit.Test
    public void main(){

        swap(a,b);
        System.out.println("a:"+a+",b:"+b);
    }


    private void swap(int a,int b ){
        int temp;
        temp=a;
        a=b;
        b=temp;
        System.out.println("a:"+a+",b:"+b);
    }
}
