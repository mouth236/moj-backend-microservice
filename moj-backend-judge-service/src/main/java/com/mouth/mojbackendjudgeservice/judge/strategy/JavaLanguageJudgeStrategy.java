package com.mouth.mojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.mouth.mojbackendmodel.model.codesandbox.JudgeInfo;
import com.mouth.mojbackendmodel.model.dto.question.JudgeCase;
import com.mouth.mojbackendmodel.model.dto.question.JudgeConfig;
import com.mouth.mojbackendmodel.model.entity.Question;
import com.mouth.mojbackendmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName DefaultJudgeStrategy
 * @Description Java程序的判题策略
 * @date 2023/11/20 23:14
 * @Version 1.0
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy{
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);


        //先判断沙箱执行结果输出数量是否和预期输出数量相等
        if (outputList.size()!= inputList.size()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        //依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))){
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        //判断题目配置
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needmemoryLimit = judgeConfig.getMemoryLimit();
        Long needtimeLimit = judgeConfig.getTimeLimit();
        if (memory>needmemoryLimit){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        //比如java本身需要额外执行10秒钟（举例子昂）
        long JAVA_PROGRAM_TIME_COST = 10000L;
        if ((time-JAVA_PROGRAM_TIME_COST) > needtimeLimit){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
