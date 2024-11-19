package cn.xgs.service.impl;


import cn.hutool.core.convert.Convert;
import cn.xgs.constants.UserConstants;
import cn.xgs.domain.TreeSelect;
import cn.xgs.domain.dto.SysRole;
import cn.xgs.domain.dto.SysRoleMenu;
import cn.xgs.domain.dto.SysUser;
import cn.xgs.mapper.system.SysRoleMapper;
import cn.xgs.mapper.system.SysRoleMenuMapper;
import cn.xgs.mapper.system.SysUserRoleMapper;
import cn.xgs.service.ISysRoleService;
import cn.xgs.utils.SecurityUtils;
import cn.xgs.utils.SpringUtils;
import cn.xgs.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.xgs.exceptions.enums.ExceptionEnum.exception;
import static cn.xgs.utils.SecurityUtils.getLoginUser;


/**
 * 角色 业务层处理
 *
 * @author XiaoHu
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;


    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role) {
        // 新增角色信息
        if (role.getParentId() != 0) {
            SysRole info = roleMapper.selectRoleById(role.getParentId());
            // 如果父节点不为正常状态,则不允许新增子节点
            if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
                exception("父角色处于停用状态,不能创建子角色!");
            }
            role.setAncestors(info.getAncestors() + "," + role.getParentId());
        } else {
            role.setAncestors("0");
        }
        int i = roleMapper.insertRole(role);

        return i;
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role) {

        SysRole newParentRole = roleMapper.selectRoleById(role.getParentId());
        SysRole oldRole = roleMapper.selectRoleById(role.getRoleId());
        if (StringUtil.isNotNull(oldRole)) {
            String newAncestors;
            if (StringUtil.isNotNull(newParentRole)) {
                newAncestors = newParentRole.getAncestors() + "," + newParentRole.getRoleId();
            }else {
                newAncestors = "0";
            }
            String oldAncestors = oldRole.getAncestors();
            role.setAncestors(newAncestors);
            updateRoleChildren(role.getRoleId(), newAncestors, oldAncestors);
        }
        // 修改角色信息
        int result = roleMapper.updateRole(role);

        if (UserConstants.DEPT_NORMAL.equals(role.getStatus()) && StringUtil.isNotEmpty(role.getAncestors())
                && !StringUtil.equals("0", role.getAncestors())) {
            // 如果该角色是启用状态，则启用该角色的所有上级角色
            updateParentRoleStatusNormal(role);
        }
        return result;
    }

    /**
     * 修改该角色的父级角色状态
     *
     * @param role 当前角色
     */
    private void updateParentRoleStatusNormal(SysRole role) {
        String ancestors = role.getAncestors();
        Long[] roleIds = Convert.toLongArray(ancestors);
        roleMapper.updateRoleStatusNormal(roleIds);
    }

    /**
     * 修改子元素关系
     *
     * @param roleId       被修改的角色ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateRoleChildren(Long roleId, String newAncestors, String oldAncestors) {
        List<SysRole> children = roleMapper.selectChildrenRoleById(roleId);
        for (SysRole child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (!children.isEmpty()) {
            roleMapper.updateRoleChildren(children);
        }
    }

    /**
     * 修改角色权限
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleMenu(SysRole role) {
        //判断是否操作的本角色
        SysUser user = getLoginUser().getUser();

        if (!SysUser.isAdmin(user.getUserId())){
            List<SysRole> roles = user.getRoles();

            for (SysRole sysRole : roles) {
                if (role.getRoleId().equals(sysRole.getRoleId())){
                    exception("不能操作本角色的权限");
                }
            }
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role);
        return insertRoleMenu(role);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param roleList 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysRole> buildRoleTree(List<SysRole> roleList) {
        List<SysRole> returnList = new ArrayList<SysRole>();
        List<Long> tempList = roleList.stream().map(SysRole::getRoleId).toList();
        for (SysRole role : roleList) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(role.getParentId())) {
                recursionFn(roleList, role);
                returnList.add(role);
            }
        }
        if (returnList.isEmpty()) {
            returnList = roleList;
        }
        return returnList;
    }

    /**
     * 查询角色树结构信息
     *
     * @param role 角色信息
     * @return 角色树信息集合
     */
    @Override
    public List<TreeSelect> selectRoleTreeList(SysRole role) {
        List<SysRole> roleList = roleMapper.selectRoleListOfEnable(role);
        return buildRoleTreeSelect(roleList);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param roleList 角色列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildRoleTreeSelect(List<SysRole> roleList) {
        List<SysRole> roleTrees = buildRoleTree(roleList);
        return roleTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据ID查询所有子角色（正常状态）
     *
     * @param roleId 角色ID
     * @return 子角色数
     */
    @Override
    public int selectNormalChildrenRoleById(Long roleId) {
        return roleMapper.selectNormalChildrenRoleById(roleId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysRole> list, SysRole r) {
        // 得到子节点列表
        List<SysRole> childList = getChildList(list, r);
        r.setChildren(childList);
        for (SysRole rChild : childList) {
            if (hasChild(list, rChild)) {
                recursionFn(list, rChild);
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysRole> list, SysRole r) {
        return !getChildList(list, r).isEmpty();
    }

    /**
     * 得到子节点列表
     */
    private List<SysRole> getChildList(List<SysRole> list, SysRole r) {
        List<SysRole> tlist = new ArrayList<SysRole>();
        for (SysRole n : list) {
            if (StringUtil.isNotNull(n.getParentId()) && n.getParentId().longValue() == r.getRoleId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            rm.setType(role.getType());
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }


    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SysRole> selectRoleList(SysRole role) {
//        SysUser user = getLoginUser().getUser();
//
//        List<SysRole> roles = user.getRoles();
//
//        List<String> list = new ArrayList<>();
//        for (SysRole sysRole : roles) {
//            list.addAll(roleMapper.selectRoleIdsByParentId(sysRole.getRoleId()));
//        }
//
//        role.setRoleIds(list);
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根据条件分页查询角色数据keycloak
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SysRole> selectRoleListForKeycloak(SysRole role) {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtil.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return selectRoleList(new SysRole());
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleKey 角色key
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleByKey(String roleKey) {
        return roleMapper.selectRoleByRoleKey(roleKey);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtil.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtil.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtil.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtil.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtil.isNotNull(role.getRoleId()) && role.isAdmin()) {
            exception("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    @Override
    public void checkRoleDataScope(Long roleId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysRole role = new SysRole();
            role.setRoleId(roleId);
            List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
            if (StringUtil.isEmpty(roles)) {
                exception("没有权限访问角色数据!");
            }
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        List<String> list = new ArrayList<>();
        for (Long roleId : roleIds) {
            int result = roleMapper.hasChildByRoleId(roleId);
            if (result > 0) {
                exception("存在子角色,不不允许删除");
            }
            checkRoleAllowed(new SysRole(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            list.add(role.getRoleKey());
            if (countUserRoleByRoleId(roleId) > 0) {
                exception(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenu(roleIds);
        int row = roleMapper.deleteRoleByIds(roleIds);

//        //keycloak删除角色
//        list.stream().forEach(roleKey -> {
//            try {
//                AuthorityClient.deleteRole(roleKey);
//            } catch (IOException e) {
//                exception("keycloak删除角色失败");
//            }
//        });
        return row;
    }

}
