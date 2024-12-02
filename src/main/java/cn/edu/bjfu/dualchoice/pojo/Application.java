package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Application")
public class Application {

    private int studentId;

    private int disciplineId;

    private int preferenceOrder;
}