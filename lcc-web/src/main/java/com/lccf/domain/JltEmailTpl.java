package com.lccf.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

/**
 * 邮箱模板
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/12/4 16:30
 * @see
 */
@Data
@Entity
@Table(name = "jlt_email_tpl")
public class JltEmailTpl {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  //应用ID
  @Column(name = "app_id",nullable = false)
  private Integer appId;

  //模板编号
  @Column(name = "code",nullable = false)
  private String code;

  //模板内容
  @Column(name = "content",nullable = false,length = 2000)
  private String content;
  //备注
  @Column(name = "remark",length = 200)
  private String remark;

  @Column(name = "create_time")
  @CreatedDate
  private Date createTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_time")
  @LastModifiedDate

  private Date updateTime;



  @Column(name = "delete_flag")
  private Boolean deleteFlag;
}
