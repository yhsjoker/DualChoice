package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Choice")
public class Choice {

    private int teacherId;

    private int studentId;

    private int priority;
}