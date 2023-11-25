package com.mouth.mojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mouth.mojbackendcommon.common.ErrorCode;
import com.mouth.mojbackendcommon.constant.CommonConstant;
import com.mouth.mojbackendcommon.exception.BusinessException;
import com.mouth.mojbackendcommon.utils.SqlUtils;
import com.mouth.mojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.mouth.mojbackendmodel.model.dto.questionsubmit.QuestionSubmitRequest;
import com.mouth.mojbackendmodel.model.entity.Question;
import com.mouth.mojbackendmodel.model.entity.QuestionSubmit;
import com.mouth.mojbackendmodel.model.entity.User;
import com.mouth.mojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.mouth.mojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.mouth.mojbackendmodel.model.vo.QuestionSubmitVO;
import com.mouth.mojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.mouth.mojbackendquestionservice.rabbitmq.MyMessageProducer;
import com.mouth.mojbackendquestionservice.service.QuestionService;
import com.mouth.mojbackendquestionservice.service.QuestionSubmitService;
import com.mouth.mojbackendserviceclient.service.JudgeFeignClient;
import com.mouth.mojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Lenovo
 * @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
 * @createDate 2023-11-17 22:04:44
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;
    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private MyMessageProducer myMessageProducer;

    /**
     * 提交题目
     *
     * @param questionSubmitRequest
     * @param loginUser
     * @return
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitRequest questionSubmitRequest, User loginUser) {
        //校验编程语言是否合法
        String language = questionSubmitRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        Long questionId = questionSubmitRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        long userId = loginUser.getId();
        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitRequest.getCode());
        questionSubmit.setLanguage(language);
        //设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WATING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交失败");
        }
        //执行判题服务
        Long questionSubmitId = questionSubmit.getId();
        //发送消息
        myMessageProducer.sendMessage("code_exchange","my_routingKey",String.valueOf(questionSubmitId));
        CompletableFuture.runAsync(()->{
            judgeFeignClient.doJudge(questionSubmitId);
        });
        return questionSubmitId;
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到mybatis框架支持的查询QueryWrapper类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();

        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(String.valueOf(status)) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        //脱敏：仅本人和管理员能看见自己（提交userId 和登录用户 id不同）提交代码的答案、提交代码
        Long userId = loginUser.getId();
        //处理脱敏
        if (!userId.equals(questionSubmit.getUserId()) && !userFeignClient.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }

        return questionSubmitVO;
    }


    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSumbitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSumbitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSumbitVOPage.setRecords(questionSubmitVOList);
        return questionSumbitVOPage;
    }
}




