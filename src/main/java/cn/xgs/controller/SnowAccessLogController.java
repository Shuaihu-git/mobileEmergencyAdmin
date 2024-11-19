package cn.xgs.controller;

import cn.xgs.dao.SnowAccessLog;
import cn.xgs.service.SnowAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SnowAccessLogController {

    @Autowired
    private SnowAccessService snowAccessService;

    @GetMapping("/openapi/snow/accesslog")
    public List<SnowAccessLog> accesslog() {
        return snowAccessService.getSnowAccessLogs();
    }
}
