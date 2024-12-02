package cn.edu.bjfu.dualchoice.pojo;

import lombok.Data;

@Data
public class StuExamInfoDTO {
    private int id;
    private java.sql.Timestamp reExamTime;
    private String reExamLocation;
    private String overallEvaluation;
    private float englishScore;
    private float professionalScore;
    private float interviewScore;

}
