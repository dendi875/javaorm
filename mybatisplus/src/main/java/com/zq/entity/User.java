/**
 * 实体类
 */
package com.zq.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mp_user")
public class User {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 姓名
     */
    @TableField("name") // 指定对应数据库中哪一列
    private String realName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 直属上级ID
     */
    private Long managerId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
