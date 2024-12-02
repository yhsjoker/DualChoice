package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class Student {
    private int id;

    private String name;

    private String phone;

    //@TableId("emergency_phone")
    private String emergencyPhone;

    private String email;

    //@TableId("undergraduate_major")
    private String undergraduateMajor;

    //@TableId("undergraduate_school")
    private String undergraduateSchool;

    private String origin;

    //@TableId("undergraduate_type")
    private String undergraduateType;

    private String resume;

    //@TableId("birth_date")
    private java.sql.Date birthDate;

    //@TableId("undergraduate_major")
    private java.sql.Date graduationDate;

    //@TableId("undergraduate_major")
    private String examTicketNumber;

    //@TableId("undergraduate_major")
    private String idCardNumber;

    private String category;

    //@TableId("undergraduate_major")
    private String volunteerStatus;

    //@TableId("undergraduate_major")
    private String graduateType;
}