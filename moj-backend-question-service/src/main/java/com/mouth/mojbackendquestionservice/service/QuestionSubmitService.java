package com.mouth.mojbackendquestionservice.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mouth.mojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.mouth.mojbackendmodel.model.dto.questionsubmit.QuestionSubmitRequest;
import com.mouth.mojbackendmodel.model.entity.QuestionSubmit;
import com.mouth.mojbackendmodel.model.entity.User;
import com.mouth.mojbackendmodel.model.vo.QuestionSubmitVO;

/**
* @author Lenovo
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2023-11-17 22:04:44
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitRequest
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitRequest questionSubmitRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
