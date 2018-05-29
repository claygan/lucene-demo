package com.quest.lucene.web.entity;

/**
 * Created by Quest on 2018/5/28.
 */
public class QueryInfo {
    private String keyword;
    private int pageSize = 10;
    private int page = 1;
    private String type;

    public QueryInfo() {
    }

    public QueryInfo(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
