package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("DisciplineInfo")
public class DisciplineInfo {
    private int d1Id;
    private String d1Name;
    private int d2Id;
    private String d2Name;
}
