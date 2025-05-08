package com.nuomi.backend.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("categories")
public class Category {
    @TableId
    private Integer categoryId;
    private String categoryName;
    private Integer navId;
}