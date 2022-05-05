package com.hc.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Table(name = "userback")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Userback implements Serializable {
    /**
     * 用户ID
     */
    @Id
    private String userid;

    /**
     * 用户名称
     */
    @NotNull
    private String username;

    @Transient
    private String newpwd;

    /**
     * 用户密码
     */
    private String pwd;

    private static final long serialVersionUID = 1L;


}