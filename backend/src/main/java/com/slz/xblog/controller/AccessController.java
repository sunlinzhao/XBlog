package com.slz.xblog.controller;


import com.slz.xblog.service.AccessService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 统计用户上次活跃时间 前端控制器
 * </p>
 *
 * @author slz
 * @since 2024-10-28
 */
@RestController
@RequestMapping("/xblog/user")
@CrossOrigin(origins = "http://139.155.158.230:17011", allowCredentials = "true")
@Api(tags = "用户状态")
public class AccessController {
    @Resource
    private AccessService activeService;

//    @PostMapping("/heartbeat")
//    public Object heartbeat(@RequestBody ActiveVo activeVo) {
//        activeService.updateUserActivity(activeVo);
//        return ResultTemplate.success().setMsg("Heartbeat received");
//    }
//
//    @GetMapping("/online-count")
//    public Object getOnlineCount() {
//        Integer onlineCount = activeService.getOnlineCount();
//        return ResultTemplate.success().setData("data",new ActiveVo().setCount(onlineCount));
//    }
}
