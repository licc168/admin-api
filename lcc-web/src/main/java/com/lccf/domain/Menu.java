package com.lccf.domain;

import java.util.Date;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author lichangchao
 * @Time 2017 -04-24 15:05:56
 */
@Data
@Entity
@Table(name = "menu")
public class Menu  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "path")
    private String path;
    @Column(name = "title", nullable = true)
    private String title;
    @Column(name = "icon", nullable = true)
    private String icon;
    @Column(name = "selected")
    private Boolean selected;
    @Column(name = "expanded")
    private Boolean expanded;
    @Column(name = "delete_flag")
    private Boolean deleteFlag;
    @Column(name="parent_id")
    private Long parentId;
    @Column(name="order_num")
    private Integer orderNum;
    
   /* @OneToMany(mappedBy = "menu",cascade = CascadeType.ALL)
    @JoinTable(
            name = "menu",
            joinColumns = {@JoinColumn(name = "parent_id", referencedColumnName = "id")})
    
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(referencedColumnName = "id", name = "parent_id", insertable = false, updatable = false)*/

    @OneToMany(fetch= FetchType.EAGER,cascade= CascadeType.ALL)
    @JoinColumn(name="parent_id")
    
    private Set<Menu> children;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;
    /**
     * 修改时间
     */
    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;


}
