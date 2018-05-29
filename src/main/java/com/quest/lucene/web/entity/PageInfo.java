package com.quest.lucene.web.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Quest on 2018/5/28.
 */
public class PageInfo<T> implements Serializable{
    private static final long serialVersionUID = 43838808963781347L;
    private long total;
    private int page;
    private int pageSize;
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
