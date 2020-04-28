<template>
  <div class="app-container">
    <el-table :data="tableData" border style="width: 100%" :default-sort="{prop: 'piPermCode', order: 'ascending'}">
      <el-table-column prop="piPermId" label="ID" align="center" width="60"/>
      <el-table-column prop="piPermCode" label="权限代码" align="center" width="160"/>
      <el-table-column prop="piPermDesc" label="权限名称" align="center"/>
      <el-table-column prop="piParentId" label="PID" align="center" width="60"/>
      <el-table-column align="center" label="权限状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.piStatus === '1' ? 'success' : 'danger'" disable-transitions>
            <span v-html="formatterStatus(scope.row.piStatus)"></span>
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" width="180">
        <template slot-scope="scope">
          {{ formatterTime(scope.row.piCreateTime) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="最近修改时间" width="180">
        <template slot-scope="scope">
          {{ formatterTime(scope.row.piUpdateTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="piVersion" align="center" label="版本" width="60"></el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleEdit(scope.$index, scope.row)">修改</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getAllPerm } from '@/api/myperm'
import { parseTime } from '@/utils/index.js'
export default {
  name: 'PermList',
  data() {
    return {
      tableData: [], // 表格的数据
      listLoading: false, // 数据加载等待动画
      roles: [], // 角色列表
      dialogStatus: 'create',
      dialogFormVisible: false,
      textMap: {
        update: '编辑',
        create: '新建用户'
      },
      tempUser: {
        username: '',
        password: '',
        nickname: '',
        roleId: '',
        userId: ''
      }
    }
  },
  created() {
    this.fetchPerm()
  },
  methods: {
    fetchPerm() {
      // 查询列表
      this.listLoading = true
      getAllPerm().then(resp => {
        const { data } = resp
        console.log('data: ' + data)
        this.tableData = data
        this.listLoading = false
      })
    },
    formatterTime(val) {
      if (val === null || val.length === 0) {
        return val
      }
      return parseTime(val)
    },
    formatterStatus(val) {
      return val === '1' ? '正常' : '失效'
    },
    handleEdit(index, row) {
      console.log(index, row)
    },
    handleDelete(index, row) {
      console.log(index, row)
    }
  }
}
</script>
