package cn.xgs.mapper.system;

import cn.xgs.domain.dto.SnowAccessLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SnowAccessLogMapper extends BaseMapper<SnowAccessLog> {
    List<SnowAccessLog> list();

    int insert(SnowAccessLog log);



}
