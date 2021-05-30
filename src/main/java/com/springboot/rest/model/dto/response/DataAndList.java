package com.springboot.rest.model.dto.response;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DataAndList<T, Z> {

    Data<T> data;
    DataList<Z> dataList;
}
