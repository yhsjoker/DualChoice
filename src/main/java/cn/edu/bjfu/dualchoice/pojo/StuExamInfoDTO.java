package cn.edu.bjfu.dualchoice.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StuExamInfoDTO {
    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private java.sql.Timestamp reExamTime;
    private String reExamLocation;
    private String overallEvaluation;
    private float englishScore;
    private float professionalScore;
    private float interviewScore;

}
