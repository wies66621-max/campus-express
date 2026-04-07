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
      <el-table :data="tableData" stripe border v-loading="loading">
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
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchStationList"
          @current-change="fetchStationList"
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
        <el-form-item label="快递站名称" prop="stationName">
          <el-input v-model="formData.stationName" placeholder="请输入快递站名称" />
        </el-form-item>
        <el-form-item label="地址" prop="location">
          <el-input v-model="formData.location" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="禁用" :value="0" />
            <el-option label="启用" :value="1" />
          </el-select>
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
import type { Station } from '@/types'
import { stationApi } from '@/api/station'

const searchForm = reactive({
  stationName: '',
  location: ''
})

const tableData = ref<Station[]>([])
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('添加快递站')
const formRef = ref<FormInstance>()

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive<Station>({
  id: '',
  stationName: '',
  location: '',
  contactPhone: '',
  status: 1,
  createTime: '',
  updateTime: ''
})

const formRules: FormRules = {
  stationName: [
    { required: true, message: '请输入快递站名称', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '请输入地址', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

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

const fetchStationList = async () => {
  loading.value = true
  try {
    const hasSearchCondition = searchForm.stationName || searchForm.location
    
    let response
    if (hasSearchCondition) {
      response = await stationApi.search(
        searchForm.stationName || undefined,
        searchForm.location || undefined,
        undefined,
        pagination.pageNum,
        pagination.pageSize
      )
    } else {
      response = await stationApi.getList({
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize
      })
    }
    
    if (response.code === 200) {
      tableData.value = response.data.records
      pagination.total = response.data.total
    } else {
      ElMessage.error(response.message || '获取快递站列表失败')
    }
  } catch (error) {
    console.error('获取快递站列表失败:', error)
    ElMessage.error('获取快递站列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchStationList()
}

const handleReset = () => {
  searchForm.stationName = ''
  searchForm.location = ''
  pagination.pageNum = 1
  fetchStationList()
}

const handleAdd = () => {
  dialogTitle.value = '添加快递站'
  Object.assign(formData, {
    id: 0,
    stationName: '',
    location: '',
    contactPhone: '',
    status: 1,
    createTime: '',
    updateTime: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row: Station) => {
  dialogTitle.value = '编辑快递站'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = (row: Station) => {
  ElMessageBox.confirm('确定要删除该快递站吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await stationApi.delete(row.id)
      if (response.code === 200) {
        ElMessage.success('删除成功')
        fetchStationList()
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
        if (!formData.id) {
          response = await stationApi.add(formData)
        } else {
          response = await stationApi.update(formData)
        }
        
        if (response.code === 200) {
          ElMessage.success(!formData.id ? '添加成功' : '更新成功')
          dialogVisible.value = false
          fetchStationList()
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
  fetchStationList()
})
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
