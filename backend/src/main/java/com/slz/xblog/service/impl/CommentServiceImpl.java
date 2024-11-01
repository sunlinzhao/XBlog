package com.slz.xblog.service.impl;

import com.slz.xblog.entity.CommentEntity;
import com.slz.xblog.mapper.CommentMapper;
import com.slz.xblog.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slz.xblog.vo.CommentVo;
import com.slz.xblog.vo.PostInfoVo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author slz
 * @since 2024-10-26
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;
    @Override
    @CacheEvict(value = "commentList", key = "#commentVo.postId") // 删除相应缓存
    public boolean addComment(CommentVo commentVo) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(commentVo.getContent());
        commentEntity.setPostId(commentVo.getPostId());
        commentEntity.setUserId(commentVo.getUserId());
        return this.save(commentEntity);
    }

    @Override
    @Cacheable(value = "commentList", key = "#postId")
    public List<CommentVo> getCommentsByPostId(String postId) {
        return commentMapper.getCommentsByPostId(postId);
    }
}
