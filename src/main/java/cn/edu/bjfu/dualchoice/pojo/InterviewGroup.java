package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("InterviewGroup")
public class InterviewGroup {

    @TableId("id")
    private int id;

    @TableId("discipline_id")
    private int disciplineId;
}
