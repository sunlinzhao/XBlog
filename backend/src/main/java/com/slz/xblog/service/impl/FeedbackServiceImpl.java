package com.slz.xblog.service.impl;

import com.slz.xblog.entity.FeedbackEntity;
import com.slz.xblog.mapper.FeedbackMapper;
import com.slz.xblog.service.IFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slz.xblog.vo.FeedbackVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 反馈意见表 服务实现类
 * </p>
 *
 * @author slz
 * @since 2024-10-31
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, FeedbackEntity> implements IFeedbackService {

    @Override
    public boolean addFeedback(FeedbackVo feedbackVo) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setUserId(feedbackVo.getUserId());
        feedbackEntity.setContent(feedbackVo.getContent());
        return this.save(feedbackEntity);
    }
}
