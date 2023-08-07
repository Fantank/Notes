package com.fantank.algorithm.Test2;

import java.util.Scanner;

public class Collection {
    //n 收藏夹数量
    //m 操作数量
    //op1 ... opm  ==0更新xi=yi ==1查询xi到yi的值
    //x1 ... xm
    //y1 ... ym
    Scanner sc = new Scanner(System.in);

    public void collect() {
        int n = sc.nextInt(), m = sc.nextInt();
        int[] op = new int[m];
        int[][] xy = new int[m][2];
        for (int i = 0; i < op.length; i++)
            op[i] = sc.nextInt();
        for (int i = 0; i < op.length; i++)
            xy[i][0] = sc.nextInt();
        for (int i = 0; i < op.length; i++)
            xy[i][1] = sc.nextInt();

        int[] prefix = new int[m + 1];
        int[] nums = new int[m + 1];

        for (int i = 0; i < m; i++) {
            if (op[i] == 0) {
                update(xy[i][0], xy[i][1] - nums[xy[i][0]], prefix);
                nums[xy[i][0]] = xy[i][1];
            }else{
                int ans = prefix[xy[i][1]] - prefix[xy[i][0]] + nums[xy[i][0]];
                System.out.println(ans);
            }
        }
    }

    public void update(int idx, int val, int[] prefix) {
        for (int i = idx; i < prefix.length; i++)
            prefix[i] += val;
    }

    public static void main(String[] args) {
        new Collection().collect();
    }
}
//37min