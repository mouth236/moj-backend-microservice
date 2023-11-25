package com.mouth.mojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * @ClassName JudgeConfig
 * @Description 题目配置
 * @date 2023/11/17 22:38
 * @Version 1.0
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制（ms）
     */
    private Long timeLimit;
    /**
     * 内存限制（kb）
     */
    private Long memoryLimit;
    /**
     * 堆栈限制（kb）
     */
    private Long stackLimit;
}
