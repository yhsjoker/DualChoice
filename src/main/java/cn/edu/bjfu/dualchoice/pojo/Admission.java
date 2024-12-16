package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Admission")
public class Admission {
    private int studentId;
    private int teacherId;
}
