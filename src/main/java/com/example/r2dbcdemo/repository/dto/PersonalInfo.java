package com.example.r2dbcdemo.repository.dto;

import lombok.Value;
import lombok.With;

@Value
@With
public class PersonalInfo {

    String id;
    String name;
    String emailAddr;

}
