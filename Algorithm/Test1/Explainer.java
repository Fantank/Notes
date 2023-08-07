package com.fantank.algorithm.Test1;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Explainer {
    Scanner sc = new Scanner(System.in);
    HashMap<String,String> map = new HashMap<>();

    void setKV(String s) {
        String[] kAndV = s.split(";");
        for(String kv : kAndV) {
            //if(kv.isEmpty()) continue;
            String[] k_v = kv.split("=");
            map.put(k_v[0], k_v[1]);
        }
    }

    public void runExplainer() {
        String input = sc.nextLine();
        int quest = sc.nextInt();
        sc.nextLine();
        setKV(input);
        for(int i = 0; i < quest; i++) {
            String key = sc.nextLine();
            String res = map.getOrDefault(key, "EMPTY");
            System.out.println(res);
        }
    }

    public static void main(String[] args) {
        new Explainer().runExplainer();
    }
}
/**

 LOGNAME=default;SHELL=/bin/bash;HOME=/home/xiaomei;LOGNAME=xiaomei;
 4
 SHELL
 HOME
 LOGNAME
 logname
 */