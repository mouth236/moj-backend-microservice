package com.mouth.mojbackendjudgeservice.controller;

import com.mouth.mojbackendjudgeservice.judge.JudgeService;
import com.mouth.mojbackendmodel.model.entity.QuestionSubmit;
import com.mouth.mojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName QuestionInnerController
 * @Description TODO
 * @date 2023/11/24 21:47
 * @Version 1.0
 */
@RestController()
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {


    @Resource
    private JudgeService judgeService;

    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
