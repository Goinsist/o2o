package com.imooc.o2o.dto;

import java.util.List;

/**
*
* 功能描述:
* 迎合echart里的series项
 * @return 
* @author gongyu
* @date 2019/1/3 0003 13:37
*/
public class EchartSeries {
    private String name;
    private String type="bar";
    private List<Integer> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
