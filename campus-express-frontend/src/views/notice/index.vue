<template>
  <div class="notice-container">
    <div class="page-header">
      <div class="header-left">
        <el-icon class="header-icon" :size="28"><Bell /></el-icon>
        <div>
          <h2>公告管理</h2>
          <p class="module-info">发布和管理系统公告信息</p>
        </div>
      </div>
      <el-button type="primary" :icon="Plus" @click="handleAdd">
        新增公告
      </el-button>
    </div>
    
    <el-card class="content-card">
      <div class="search-section">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="公告标题">
            <el-input 
              v-model="searchForm.title" 
              placeholder="请输入公告标题" 
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select 
              v-model="searchForm.status" 
              placeholder="请选择状态" 
              clearable
              style="width: 120px"
            >
              <el-option label="启用" :value="1" />
              <el-option label="停用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
            <el-button :icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="table-section">
        <el-table :data="tableData" stripe border v-loading="loading" class="notice-table">
          <el-table-column prop="title" label="公告标题" width="220" show-overflow-tooltip>
            <template #default="{ row }">
              <div class="title-cell">
                <el-icon v-if="row.status === 1" class="status-icon"><CircleCheck /></el-icon>
                <el-icon v-else class="status-icon disabled"><CircleClose /></el-icon>
                <span>{{ row.title }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="content" label="公告内容" min-width="300" show-overflow-tooltip />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)" effect="dark" size="small">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="creatorName" label="发布人" width="120" align="center" />
          <el-table-column prop="createTime" label="发布时间" width="180" align="center" />
          <el-table-column label="操作" width="220" fixed="right" align="center">
            <template #default="{ row }">
              <el-button 
                type="primary" 
                link 
                :icon="Edit" 
                @click="handleEdit(row)"
              >
                编辑
              </el-button>
              <el-button 
                :type="row.status === 1 ? 'warning' : 'success'" 
                link
                @click="handleToggleStatus(row)"
              >
                {{ row.status === 1 ? '停用' : '启用' }}
              </el-button>
              <el-button 
                type="danger" 
                link 
                :icon="Delete" 
                @click="handleDelete(row)"
              >
                删除
              </el-button>
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
            @size-change="fetchNoticeList"
            @current-change="fetchNoticeList"
          />
        </div>
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
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input 
            v-model="formData.content" 
            type="textarea" 
            :rows="6" 
            placeholder="请输入公告内容" 
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
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
import { Bell, Plus, Search, Refresh, Edit, Delete, CircleCheck, CircleClose } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { Notice } from '@/api/notice'
import { noticeApi } from '@/api/notice'

const searchForm = reactive({
  title: '',
  status: undefined as number | undefined
})

const tableData = ref<Notice[]>([])
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增公告')
const formRef = ref<FormInstance>()

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive<Notice>({
  id: '',
  title: '',
  content: '',
  status: 1,
  creatorId: '',
  creatorName: '',
  createTime: '',
  updateTime: ''
})

const formRules: FormRules = {
  title: [
    { required: true, message: '请输入公告标题', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入公告内容', trigger: 'blur' }
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
    0: '停用',
    1: '启用'
  }
  return map[status] || '未知'
}

const fetchNoticeList = async () => {
  loading.value = true
  try {
    const response = await noticeApi.getPage({
      current: pagination.pageNum,
      size: pagination.pageSize,
      status: searchForm.status
    })
    
    if (response.code === 200) {
      tableData.value = response.data.records
      pagination.total = response.data.total
    } else {
      ElMessage.error(response.message || '获取公告列表失败')
    }
  } catch (error) {
    console.error('获取公告列表失败:', error)
    ElMessage.error('获取公告列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchNoticeList()
}

const handleReset = () => {
  searchForm.title = ''
  searchForm.status = undefined
  pagination.pageNum = 1
  fetchNoticeList()
}

const handleAdd = () => {
  dialogTitle.value = '新增公告'
  Object.assign(formData, {
    id: 0,
    title: '',
    content: '',
    status: 1,
    creatorId: undefined,
    creatorName: '',
    createTime: '',
    updateTime: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row: Notice) => {
  dialogTitle.value = '编辑公告'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleToggleStatus = (row: Notice) => {
  const newStatus = row.status === 1 ? 0 : 1
  const statusText = newStatus === 1 ? '启用' : '停用'
  
  ElMessageBox.confirm(`确定要${statusText}该公告吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await noticeApi.update({
        ...row,
        status: newStatus
      })
      if (response.code === 200) {
        ElMessage.success(`${statusText}成功`)
        fetchNoticeList()
      } else {
        ElMessage.error(response.message || `${statusText}失败`)
      }
    } catch (error) {
      console.error(`${statusText}失败:`, error)
      ElMessage.error(`${statusText}失败`)
    }
  }).catch(() => {
    ElMessage.info('已取消操作')
  })
}

const handleDelete = (row: Notice) => {
  ElMessageBox.confirm('确定要删除该公告吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await noticeApi.delete(row.id!)
      if (response.code === 200) {
        ElMessage.success('删除成功')
        fetchNoticeList()
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
          response = await noticeApi.add(formData)
        } else {
          response = await noticeApi.update(formData)
        }
        
        if (response.code === 200) {
          ElMessage.success(!formData.id ? '新增成功' : '更新成功')
          dialogVisible.value = false
          fetchNoticeList()
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
  fetchNoticeList()
})
</script>

<style scoped>
.notice-container {
  padding: 24px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 24px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  color: #409eff;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.module-info {
  margin: 4px 0 0 0;
  font-size: 14px;
  color: #909399;
}

.content-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.search-section {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

.search-form {
  margin: 0;
}

.table-section {
  padding: 20px;
}

.notice-table {
  margin-bottom: 20px;
}

.title-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-icon {
  font-size: 16px;
  color: #67c23a;
}

.status-icon.disabled {
  color: #f56c6c;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}

:deep(.el-card__body) {
  padding: 0;
}

:deep(.el-table) {
  border-radius: 4px;
}

:deep(.el-table th) {
  background-color: #fafafa;
  color: #606266;
  font-weight: 600;
}

:deep(.el-button--link) {
  padding: 4px 8px;
}
</style>
