package com.imooc.o2o.dto;

import java.util.TreeSet;

/**
*
* 功能描述:
* 迎合echart里的xAxis项
 * @return
* @author gongyu
* @date 2019/1/3 0003 13:35
*/
public class EchartXAxis {
private String type="category";
//为了去重
    private TreeSet<String> data;

    public TreeSet<String> getData() {
        return data;
    }

    public void setData(TreeSet<String> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

}
