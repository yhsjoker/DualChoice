package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("TeacherQuotaInfo")
public class TeacherQuotaInfo {
    private String name;

    @TableField("academic_quota")
    private int academicQuota;

    @TableField("professional_quota")
    private int professionalQuota;

    @TableField("direct_phd_quota")
    private int phdQuota;
}
