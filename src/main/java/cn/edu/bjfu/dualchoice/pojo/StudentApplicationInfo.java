package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("StudentApplicationInfo")
public class StudentApplicationInfo {
    int studentId;
    String studentName;
    String graduateType;
    int disciplineId;
    String disciplineName;
    int parentDiscipline;
    int preferenceOrder;
}
