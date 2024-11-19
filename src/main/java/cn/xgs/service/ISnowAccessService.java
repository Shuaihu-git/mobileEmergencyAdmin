package cn.xgs.service;

import cn.xgs.domain.dto.SnowAccessLog;

import java.util.List;

public interface ISnowAccessService {
    List<SnowAccessLog> list();
    int insert(SnowAccessLog snowAccessLog);
}
