package cn.xgs.domain.security;

import cn.xgs.domain.LoginUser;
import cn.xgs.domain.dto.SysUser;
import cn.xgs.service.ISysUserService;
import cn.xgs.service.impl.SysPasswordService;
import cn.xgs.utils.permission.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户验证处理
 *
 * @author Xiaohu
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService sysPasswordService;

    @Autowired
    private SysPermissionService permissionService;
//
//    @Autowired
//    private SysUserRoleMapper sysUserRoleMapper;
//
//    @Autowired
//    private ISysDeptService sysDeptService;
//
//    @Autowired
//    private ISysRoleService sysRoleService;
//
//    @Autowired
//    private SysRoleMapper roleMapper;

    protected static final int SECOND_MINUTE = 60;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        //2.1如果为白名单走本服务数据库
        sysPasswordService.validate(user);
        UserDetails userDetails = whiteCreateLoginUser(user);
        return userDetails;

    }

    public UserDetails whiteCreateLoginUser(SysUser user) {
        return new LoginUser(user.getUserId() + "", null,user, permissionService.getMenuPermission(user));
    }


    /**
     * 得到部门
     *
     * @param depts 部门
     * @return {@link Long}
     */
//    private Long getDept(List<String> depts) {
//        List<SysDept> sysDepts = new ArrayList<>();
//        for (String dept : depts) {
//            String[] split = dept.split("/");
//            SysDept sysDept = new SysDept();
//            sysDept.setDeptName(split[split.length - 1]);
//            sysDepts = sysDeptService.selectDeptListForKeycloak(sysDept);
//            break;
//        }
//        if (sysDepts.isEmpty()) {
//            return UserConstants.NEW_USER_DEPARTMENT_ID;
//        }
//        return sysDepts.get(0).getDeptId();
//    }

}
