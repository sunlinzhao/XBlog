package com.slz.xblog.controller;

import com.slz.xblog.service.*;
import com.slz.xblog.utils.template.ResultTemplate;
import com.slz.xblog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/23
 */
@RestController
@CrossOrigin(origins = "http://localhost:17011", allowCredentials = "true")
@RequestMapping("/xblog/public")
@Api(tags = "公共接口")
public class PublicController {
    @Resource
    private IUserService userService;
    @Resource
    private EmailService emailService;
    @Resource
    private IPostService postService;
    @Resource
    private ITagService tagService;
    @Resource
    private AccessService accessService;
    @Resource
    private ICommentService commentService;
    @Resource
    private IFeedbackService feedbackService;

    /* ============================================================ */
    /*                         登录相关接口                           */
    /* ============================================================ */
    /**
     * 邮箱登录
     *
     * @param user UserEntity
     * @return o Object
     */

//    @PreAuthorize("permitAll()")
    // @PreAuthorize：通过此注解，可以在方法级别控制访问权限。你可以根据角色设置不同的接口访问规则。
//    @PreAuthorize("hasAnyRole('admin', 'user')") // user 和 admin 角色都可以访问
    @ApiOperation(value = "邮箱登录")
    @PostMapping("/login")
    public Object emailLogin(@RequestBody UserInfoVo user, HttpServletRequest request, HttpServletResponse response) {
        // 从请求中获取 session
        HttpSession session = request.getSession();
        // 登录
        UserInfoVo userInfoVo = userService.emailLogin(user, session);
        if(userInfoVo != null)
            return ResultTemplate.success().setData("data", userInfoVo).setMsg("登录成功！");
        return ResultTemplate.error().setMsg("用户不存在，或密码错误！");
    }

    /**
     * 邮箱注册
     *
     * @param user UserEntity
     * @return o Object
     */
    @ApiOperation(value = "邮箱注册")
    @PostMapping("/register")
    public Object emailRegister(@RequestBody UserInfoVo user) {
        boolean b = userService.emailRegister(user);
        if(b) return ResultTemplate.success();
        return ResultTemplate.error().setMsg("注册失败，请检查输入是否正确！");
    }
    /**
     * 发送邮箱验证码
     *
     * @param userInfoVo UserInfoVo
     * @return o Object
     */
    @ApiOperation(value = "发送邮箱验证码")
    @PostMapping ("/sendCode")
    public Object sendVerificationCode(@RequestBody UserInfoVo userInfoVo) {
        emailService.sendVerificationCode(userInfoVo);
        return ResultTemplate.success().setMsg("验证码发送成功！");
    }

    /**
     * 重置密码
     * @param userInfoVo UserInfoVo
     * @return o Object
     */
    @ApiOperation(value = "重置密码")
    @PostMapping("/resetPwd")
    public Object resetPassword(@RequestBody UserInfoVo userInfoVo) {
        boolean b = userService.resetPassword(userInfoVo);
        if (b){
            return ResultTemplate.success().setMsg("重置密码成功！");
        } else {
            return ResultTemplate.error().setMsg("重置密码失败！");
        }
    }

    /* ============================================================ */
    /*                         文章相关接口                           */
    /* ============================================================ */
    /**
     * 文章多条件查询
     *
     * @param postSearchVo PostSearchVo
     * @return o Object
     */
    @ApiOperation(value = "文章多条件查询")
    @PostMapping("/post/search")
    public Object getPostList(@RequestBody PostSearchVo postSearchVo) {
        List<PostInfoVo> list = postService.getPostSearchList(postSearchVo);
        return ResultTemplate.success().setData("data", list);
    }

    /**
     * 获取文章内容
     *
     * @param postInfoVo PostInfoVo
     * @return o Object
     */
    @ApiOperation(value = "获取文章内容")
    @PostMapping("/post/one")
    public Object getPost(@RequestBody PostInfoVo postInfoVo) {
        PostInfoVo post = postService.getOneById(postInfoVo.getPostId());
        if(post != null) return ResultTemplate.success().setData("data", post);
        return ResultTemplate.error().setMsg("获取博客内容失败！");
    }

    /**
     * 添加评论
     * @param commentVo CommentVo
     * @return o Object
     */
    @ApiOperation(value = "添加评论")
    @PostMapping("/comment/add")
    public Object addComment(@RequestBody CommentVo commentVo) {
        if(commentService.addComment(commentVo))
            return ResultTemplate.success().setMsg("评论成功！");
        return ResultTemplate.error().setMsg("评论失败！");
    }

    /**
     * 获取文章评论
     * @param postInfoVo PostInfoVo
     * @return o Object
     */
    @ApiOperation(value = "获取文章评论")
    @PostMapping("/comment/list")
    public Object getCommentList(@RequestBody PostInfoVo postInfoVo) {
        List<CommentVo> list = commentService.getCommentsByPostId(postInfoVo.getPostId());
        return ResultTemplate.success().setData("data", list);
    }

    /* ============================================================ */
    /*                         排名统计相关接口                        */
    /* ============================================================ */
    /**
     * 统计文章浏览量排名（降序）（取前十，浏览量相同按创建时间升序排列）
     *
     * @return o Object
     */
    @ApiOperation(value = "统计文章浏览量排名")
    @GetMapping ("/post/view-rank")
    public Object getPostViewRank() {
        return ResultTemplate.success().setData("data", postService.getPostViewRank());
    }
    /**
     * 统计用户浏览量排名（降序）（取前十，浏览量相同按创建时间升序排列）
     *
     * @return o Object
     */
    @ApiOperation(value = "统计用户浏览量排名")
    @GetMapping("/user/view-rank")
    public Object getUserViewRank() {
        return ResultTemplate.success().setData("data", userService.getUserViewRank());
    }


    /* ============================================================ */
    /*                         首页统计相关接口                        */
    /* ============================================================ */
    /**
     * 获取各个标签的文章数量
     *
     * @return o Object
     */
    // 使用 HttpSession 完成在线人数统计
    @GetMapping("/online-count")
    public Object getOnlineCount(HttpServletRequest request) {
        ServletContext servletContext = request.getSession().getServletContext();
        Integer activeSessionCount = (Integer) servletContext.getAttribute("activeSessionCount");
        return ResultTemplate.success()
                .setData("data",new ActiveVo()
                        .setCount(activeSessionCount));
    }
    @ApiOperation(value = "获取各个标签的文章数量")
    @GetMapping ("/tag/postNum")
    public Object getTagPostsNum() {
        return ResultTemplate.success().setData("data", tagService.getTagPostsNum());
    }
    /**
     * 获取网站访问量
     * @return o Object
     */
    @ApiOperation(value = "获取网站访问量")
    @GetMapping("/visit-count")
    public Object getVisitCount() {
        return ResultTemplate.success().setData("data", accessService.getVisitCount());
    }

    /**
     * 获取近30日，每日文章发布数量
     * @return o Object
     */
    @ApiOperation(value = "获取近30日，每日文章发布数量")
    @GetMapping("/post/publish-count")
    public Object getPublishCount() {
        return ResultTemplate.success().setData("data", postService.getPublishCount());
    }
    /* ============================================================ */
    /*                         反馈相关接口                           */
    /* ============================================================ */
    @ApiOperation(value = "反馈")
    @PostMapping("/feedback")
    public Object addFeedback(@RequestBody FeedbackVo feedbackVo) {
        if(feedbackService.addFeedback(feedbackVo))
            return ResultTemplate.success().setMsg("反馈成功！");
        else return ResultTemplate.error().setMsg("反馈失败！");
    }
}
