package com.mars.modules.message.service.impl;

import com.mars.modules.message.entity.SysMessage;
import com.mars.modules.message.service.ISysMessageService;
import com.mars.common.system.base.service.impl.JeecgServiceImpl;
import com.mars.modules.message.mapper.SysMessageMapper;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageServiceImpl extends JeecgServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

}
