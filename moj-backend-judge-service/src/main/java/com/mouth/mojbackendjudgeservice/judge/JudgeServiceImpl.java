package com.mouth.mojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.mouth.mojbackendcommon.common.ErrorCode;
import com.mouth.mojbackendcommon.exception.BusinessException;
import com.mouth.mojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.mouth.mojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.mouth.mojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.mouth.mojbackendjudgeservice.judge.strategy.JudgeContext;
import com.mouth.mojbackendmodel.model.codesandbox.ExecuteCodeReponse;
import com.mouth.mojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.mouth.mojbackendmodel.model.codesandbox.JudgeInfo;
import com.mouth.mojbackendmodel.model.dto.question.JudgeCase;
import com.mouth.mojbackendmodel.model.entity.Question;
import com.mouth.mojbackendmodel.model.entity.QuestionSubmit;
import com.mouth.mojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.mouth.mojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName JudgeServiceImpl
 * @Description TODO
 * @date 2023/11/20 22:39
 * @Version 1.0
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //1.传入题目的提交id，获取对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit ==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        //2.再根据题目提交状态判断
        //如果不为等待中,就无需重复执行
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WATING.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"正在判题中");
        }
        //3.更新状态为判题中，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if(!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        //4.调用沙箱，获取执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        //获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeReponse executeCodeReponse = codeSandbox.exexuteCode(executeCodeRequest);
        List<String> outputList = executeCodeReponse.getOutputList();
        //5.根据沙箱执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeReponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        //6.修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        return questionSubmitResult;
    }
}
