package cn.xgs.config;

import cn.xgs.auth.AuthenticationEntryPointImpl;
import cn.xgs.auth.LogoutSuccessHandlerImpl;
import cn.xgs.config.filter.JwtAuthenticationTokenFilter;
import cn.xgs.config.filter.PermitAllUrlProperties;
import cn.xgs.config.filter.UserDetailsAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Objects;

/**
 * spring security配置
 * @author XiaoHu
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * 允许匿名访问的地址
     */
    @Autowired
    private PermitAllUrlProperties permitAllUrl;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Autowired
    private UserDetailsAuthenticationProvider userDetailsAuthenticationProvider;

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return authenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, @Autowired(required = false) UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource) throws Exception {
        // 注解标记允许匿名访问的url
        httpSecurity.authorizeHttpRequests(registry -> permitAllUrl.getUrls().forEach(url -> registry.requestMatchers(url).permitAll()));
        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf(CsrfConfigurer::disable)
                .headers(headersConfigurer -> headersConfigurer.cacheControl(HeadersConfigurer.CacheControlConfig::disable))
                // 认证失败处理类
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedHandler))
                // 基于token，所以不需要session
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //允许通过
                .authorizeHttpRequests(registry -> registry.requestMatchers( "/auth/**",
                                "/api/public-key","/websocket/**",
                                "/health").permitAll()
                        // 静态资源，可匿名访问
                        .requestMatchers(HttpMethod.GET, "/", "/*.html", "/*/*.html", "/*/*.css", "/*/*.js", "/profile/**", "/appfiles/**").permitAll()
                        .requestMatchers("/static/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                        //其它均要认证
                        .anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .authenticationProvider(userDetailsAuthenticationProvider)
                .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        // 添加Logout filter
        httpSecurity.logout(logoutConfigurer -> logoutConfigurer.logoutUrl("/api/logout").logoutSuccessHandler(logoutSuccessHandler));
        // 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        if (Objects.nonNull(urlBasedCorsConfigurationSource)) {
            httpSecurity.cors(corsFilter -> corsFilter.configurationSource(urlBasedCorsConfigurationSource));
        }
        return httpSecurity.build();
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}