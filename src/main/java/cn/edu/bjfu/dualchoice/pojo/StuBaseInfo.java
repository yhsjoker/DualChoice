package cn.edu.bjfu.dualchoice.pojo;

import lombok.Data;

@Data
public class StuBaseInfo {
    private String discipline;
    private String examNumber;
    int studentId;
    private String name;
    private String studentType;
    private String graduationSchool;
    private java.sql.Date graduationTime;
    private String graduatedMajor;
    private String email;
    private String contact;
    private String emergencyContact;
    private String origin;
    private String idNumber;
    private String graduateType;

}
