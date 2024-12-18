package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.Result;
import cn.edu.bjfu.dualchoice.pojo.User;
import cn.edu.bjfu.dualchoice.service.UserService;
import cn.edu.bjfu.dualchoice.utils.AddSalt;
import cn.edu.bjfu.dualchoice.utils.JwtUtil;
import cn.edu.bjfu.dualchoice.utils.Logger;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User loginUser = userService.findByUserName(user.getUserName());
        if(loginUser == null){
            return Result.error("用户名错误");
        }
        if(!AddSalt.hashPasswordWithSalt(user.getPassword()).equals(loginUser.getPassword())){
            return Result.error("密码错误");
        }

        // 根据id和身份获得token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginUser.getId());
        claims.put("user_identity", loginUser.getUserIdentity());
        String token = JwtUtil.generateToken(claims);

        // 制造返回data
        JSONObject data = new JSONObject();
        data.put("token", token);
        data.put("user_identity", loginUser.getUserIdentity());

        Logger.log(loginUser.getUserIdentity(), loginUser.getId(), "登录成功！", Logger.LogType.SUCCESS, "/api/user/login");
        return Result.success(data);
    }
}
