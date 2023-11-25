package com.mouth.mojbackendserviceclient.service;


import com.mouth.mojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName JudgeService
 * @Description 判题服务
 * @date 2023/11/20 22:22
 * @Version 1.0
 */

@FeignClient(name = "moj-backend-judge-service" ,path = "/api/judge/inner")
public interface JudgeFeignClient {

    /**
     * 执行判题操作
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
