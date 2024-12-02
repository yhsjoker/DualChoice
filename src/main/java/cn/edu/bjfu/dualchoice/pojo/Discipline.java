package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Discipline")
public class Discipline {

    private int id;

    private String name;

    private String type;

    private int quota;

    private String overview;

    private int parentDiscipline;

    private int level;

    private int collegeId;
}