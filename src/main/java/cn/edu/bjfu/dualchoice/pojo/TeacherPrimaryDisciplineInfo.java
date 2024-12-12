package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("TeacherPrimaryDisciplineInfo")
public class TeacherPrimaryDisciplineInfo {
    int teacherId;
    String teacherName;
    int disciplineId;
    String disciplineName;
}
