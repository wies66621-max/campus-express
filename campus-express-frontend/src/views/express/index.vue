<template>
  <div class="express-container">
    <div class="page-header">
      <h2>快递快速查询</h2>
      <p class="module-info">毕业设计模块：快递管理模块</p>
      <p class="module-desc">用于快递站高频查件操作，支持按单号、取件码、手机号快速定位快递。</p>
    </div>
    
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="快递单号">
          <el-input 
            v-model="searchForm.trackingNumber" 
            placeholder="请输入快递单号" 
            clearable 
            @keyup.enter="handleQuickSearch"
          />
        </el-form-item>
        <el-form-item label="取件码">
          <el-input 
            v-model="searchForm.pickupCode" 
            placeholder="请输入取件码" 
            clearable 
            @keyup.enter="handleQuickSearch"
          />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input 
            v-model="searchForm.receiverPhone" 
            placeholder="请输入收件人手机号" 
            clearable 
            @keyup.enter="handleQuickSearch"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="仅待取件" :value="0" />
            <el-option label="已取件" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuickSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>查询结果</span>
          <div class="header-actions">
            <el-button 
              v-if="userRole === 'admin'" 
              type="success" 
              @click="handleInbound"
            >
              快递入库（扫码）
            </el-button>
          </div>
        </div>
      </template>
      <el-table :data="tableData" stripe border v-loading="loading" empty-text="暂无数据，请输入查询条件">
        <el-table-column prop="trackingNumber" label="快递单号" width="180" />
        <el-table-column prop="receiverName" label="收件人" width="100" />
        <el-table-column prop="receiverPhone" label="手机号" width="120" />
        <el-table-column prop="pickupCode" label="取件码" width="100">
          <template #default="{ row }">
            <el-tag type="info">{{ row.pickupCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.status === 0 && (userRole === 'admin' || userRole === 'courier')" 
              type="success" 
              size="small" 
              @click="handlePickup(row)"
            >
              确认取件
            </el-button>
            <el-button 
              v-else-if="row.status === 0" 
              type="info" 
              size="small" 
              disabled
            >
              待取件
            </el-button>
            <el-button 
              v-else 
              type="info" 
              size="small" 
              disabled
            >
              已取件
            </el-button>
            <el-button type="primary" size="small" @click="handleViewDetail(row)">
              查看详情
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
          @size-change="handleQuickSearch"
          @current-change="handleQuickSearch"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="inboundDialogVisible"
      title="快递入库（扫码）"
      width="500px"
      draggable
      @close="handleInboundDialogClose"
    >
      <div class="inbound-tip">
        <el-alert
          title="注：通过输入快递单号模拟扫码入库流程"
          type="info"
          :closable="false"
          show-icon
        />
      </div>
      <el-form
        ref="inboundFormRef"
        :model="inboundFormData"
        :rules="inboundFormRules"
        label-width="100px"
        style="margin-top: 20px"
      >
        <el-form-item label="快递单号" prop="trackingNumber">
          <el-input v-model="inboundFormData.trackingNumber" placeholder="请扫描或输入快递单号" />
        </el-form-item>
        <el-form-item label="收件人手机号" prop="receiverPhone">
          <el-input v-model="inboundFormData.receiverPhone" placeholder="用于自动匹配用户" />
        </el-form-item>
        <el-form-item label="快递公司" prop="company">
          <el-select v-model="inboundFormData.company" placeholder="请选择快递公司" style="width: 100%">
            <el-option label="顺丰" value="SF" />
            <el-option label="中通" value="ZT" />
            <el-option label="圆通" value="YT" />
            <el-option label="韵达" value="YD" />
            <el-option label="申通" value="ST" />
            <el-option label="京东" value="JD" />
            <el-option label="邮政" value="YZ" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="inboundDialogVisible = false">取消</el-button>
        <el-button type="success" @click="handleInboundSubmit" :loading="inboundSubmitLoading">入库</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="detailDialogVisible"
      title="快递详情"
      width="600px"
      draggable
    >
      <el-descriptions :column="2" border v-if="currentExpress">
        <el-descriptions-item label="快递单号">{{ currentExpress.trackingNumber }}</el-descriptions-item>
        <el-descriptions-item label="快递公司">{{ currentExpress.company || '-' }}</el-descriptions-item>
        <el-descriptions-item label="收件人">{{ currentExpress.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentExpress.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="取件码">
          <el-tag type="info">{{ currentExpress.pickupCode }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentExpress.status)">
            {{ getStatusText(currentExpress.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="快递站ID">{{ currentExpress.stationId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ currentExpress.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ currentExpress.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间" :span="2">{{ currentExpress.updateTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import type { Express } from '@/types'
import { expressApi } from '@/api/express'

const userRole = computed(() => localStorage.getItem('userRole') || 'user')

const searchForm = reactive({
  trackingNumber: '',
  pickupCode: '',
  receiverPhone: '',
  status: 0 as number | undefined
})

const tableData = ref<Express[]>([])
const loading = ref(false)
const inboundDialogVisible = ref(false)
const inboundSubmitLoading = ref(false)
const inboundFormRef = ref<FormInstance>()
const detailDialogVisible = ref(false)
const currentExpress = ref<Express | null>(null)

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const inboundFormData = reactive({
  trackingNumber: '',
  receiverPhone: '',
  company: ''
})

const inboundFormRules: FormRules = {
  trackingNumber: [
    { required: true, message: '请输入快递单号', trigger: 'blur' }
  ],
  receiverPhone: [
    { required: true, message: '请输入收件人手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
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
    0: '待取件',
    1: '已取件'
  }
  return map[status] || '未知'
}

const handleQuickSearch = async () => {
  if (!searchForm.trackingNumber && !searchForm.pickupCode && !searchForm.receiverPhone) {
    ElMessage.warning('请至少输入一个查询条件')
    return
  }

  loading.value = true
  try {
    const params = {
      trackingNumber: searchForm.trackingNumber || undefined,
      pickupCode: searchForm.pickupCode || undefined,
      receiverPhone: searchForm.receiverPhone || undefined,
      status: searchForm.status,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const response = await expressApi.quickSearch(params)
    if (response.code === 200) {
      tableData.value = response.data.records
      pagination.total = response.data.total
    } else {
      ElMessage.error(response.message || '查询失败')
    }
  } catch (error) {
    console.error('查询失败:', error)
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  searchForm.trackingNumber = ''
  searchForm.pickupCode = ''
  searchForm.receiverPhone = ''
  searchForm.status = 0
  pagination.pageNum = 1
  tableData.value = []
  pagination.total = 0
}

const handleInbound = () => {
  inboundFormData.trackingNumber = ''
  inboundFormData.receiverPhone = ''
  inboundFormData.company = ''
  inboundDialogVisible.value = true
}

const handleInboundSubmit = async () => {
  if (!inboundFormRef.value) return
  
  await inboundFormRef.value.validate(async (valid) => {
    if (valid) {
      inboundSubmitLoading.value = true
      try {
        await expressApi.inbound(inboundFormData)
        ElMessage.success('快递入库成功')
        inboundDialogVisible.value = false
        handleQuickSearch()
      } catch (error) {
        console.error('入库失败:', error)
      } finally {
        inboundSubmitLoading.value = false
      }
    }
  })
}

const handleInboundDialogClose = () => {
  inboundFormRef.value?.resetFields()
}

const handlePickup = (row: Express) => {
  ElMessageBox.confirm(`确认取件码为 ${row.pickupCode} 的快递吗？`, '确认取件', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await expressApi.pickupByCode(row.pickupCode)
      ElMessage.success('取件成功')
      handleQuickSearch()
    } catch (error) {
      console.error('取件失败:', error)
    }
  }).catch(() => {
    ElMessage.info('已取消取件')
  })
}

const handleViewDetail = (row: Express) => {
  currentExpress.value = row
  detailDialogVisible.value = true
}

onMounted(() => {
  handleReset()
})
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
  margin-bottom: 5px;
}

.module-desc {
  font-size: 13px;
  color: #606266;
  background-color: #f4f4f5;
  padding: 8px 12px;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
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

.header-actions {
  display: flex;
  gap: 10px;
}

.inbound-tip {
  margin-bottom: 15px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
