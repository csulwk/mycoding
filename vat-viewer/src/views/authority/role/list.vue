<template>
  <div class="app-container">
    <div class="tool-bar">
      <el-form>
        <el-form-item>
          <el-button v-if="hasPerm('YHGL')" size="mini" type="primary" icon="plus" @click="showCreate">新增</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" border style="width: 100%" :default-sort="{prop: 'piPermId', order: 'ascending'}">
      <el-table-column prop="riRoleId" label="ID" align="center" width="60" />
      <el-table-column prop="riRoleCode" label="角色代码" align="center" width="140" />
      <el-table-column prop="riRoleDesc" label="角色名称" align="center" />
      <el-table-column align="center" label="角色状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.riStatus === '0' ? 'success' : 'danger'" disable-transitions>
            <span v-html="formatterStatus(scope.row.riStatus)" />
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" width="180">
        <template slot-scope="scope">
          {{ formatterTime(scope.row.riCreateTime) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="最近修改时间" width="180">
        <template slot-scope="scope">
          {{ formatterTime(scope.row.riUpdateTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="riVersion" align="center" label="版本" width="60" />
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-s-unfold" round @click="showPermDraw(scope.$index, scope.row)" />
          <el-button type="warning" size="mini" icon="el-icon-edit-outline" round @click="showUpdate(scope.$index, scope.row)" />
          <el-popconfirm title="删除须谨慎，请确认？" style="margin-left: 10px;" @onConfirm="showDelete(scope.$index, scope.row)">
            <el-button slot="reference" type="danger" size="mini" icon="el-icon-delete" round />
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-drawer title="查看当前角色拥有的权限" :visible.sync="drawer" :with-header="false">
      <div style="margin: 10px 15px;">
        <el-form :model="tempRole" label-position="top" label-width="80px">
          <el-form-item label="角色描述">
            <el-input v-model="tempRole.desc" :disabled="true" />
          </el-form-item>
          <el-form-item label="角色权限">
            <div v-for="(item,index) in rolePerms" :key="index">
              <template v-if="item.children.length > 0">
                <el-divider content-position="left">{{ item.permDesc }}</el-divider>
                <el-tag v-for="itemChild in item.children" :key="itemChild.permId" type="primary" v-text="itemChild.permDesc"  style="width: 120px;margin-left: 6px;text-align: center;" />
              </template>
            </div>
          </el-form-item>
          <el-form-item label="归属用户">
            <el-tag v-for="item in roleUsers" :key="item.menuName" effect="plain" v-text="item.menuName" />
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @open="getRolePerm">
      <el-form :model="tempRole" label-position="right" label-width="80px">
        <el-form-item label="角色代码" required>
          <el-input v-model="tempRole.code" type="text" />
        </el-form-item>
        <el-form-item label="角色描述" required>
          <el-input v-model="tempRole.desc" type="text" />
        </el-form-item>
        <el-form-item label="角色管理" size="medium" required>
          <div class="permission">
            <div class="permissionContent">
              <div v-for="(item,index) in allPerms" :key="index">
                <template v-if="item.children.length > 0">
                  <div class="permissionTitle">{{ item.permDesc }}</div>
                  <el-checkbox-group v-model="tempRole.perms">
                    <el-checkbox v-for="itemChild in item.children" :key="itemChild.permId" :label="itemChild.permId" style="width: 100px;margin-left: 6px;">
                      {{ itemChild.permDesc }}
                    </el-checkbox>
                  </el-checkbox-group>
                </template>
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="是否生效">
          <el-switch v-model="tempRole.status" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus === 'create'" type="success" @click="onSubmitRole">新 增</el-button>
        <el-button v-else type="primary" @click="onUpdateRole">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAllRole, getRolePermList } from '@/api/myrole'
import { getAllPermList } from '@/api/myperm'
import { parseTime } from '@/utils/index.js'
export default {
  name: 'RoleList',
  data() {
    return {
      drawer: false,
      list: [], // 表格的数据
      listLoading: false, // 数据加载等待动画
      roles: [], // 角色列表
      dialogStatus: 'create',
      dialogFormVisible: false,
      textMap: {
        update: '编辑角色信息',
        create: '新增角色信息'
      },
      tempRole: {
        code: '',
        desc: '',
        roleId: '',
        status: '',
        perms: [13, 10]
      },
      allPerms: [],
      rolePerms: [],
      roleUsers: []
    }
  },
  created() {
    this.fetchRole()
  },
  methods: {
    getRolePerm() {
    },
    fetchRole() {
      // 查询列表
      this.listLoading = true
      getAllRole().then(resp => {
        const { data } = resp
        console.log('data: ' + JSON.stringify(data))
        this.list = data
        this.listLoading = false
      })
    },
    showCreate() {
      // 显示新增对话框
      this.tempRole.code = ''
      this.tempRole.desc = ''
      this.tempRole.roleId = ''
      this.tempRole.status = true
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    showUpdate(index, row) {
      console.log('index:' + index)
      console.log('row:' + JSON.stringify(row))
      getAllPermList().then(resp => {
        console.log('resp:' + JSON.stringify(resp.data))
        this.allPerms = resp.data
      }).catch((err) => {
        console.log(err)
      })
      this.tempRole.code = row.riRoleCode
      this.tempRole.desc = row.riRoleDesc
      this.tempRole.roleId = row.riRoleId
      this.tempRole.status = row.riStatus === '0'
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    showPermDraw(index, row) {
      this.drawer = true
      console.log('row:' + JSON.stringify(row))
      getRolePermList(row.riRoleId).then(resp => {
        console.log('resp:' + JSON.stringify(resp.data))
        this.rolePerms = resp.data
      }).catch((err) => {
        console.log(err)
      })
      console.log('index:' + index)
      console.log('row:' + JSON.stringify(row))
      this.tempRole.desc = row.riRoleDesc
    },
    showDelete(index, row) {
      console.log('index:' + index)
      console.log('row:' + JSON.stringify(row))
      this.$message({
        showClose: true,
        message: '注意哦，要删除了！',
        type: 'warning'
      })
    },
    formatterTime(val) {
      if (val === null || val.length === 0) {
        return val
      }
      return parseTime(val)
    },
    formatterStatus(val) {
      return val === '0' ? '正常' : '失效'
    },
    onSubmitRole() {
      console.log('tempRole:' + JSON.stringify(this.tempRole))
    },
    onUpdateRole() {
      console.log('tempRole:' + JSON.stringify(this.tempRole))
    }
  }
}
</script>

<style lang="scss">
  .permission{
    // border: 1px solid #ebebeb;
    border-radius: 3px;
    .permissionContent {
      border: 1px solid #f5f5f5;
      border-radius: 2px;
    };
    .permissionTitle {
      font-size: 13px;
      background-color: #f5f5f5;
    }
  }
  .el-checkbox__label {
    font-size: 13px;
  }
  /*当内容超出容器的时候，可以拖动，并隐藏滚动条*/
  .el-drawer__body {
    overflow: auto;
  }
  .el-drawer__container ::-webkit-scrollbar{
    display: none;
  }
</style>
