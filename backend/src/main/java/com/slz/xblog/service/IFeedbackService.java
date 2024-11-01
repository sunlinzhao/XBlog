package com.slz.xblog.service;

import com.slz.xblog.entity.FeedbackEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.slz.xblog.vo.FeedbackVo;

/**
 * <p>
 * 反馈意见表 服务类
 * </p>
 *
 * @author slz
 * @since 2024-10-31
 */
public interface IFeedbackService extends IService<FeedbackEntity> {

    boolean addFeedback(FeedbackVo feedbackVo);
}
