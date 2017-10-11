package com.lccf.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


/**
 * @author lichangchao
 * @Time 2017 -03-20 15:05:56
 */
@Entity
@Table(name = "user")
@Data
public class User  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "user_name",length = 50,unique = true,nullable = false)
    private String userName;

    @Column(name = "real_name",length = 100)
    private String realName;

    @Column(name = "email",length = 100)
    private String email;

    @Column(name = "mobile",length = 100)
    private String mobile;

    @JsonIgnore

    @Column(name = "password",length = 60)
    private String password;


    @Column(name = "status")
    private Integer status;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Column(name = "delete_flag" )
    private Boolean  deleteFlag;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;
    /**
     * 修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    @LastModifiedDate

    private Date updateTime;


}
