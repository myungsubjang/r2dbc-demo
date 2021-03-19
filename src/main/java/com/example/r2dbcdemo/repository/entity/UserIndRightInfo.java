package com.example.r2dbcdemo.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("ST_IND_INFO_QRY_RT_INFO")
public class UserIndRightInfo implements Persistable<String> {

    @Id
    private String userId;
    private String indInfoGbCd;
    private String useYn;
    @CreatedDate
    private LocalDateTime sysRegDtm;
    @CreatedBy
    private String sysRegId;
    @LastModifiedDate
    private LocalDateTime sysModDtm;
    @LastModifiedBy
    private String sysModId;

    @Transient
    private boolean newIndRightInfo;

    @Override
    public String getId() {
        return userId;
    }

    @Override
    @Transient
    public boolean isNew() {
        return newIndRightInfo || userId == null;
    }
}
