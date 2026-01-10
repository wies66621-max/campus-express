<template>
  <div class="express-container">
    <div class="page-header">
      <h2>快递管理</h2>
      <p class="module-info">毕业设计模块：快递管理模块</p>
    </div>
    
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="快递单号">
          <el-input v-model="searchForm.trackingNumber" placeholder="请输入快递单号" clearable />
        </el-form-item>
        <el-form-item label="收件人">
          <el-input v-model="searchForm.receiverName" placeholder="请输入收件人" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.receiverPhone" placeholder="请输入手机号" clearable />
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
          <span>快递列表</span>
          <el-button type="primary" @click="handleAdd">添加快递</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe border>
        <el-table-column prop="trackingNumber" label="快递单号" />
        <el-table-column prop="company" label="快递公司" />
        <el-table-column prop="receiverName" label="收件人" />
        <el-table-column prop="receiverPhone" label="手机号" />
        <el-table-column prop="stationId" label="快递站ID" />
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
import type { Express } from '@/types'

const searchForm = reactive({
  trackingNumber: '',
  receiverName: '',
  receiverPhone: ''
})

const tableData = ref<Express[]>([])

const getStatusType = (status: number) => {
  const map: Record<number, string> = {
    0: 'warning',
    1: 'success'
  }
  return map[status] || 'info'
}

const getStatusText = (status: number) => {
  const map: Record<number, string> = {
    0: '待取件',
    1: '已取件'
  }
  return map[status] || '未知'
}

const handleSearch = () => {
  ElMessage.info('查询功能开发中')
}

const handleReset = () => {
  searchForm.trackingNumber = ''
  searchForm.receiverName = ''
  searchForm.receiverPhone = ''
}

const handleAdd = () => {
  ElMessage.info('添加功能开发中')
}

const handleEdit = (_row: Express) => {
  ElMessage.info('编辑功能开发中')
}

const handleDelete = (_row: Express) => {
  ElMessageBox.confirm('确定要删除该快递吗？', '提示', {
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
.express-container {
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
