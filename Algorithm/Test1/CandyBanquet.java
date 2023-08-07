package com.fantank.algorithm.Test1;

import java.util.Scanner;

public class CandyBanquet {
    Scanner sc = new Scanner(System.in);

    //int[] record;
    public static void main(String[] args) {
        new CandyBanquet().eatPlan();
    }

    public void eatPlan() {
        int n = sc.nextInt(), k = sc.nextInt();
        int[] candy = new int[n];
        for (int i = 0; i < n; i++)
            candy[i] = sc.nextInt();

        //record = new int[n];
        int res = backtrack(0, k, 0, false, candy);
        System.out.println(res);
    }

    int backtrack(int idx, int remain, int sum, boolean yesterday, int[] candy) {
        if (idx >= candy.length)
            return sum;
        //if(record[idx] > 0) return record[idx];
        int res = 0;

        if (!yesterday)
            res = Math.max(backtrack(idx + 1, remain, sum + candy[idx], true, candy),
                    backtrack(idx + 1, remain, sum, false, candy));
        else {
            res = backtrack(idx + 1, remain, sum, false, candy);
            if (remain > 0)
                res = Math.max(res,
                        backtrack(idx + 1, remain - 1, sum + candy[idx], true, candy));
        }

        return res;
    }
}
