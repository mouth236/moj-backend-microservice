package com.mouth.mojbackendjudgeservice.judge;


import com.mouth.mojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.mouth.mojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.mouth.mojbackendjudgeservice.judge.strategy.JudgeContext;
import com.mouth.mojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.mouth.mojbackendmodel.model.codesandbox.JudgeInfo;
import com.mouth.mojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * @ClassName JudgeManager
 * @Description 判题管理（简化调用）
 * @date 2023/11/20 23:42
 * @Version 1.0
 */
@Service
public class JudgeManager {

    public JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if("java".equals(language)){
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
