package com.csswust.patest2.service.result;

import com.csswust.patest2.common.APIResult;

import java.util.List;

/**
 * Created by 972536780 on 2018/3/19.
 */
public class SelectProblemDataRe extends APIResult {
    private List<String> input;
    private List<String> output;
    private Integer total;

    public List<String> getInput() {
        return input;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
