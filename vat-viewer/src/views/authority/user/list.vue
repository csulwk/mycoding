<template>
  <div class="app-container">
    <div class="tool-bar">
      <el-form>
        <el-form-item>
          <el-button v-if="hasPerm('YHGL')" size="mini" type="primary" icon="plus" @click="showCreate">
            新增
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      v-loading.body="listLoading"
      :data="list"
      element-loading-text="loading"
      border
      fit
      highlight-current-row
      :default-sort="{prop: 'user.uiUserId', order: 'ascending'}"
    >
      <el-table-column prop="user.uiUserId" align="center" label="ID" width="60">
      </el-table-column>
      <el-table-column align="center" label="用户名称" width="120">
        <template slot-scope="scope">
          {{ scope.row.user.uiUsername }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户描述">
        <template slot-scope="scope">
          {{ scope.row.user.uiUserDesc }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户角色" width="140">
        <template slot-scope="scope">
          <div v-if="scope.row.roles.length > 0">
            <span v-for="role in scope.row.roles" :key="role">{{ role.riRoleDesc }} </span>
          </div>
          <div v-else >无</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.user.uiStatus === '0' ? 'success' : 'danger'" disable-transitions>
            {{ formatterStatus(scope.row.user.uiStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" width="180">
        <template slot-scope="scope">
          {{ formatterTime(scope.row.user.uiCreateTime) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="最近修改时间" width="180">
        <template slot-scope="scope">
          {{ formatterTime(scope.row.user.uiUpdateTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="user.uiVersion" label="版本" width="60">
      </el-table-column>
      <el-table-column v-if="hasPerm('YHGL')" align="center" label="管理" width="160">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="edit" @click="showUpdate(scope.$index)">
            修改
          </el-button>
          <el-button type="danger" icon="delete" size="mini" @click="showDelete(scope.$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="dialogFormVisible"
      @open='getUserRole'
    >
      <el-form class="small-space" :model="tempUser" label-position="right" label-width="80px" style="width: 300px; margin-left:50px;">
        <el-form-item v-if="dialogStatus=='create'" label="用户昵称" required>
          <el-input v-model="tempUser.username" type="text" />
        </el-form-item>
        <el-form-item v-if="dialogStatus=='create'" label="用户密码" required>
          <el-input v-model="tempUser.password" type="password" show-password />
        </el-form-item>
        <el-form-item v-else label="修改密码">
          <el-input v-model="tempUser.password" type="password" placeholder="不填则表示不修改" />
        </el-form-item>
        <el-form-item label="用户性别" required>
          <el-radio v-model="tempUser.sex" label="1">♂</el-radio>
          <el-radio v-model="tempUser.sex" label="2">♀</el-radio>
          <el-radio v-model="tempUser.sex" label="0">?</el-radio>
        </el-form-item>
        <el-form-item label="用户手机" required>
          <el-input v-model="tempUser.mobile" type="text" />
        </el-form-item>
        <el-form-item label="用户邮箱" required>
          <el-input v-model="tempUser.email" type="text" />
        </el-form-item>
        <el-form-item label="用户描述" required>
          <el-input type="textarea" :rows="2" placeholder="请输入描述信息" v-model="tempUser.desc" />
        </el-form-item>
        <el-form-item label="角色名称">
          <el-select v-model="tempUser.roleId" placeholder="请选择" clearable style="display: inline">
            <el-option v-for="item in roles" :key="item.riRoleId" :label="item.riRoleDesc" :value="item.riRoleId" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否生效">
          <el-switch v-model="tempUser.status"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="dialogStatus=='create'" type="success" @click="onSubmitUser">新 增</el-button>
        <el-button type="primary" v-else @click="onUpdateUser">修 改</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getUserRoles, addUser, updateUser, deleteUser } from '@/api/myuser'
import { getAllRole } from '@/api/myrole'
import { parseTime } from '@/utils/index.js'
export default {
  name: 'UserList',
  data() {
    return {
      list: [], // 表格的数据
      listLoading: false, // 数据加载等待动画
      roles: [], // 角色列表
      dialogStatus: 'create',
      dialogFormVisible: false,
      textMap: {
        update: '编辑用户信息',
        create: '新增用户信息'
      },
      tempUser: {
        username: '',
        password: '',
        sex: '',
        mobile: '',
        email: '',
        desc: '',
        status: '',
        roleId: ''
      }
    }
  },
  created() {
    this.fetchUser()
  },
  methods: {
    getUserRole() {
      getAllRole().then(resp => {
        this.roles = resp.data
      }).catch((err) => {
        console.log(err)
      })
    },
    fetchUser() {
      // 查询列表
      this.listLoading = true
      getUserRoles().then(resp => {
        const { data } = resp
        console.log('data: ' + data)
        this.list = data
        this.listLoading = false
      })
    },
    showCreate() {
      // 显示新增对话框
      this.tempUser.username = ''
      this.tempUser.password = ''
      this.tempUser.sex = '1'
      this.tempUser.mobile = ''
      this.tempUser.email = ''
      this.tempUser.desc = ''
      this.tempUser.status = true
      this.tempUser.roleId = 3
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    showUpdate($index) {
      const data = this.list[$index]
      this.tempUser.username = data.user.uiUsername
      this.tempUser.password = ''
      this.tempUser.sex = data.user.uiSex + ''
      this.tempUser.mobile = data.user.uiMobile
      this.tempUser.email = data.user.uiEmail
      this.tempUser.desc = data.user.uiUserDesc
      this.tempUser.status = data.user.uiStatus === '0'
      this.tempUser.roleId = data.roles.length === 0 ? '' : data.roles[0].riRoleId
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    showDelete($index) {
      const data = this.list[$index]
      if (data.user.uiStatus === '1') {
        this.$message({
          showClose: true,
          message: '注意哦，当前用户已处理失效状态！',
          type: 'warning'
        })
        return
      }
      this.$confirm('确定删除此用户?', '提示', {
        confirmButtonText: '确定',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        deleteUser(data.user.uiUsername).then(() => {
          this.fetchUser()
        }).catch(() => {
          this.$message.error('删除失败')
        })
      })
    },
    formatterStatus(val) {
      return val === '0' ? '正常' : '失效'
    },
    formatterTime(val) {
      if (val === null || val.length === 0) {
        return val
      }
      return parseTime(val)
    },
    onSubmitUser() {
      this.tempUser.status = this.tempUser.status === true ? '0' : '1'
      addUser(this.tempUser).then(() => {
        this.dialogFormVisible = false
        this.$message({
          message: '添加成功',
          type: 'success',
          duration: 1 * 1000,
          onClose: () => {
            this.fetchUser()
          }
        })
      }).catch(() => {
        this.$message.error('添加失败')
      })
    },
    onUpdateUser() {
      this.tempUser.status = this.tempUser.status === true ? '0' : '1'
      updateUser(this.tempUser).then(() => {
        this.dialogFormVisible = false
        this.$message({
          message: '修改成功',
          type: 'success',
          duration: 1 * 1000,
          onClose: () => {
            this.fetchUser()
          }
        })
      }).catch(() => {
        this.$message.error('修改失败')
      })
    }
  }
}
</script>
