package com.mars.modules.demo.test.mapper;

import java.util.List;

import com.mars.modules.demo.test.entity.JeecgDemo;
import org.springframework.data.repository.query.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface JeecgDemoMapper extends BaseMapper<JeecgDemo> {

	List<JeecgDemo> getDemoByName(@Param("name") String name);

}
