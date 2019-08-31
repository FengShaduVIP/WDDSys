package com.mars.modules.demo.test.service;

import com.mars.common.system.base.service.JeecgService;
import com.mars.modules.demo.test.entity.JeecgDemo;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface IJeecgDemoService extends JeecgService<JeecgDemo> {
	
	public void testTran();
	
	public JeecgDemo getByIdCacheable(String id);
}
