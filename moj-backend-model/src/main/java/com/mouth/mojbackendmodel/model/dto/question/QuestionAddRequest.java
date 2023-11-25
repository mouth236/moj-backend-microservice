package com.mouth.mojbackendmodel.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建请求
 *
 * @author Mouth
 */
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     *  判题用例（json数组）
     */
    private List<JudgeCase> judgeCase;

    /**
     *  判题配置（json对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 通过率
     */
    private BigDecimal passRate;

    private static final long serialVersionUID = 1L;
}
