package cn.xgs.config.filter;

import cn.xgs.domain.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 额外身份验证检查
     *
     * @param userDetails    用户详细信息
     * @param authentication 身份验证
     * @throws AuthenticationException 身份验证异常
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    	//进行秘密检查的方法，keycloak密码接管了，不需要
        logger.info("认证");
    }

    /**
     * 检索用户
     *
     * @param username       用户名
     * @param authentication 身份验证
     * @return {@link UserDetails}
     * @throws AuthenticationException 身份验证异常
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails loadedUser = userDetailsService.loadUserByUsername(username);

        if (!Optional.ofNullable(loadedUser).isPresent()){
            throw new InternalAuthenticationServiceException("没有该用户！");
        }else {
            return loadedUser;
        }
    }
}