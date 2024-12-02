package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Teacher")
public class Teacher {

    private int id;

    private String name;

    private String phone;

    private String qualification;

    private byte[] image;

    private String email;

    private String title;

    private String profile;

    private int academicQuota;

    private int professionalQuota;

    private int directPhdQuota;

    private int volunteerRound;

    private int collegeId;
}