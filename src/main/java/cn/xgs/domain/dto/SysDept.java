package cn.xgs.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dept")
public class SysDept {
    /**
     *部门ID
     */
    @TableId(value = "dept_id",type = IdType.AUTO)
    private Long deptId;

    /**
     *父ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     *祖级列表
     */
    @TableField(value = "ancestors")
    private String ancestors;

    /**
     *部门名称
     */
    @TableField(value = "dept_name")
    private String deptName;

    /**
     *逻辑删除
     */
    @TableField(value = "del_flag")
    private String delFlag;

    /**
     *创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     *创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time")
    private Date createTime;

    /**
     *更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     *更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time")
    private Date updateTime;
    /**
     * 部门状态:0正常,1停用
     */
    private String status;

    /**
     * 子部门
     */
    private List<SysDept> children = new ArrayList<>();

}