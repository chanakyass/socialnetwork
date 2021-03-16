package com.springboot.rest.model.dto;

import java.util.List;

public class DataList<T> {

    List<T> dataList;
    int noOfPages;
    int currentPageNo;

    public DataList(List<T> dataList, int noOfPages, int currentPageNo) {
        this.dataList = dataList;
        this.noOfPages = noOfPages;
        this.currentPageNo = currentPageNo;
    }

    public DataList(List<T> dataList)
    {
        this.dataList = dataList;
        this.noOfPages = 1;
        this.currentPageNo = 0;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(int noOfPages) {
        this.noOfPages = noOfPages;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }
}
