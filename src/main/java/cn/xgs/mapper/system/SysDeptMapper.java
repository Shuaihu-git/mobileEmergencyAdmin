package cn.xgs.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.xgs.domain.dto.SysDept;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysDeptMapper extends BaseMapper<SysDept> {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_dept
     *
     * @mbg.generated Wed Sep 18 22:51:36 CST 2024
     */
    int deleteByPrimaryKey(Long deptId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_dept
     *
     * @mbg.generated Wed Sep 18 22:51:36 CST 2024
     */
    int insert(SysDept row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_dept
     *
     * @mbg.generated Wed Sep 18 22:51:36 CST 2024
     */
    SysDept selectByPrimaryKey(Long deptId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_dept
     *
     * @mbg.generated Wed Sep 18 22:51:36 CST 2024
     */
    List<SysDept> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_dept
     *
     * @mbg.generated Wed Sep 18 22:51:36 CST 2024
     */
    int updateByPrimaryKey(SysDept row);
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int hasChildByDeptId(Long deptId);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(Long deptId);

    /**
     * 修改所在部门正常状态
     *
     * @param deptIds 部门ID组
     */
    public void updateDeptStatusNormal(Long[] deptIds);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(Long deptId);

    /**
     * 获取事业部集合
     * @return
     */
    List<SysDept> getDivision();

    /**
     * 获取事业部ID集合
     * @return
     */
    List<Long> getDivisionIdList();

    /**
     * 根据归集日期查询部门
     * @param id
     * @return
     */
    List<SysDept> selectLeaderByDateId(Integer id);

    /**
     * 获取所有的部门集合
     * @return
     */
    List<SysDept> getAllDept();

    /**
     * 查询启用部门
     * @param dept 条件
     * @return 结果
     */
    List<SysDept> selectDeptListOfEnable(SysDept dept);

    /**
     * 获取有效的事业部
     * @return 结果
     */
    List<SysDept> getEffectiveDivision();

    /**
     * 获取部门信息
     */
    @MapKey("deptId")
    List<Map<String, Object>> getDeptInfoWhoCanApply(@Param("deptId") Long deptId);
}