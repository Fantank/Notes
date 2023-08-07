package com.fantank.algorithm.Test2;

import java.util.Arrays;
import java.util.Scanner;

public class TidyUp {
    //n 物品数量
    // a1 ... an n个物品的值
    Scanner sc = new Scanner(System.in);

    public void swap() {
        int n = sc.nextInt();
        int[] obj = new int[n];
        for (int i = 0; i < n; i++)
            obj[i] = sc.nextInt();

    }

    int dfs(int idx, int sum, int[] obj) {
        if (idx == obj.length)
            return 0;
        int res = Integer.MAX_VALUE;
        for (int i = idx + 1; i < obj.length; i++) {
            res = Math.min(res, dfs(i,afterSwap(idx, i, sum, obj),obj));
        }
        return 1;
    }

    public void test() {
        int n = sc.nextInt();
        int[] obj = new int[n];
        for (int i = 0; i < n; i++)
            obj[i] = sc.nextInt();

        int res = 0;
        Arrays.sort(obj);
        for (int i = 0; i < n - 1; i++) {
            res += Math.abs(obj[i] - obj[i + 1]);
        }
        System.out.println(res);
    }

    public int afterSwap(int a, int b, int sum, int[] obj) {
        if (a > 0)
            sum -= Math.abs(obj[a] - obj[a - 1]);
        if (b < obj.length - 1)
            sum -= Math.abs(obj[b] - obj[b + 1]);

        sum -= Math.abs(obj[a] - obj[a + 1]);
        sum -= Math.abs(obj[b - 1] - obj[b]);
        return 1;
    }

    public static void main(String[] args) {
        new TidyUp().test();
    }
}
//22 min 未完成