package cn.xgs.controller;

import cn.xgs.domain.dto.SnowAccessLog;
import cn.xgs.service.ISnowAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class SnowAccessLogController {

    @Autowired
    private ISnowAccessService snowAccessService;

    @GetMapping("/log/list")
    public List<SnowAccessLog> list() {
        return snowAccessService.list();
    }
    @GetMapping("/log/add")
    public Integer add() {
        SnowAccessLog snowAccessLog = new SnowAccessLog();
        snowAccessLog.setData("G42 K-97898988发起救援服务！！！");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        snowAccessLog.setTime(simpleDateFormat.format(new Date()));
        snowAccessLog.setTag("测试");
        return snowAccessService.insert(snowAccessLog);
    }
}
