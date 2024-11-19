package cn.xgs.service;

import cn.xgs.dao.SnowAccessLog;

import java.util.List;

public interface SnowAccessService {

    public List<SnowAccessLog> getSnowAccessLogs();

    public SnowAccessLog getSnowAccessLog();
}
