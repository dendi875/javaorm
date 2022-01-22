/**
 * 实体类
 */
package com.zq.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
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
    @TableField(value = "name", condition = SqlCondition.NOT_EQUAL) // 指定对应数据库中哪一列
    private String realName;

    /**
     * 年龄
     * %s&lt;&gt;#{%s}
     *
     * %s代表列表
     * &lt; 代表 <
     * &gt; 代表 >
     * #{%s} 代表该列的值
     */
    @TableField(condition = "%s&lt;#{%s}")
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

    /**
     * 备注，这个字段在数据库中是没有的，需要排除
     */
    @TableField(exist = false)
    private String remark;
}
