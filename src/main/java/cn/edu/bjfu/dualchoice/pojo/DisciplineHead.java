package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("DisciplineHead")
public class DisciplineHead {

    private int id;

    private String name;

    private int disciplineId;
}