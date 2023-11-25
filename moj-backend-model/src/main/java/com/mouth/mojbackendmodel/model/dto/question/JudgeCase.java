package com.mouth.mojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * @ClassName JudgeCase
 * @Description 题目用例
 * @date 2023/11/17 22:38
 * @Version 1.0
 */
@Data
public class JudgeCase {

    /**
     * 输入用例
     */
    private String input;
    /**
     * 输出用例
     */
    private String output;
}
