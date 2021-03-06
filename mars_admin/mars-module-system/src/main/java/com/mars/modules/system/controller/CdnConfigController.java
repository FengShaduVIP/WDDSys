package com.mars.modules.system.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mars.common.api.vo.Result;
import com.mars.common.system.query.QueryGenerator;
import com.mars.common.aspect.annotation.AutoLog;
import com.mars.common.util.oConvertUtils;
import com.mars.modules.system.entity.CdnConfig;
import com.mars.modules.system.service.ICdnConfigService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: cdn配置
 * @Author: jeecg-boot
 * @Date:   2019-07-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="cdn配置")
@RestController
@RequestMapping("/system/cdnConfig")
public class CdnConfigController {
	@Autowired
	private ICdnConfigService cdnConfigService;
	
	/**
	  * 分页列表查询
	 * @param cdnConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "cdn配置-分页列表查询")
	@ApiOperation(value="cdn配置-分页列表查询", notes="cdn配置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CdnConfig>> queryPageList(CdnConfig cdnConfig,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<CdnConfig>> result = new Result<IPage<CdnConfig>>();
		QueryWrapper<CdnConfig> queryWrapper = QueryGenerator.initQueryWrapper(cdnConfig, req.getParameterMap());
		Page<CdnConfig> page = new Page<CdnConfig>(pageNo, pageSize);
		IPage<CdnConfig> pageList = cdnConfigService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param cdnConfig
	 * @return
	 */
	@AutoLog(value = "cdn配置-添加")
	@ApiOperation(value="cdn配置-添加", notes="cdn配置-添加")
	@PostMapping(value = "/add")
	public Result<CdnConfig> add(@RequestBody CdnConfig cdnConfig) {
		Result<CdnConfig> result = new Result<CdnConfig>();
		try {
			cdnConfigService.save(cdnConfig);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param cdnConfig
	 * @return
	 */
	@AutoLog(value = "cdn配置-编辑")
	@ApiOperation(value="cdn配置-编辑", notes="cdn配置-编辑")
	@PutMapping(value = "/edit")
	public Result<CdnConfig> edit(@RequestBody CdnConfig cdnConfig) {
		Result<CdnConfig> result = new Result<CdnConfig>();
		CdnConfig cdnConfigEntity = cdnConfigService.getById(cdnConfig.getId());
		if(cdnConfigEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = cdnConfigService.updateById(cdnConfig);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "cdn配置-通过id删除")
	@ApiOperation(value="cdn配置-通过id删除", notes="cdn配置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			cdnConfigService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "cdn配置-批量删除")
	@ApiOperation(value="cdn配置-批量删除", notes="cdn配置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<CdnConfig> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<CdnConfig> result = new Result<CdnConfig>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.cdnConfigService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "cdn配置-通过id查询")
	@ApiOperation(value="cdn配置-通过id查询", notes="cdn配置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CdnConfig> queryById(@RequestParam(name="id",required=true) String id) {
		Result<CdnConfig> result = new Result<CdnConfig>();
		CdnConfig cdnConfig = cdnConfigService.getById(id);
		if(cdnConfig==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(cdnConfig);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<CdnConfig> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              CdnConfig cdnConfig = JSON.parseObject(deString, CdnConfig.class);
              queryWrapper = QueryGenerator.initQueryWrapper(cdnConfig, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<CdnConfig> pageList = cdnConfigService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "cdn配置列表");
      mv.addObject(NormalExcelConstants.CLASS, CdnConfig.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("cdn配置列表数据", "导出人:Jeecg", "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }

  /**
      * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<CdnConfig> listCdnConfigs = ExcelImportUtil.importExcel(file.getInputStream(), CdnConfig.class, params);
              cdnConfigService.saveBatch(listCdnConfigs);
              return Result.ok("文件导入成功！数据行数:" + listCdnConfigs.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }

}
