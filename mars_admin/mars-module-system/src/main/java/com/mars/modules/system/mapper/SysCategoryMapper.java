package com.mars.modules.system.mapper;

import java.util.List;

import com.mars.modules.system.entity.SysCategory;
import org.apache.ibatis.annotations.Param;
import com.mars.modules.system.model.TreeSelectModel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 分类字典
 * @Author: jeecg-boot
 * @Date:   2019-05-29
 * @Version: V1.0
 */
public interface SysCategoryMapper extends BaseMapper<SysCategory> {
	
	/**
	  *  根据父级ID查询树节点数据
	 * @param pid
	 * @return
	 */
	public List<TreeSelectModel> queryListByPid(@Param("pid")  String pid);
	

}
