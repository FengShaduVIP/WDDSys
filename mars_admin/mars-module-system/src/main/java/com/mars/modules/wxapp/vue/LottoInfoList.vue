<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="活动编号">
              <a-input placeholder="请输入活动编号" v-model="queryParam.lottoNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="活动封面图片地址">
              <a-input placeholder="请输入活动封面图片地址" v-model="queryParam.banner"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="活动类型">
              <a-input placeholder="请输入活动类型" v-model="queryParam.lottoType"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="赞助名称">
              <a-input placeholder="请输入赞助名称" v-model="queryParam.nickName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="微信号">
              <a-input placeholder="请输入微信号" v-model="queryParam.wxNo"></a-input>
            </a-form-item>
          </a-col>
          </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('微信小程序活动表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <lottoInfo-modal ref="modalForm" @ok="modalFormOk"></lottoInfo-modal>
  </a-card>
</template>

<script>
  import LottoInfoModal from './modules/LottoInfoModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "LottoInfoList",
    mixins:[JeecgListMixin],
    components: {
      LottoInfoModal
    },
    data () {
      return {
        description: '微信小程序活动表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '活动编号',
            align:"center",
            dataIndex: 'lottoNo'
           },
		   {
            title: '活动封面图片地址',
            align:"center",
            dataIndex: 'banner'
           },
		   {
            title: '活动类型',
            align:"center",
            dataIndex: 'lottoType'
           },
		   {
            title: '赞助名称',
            align:"center",
            dataIndex: 'nickName'
           },
		   {
            title: '微信号',
            align:"center",
            dataIndex: 'wxNo'
           },
		   {
            title: '微信名称',
            align:"center",
            dataIndex: 'wxName'
           },
		   {
            title: '开奖类型 0 自动 1人数 2 手动',
            align:"center",
            dataIndex: 'openType'
           },
		   {
            title: '是否置顶0否1 是',
            align:"center",
            dataIndex: 'isTop'
           },
		   {
            title: '是否首页查询',
            align:"center",
            dataIndex: 'isShow'
           },
		   {
            title: '分享次数',
            align:"center",
            dataIndex: 'shareNum'
           },
		   {
            title: '参与人数',
            align:"center",
            dataIndex: 'joinNum'
           },
		   {
            title: '浏览量',
            align:"center",
            dataIndex: 'viewNum'
           },
		   {
            title: '手机号',
            align:"center",
            dataIndex: 'tel'
           },
		   {
            title: '是否内定 0 否',
            align:"center",
            dataIndex: 'isGod'
           },
		   {
            title: '开奖人数',
            align:"center",
            dataIndex: 'openNum'
           },
		   {
            title: '是否加入机器人 0 否',
            align:"center",
            dataIndex: 'isJoinRobot'
           },
		   {
            title: '是否担保0 否',
            align:"center",
            dataIndex: 'isConfirm'
           },
		   {
            title: '活动结束时间',
            align:"center",
            dataIndex: 'endDate'
           },
		   {
            title: '活动开始时间',
            align:"center",
            dataIndex: 'startDate'
           },
		   {
            title: '开奖时间',
            align:"center",
            dataIndex: 'openTime'
           },
		   {
            title: '状态 0 删除 1 正常 2 违规',
            align:"center",
            dataIndex: 'status'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/wxapp/lottoInfo/list",
          delete: "/wxapp/lottoInfo/delete",
          deleteBatch: "/wxapp/lottoInfo/deleteBatch",
          exportXlsUrl: "wxapp/lottoInfo/exportXls",
          importExcelUrl: "wxapp/lottoInfo/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>