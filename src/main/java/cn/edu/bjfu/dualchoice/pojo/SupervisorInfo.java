package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("SupervisorInfo")
public class SupervisorInfo {
    int supervisorId;
    String supervisorName;
    int collegeId;
    String collegeName;
}
