package com.slz.xblog.service;

import com.slz.xblog.entity.CommentEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.slz.xblog.vo.CommentVo;
import com.slz.xblog.vo.PostInfoVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author slz
 * @since 2024-10-26
 */
public interface ICommentService extends IService<CommentEntity> {

    boolean addComment(CommentVo commentVo);

    List<CommentVo> getCommentsByPostId(String postId);
}
