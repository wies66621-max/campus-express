<template>
  <div class="station-container">
    <div class="page-header">
      <h2>快递站管理</h2>
      <p class="module-info">毕业设计模块：快递站管理模块</p>
    </div>
    
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="快递站名称">
          <el-input v-model="searchForm.stationName" placeholder="请输入快递站名称" clearable />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="searchForm.location" placeholder="请输入地址" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>快递站列表</span>
          <el-button type="primary" @click="handleAdd">添加快递站</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe border>
        <el-table-column prop="stationName" label="快递站名称" />
        <el-table-column prop="location" label="地址" />
        <el-table-column prop="contactPhone" label="联系电话" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Station } from '@/types'

const searchForm = reactive({
  stationName: '',
  location: ''
})

const tableData = ref<Station[]>([])

const getStatusType = (status: number) => {
  const map: Record<number, string> = {
    0: 'danger',
    1: 'success'
  }
  return map[status] || 'info'
}

const getStatusText = (status: number) => {
  const map: Record<number, string> = {
    0: '禁用',
    1: '启用'
  }
  return map[status] || '未知'
}

const handleSearch = () => {
  ElMessage.info('查询功能开发中')
}

const handleReset = () => {
  searchForm.stationName = ''
  searchForm.location = ''
}

const handleAdd = () => {
  ElMessage.info('添加功能开发中')
}

const handleEdit = (_row: Station) => {
  ElMessage.info('编辑功能开发中')
}

const handleDelete = (_row: Station) => {
  ElMessageBox.confirm('确定要删除该快递站吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}
</script>

<style scoped>
.station-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin-bottom: 5px;
  color: #303133;
}

.module-info {
  font-size: 14px;
  color: #909399;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
</style>
