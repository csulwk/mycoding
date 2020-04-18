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
      <el-table-column prop="riRoleId" label="ID" align="center" width="60"></el-table-column>
      <el-table-column prop="riRoleCode" label="角色代码" align="center" width="140"></el-table-column>
      <el-table-column prop="riRoleDesc" label="角色名称" align="center"></el-table-column>
      <el-table-column align="center" label="角色状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.riStatus === '0' ? 'success' : 'danger'" disable-transitions>
            <span v-html="formatterStatus(scope.row.riStatus)"></span>
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
      <el-table-column prop="riVersion" align="center" label="版本" width="60"></el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-tooltip content="查看" placement="top" effect="light">
          </el-tooltip>
          <el-tooltip content="编辑" placement="top" effect="light">
          </el-tooltip>
          <el-button type="primary" size="mini" icon="el-icon-s-unfold" round @click="showPermDraw(scope.$index, scope.row)"></el-button>
          <el-button type="warning" size="mini" icon="el-icon-edit-outline" round @click="showUpdate(scope.$index, scope.row)"></el-button>
          <el-popconfirm title="删除须谨慎，请确认？" style="margin-left: 10px;" @onConfirm="showDelete(scope.$index, scope.row)">
            <el-button slot="reference" type="danger" size="mini" icon="el-icon-delete" round></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-drawer title="查看当前角色拥有的权限" :visible.sync="drawer" :with-header="false">
      <div style="margin: 10px 15px;">
        <el-form :model="tempRole" label-position="top" label-width="80px">
          <el-form-item label="角色描述">
            <el-input v-model="tempRole.desc" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="角色权限">
            <div v-for="(item,index) in tempRole.perms" :key="index">
              <el-divider content-position="left">{{ item.label }}</el-divider>
              <el-tag v-for="itemChild in item.children" :key="itemChild.id" v-text="itemChild.label" style="margin-right: 3px;" type="primary"></el-tag>
            </div>
          </el-form-item>
          <el-form-item label="用户归属">
            <el-tag v-for="item in menus" :key="item.menuName" v-text="item.menuName" effect="plain"></el-tag>
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @open='getRolePerm'>
      <el-form :model="tempRole" label-position="right" label-width="80px" style="width: 600px; margin-left:50px;">
        <el-form-item label="角色代码" required>
          <el-input v-model="tempRole.code" type="text"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" required>
          <el-input v-model="tempRole.desc" type="text"></el-input>
        </el-form-item>
        <el-form-item label="角色管理" size="medium" required>
          <div class="permission">
            <div class="permissionContent">
              <div v-for="(item,index) in tempRole.perms" :key="index">
                <div class="permissionTitle">{{ item.label }}</div>
                <el-checkbox-group v-model="checkedPermissionIds">
                  <el-checkbox v-for="itemChild in item.children" :key="itemChild.id" :label="itemChild.id" style="width: 100px;margin-left: 6px;margin-bottom: 8px">
                    {{ itemChild.label }}
                  </el-checkbox>
                </el-checkbox-group>
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="是否生效">
          <el-switch v-model="tempRole.status"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus === 'create'" type="success" @click="onSubmitUser">新 增</el-button>
        <el-button v-else type="primary" @click="onUpdateUser">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAllRole } from '@/api/myrole'
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
        perms: []
      },
      checkedPermissionIds: [],
      menus: [
        {
          'permissions': [
            {
              'requiredPerm': 1,
              'id': 101,
              'permissionName': '列表'
            },
            {
              'requiredPerm': 2,
              'id': 102,
              'permissionName': '新增'
            }
          ],
          'menuName': '文章'
        },
        {
          'permissions': [
            {
              'requiredPerm': 1,
              'id': 601,
              'permissionName': '列表'
            },
            {
              'requiredPerm': 2,
              'id': 602,
              'permissionName': '新增'
            }
          ],
          'menuName': '用户'
        },
        {
          'permissions': [
            {
              'requiredPerm': 1,
              'id': 701,
              'permissionName': '列表'
            },
            {
              'requiredPerm': 2,
              'id': 704,
              'permissionName': '删除'
            }
          ],
          'menuName': '角色'
        }
      ]
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
      this.tempRole.perms = [{ 'id': 1, 'parentId': 0, 'label': '系统管理', 'value': 1, 'level': 1, 'other': { 'id': 1, 'code': '1', 'name': '系统管理', 'parentId': 0 }, 'children': [{ 'id': 100, 'parentId': 1, 'label': '用户管理-查看', 'value': 100, 'level': 2, 'other': { 'id': 100, 'code': 'user: query', 'name': '用户管理-查看', 'parentId': 1 }, 'children': null }, { 'id': 101, 'parentId': 1, 'label': '用户管理-增删改', 'value': 101, 'level': 2, 'other': { 'id': 101, 'code': 'user: edit', 'name': '用户管理-增删改', 'parentId': 1 }, 'children': null }, { 'id': 110, 'parentId': 1, 'label': '角色管理-查看', 'value': 110, 'level': 2, 'other': { 'id': 110, 'code': 'role: query', 'name': '角色管理-查看', 'parentId': 1 }, 'children': null }, { 'id': 111, 'parentId': 1, 'label': '角色管理-增删改', 'value': 111, 'level': 2, 'other': { 'id': 111, 'code': 'role: edit', 'name': '角色管理-增删改', 'parentId': 1 }, 'children': null }, { 'id': 120, 'parentId': 1, 'label': '部门管理-查看', 'value': 120, 'level': 2, 'other': { 'id': 120, 'code': 'dept: query', 'name': '部门管理-查看', 'parentId': 1 }, 'children': null }, { 'id': 121, 'parentId': 1, 'label': '部门管理-增删改', 'value': 121, 'level': 2, 'other': { 'id': 121, 'code': 'dept: edit', 'name': '部门管理-增删改', 'parentId': 1 }, 'children': null }] }, { 'id': 2, 'parentId': 0, 'label': 'DEMO管理', 'value': 2, 'level': 1, 'other': { 'id': 2, 'code': '2', 'name': 'DEMO管理', 'parentId': 0 }, 'children': [{ 'id': 130, 'parentId': 2, 'label': '设备管理-查看', 'value': 130, 'level': 2, 'other': { 'id': 130, 'code': 'device: query', 'name': '设备管理-查看', 'parentId': 2 }, 'children': null }, { 'id': 131, 'parentId': 2, 'label': '设备管理-增删改', 'value': 131, 'level': 2, 'other': { 'id': 131, 'code': 'device: edit', 'name': '设备管理-增删改', 'parentId': 2 }, 'children': null }] }]
      this.checkedPermissionIds = [100, 120, 110, 121, 131]
      console.log('index:' + index)
      console.log('row:' + JSON.stringify(row))
      const data = this.list[index]
      console.log('data:' + JSON.stringify(data))
      this.tempRole.code = data.riRoleCode
      this.tempRole.desc = data.riRoleDesc
      this.tempRole.roleId = data.riRoleId
      this.tempRole.status = data.riStatus === '0'
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    showPermDraw(index, row) {
      this.drawer = true
      this.tempRole.perms = [{ 'id': 1, 'parentId': 0, 'label': '系统管理', 'value': 1, 'level': 1, 'other': { 'id': 1, 'code': '1', 'name': '系统管理', 'parentId': 0 }, 'children': [{ 'id': 100, 'parentId': 1, 'label': '用户管理-查看', 'value': 100, 'level': 2, 'other': { 'id': 100, 'code': 'user: query', 'name': '用户管理-查看', 'parentId': 1 }, 'children': null }, { 'id': 101, 'parentId': 1, 'label': '用户管理-增删改', 'value': 101, 'level': 2, 'other': { 'id': 101, 'code': 'user: edit', 'name': '用户管理-增删改', 'parentId': 1 }, 'children': null }, { 'id': 110, 'parentId': 1, 'label': '角色管理-查看', 'value': 110, 'level': 2, 'other': { 'id': 110, 'code': 'role: query', 'name': '角色管理-查看', 'parentId': 1 }, 'children': null }, { 'id': 111, 'parentId': 1, 'label': '角色管理-增删改', 'value': 111, 'level': 2, 'other': { 'id': 111, 'code': 'role: edit', 'name': '角色管理-增删改', 'parentId': 1 }, 'children': null }, { 'id': 120, 'parentId': 1, 'label': '部门管理-查看', 'value': 120, 'level': 2, 'other': { 'id': 120, 'code': 'dept: query', 'name': '部门管理-查看', 'parentId': 1 }, 'children': null }, { 'id': 121, 'parentId': 1, 'label': '部门管理-增删改', 'value': 121, 'level': 2, 'other': { 'id': 121, 'code': 'dept: edit', 'name': '部门管理-增删改', 'parentId': 1 }, 'children': null }] }, { 'id': 2, 'parentId': 0, 'label': 'DEMO管理', 'value': 2, 'level': 1, 'other': { 'id': 2, 'code': '2', 'name': 'DEMO管理', 'parentId': 0 }, 'children': [{ 'id': 130, 'parentId': 2, 'label': '设备管理-查看', 'value': 130, 'level': 2, 'other': { 'id': 130, 'code': 'device: query', 'name': '设备管理-查看', 'parentId': 2 }, 'children': null }, { 'id': 131, 'parentId': 2, 'label': '设备管理-增删改', 'value': 131, 'level': 2, 'other': { 'id': 131, 'code': 'device: edit', 'name': '设备管理-增删改', 'parentId': 2 }, 'children': null }] }]
      this.checkedPermissionIds = [100, 120, 110, 121, 131]
      console.log('index:' + index)
      console.log('row:' + JSON.stringify(row))
      const data = this.list[index]
      console.log('data:' + JSON.stringify(data))
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
    onSubmitUser() {
      console.log('tempRole:' + JSON.stringify(this.tempRole))
      console.log('perms:' + this.checkedPermissionIds)
    },
    onUpdateUser() {
      console.log('tempRole:' + JSON.stringify(this.tempRole))
      console.log('perms:' + this.checkedPermissionIds)
    }
  }
}
</script>

<style lang="scss">
  .permission{
    border: 1px solid #ebebeb;
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
