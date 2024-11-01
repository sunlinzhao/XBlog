package com.slz.xblog.service.impl;

import com.slz.xblog.entity.PostEntity;
import com.slz.xblog.entity.PostTagEntity;
import com.slz.xblog.entity.TagEntity;
import com.slz.xblog.mapper.CommentMapper;
import com.slz.xblog.mapper.PostMapper;
import com.slz.xblog.mapper.PostTagMapper;
import com.slz.xblog.mapper.TagMapper;
import com.slz.xblog.service.IPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slz.xblog.vo.PostCountByDayVo;
import com.slz.xblog.vo.PostInfoVo;
import com.slz.xblog.vo.PostSearchVo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author slz
 * @since 2024-10-25
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, PostEntity> implements IPostService {
    @Resource
    private PostMapper postMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private PostTagMapper postTagMapper;
    @Resource
    private CommentMapper commentMapper;

    @Override
    @Transactional // 开启事务, 默认捕获运行时异常和错误时才回退事务
    @CacheEvict(value = "postList")
    public boolean uploadPost(PostInfoVo postInfoVo) {
        String postId = null;
        try {
            // 插入 post
            PostEntity postEntity = new PostEntity()
                    .setTitle(postInfoVo.getTitle())
                    .setContent(postInfoVo.getContent())
                    .setStatus(postInfoVo.getStatus())
                    .setUserId(postInfoVo.getUserId());
            int insert = postMapper.insert(postEntity);
            if (insert > 0) {
                postId = postEntity.getPostId();
            } else {
                throw new RuntimeException("插入失败");
            }

            // 插入 tag
            List<String> tagIds = new ArrayList<>();
            List<String> tagNameList = postInfoVo.getTagNameList();
            // 去重
            List<String> uniqueTagNameList = new ArrayList<>(new HashSet<>(tagNameList));
            for (String tagName : uniqueTagNameList) {
                String tagId = tagMapper.selectByTagName(tagName);
                if (tagId == null) { // 不存在
                    TagEntity tagEntity = new TagEntity().setTagName(tagName);
                    int insertTag = tagMapper.insert(tagEntity);
                    if (insertTag > 0) {
                        tagId = tagEntity.getTagId();
                    } else {
                        throw new RuntimeException("插入失败");
                    }
                }
                tagIds.add(tagId);
            }

            // 插入 post_tag
            for (String tagId : tagIds) {
                PostTagEntity postTagEntity = new PostTagEntity()
                        .setPostId(postId)
                        .setTagId(tagId);
                int insertPostTag = postTagMapper.insert(postTagEntity);
                if (insertPostTag == 0) {
                    throw new RuntimeException("插入失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // 重新抛出异常以确保事务回滚
        }
        return true;
    }


    @Override
    public List<PostInfoVo> getPostListById(PostInfoVo postInfoVo) {
        return postMapper.getPostListById(postInfoVo);
    }

    @Override
    @Transactional
    @CacheEvict(value = "postList")
    public boolean deletePost(PostInfoVo postInfoVo) {
        String postId = postInfoVo.getPostId();
        List<String> tagIdList = postInfoVo.getTagIdList();
        if (postId == null) return false;
//        List<String> tagIdList = postTagMapper.selectTagIdByPostId(postId);
        try {
            // 删除 post
            if (postMapper.deleteById(postId) > 0) {
                // 删除 comment
                if (commentMapper.countByPostId(postId) > 0) { // 有评论才进行删除
                    if (commentMapper.deleteByPostId(postId) == 0)
                        throw new RuntimeException("删除失败");
                }
                // 如果文章没有标签，直接返回
                if (tagIdList.size() == 0) return true;
                // 删除 post_tag
                if (deleteIfNoUse(postId, tagIdList)) return true;
            }
            throw new RuntimeException("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // 重新抛出异常以触发事务回滚
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "postDetail", key = "#postInfoVo.postId") // 删除相应缓存
    public boolean updatePost(PostInfoVo postInfoVo) {
        String postId = postInfoVo.getPostId(); // 文章ID
        if (postId == null) return false;
        String title = postInfoVo.getTitle(); // 标题
        String content = postInfoVo.getContent(); // 内容
        List<String> tagNameList = postInfoVo.getTagNameList(); // 标签
        String status = postInfoVo.getStatus(); // 状态
        try {
            // 构建实体对象
            PostEntity postEntity = new PostEntity().setTitle(title)
                    .setContent(content)
                    .setStatus(status)
                    .setPostId(postId);
            // 更新 post
            if (postMapper.updateSelective(postEntity) > 0) {
                // 更新 tag，tag 已存在则拿到 tagId，不存在则插入后拿到 tagIds
                if (tagNameList.size() == 0) { // 标签为空，先断开所有标签关联，再检查标签是否被其他文章使用，如果可以删除则删除
                    // 拿到所有与 postId 关联的 tagId
                    List<String> tagIdList = postTagMapper.selectTagIdByPostId(postId);
                    // 如果文章存在关联标签，则删除所有关联
                    if (tagIdList.size() != 0) {
                        if (deleteIfNoUse(postId, tagIdList)) return true;
                    } else return true; // 文章不存在关联标签，直接返回
                } else { // 标签不为空，考虑标签是否重复（去重），标签已存在则获取 tagId，不存在则插入后获取 tagId，再插入 post_tag
                    List<String> uniqueTagNameList = new ArrayList<>(new HashSet<>(tagNameList)); // 去重
                    // 拿到所有与 postId 关联的 tagId
                    List<String> oldTagIdList = postTagMapper.selectTagIdByPostId(postId);
                    // 新的与 postId 关联的 tagId
                    List<String> newTagIdList = new ArrayList<>();
                    for (String tagName : uniqueTagNameList) {
                        String tagId = tagMapper.selectByTagName(tagName);
                        if (tagId == null) { // 标签不存在
                            TagEntity tagEntity = new TagEntity().setTagName(tagName);
                            int insertTag = tagMapper.insert(tagEntity);
                            if (insertTag > 0) {
                                tagId = tagEntity.getTagId();
                                newTagIdList.add(tagId); // 不存在的标签，一定需要插入关联表
                            } else throw new RuntimeException("更新失败");
                        } else { // 标签已存在
                            if (oldTagIdList.contains(tagId)) { // 如果已经关联则跳过
                                oldTagIdList.remove(tagId);
                            } else { // 没有关联过则插入关联表 post_tag
                                newTagIdList.add(tagId);
                            }
                        }
                    }
                    // 最后需要删除掉不再关联的标签，即 oldTagIdList 中剩余的标签（tag，post_tag）
                    // 插入需要插入的关联表，即 newTagIdList（post_tag）
                    if (oldTagIdList.size() == 0 || deleteIfNoUse(postId, oldTagIdList)) {
                        if (newTagIdList.size() == 0 || postTagMapper.insertPostTag(postId, newTagIdList) > 0) {
                            return true;
                        }
                    }
                }
            }
            throw new RuntimeException("更新失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // 重新抛出异常以触发事务回滚
        }
    }

    @Override
    @Transactional
    @Cacheable(value = "postDetail", key = "#postId") // postDetail 相当于缓存组的名称，postDetail::postId，表示缓存组名称为 postDetail，key 为 postDetail::postId
    public PostInfoVo getOneById(String postId) {
        PostInfoVo oneById = postMapper.getOneById(postId);
        if (oneById == null) throw new RuntimeException("获取失败");
        int i = postMapper.updateViewCount(postId);
        if (i == 0) throw new RuntimeException("更新浏览量失败");
        return oneById;
    }

    @Override
    public List<PostInfoVo> getDraftListById(PostInfoVo setUserId) {
        return postMapper.getDraftListById(setUserId);
    }

    @Override
    @Cacheable(value = "postViewRank", key = "'postViewRank'")
    public List<PostInfoVo> getPostViewRank() {
        return postMapper.getPostViewRank();
    }

    @Override
    @Cacheable(value = "postPublishCount", key = "'postPublishCount'")
    public List<PostCountByDayVo> getPublishCount() {
        return postMapper.getPublishCount();
    }

    @Override
    public List<PostInfoVo> getPostSearchList(PostSearchVo postSearchVo) {
        return postMapper.getPostSearchList(postSearchVo);
    }

    // 删除关联表，并检查是否可以删除标签
    private boolean deleteIfNoUse(String postId, List<String> tagIdList) {
        if (postTagMapper.deletePostTag(postId, tagIdList) > 0) {
            // 检查标签是否被其他文章使用，如果可以删除则删除
            List<String> cnaTagIdList = new ArrayList<>();
            for (String tagId : tagIdList) {
                if (postTagMapper.countByTagId(tagId) == 0)
                    cnaTagIdList.add(tagId);
            }
            return cnaTagIdList.size() == 0 || tagMapper.deleteTags(cnaTagIdList) > 0;
        }
        return false;
    }

}
