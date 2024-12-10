package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("StudentChoiceInfo")
public class StudentChoiceInfo {
    int student_id;
    String student_name;
    String graduate_type;
}
