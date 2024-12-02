package cn.edu.bjfu.dualchoice.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("User")
public class User {

    @TableId("id")
    private int id;

    @TableField("user_name")
    @JsonProperty(value = "username")
    private String userName;

    @TableField("password")
    private String password;

    @TableField("user_identity")
    private String userIdentity;
}