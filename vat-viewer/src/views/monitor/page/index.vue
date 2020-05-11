<template>
  <div class="app-container">
    <!-- 搜索选项 -->
    <div style="margin-bottom: 10px">
      <el-form ref="queryForm" :inline="true" :model="queryParam" class="demo-form-inline">
        <el-form-item prop="queryDate" style="margin-bottom: 0;">
          <el-date-picker
            v-model="queryParam.queryDate"
            type="date"
            placeholder="请选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item prop="queryName" style="margin-bottom: 0;">
          <el-input v-model="queryParam.queryName" uffix-icon="el-icon-search" style="width: 160px;" placeholder="请输入角色编号" clearable>
            <i slot="prefix" class="el-input__icon el-icon-user" />
          </el-input>
        </el-form-item>
        <el-button type="primary" plain class="search-btn el-button--infoSearch" uffix-icon="el-icon-search" @click="doFilter">查询</el-button>
        <el-button type="danger" plain class="search-btn el-button--infoSearch" uffix-icon="el-icon-delete" @click="resetForm('queryForm')">重置</el-button>

        <el-tag v-if="false">{{ roleId }}</el-tag></el-form>
    </div>

    <!-- 表格数据 -->
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="rptId" label="rptId" width="120" />
      <el-table-column prop="roleId" label="roleId" width="120" />
      <el-table-column prop="roleCode" label="roleCode" width="180" />
      <el-table-column prop="roleDesc" label="roleDesc" width="200" />
      <el-table-column prop="permId" label="permId" width="120" />
      <el-table-column prop="permCode" label="permCode" width="180" />
      <el-table-column prop="permDesc" label="permDesc" />
    </el-table>
    <!-- 分页展示 -->
    <el-pagination
      :current-page="currentPage"
      :page-sizes="[5, 10, 30]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalItems"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { getRolePage } from '@/api/myrole'
import { parseTime } from '@/utils/index.js'
export default {
  data() {
    return {
      queryParam: {
        queryDate: '',
        queryName: ''
      },
      roleId: '',
      tableData: [],
      currentPage: 1, // 默认在第1页
      pageSize: 10, // 默认每页显示5条数据
      totalItems: 1, // 默认总条数为1条
      loading: false
    }
  },
  created() {
    console.log('页面加载时初始化数据...')
    const d = new Date()
    this.queryParam.queryDate = parseTime(d, '{y}-{m}-{d}')
    console.log('当前日期：' + this.queryParam.queryDate)
  },
  methods: {
    initData() {
      const page = {
        pageNum: this.currentPage,
        pageSize: this.pageSize,
        roleId: this.roleId
      }
      console.log('page -> ' + JSON.stringify(page))
      getRolePage(page).then(resp => {
        this.tableData = resp.data.items
        this.totalItems = resp.data.totalSize
        console.log('size -> ' + JSON.stringify(this.totalItems))
      }).catch((err) => {
        console.log(err)
      })
    },
    doFilter() {
      if (this.queryParam.queryDate === null || this.queryParam.queryDate === 0) {
        this.$message.warning('查询日期不能为空，请检查输入数据！')
        return
      }
      // 每次查询前先初始化数据
      this.roleId = this.queryParam.queryName
      this.tableData = []
      this.totalItems = 1
      this.pageSize = 10
      this.currentPage = 1
      this.initData()
    },
    handleSizeChange(val) {
      console.log(`切换分页，每页 ${val} 条`)
      this.pageSize = val
      this.handleCurrentChange(1)
    },
    handleCurrentChange(val) {
      console.log(`切换页面，当前页: ${val}`)
      this.currentPage = val
      this.initData()
    },
    resetForm(formName) {
      console.log('重置表单...')
      this.$refs[formName].resetFields()
    }
  }
}
</script>
