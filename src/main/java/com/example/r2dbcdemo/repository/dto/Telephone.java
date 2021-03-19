package com.example.r2dbcdemo.repository.dto;

import lombok.Value;
import lombok.With;

@Value
@With
public class Telephone {
    String id;  // 조회를 위한 프로퍼티를 가지고 있어야만 한다.
    String telRgnNo;
    String telTxnoNo;
    String telEndNo;
}
