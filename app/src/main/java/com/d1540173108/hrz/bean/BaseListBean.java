package com.d1540173108.hrz.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yc on 2017/8/17.
 */

public class BaseListBean<T> implements Serializable {

    private List<T> rows;
    private int records;
    private int total;
    private int page;

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
