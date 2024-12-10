package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("TeacherBaseInfo")
public class TeacherBaseInfo {
    int teacherId;
    String teacherName;
    int collegeId;
    String collegeName;
    String email;
    String phone;
    String title;
    int volunteer_round;
}
