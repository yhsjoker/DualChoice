package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("DisciplineSecretaryInfo")
public class DisciplineSecretaryInfo {
    int secretaryId;
    String secretaryName;
    int disciplineId;
    String disciplineName;
}
