package com.lccf.service.menu;

import com.lccf.base.service.IBaseService;
import com.lccf.domain.Menu;
import org.springframework.data.domain.Page;
import java.util.List;


public interface IMenuService  extends IBaseService<Menu,MenuParam,MenuVo> {
	public List<Menu> findByDeleteFlagAndParentId(Boolean  deleteFlag,Long parentId);

	/**
	 * 查询菜单列表
	 * @param menuParam
	 * @return
	 */
	Page<MenuVo> page(MenuParam menuParam);

}
