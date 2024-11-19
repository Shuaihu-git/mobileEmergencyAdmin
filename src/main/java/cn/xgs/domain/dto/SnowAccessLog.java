package cn.xgs.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("access")
public class SnowAccessLog {
    /**
     *日志id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long logId;

    /**
     *日志标识
     */
    @TableField(value = "tag")
    private String tag;

    /**
     *日志时间
     */
    @TableField(value = "time")
    private String time;


    /**
     *日志内容
     */
    @TableField(value = "data")
    private String data;





}
