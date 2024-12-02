package cn.edu.bjfu.dualchoice.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExamInfo {
    private int id;
    private String Name;
    private java.sql.Timestamp time;
    private String location;
    private String comment;
    private float score;
}
