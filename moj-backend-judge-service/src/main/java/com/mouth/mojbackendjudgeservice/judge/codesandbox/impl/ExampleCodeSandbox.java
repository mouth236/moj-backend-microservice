package com.mouth.mojbackendjudgeservice.judge.codesandbox.impl;


import com.mouth.mojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.mouth.mojbackendmodel.model.codesandbox.ExecuteCodeReponse;
import com.mouth.mojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.mouth.mojbackendmodel.model.codesandbox.JudgeInfo;
import com.mouth.mojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.mouth.mojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * @ClassName ExampleCodeSandbox
 * @Description 示例代码沙箱（跑通流程）
 * @date 2023/11/20 21:25
 * @Version 1.0
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeReponse exexuteCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeReponse executeCodeReponse  = new ExecuteCodeReponse();
        executeCodeReponse.setOutputList(inputList);
        executeCodeReponse.setMessage("测试执行成功");
        executeCodeReponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeReponse.setJudgeInfo(judgeInfo);
        return executeCodeReponse;
    }
}
