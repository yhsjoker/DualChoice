package cn.edu.bjfu.dualchoice.pojo;

import lombok.Data;

import java.util.List;

@Data
public class SelectionsDTO {
    List<TeachingQuotaUpdateDTO> secondarySubjects;
    List<Integer> selectedStudents;
}
