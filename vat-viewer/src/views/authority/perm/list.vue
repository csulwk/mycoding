<template>
  <div class="app-container">
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      :default-sort="{prop: 'piPermId', order: 'ascending'}"
    >
      <el-table-column
        prop="piPermId"
        label="ID"
        sortable
        align="center"
        width="180"
      />
      <el-table-column
        prop="piPermCode"
        label="权限代码"
        sortable
        align="center"
        width="180"
      />
      <el-table-column
        prop="piPermDesc"
        label="权限名称"
        align="center"
        width="180"
      />
      <el-table-column
              align="center"
              label="权限状态"
              width="180"
      >
        <template slot-scope="scope">
          <el-tag
                  :type="scope.row.piStatus === '0' ? 'success' : 'danger'"
                  disable-transitions
          >
            <span v-html="formatter(scope.row.piStatus)"></span>
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="left"
      >
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            @click="handleEdit(scope.$index, scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getAllPerm } from '@/api/myperm'
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
    handleEdit(index, row) {
      console.log(index, row)
    },
    handleDelete(index, row) {
      console.log(index, row)
    },
    formatter(val) {
      return val === '0' ? '正常' : '失效'
    }
  }
}
</script>
