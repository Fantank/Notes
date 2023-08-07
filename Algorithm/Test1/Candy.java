package com.fantank.algorithm.Test1;

import java.util.Scanner;

public class Candy {
    Scanner sc = new Scanner(System.in);
    int[] maxVal;
    public void chooseCandy() {
        //n 糖果数
        //糖果编号 1 - 50000
        // a1 a2 ... 糖果美味值
        int n = sc.nextInt();
        int[] val = new int[n];
        for(int i = 0; i < n; i++)
            val[i] = sc.nextInt();

        maxVal = new int[n];
        int res = backtrack(0, val);
        System.out.println(res);
    }
    int backtrack(int index, int[] val) {
        if(index >= val.length)
            return 0;
        if(maxVal[index] > 0)
            return maxVal[index];
        int chose = val[index] + backtrack(index + 3, val);
        int notChose = Math.max(backtrack(index + 1, val), backtrack(index + 2, val));
        return maxVal[index] = Math.max(chose, notChose);
    }

    public static void main(String[] args) {
        new Candy().chooseCandy();
    }
}
