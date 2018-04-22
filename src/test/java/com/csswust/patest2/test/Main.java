package com.csswust.patest2.test;

import java.util.Scanner;

/**
 * Created by 972536780 on 2018/4/19.
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = in.nextInt();
            int sum1 = 0, sum2 = 0;
            for (int i = 0; i < n; i++) {
                int a = in.nextInt();
                int b = in.nextInt();
                int c = in.nextInt();
                if (a == 1) sum1 += b * c;
                else sum2 += b * c;
            }
            boolean flag = false;
            if (4 * sum2 > 3 * (sum1 + sum2)) flag = true;
            System.out.println(sum1 + " " + sum2 + " " + (flag ? "Y" : "N"));
        }
        in.close();
    }
}
