package com.nuomi.backend.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("user_follow")
public class UserFollow {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("follow_user_id")
    private Long followUserId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}