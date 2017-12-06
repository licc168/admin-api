package com.lccf.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lccf.base.controller.BaseController;
import com.lccf.domain.User;
import com.lccf.service.user.IUserService;
import com.lccf.service.user.UserParam;
import com.lccf.util.ResponseVo;
import com.lccf.util.ResponseVoUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class UserController extends BaseController {

    @Resource
    IUserService userService;

    /**
     * <strong>注册用户</strong>
     * 
     * @param userParam
     * @see com.lccf.service.user.UserParam
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(value = "注册用户", httpMethod = "POST", response = String.class, notes = "注册接口（用户名/邮箱/密码）")
    public ResponseVo register(@Valid @RequestBody @ApiParam(value = "用户参数", required = true) UserParam userParam) {
        userService.register(userParam);
        return ResponseVoUtil.successMsg("注册成功");
    }

    /**
     * <strong>验证用户名是否存在</strong>
     * 
     * @see com.lccf.service.user.UserParam
     */
    @RequestMapping(value = "/user/isExistsUserName", method = RequestMethod.GET)
    @ApiOperation(value = "验证用户名是否存在", httpMethod = "GET", response = String.class, notes = "判断用户名是否存在")
    public ResponseVo isExistsUserName(@ApiParam(value = "用户名", required = true) @RequestParam String userName) {
        User user = userService.getByUserName(userName);
        if (user == null) {
            return ResponseVoUtil.successData(0);
        }
        return ResponseVoUtil.successData(1);
    }

    /**
     * 获取用户数据-分页
     * 
     * @param userParam
     * @return
     */
    @RequestMapping(value = "/user/page", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户数据", httpMethod = "GET", response = Page.class, notes = "")
    public ResponseVo page(@ApiParam(value = "用户参数", required = true) UserParam userParam) {
        Page<User> userPage = userService.page(userParam);
        return ResponseVoUtil.successData(userPage);
    }

    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除用户", httpMethod = "DELETE", response = String.class, notes = "")
    public ResponseVo deleteById(@ApiParam(value = "用户ID", required = true) @PathVariable Long id) {
        userService.deleteById(id);
        return ResponseVoUtil.successMsg("用户成功");
    }
}
