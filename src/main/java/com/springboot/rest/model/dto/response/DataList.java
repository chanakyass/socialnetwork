package com.springboot.rest.model.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DataList<T> {

    List<T> dataList;
    int noOfPages;
    int currentPageNo;

    public DataList(List<T> dataList)
    {
        this.dataList = dataList;
        this.noOfPages = 1;
        this.currentPageNo = 0;
    }
}
