package com.mouth.mojbackendjudgeservice.judge.codesandbox;


import com.mouth.mojbackendmodel.model.codesandbox.ExecuteCodeReponse;
import com.mouth.mojbackendmodel.model.codesandbox.ExecuteCodeRequest;

/**
 * @ClassName CodeSandbox
 * @Description TODO
 * @date 2023/11/20 21:01
 * @Version 1.0
 */
public interface CodeSandbox {

    ExecuteCodeReponse exexuteCode(ExecuteCodeRequest executeCodeRequest);
}
