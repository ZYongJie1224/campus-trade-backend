package com.nuomi.backend.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("navigation")
public class Navigation {

    @TableId
    private Integer navId;

    private String navName;

    private String navIcon;

    private String navRoute;

    private Integer parentId;


    private Integer orderIndex;

    private Boolean isActive;
}