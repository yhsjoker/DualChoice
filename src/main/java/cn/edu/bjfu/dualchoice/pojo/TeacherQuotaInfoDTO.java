package cn.edu.bjfu.dualchoice.pojo;

import lombok.Data;

@Data
public class TeacherQuotaInfoDTO {
    String name;
    int academicQuota;
    int professionalQuota;
    int phdQuota;
}
