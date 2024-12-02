package cn.edu.bjfu.dualchoice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.edu.bjfu.dualchoice.mapper")
public class DualChoiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DualChoiceApplication.class, args);
    }

}
