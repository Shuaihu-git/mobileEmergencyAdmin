package cn.xgs.controller.system;

import cn.xgs.constants.Constants;
import cn.xgs.domain.LoginBody;
import cn.xgs.domain.response.Result;
import cn.xgs.service.impl.SysLoginService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static cn.xgs.domain.response.Result.success;

@RestController
public class AuthController {
    @Resource(name = "sysLoginService")
    private SysLoginService loginService;
    @PostMapping("/auth")
    public Result auth(@RequestBody LoginBody loginBody){

        Map<String, Object> map = new HashMap<>();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid(), loginBody.getBackendPublicKey());
        map.put(Constants.TOKEN, token);
        return success(map);
    }

}
