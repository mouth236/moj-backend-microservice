package com.mouth.mojbackendserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mouth.mojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.mouth.mojbackendmodel.model.entity.Question;
import com.mouth.mojbackendmodel.model.entity.QuestionSubmit;
import com.mouth.mojbackendmodel.model.vo.QuestionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【quetion(题目表)】的数据库操作Service
* @createDate 2023-11-17 22:03:55
*/
@FeignClient(name = "moj-backend-question-service" ,path = "/api/question/inner")
public interface QuestionFeignClient{

    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);
    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId);

    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
