package com.mouth.mojbackendjudgeservice.judge.codesandbox.impl;


import com.mouth.mojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.mouth.mojbackendmodel.model.codesandbox.ExecuteCodeReponse;
import com.mouth.mojbackendmodel.model.codesandbox.ExecuteCodeRequest;

/**
 * @ClassName ExampleCodeSandbox
 * @Description 第三方代码沙箱
 * @date 2023/11/20 21:25
 * @Version 1.0
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeReponse exexuteCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
