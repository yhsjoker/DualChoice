package cn.edu.bjfu.dualchoice;

import cn.edu.bjfu.dualchoice.service.UserService;
import cn.edu.bjfu.dualchoice.utils.AddSalt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DualChoiceApplicationTests {
    @Autowired
    UserService userService;
    @Test
    void getPasswd() {
        String pwd = "yjsglzA";
        System.out.println(AddSalt.hashPasswordWithSalt(pwd));
    }

}
