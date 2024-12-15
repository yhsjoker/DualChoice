package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("GraduateManagerInfo")
public class GraduateManagerInfo {
    int graduateManagerId;
    String graduateManagerName;
    int collegeId;
    String collegeName;
    String collegeWebsite;
}
