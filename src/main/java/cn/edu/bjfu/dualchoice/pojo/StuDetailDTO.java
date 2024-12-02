package cn.edu.bjfu.dualchoice.pojo;

import lombok.Data;

import java.util.List;

@Data
public class StuDetailDTO {
    private String subject;
    private String examNumber ;
    private String name  ;
    private String studentType;
    private String graduationSchool  ;
    private java.sql.Date graduationTime  ;
    private String graduatedMajor;
    private String email ;
    private String contact  ;
    private String emergencyContact  ;
    private String origin   ;
    private String idNumber ;
    private String supervisor1  ;
    private String supervisor2  ;
    private String supervisor3  ;
    private String acceptAdjustment   ;
    private List<String> preferredSubjects    ;
    private String singleSubSubject ;
    private String researchDirection;
    private String graduateType ;
    private List<String> subSubjectOptions;
}
