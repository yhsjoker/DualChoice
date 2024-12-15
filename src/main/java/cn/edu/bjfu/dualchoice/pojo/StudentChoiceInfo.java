package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("StudentChoiceInfo")
public class StudentChoiceInfo {
    int studentId;
    String studentName;
    String studentPhone;
    String graduateType;
    int disciplineId;
    String disciplineName;
    int teacherId;
    String teacherName;
    String teacherPhone;
}
