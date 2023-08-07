package com.fantank.algorithm.Test1;

import org.junit.Test;

import java.util.Scanner;
import java.util.Stack;


public class Train {
    //T 测试样例数
    //n 火车数量
    //x1 x2 ... 驶入顺序
    //y1 y2 ... 驶出顺序
    Scanner sc = new Scanner(System.in);

    public void checkSeqOfTrains() {
        int T = sc.nextInt();
        for (int i = 0; i < T; i++) {
            int n = sc.nextInt();
            int[] in = new int[n];
            int[] out = new int[n];
            for(int j = 0; j < n; j++)
                in[j] = sc.nextInt();
            for(int j = 0; j < n; j++)
                out[j] = sc.nextInt();

            Stack<Integer> st = new Stack<>();
            int ptrOut = 0;
            for(int j = 0; j < n; j++) {
                if(st.isEmpty() || in[st.peek()] != out[ptrOut])
                    st.push(j);

                while(!st.isEmpty() && in[st.peek()] == out[ptrOut]) {
                    st.pop();
                    ptrOut++;
                }
            }
            if(ptrOut == n)
                System.out.println("Yes");
            else
                System.out.println("No");
        }
    }

    public static void main(String[] args) {
        new Train().checkSeqOfTrains();
    }
}
/**
 1
 3
 1 2 3
 3 2 1

 */