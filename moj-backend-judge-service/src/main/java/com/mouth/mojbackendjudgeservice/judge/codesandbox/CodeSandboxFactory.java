package com.mouth.mojbackendjudgeservice.judge.codesandbox;


import com.mouth.mojbackendjudgeservice.judge.codesandbox.impl.ExampleCodeSandbox;
import com.mouth.mojbackendjudgeservice.judge.codesandbox.impl.RemoteCodeSandbox;
import com.mouth.mojbackendjudgeservice.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * @ClassName CodeSandboxFactory
 * @Description 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 * @date 2023/11/20 21:43
 * @Version 1.0
 */
public class CodeSandboxFactory {
    public static CodeSandbox newInstance(String type){
        switch (type){
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return  new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
