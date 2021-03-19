package com.example.r2dbcdemo.repository.entity;

import com.example.r2dbcdemo.domain.OrgRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("st_user_base")
public class User implements Persistable<String> {
    @Column("user_id")
    @Id
    private	String id;
    @Column("user_nm")
    private	String name;
    @Column("pwd")
    private	String password;
    private	String userGbCd;	            //사용자구분코드(UR001)
    private	String rtGrpNo;	                //권한그룹번호
    private	String orgTypCd;	            //조직유형코드(UR002)
    @Column("org_rol_cd")
    private OrgRole orgRole;	            //조직역할코드(UR003)
    private	String workStatCd;	            //근무상태코드(UR004)
    private	String empNo;	                //사원번호
    private	String deptNm;	                //부서명
    private	String telRgnNo;	            //전화지역번호
    private	String telTxnoNo;	            //전화국번번호
    private	String telEndNo;	            //전화끝번호
    private	String cellSctNo;	            //휴대폰구분번호
    private	String cellTxnoNo;	            //휴대폰국번번호
    private	String cellEndNo;	            //휴대폰끝번호
    private	String itelNo;	                //내선번호
    private	String emailAddr;	            //이메일주소
    private	String indInfoDealYn;	        //개인정보취급여부
    private	String useStrtDt;	            //사용시작일자
    private	String useEndDt;	            //사용종료일자
    private LocalDateTime rcntUseDtm;	    //최근사용일시
    private	Long pwdCntnFailCnt;	        //비밀번호연속실패수
    private	LocalDateTime lstPwdChgDtm;	    //최종비밀번호변경일시
    private	String pwdLockYn;	            //비밀번호잠김여부
    private	String pwdIniYn;	            //비밀번호초기화여부
    private	String useYn;	                //사용여부
    private	String entrNo;	                //협력사번호
    @CreatedDate
    private LocalDateTime sysRegDtm;
    @CreatedBy
    private String sysRegId;
    @LastModifiedDate
    private LocalDateTime sysModDtm;
    @LastModifiedBy
    private String sysModId;

    @With
    @Transient
    private List<UserIndRightInfo> userIndRightInfoList;

    @Transient
    private boolean newUser;
    @Override
    @Transient
    public boolean isNew() {
        return this.newUser || id == null;
    }

}
