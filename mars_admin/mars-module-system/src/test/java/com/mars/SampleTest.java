package com.mars;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.mars.modules.demo.test.service.IJeecgDemoService;
import com.mars.modules.system.service.ISysDataLogService;
import com.mars.modules.demo.mock.MockController;
import com.mars.modules.demo.test.entity.JeecgDemo;
import com.mars.modules.demo.test.mapper.JeecgDemoMapper;
import com.mars.modules.wxapp.service.ICommonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

	@Resource
	private JeecgDemoMapper jeecgDemoMapper;
	@Resource
	private IJeecgDemoService jeecgDemoService;
	@Resource
	private ISysDataLogService sysDataLogService;
	@Resource
	private MockController mock;

	@Resource
	private ICommonService commonService;

	@Test
	public void getDemo(){
		String str = commonService.getQiniuCloudUploadToken("wdd_01");
		System.out.println("------------------------------"+str);
	}

	@Test
	public void testXmlSql() {
		System.out.println(("----- selectAll method test ------"));
		List<JeecgDemo> userList = jeecgDemoMapper.getDemoByName("Sandy12");
		userList.forEach(System.out::println);
	}

	/**
	 * 测试事务
	 */
	@Test
	public void testTran() {
		jeecgDemoService.testTran();
	}
	
	//author:lvdandan-----date：20190315---for:添加数据日志测试----
	/**
	 * 测试数据日志添加
	 */
	@Test
	public void testDataLogSave() {
		System.out.println(("----- datalog test ------"));
		String tableName = "jeecg_demo";
		String dataId = "4028ef81550c1a7901550c1cd6e70001";
		String dataContent = mock.sysDataLogJson();
		sysDataLogService.addDataLog(tableName, dataId, dataContent);
	}
	//author:lvdandan-----date：20190315---for:添加数据日志测试----
}
