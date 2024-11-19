package cn.xgs.service;


import cn.xgs.dao.SnowAccessLog;
import cn.xgs.dao.SnowAccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnowAccessServiceImpl implements SnowAccessService{

    @Autowired
    private SnowAccessLogRepository snowAccessLogRepository;

    @Override
    public List<SnowAccessLog> getSnowAccessLogs() {

        return snowAccessLogRepository.findAll();
    }

    @Override
    public SnowAccessLog getSnowAccessLog() {
        return snowAccessLogRepository.findAll().get(0);
    }
}
