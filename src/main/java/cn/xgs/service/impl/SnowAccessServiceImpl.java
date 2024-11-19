package cn.xgs.service.impl;


import cn.xgs.domain.dto.SnowAccessLog;
import cn.xgs.mapper.system.SnowAccessLogMapper;
import cn.xgs.service.ISnowAccessService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("snowAccessService")
public class SnowAccessServiceImpl implements ISnowAccessService {
    @Resource
    private SnowAccessLogMapper snowAccessLogMapper;


    @Override
    public List<SnowAccessLog> list() {
        return snowAccessLogMapper.list();
    }

    @Override
    public int insert(SnowAccessLog snowAccessLog) {
        return snowAccessLogMapper.insert(snowAccessLog);
    }
}
