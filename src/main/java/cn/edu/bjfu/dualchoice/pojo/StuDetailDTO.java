package cn.edu.bjfu.dualchoice.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class StuDetailDTO {
    private String subject;
    private String examNumber ;
    private String name  ;
    private String studentType;
    private String graduationSchool  ;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Timestamp graduationTime  ;
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
