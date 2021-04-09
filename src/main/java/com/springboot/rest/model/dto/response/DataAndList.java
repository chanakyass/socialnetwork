package com.springboot.rest.model.dto.response;

public class DataAndList<T, Z> {

    Data<T> data;
    DataList<Z> dataList;

    public DataAndList(Data<T> data, DataList<Z> dataList) {
        this.data = data;
        this.dataList = dataList;
    }

    public Data<T> getData() {
        return data;
    }

    public void setData(Data<T> data) {
        this.data = data;
    }

    public DataList<Z> getDataList() {
        return dataList;
    }

    public void setDataList(DataList<Z> dataList) {
        this.dataList = dataList;
    }
}
