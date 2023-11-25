package com.mouth.mojbackendquestionservice.controller.inner;

import com.mouth.mojbackendmodel.model.entity.Question;
import com.mouth.mojbackendmodel.model.entity.QuestionSubmit;
import com.mouth.mojbackendquestionservice.service.QuestionService;
import com.mouth.mojbackendquestionservice.service.QuestionSubmitService;
import com.mouth.mojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController()
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;
    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId")long questionId) {
        return questionService.getById(questionId);
    }

    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }


    @Override
    @GetMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
