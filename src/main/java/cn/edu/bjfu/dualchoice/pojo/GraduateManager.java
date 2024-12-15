package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("GraduateManager")
public class GraduateManager {

    private int id;

    private String name;

    private int collegeId;
}