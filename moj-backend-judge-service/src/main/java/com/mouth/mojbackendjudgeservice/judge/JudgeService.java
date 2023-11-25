package com.mouth.mojbackendjudgeservice.judge;


import com.mouth.mojbackendmodel.model.entity.QuestionSubmit;

/**
 * @ClassName JudgeService
 * @Description 判题服务
 * @date 2023/11/20 22:22
 * @Version 1.0
 */
public interface JudgeService {

    /**
     * 执行判题操作
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
