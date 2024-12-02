package cn.edu.bjfu.dualchoice.pojo;

import lombok.Data;

@Data
public class Teaching {

    private int disciplineId;

    private int teacherId;

    private int academicQuota;

    private int professionalQuota;

    private int directPhdQuota;
}