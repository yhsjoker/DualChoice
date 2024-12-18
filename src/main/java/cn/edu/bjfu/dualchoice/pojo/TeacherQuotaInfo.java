package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("TeacherQuotaInfo")
public class TeacherQuotaInfo {
    private int disciplineId;

    private String disciplineName;
    private int teacherId;

    private String teacherName;

    private int academicQuota;

    private int professionalQuota;

    @TableField("direct_phd_quota")
    private int phdQuota;
}