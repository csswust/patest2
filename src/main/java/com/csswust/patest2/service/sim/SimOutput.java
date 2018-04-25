package com.csswust.patest2.service.sim;

import java.util.List;

/**
 * Created by 972536780 on 2018/4/25.
 */
public class SimOutput {
    private List<SimResult> simResultList;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<SimResult> getSimResultList() {
        return simResultList;
    }

    public void setSimResultList(List<SimResult> simResultList) {
        this.simResultList = simResultList;
    }
}
