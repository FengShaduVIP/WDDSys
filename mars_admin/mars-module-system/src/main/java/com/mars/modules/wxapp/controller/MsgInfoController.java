package com.mars.modules.wxapp.controller;

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
import com.mars.modules.wxapp.entity.MsgInfo;
import com.mars.modules.wxapp.service.IMsgInfoService;
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
 * @Description: 微信小程序发送消息记录
 * @Author: jeecg-boot
 * @Date:   2019-09-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="微信小程序发送消息记录")
@RestController
@RequestMapping("/wxapp/msgInfo")
public class MsgInfoController {
	@Autowired
	private IMsgInfoService msgInfoService;
	
	/**
	  * 分页列表查询
	 * @param msgInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "微信小程序发送消息记录-分页列表查询")
	@ApiOperation(value="微信小程序发送消息记录-分页列表查询", notes="微信小程序发送消息记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<MsgInfo>> queryPageList(MsgInfo msgInfo,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<MsgInfo>> result = new Result<IPage<MsgInfo>>();
		QueryWrapper<MsgInfo> queryWrapper = QueryGenerator.initQueryWrapper(msgInfo, req.getParameterMap());
		Page<MsgInfo> page = new Page<MsgInfo>(pageNo, pageSize);
		IPage<MsgInfo> pageList = msgInfoService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param msgInfo
	 * @return
	 */
	@AutoLog(value = "微信小程序发送消息记录-添加")
	@ApiOperation(value="微信小程序发送消息记录-添加", notes="微信小程序发送消息记录-添加")
	@PostMapping(value = "/add")
	public Result<MsgInfo> add(@RequestBody MsgInfo msgInfo) {
		Result<MsgInfo> result = new Result<MsgInfo>();
		try {
			msgInfoService.save(msgInfo);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param msgInfo
	 * @return
	 */
	@AutoLog(value = "微信小程序发送消息记录-编辑")
	@ApiOperation(value="微信小程序发送消息记录-编辑", notes="微信小程序发送消息记录-编辑")
	@PutMapping(value = "/edit")
	public Result<MsgInfo> edit(@RequestBody MsgInfo msgInfo) {
		Result<MsgInfo> result = new Result<MsgInfo>();
		MsgInfo msgInfoEntity = msgInfoService.getById(msgInfo.getId());
		if(msgInfoEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = msgInfoService.updateById(msgInfo);
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
	@AutoLog(value = "微信小程序发送消息记录-通过id删除")
	@ApiOperation(value="微信小程序发送消息记录-通过id删除", notes="微信小程序发送消息记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			msgInfoService.removeById(id);
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
	@AutoLog(value = "微信小程序发送消息记录-批量删除")
	@ApiOperation(value="微信小程序发送消息记录-批量删除", notes="微信小程序发送消息记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<MsgInfo> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<MsgInfo> result = new Result<MsgInfo>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.msgInfoService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "微信小程序发送消息记录-通过id查询")
	@ApiOperation(value="微信小程序发送消息记录-通过id查询", notes="微信小程序发送消息记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<MsgInfo> queryById(@RequestParam(name="id",required=true) String id) {
		Result<MsgInfo> result = new Result<MsgInfo>();
		MsgInfo msgInfo = msgInfoService.getById(id);
		if(msgInfo==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(msgInfo);
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
      QueryWrapper<MsgInfo> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              MsgInfo msgInfo = JSON.parseObject(deString, MsgInfo.class);
              queryWrapper = QueryGenerator.initQueryWrapper(msgInfo, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<MsgInfo> pageList = msgInfoService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "微信小程序发送消息记录列表");
      mv.addObject(NormalExcelConstants.CLASS, MsgInfo.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("微信小程序发送消息记录列表数据", "导出人:Jeecg", "导出信息"));
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
              List<MsgInfo> listMsgInfos = ExcelImportUtil.importExcel(file.getInputStream(), MsgInfo.class, params);
              msgInfoService.saveBatch(listMsgInfos);
              return Result.ok("文件导入成功！数据行数:" + listMsgInfos.size());
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
