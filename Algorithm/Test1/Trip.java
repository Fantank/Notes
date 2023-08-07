package com.fantank.algorithm.Test1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Trip {
    //n 巧克力数量
    //m m个不同容量的包
    //a1 a2 ... 巧克力边长
    //q1 q2 ... 包的容量
    //回答能装多少块板，不是容量
    Scanner sc = new Scanner(System.in);

    int maxVal = 0;
    public void loadChocolate() {
        int n = sc.nextInt(), m = sc.nextInt();
        int[] choco = new int[n];
        int[] bagCapacity = new int[m];
        for(int i = 0; i < n; i++){
            choco[i] = sc.nextInt();
            choco[i] *= choco[i];
        }
        Arrays.sort(choco);

        for(int i = 0; i < m; i++)
            bagCapacity[i] = sc.nextInt();

        for(int i = 0; i < m; i++) {
            maxVal = 0;
            backtrack(0, bagCapacity[i], 0, choco);
            System.out.print(maxVal);
            System.out.print(" ");
        }

    }
    void backtrack(int idx, int cap, int num, int[] choco) {
        //System.out.println(cap + " " + idx + " " + num);
        maxVal = Math.max(num, maxVal);

        for (int i = idx; i < choco.length - 1; i++) {
            if(cap - choco[i] < 0) break;
            backtrack(i + 1, cap - choco[i], num + 1, choco);

        }
    }

    public static void main(String[] args) {
        new Trip().loadChocolate();
    }
}
/**

 5 1

 1 2 2 4 5

 3

 */