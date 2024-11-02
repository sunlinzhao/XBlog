package com.slz.xblog.controller;

import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;
import com.slz.xblog.service.AccessService;
import com.slz.xblog.service.IPostService;

import com.slz.xblog.utils.template.ResultTemplate;
import com.slz.xblog.utils.token.JwtTokenUtil;
import com.slz.xblog.vo.BaseVo;
import com.slz.xblog.vo.PostInfoVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author slz
 * @since 2024-10-21
 */
@RestController
@CrossOrigin(origins = "http://139.155.158.230:17011", allowCredentials = "true")
@RequestMapping("/xblog/user")
@Api(tags = "用户接口")
@Slf4j
public class UserController {
    @Resource
    private IPostService postService;
    @Resource
    private AccessService activeService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;


    /* ============================================================ */
    /*                         文章相关接口                           */
    /* ============================================================ */

    /**
     * 获取草稿列表（分页）
     * @param baseVo BaseVo
     * @return o Object
     */
    @ApiOperation(value = "获取草稿列表（分页）")
    @PostMapping("/post/drafts")
    public Object getDraftList(@RequestBody BaseVo baseVo) {
        // 开始分页
        PageHelper.startPage(baseVo.getPageNum(), baseVo.getPageSize());
        List<PostInfoVo> posts = postService.getDraftListById(
                new PostInfoVo().setUserId(baseVo.getUserId())
        );
        return ResultTemplate.success().setData("data", new PageInfo<>(posts));
    }

    /**
     * 获取用户文章列表
     *
     * @param postInfoVo PostInfoVo
     * @return o Object
     */
    @ApiOperation(value = "获取用户文章列表")
    @PostMapping("/post/list")
    public Object getPostList(@RequestBody PostInfoVo postInfoVo) {
        List<PostInfoVo> posts = postService.getPostListById(postInfoVo);
        return ResultTemplate.success().setData("data", posts);
    }
    /**
     * 获取用户文章列表 (分页)
     * 使用 pagehelper
     * @param baseVo BaseVo
     * @return o Object
     */
    @ApiOperation(value = "获取用户文章列表 (分页)")
    @PostMapping("/post/page")
    @Cacheable(value = "postList", key = "#baseVo.userId + ':' + #baseVo.pageNum + ':' + #baseVo.pageSize")
    public Object getPostListPage(@RequestBody BaseVo baseVo) {
        // 开始分页
        PageHelper.startPage(baseVo.getPageNum(), baseVo.getPageSize());
        List<PostInfoVo> posts = postService.getPostListById(
                new PostInfoVo().setUserId(baseVo.getUserId())
        );
        return ResultTemplate.success().setData("data", new PageInfo<>(posts));
    }
    /**
     * 上传文章
     * @param postInfoVo PostInfoVo
     * @return o Object
     */
    @ApiOperation(value = "上传文章")
    @PostMapping("/post/upload")
    public Object uploadPost(@RequestBody PostInfoVo postInfoVo) {
        boolean b = postService.uploadPost(postInfoVo);
        if(b) return ResultTemplate.success().setMsg("发布成功");
        return ResultTemplate.error().setMsg("发布失败");
    }
    /**
     * 上传文章（文件）
     * @return o Object
     */
    @ApiOperation(value = "上传文章（文件）")
    @PostMapping("/post/uploadFile")
    public Object uploadPostFile( @RequestParam("file") MultipartFile file,
                                  @RequestParam("title") String title,
                                  @RequestParam("userId") String userId,
                                  @RequestParam("postId") String postId,
                                  @RequestParam("tagNames") String tagNames,
                                  @RequestParam("status") String status)
    {
        try {
            // 确保文件是 .md 格式
            if (!file.getOriginalFilename().endsWith(".md")) {
                return ResultTemplate.error().setMsg("请上传一个有效的 .md 文件");
            }
            // 将文件内容读取为字符串
            String content = new String(file.getBytes());
            // 创建新的 PostInfo 实例并保存到数据库
            PostInfoVo postInfoVo = new PostInfoVo().setContent(content)
                    .setTitle(title)
                    .setPostId(postId)
                    .setTagNames(tagNames)
                    .setStatus(status)
                    .setUserId(userId);
            if(postService.uploadPost(postInfoVo))
                return ResultTemplate.success().setMsg("文件上传成功");
            return ResultTemplate.error().setMsg("文件上传失败");
        } catch (IOException e) {
            e.printStackTrace();
            return ResultTemplate.success().setMsg("文件上传失败");
        }
    }
    /**
     * 删除文章
     * @param postInfoVo PostInfoVo
     * @return o Object
     */
    @ApiOperation(value = "删除文章")
    @PostMapping("/post/del")
    public Object deletePost(@RequestBody PostInfoVo postInfoVo) {
        boolean b = postService.deletePost(postInfoVo);
        if(b) return ResultTemplate.success().setMsg("删除成功");
        return ResultTemplate.error().setMsg("删除失败");
    }
    /**
     * 修改文章
     * @param postInfoVo PostInfoVo
     * @return o Object
     */
    @ApiOperation(value = "修改文章")
    @PostMapping("/post/update")
    public Object updatePost(@RequestBody PostInfoVo postInfoVo) {
        boolean b = postService.updatePost(postInfoVo);
        if(b) return ResultTemplate.success().setMsg("修改成功");
        return ResultTemplate.error().setMsg("修改失败");
    }


    /* ============================================================ */
    /*                         状态相关接口                           */
    /* ============================================================ */

    /**
     * 登出
     *
     * @param request HttpServletRequest
     * @return o Object
     */
    @PostMapping("/logout")
    public Object logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            authorization = authorization.replace("\"", "");
            String token = authorization.substring(7); // 去掉 "Bearer " 前缀

            // 从 token 中获取 SessionId
            String sessionId = jwtTokenUtil.getSessionId(token);
            // 使会话失效
            if (sessionId != null) {
                activeService.removeUser(sessionId);
                return ResultTemplate.success().setMsg("登出成功！");
            } else {
                return ResultTemplate.error().setMsg("会话已过期或不存在！");
            }
        }
        return ResultTemplate.error().setMsg("请求中未提供有效的授权信息！");
    }
}
