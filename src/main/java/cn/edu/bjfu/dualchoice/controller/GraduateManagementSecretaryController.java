package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graduateManagementSecretary")
public class GraduateManagementSecretaryController {
    @GetMapping("/getCollege")
    public Result getCollege(){
        return Result.success();
    }
}
