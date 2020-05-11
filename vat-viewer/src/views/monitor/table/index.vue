<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.$index }}
        </template>
      </el-table-column>
      <el-table-column width="160" label="riRoleId">
        <template slot-scope="scope">
          {{ scope.row.riRoleId }}
        </template>
      </el-table-column>
      <el-table-column label="riRoleCode" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.riRoleCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="riRoleDesc" width="160" align="center">
        <template slot-scope="scope">
          {{ scope.row.riRoleDesc }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="riStatus" width="160" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.riStatus | statusFilter">{{ scope.row.riStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="riCreateTime">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.riCreateTime }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getAllRole } from '@/api/myrole'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      listLoading: true
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getAllRole().then(response => {
        this.list = response.data
        this.listLoading = false
      })
    }
  }
}
</script>
