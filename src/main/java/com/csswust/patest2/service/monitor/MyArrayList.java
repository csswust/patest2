package com.csswust.patest2.service.monitor;

import java.util.ArrayList;

/**
 * Created by 972536780 on 2018/4/24.
 */
public class MyArrayList<E> extends ArrayList<E> {
    public MyArrayList() {
        super();
    }

    public void myRemoveRange(int fromIndex, int toIndex) {
        this.removeRange(fromIndex, toIndex);
    }
}
