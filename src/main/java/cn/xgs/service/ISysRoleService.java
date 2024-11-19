package cn.xgs.service;


import cn.xgs.domain.TreeSelect;
import cn.xgs.domain.dto.SysRole;

import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 *
 * @author feilong.li
 */
public interface ISysRoleService {

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(SysRole role);

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    public List<SysRole> selectRoleAll();

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    public SysRole selectRoleById(Long roleId);

    /**
     * 通过角色key查询角色
     *
     * @param roleKey 角色key
     * @return 角色对象信息
     */
    public SysRole selectRoleByKey(String roleKey);

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    public boolean checkRoleNameUnique(SysRole role);

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    public boolean checkRoleKeyUnique(SysRole role);

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    public void checkRoleAllowed(SysRole role);

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    public void checkRoleDataScope(Long roleId);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(SysRole role);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    public int deleteRoleByIds(Long[] roleIds);

    /**
     * 根据条件分页查询角色数据keycloak
     *
     * @param sysRole
     * @return
     */
    List<SysRole> selectRoleListForKeycloak(SysRole sysRole);

    /**
     * 修改角色权限
     *
     * @param role
     */
    int updateRoleMenu(SysRole role);

    /**
     * 构建前端所需要树结构
     *
     * @param roles 部门列表
     * @return 树结构列表
     */
    List<SysRole> buildRoleTree(List<SysRole> roles);

    /**
     * 查询角色树结构信息
     * @param role 角色信息
     * @return 角色树信息集合
     */
    public List<TreeSelect> selectRoleTreeList(SysRole role);

    /**
     * 构建前端所需要下拉树结构
     * @param roleList 角色列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildRoleTreeSelect(List<SysRole> roleList);

    /**
     * 根据ID查询所有子角色（正常状态）
     *
     * @param roleId 角色ID
     * @return 子角色数
     */
    int selectNormalChildrenRoleById(Long roleId);

}