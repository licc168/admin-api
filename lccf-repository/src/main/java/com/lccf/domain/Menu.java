package com.lccf.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;

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

    @OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name="parent_id")
    
    private Set<Menu> children;



}
