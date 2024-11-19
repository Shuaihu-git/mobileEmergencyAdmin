package cn.xgs.controller;

import cn.xgs.domain.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "服务心跳检测",description = "服务心跳检测" )
public class HealthCheckController {
    @RequestMapping("/health")
    @Operation(summary = "服务心跳检测",description = "用于后端服务心跳检测")
    public Result healthCheck(){
        return Result.success(String.valueOf(HttpStatus.OK.value()),"success",null);
    }
    @RequestMapping("/")
    public Result root(){
        return Result.error(String.valueOf(HttpStatus.UNAUTHORIZED.value()),"请使用前端地址访问本服务！！");
    }


}
