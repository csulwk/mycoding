<template>
  <div class="app-container">
    <div class="tool-bar">
      <el-form>
        <el-form-item>
          <el-button v-if="hasPerm('YHGL')" size="mini" type="primary" icon="plus" @click="showCreate">
            添加用户
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
    >
      <el-table-column align="center" label="用户名称" >
        <template slot-scope="scope">
          {{ scope.row.uiUsername }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="联系方式" >
        <template slot-scope="scope">
          {{ scope.row.uiMobile }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="电子邮箱" >
        <template slot-scope="scope">
          {{ scope.row.uiEmail }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="用户状态"
        width="180"
      >
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.uiStatus === '0' ? 'success' : 'danger'"
            disable-transitions
          >
            <span v-html="formatter(scope.row.uiStatus)"></span>
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="hasPerm('YHGL')" align="center" label="管理" width="240">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button
            type="danger"
            icon="delete"
            size="mini"
            @click="removeUser(scope.$index)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="dialogFormVisible"
      @open='getUserRole'
    >
      <el-form
        class="small-space"
        :model="tempUser"
        label-position="left"
        label-width="80px"
        style="width: 300px; margin-left:50px;"
      >
        <el-form-item v-if="dialogStatus=='create'" label="用户昵称" required>
          <el-input v-model="tempUser.uiUsername" type="text" />
        </el-form-item>
        <el-form-item v-if="dialogStatus=='create'" label="用户密码" required>
          <el-input v-model="tempUser.uiPassword" type="password" show-password />
        </el-form-item>
        <el-form-item v-else label="修改密码">
          <el-input v-model="tempUser.uiPassword" type="password" placeholder="不填则表示不修改" />
        </el-form-item>
        <el-form-item label="用户性别" required>
          <el-radio v-model="tempUser.uiSex" label="1">♂</el-radio>
          <el-radio v-model="tempUser.uiSex" label="2">♀</el-radio>
          <el-radio v-model="tempUser.uiSex" label="0">?</el-radio>
        </el-form-item>
        <el-form-item label="用户手机" required>
          <el-input v-model="tempUser.uiMobile" type="text" />
        </el-form-item>
        <el-form-item label="用户邮箱" required>
          <el-input v-model="tempUser.uiEmail" type="text" />
        </el-form-item>
        <el-form-item label="用户描述" required>
          <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入描述信息"
                  v-model="tempUser.uiUserDesc">
          </el-input>
        </el-form-item>
        <el-form-item label="角色名称">
          <el-select v-model="tempUser.roleId" placeholder="请选择" clearable style="display: inline">
            <el-option
                    v-for="item in roles"
                    :key="item.riRoleId"
                    :label="item.riRoleDesc"
                    :value="item.riRoleId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="onSubmitUser">添 加</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getAllUser } from '@/api/myuser'
import { getAllRole } from '@/api/myrole'
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
        update: '编辑',
        create: '新建用户'
      },
      tempUser: {
        uiUsername: '',
        uiPassword: '',
        uiUserDesc: '',
        uiSex: '0',
        uiMobile: '',
        uiEmail: '',
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
      getAllUser().then(resp => {
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
      this.tempUser.nickname = ''
      this.tempUser.roleId = ''
      this.tempUser.userId = ''
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    showUpdate($index) {
      const user = this.list[$index]
      this.tempUser.username = user.username
      this.tempUser.nickname = user.nickname
      this.tempUser.roleId = user.roleId
      this.tempUser.userId = user.userId
      this.tempUser.deleteStatus = '1'
      this.tempUser.password = ''
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    formatter(val) {
      return val === '0' ? '正常' : '失效'
    },
    onSubmitUser() {
      this.$message(this.tempUser)
    }
  }
}
</script>
<style>
  .el-form--label-left .el-form-item__label {
    text-align: right;
  }
</style>
