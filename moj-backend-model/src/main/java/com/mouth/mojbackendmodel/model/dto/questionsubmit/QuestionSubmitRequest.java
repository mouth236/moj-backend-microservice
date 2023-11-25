package com.mouth.mojbackendmodel.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author Mouth
 */
@Data
public class QuestionSubmitRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}
