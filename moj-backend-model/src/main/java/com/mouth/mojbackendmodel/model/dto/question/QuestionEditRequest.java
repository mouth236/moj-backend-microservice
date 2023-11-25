package com.mouth.mojbackendmodel.model.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 编辑请求
 *
 * @author Mouth
 */
@Data
public class QuestionEditRequest implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
