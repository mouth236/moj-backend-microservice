package com.mouth.mojbackenduserservice.controller.inner;

import com.mouth.mojbackendmodel.model.entity.User;
import com.mouth.mojbackendserviceclient.service.UserFeignClient;
import com.mouth.mojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName UserInnerController
 * @Description 该服务为内部调用，不提供给前端
 * @date 2023/11/24 21:41
 * @Version 1.0
 */
@RestController()
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList")Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}
