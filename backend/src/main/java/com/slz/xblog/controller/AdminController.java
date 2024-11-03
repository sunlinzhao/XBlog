package com.slz.xblog.controller;

import com.slz.xblog.service.AdminService;
import com.slz.xblog.service.IUserService;
import com.slz.xblog.utils.template.ResultTemplate;
import com.slz.xblog.utils.token.JwtTokenUtil;
import com.slz.xblog.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/22
 */
@RestController
@CrossOrigin(origins = "http://139.155.158.230:17011", allowCredentials = "true")
@RequestMapping("/xblog/admin")
@Api(tags = "管理接口")
public class AdminController {
    @Resource
    private IUserService userService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private AdminService adminService;
    /**
     * 验证 token
     * @param userInfoVo UserInfoVo
     * @return b boolean
     */

    @PostMapping("/verifyToken")
    @ApiOperation(value = "验证 token")
    public Object verifyToken(@RequestBody UserInfoVo userInfoVo) {
        String token = userInfoVo.getToken();
        if(token == null) ResultTemplate.error().setMsg("验证失败");
        boolean b = jwtTokenUtil.validateToken(token);
        if(b) return ResultTemplate.success().setMsg("验证通过");
        return ResultTemplate.error().setMsg("验证失败");
    }

    /* ============================================================ */
    /*                         用户管理相关接口                        */
    /* ============================================================ */

    // 查询所有用户
    @GetMapping("/user/all")
    @ApiOperation(value = "查询所有用户")
    public Object getAllUser() {
        List<UserInfoVo> allUser = adminService.getAllUser();
        return ResultTemplate.success().setData("data", allUser);
    }
    @PostMapping ("/user/del")
    @ApiOperation(value = "删除用户")
    public Object delUser(@RequestBody UserInfoVo userInfoVo) {
        boolean b = adminService.delUser(userInfoVo);
        if(b) return ResultTemplate.success().setMsg("删除成功");
        return ResultTemplate.error().setMsg("删除失败");
    }

    @PostMapping("/user/update")
    @ApiOperation(value = "更新用户信息")
    public Object updateUser(@RequestBody UserInfoVo userInfoVo) {
        boolean b = adminService.updateById(userInfoVo);
        if(b) return ResultTemplate.success().setMsg("更新成功");
        return ResultTemplate.error().setMsg("更新失败");
    }

    @PostMapping("/user/add")
    @ApiOperation(value = "添加用户")
    public Object addUser(@RequestBody UserInfoVo userInfoVo) {
        boolean b = adminService.addUser(userInfoVo);
        if(b) return ResultTemplate.success().setMsg("添加成功");
        return ResultTemplate.error().setMsg("添加失败");
    }
}
