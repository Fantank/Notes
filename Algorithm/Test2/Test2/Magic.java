package com.fantank.algorithm.Test2;

import java.util.Arrays;
import java.util.Scanner;

public class Magic {
    //n 杯子数量
    // x1 ... xn 杯子容量
    //y1 ... yn 杯子水初始量
    //z1 .. zn 第i个杯子添加1单位水的消耗值
    //m 练习次数
    // q1 ... qm 第i次时，将第qi个杯子倒满
    //求m个最小值
    Scanner sc = new Scanner(System.in);
    int[] rem;
    public void practice() {
        int n = sc.nextInt();
        int[] vol = new int[n];
        for (int i = 0; i < n; i++)
            vol[i] = sc.nextInt();
        for (int i = 0; i < n; i++)
            vol[i] -= sc.nextInt();
        int[] consume = new int[n];
        for (int i = 0; i < consume.length; i++)
            consume[i] = sc.nextInt();

        int m = sc.nextInt();
        int[] qi = new int[m];
        for (int i = 0; i < m; i++)
            qi[i] = sc.nextInt();
        rem = new int[n];
        Arrays.fill(rem, -1);
        for (int i = 0; i < m; i++)
            System.out.println(dfs(qi[i] - 1, vol, consume));
    }

    int dfs(int idx, int[] vol, int[] consume) {
        if (idx == 0)
            return vol[0] * consume[0];
        if(rem[idx] > -1) return rem[idx];
        int cost = vol[idx] * consume[idx];
        int vom = vol[idx];
        for(int i = idx - 1; i >= 0; i--) {
            cost = Math.min(cost, dfs(i, vol, consume) + consume[i] * vom);
            vom += vol[i];
        }
        return rem[idx] = cost;
    }

    public static void main(String[] args) {
        new Magic().practice();
    }
}
//43min