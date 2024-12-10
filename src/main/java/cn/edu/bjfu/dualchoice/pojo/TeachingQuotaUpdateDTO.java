package cn.edu.bjfu.dualchoice.pojo;

import lombok.Data;

@Data
public class TeachingQuotaUpdateDTO {
    String disciplineName;
    int academicQuota;
    int professionalQuota;
    int phdQuota;
}
