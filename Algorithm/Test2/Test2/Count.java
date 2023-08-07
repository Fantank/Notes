package com.fantank.algorithm.Test2;

import java.util.Arrays;
import java.util.Scanner;

public class Count {
    //n 每排的数字个数
    // a1 ... an n个数
    //m 运算次数
    //t1 o1 ... tm om 表示第ti个加号变成了oi符号
    Scanner sc = new Scanner(System.in);

    public void count() {
        int n = sc.nextInt();
        int[] nums = new int[n];
        for (int i = 0; i < nums.length; i++)
            nums[i] = sc.nextInt();
        int m = sc.nextInt();

        int sum = Arrays.stream(nums).sum();
        for (int i = 0; i < m; i++) {
            int t = sc.nextInt();
            String sign = sc.next();
            double res = 0;
            switch (sign) {
                case "-":
                    res = sum - 2 * nums[t];
                    break;
                case "*":
                    res = sum - nums[t] - nums[t - 1] + nums[t] * nums[t - 1];
                    break;
                case "/":
                    res = sum - nums[t] - nums[t - 1] + (double) nums[t - 1] / nums[t];
                    break;
            }
            System.out.print(res);
            System.out.println(' ');

        }

    }

    public static void main(String[] args) {
        new Count().count();
    }
}
//16min