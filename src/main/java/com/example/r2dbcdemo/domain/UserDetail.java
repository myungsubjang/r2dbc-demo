package com.example.r2dbcdemo.domain;

import com.example.r2dbcdemo.repository.entity.User;
import com.example.r2dbcdemo.repository.entity.UserIndRightInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class UserDetail {

    private User user;
    private List<UserIndRightInfo> userIndRightInfo;
}
