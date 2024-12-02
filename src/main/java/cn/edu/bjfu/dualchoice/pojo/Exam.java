package cn.edu.bjfu.dualchoice.pojo;

import lombok.Data;

@Data
public class Exam {

    private int subjectId;

    private int studentId;

    private String type;

    private float score;

    private java.sql.Timestamp time;

    private String location;

    private String comment;

    private String signature;
}