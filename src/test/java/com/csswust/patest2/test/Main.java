package com.csswust.patest2.test;

import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Created by 972536780 on 2018/4/19.
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            double x = in.nextDouble();
            DecimalFormat df = new DecimalFormat("#.##");
            System.out.println(df.format(x));
        }
        in.close();
    }
}
