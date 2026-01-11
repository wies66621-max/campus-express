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
      <el-table :data="tableData" stripe border v-loading="loading">
        <el-table-column prop="expressId" label="快递ID" />
        <el-table-column prop="operatorId" label="操作员ID" />
        <el-table-column prop="pickupTime" label="取件时间" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchPickupList"
          @current-change="fetchPickupList"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="快递ID" prop="expressId">
          <el-input v-model="formData.expressId" type="number" placeholder="请输入快递ID" />
        </el-form-item>
        <el-form-item label="操作员ID" prop="operatorId">
          <el-input v-model="formData.operatorId" type="number" placeholder="请输入操作员ID" />
        </el-form-item>
        <el-form-item label="取件时间" prop="pickupTime">
          <el-date-picker
            v-model="formData.pickupTime"
            type="datetime"
            placeholder="选择取件时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="待处理" :value="0" />
            <el-option label="已完成" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import type { Pickup } from '@/types'
import { pickupApi } from '@/api/pickup'

const searchForm = reactive({
  expressId: '',
  operatorId: ''
})

const tableData = ref<Pickup[]>([])
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('添加取件记录')
const formRef = ref<FormInstance>()

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive<Pickup>({
  id: 0,
  expressId: 0,
  operatorId: 0,
  pickupTime: '',
  status: 0,
  remark: '',
  createTime: ''
})

const formRules: FormRules = {
  expressId: [
    { required: true, message: '请输入快递ID', trigger: 'blur' }
  ],
  operatorId: [
    { required: true, message: '请输入操作员ID', trigger: 'blur' }
  ],
  pickupTime: [
    { required: true, message: '请选择取件时间', trigger: 'change' }
  ]
}

const getStatusType = (status: number) => {
  const map: Record<number, string> = {
    0: 'warning',
    1: 'success'
  }
  return map[status] || 'info'
}

const getStatusText = (status: number) => {
  const map: Record<number, string> = {
    0: '待处理',
    1: '已完成'
  }
  return map[status] || '未知'
}

const fetchPickupList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      expressId: searchForm.expressId ? Number(searchForm.expressId) : undefined,
      operatorId: searchForm.operatorId ? Number(searchForm.operatorId) : undefined
    }
    const response = await pickupApi.getList(params)
    if (response.code === 200) {
      tableData.value = response.data.records
      pagination.total = response.data.total
    } else {
      ElMessage.error(response.message || '获取取件记录列表失败')
    }
  } catch (error) {
    console.error('获取取件记录列表失败:', error)
    ElMessage.error('获取取件记录列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchPickupList()
}

const handleReset = () => {
  searchForm.expressId = ''
  searchForm.operatorId = ''
  pagination.pageNum = 1
  fetchPickupList()
}

const handleAdd = () => {
  dialogTitle.value = '添加取件记录'
  Object.assign(formData, {
    id: 0,
    expressId: 0,
    operatorId: 0,
    pickupTime: '',
    status: 0,
    remark: '',
    createTime: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row: Pickup) => {
  dialogTitle.value = '编辑取件记录'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = (row: Pickup) => {
  ElMessageBox.confirm('确定要删除该取件记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await pickupApi.delete(row.id)
      if (response.code === 200) {
        ElMessage.success('删除成功')
        fetchPickupList()
      } else {
        ElMessage.error(response.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        if (formData.id === 0) {
          response = await pickupApi.add(formData)
        } else {
          response = await pickupApi.update(formData)
        }
        
        if (response.code === 200) {
          ElMessage.success(formData.id === 0 ? '添加成功' : '更新成功')
          dialogVisible.value = false
          fetchPickupList()
        } else {
          ElMessage.error(response.message || '操作失败')
        }
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

onMounted(() => {
  fetchPickupList()
})
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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
