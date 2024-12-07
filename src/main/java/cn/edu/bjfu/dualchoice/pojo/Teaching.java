package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Teaching")
public class Teaching {

    private int disciplineId;

    private int teacherId;

    private int academicQuota;

    private int professionalQuota;

    private int directPhdQuota;
}