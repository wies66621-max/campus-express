<template>
  <div class="pickup-container">
    <div class="page-header">
      <h2>取件记录</h2>
      <p class="module-info">毕业设计模块：取件记录管理模块</p>
    </div>
    
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="快递ID">
          <el-input v-model="searchForm.expressId" placeholder="请输入快递ID" clearable />
        </el-form-item>
        <el-form-item label="操作员ID">
          <el-input v-model="searchForm.operatorId" placeholder="请输入操作员ID" clearable />
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
          <span>取件记录列表</span>
          <el-button type="primary" @click="handleAdd">添加取件记录</el-button>
        </div>
      </template>
      <el-table :data="tableData" stripe border>
        <el-table-column prop="expressId" label="快递ID" />
        <el-table-column prop="operatorId" label="操作员ID" />
        <el-table-column prop="pickupTime" label="取件时间" />
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
import type { Pickup } from '@/types'

const searchForm = reactive({
  expressId: '',
  operatorId: ''
})

const tableData = ref<Pickup[]>([])

const handleSearch = () => {
  ElMessage.info('查询功能开发中')
}

const handleReset = () => {
  searchForm.expressId = ''
  searchForm.operatorId = ''
}

const handleAdd = () => {
  ElMessage.info('添加功能开发中')
}

const handleEdit = (_row: Pickup) => {
  ElMessage.info('编辑功能开发中')
}

const handleDelete = (_row: Pickup) => {
  ElMessageBox.confirm('确定要删除该取件记录吗？', '提示', {
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
.pickup-container {
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
