<template>
  <div class="shipping-admin-container">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable>
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已拒绝" :value="2" />
            <el-option label="已取消" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="快递站">
          <el-select v-model="searchForm.stationId" placeholder="全部站点" clearable>
            <el-option
              v-for="station in stations"
              :key="station.id"
              :label="station.name"
              :value="station.id"
            />
          </el-select>
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
          <span>寄件预约管理</span>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="预约ID" width="80" />
        <el-table-column prop="userName" label="用户姓名" width="100" />
        <el-table-column prop="userPhone" label="用户电话" width="120" />
        <el-table-column prop="senderName" label="寄件人" width="100" />
        <el-table-column prop="senderPhone" label="寄件人电话" width="120" />
        <el-table-column prop="receiverName" label="收件人" width="100" />
        <el-table-column prop="receiverPhone" label="收件人电话" width="120" />
        <el-table-column prop="company" label="快递公司" width="100" />
        <el-table-column label="包裹类型" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.packageType === 0" type="info">普通</el-tag>
            <el-tag v-else-if="row.packageType === 1" type="warning">易碎</el-tag>
            <el-tag v-else-if="row.packageType === 2" type="danger">贵重</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else-if="row.status === 2" type="danger">已拒绝</el-tag>
            <el-tag v-else-if="row.status === 3" type="info">已取消</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleViewDetail(row)">详情</el-button>
            <el-button
              v-if="row.status === 0"
              type="warning"
              size="small"
              @click="handleAudit(row)"
            >
              审核
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSearch"
        @current-change="handleSearch"
        class="pagination"
      />
    </el-card>

    <el-dialog v-model="detailVisible" title="预约详情" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="预约ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="用户姓名">{{ detailData.userName }}</el-descriptions-item>
        <el-descriptions-item label="用户电话">{{ detailData.userPhone }}</el-descriptions-item>
        <el-descriptions-item label="寄件人姓名">{{ detailData.senderName }}</el-descriptions-item>
        <el-descriptions-item label="寄件人电话">{{ detailData.senderPhone }}</el-descriptions-item>
        <el-descriptions-item label="寄件人地址" :span="2">{{ detailData.senderAddress }}</el-descriptions-item>
        <el-descriptions-item label="收件人姓名">{{ detailData.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="收件人电话">{{ detailData.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="收件人地址" :span="2">{{ detailData.receiverAddress }}</el-descriptions-item>
        <el-descriptions-item label="快递公司">{{ detailData.company }}</el-descriptions-item>
        <el-descriptions-item label="包裹类型">
          <el-tag v-if="detailData.packageType === 0" type="info">普通</el-tag>
          <el-tag v-else-if="detailData.packageType === 1" type="warning">易碎</el-tag>
          <el-tag v-else-if="detailData.packageType === 2" type="danger">贵重</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="重量">{{ detailData.weight }} kg</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detailData.status === 0" type="warning">待审核</el-tag>
          <el-tag v-else-if="detailData.status === 1" type="success">已通过</el-tag>
          <el-tag v-else-if="detailData.status === 2" type="danger">已拒绝</el-tag>
          <el-tag v-else-if="detailData.status === 3" type="info">已取消</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '无' }}</el-descriptions-item>
        <el-descriptions-item v-if="detailData.auditTime" label="审核时间">{{ detailData.auditTime }}</el-descriptions-item>
        <el-descriptions-item v-if="detailData.auditorName" label="审核人">{{ detailData.auditorName }}</el-descriptions-item>
        <el-descriptions-item v-if="detailData.auditRemark" label="审核备注" :span="2">{{ detailData.auditRemark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="auditDialogVisible" title="审核寄件预约" width="500px">
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.status">
            <el-radio :value="1">通过</el-radio>
            <el-radio :value="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注">
          <el-input v-model="auditForm.auditRemark" type="textarea" :rows="3" placeholder="请输入审核备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAuditSubmit">提交</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { shippingAppointmentApi, type ShippingAppointment } from '@/api/shipping'
import { stationApi } from '@/api/station'

const loading = ref(false)
const tableData = ref<ShippingAppointment[]>([])
const detailVisible = ref(false)
const auditDialogVisible = ref(false)

const searchForm = reactive({
  userId: undefined as string | undefined,
  status: undefined as number | undefined,
  stationId: undefined as string | undefined
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const detailData = ref<ShippingAppointment>({} as ShippingAppointment)
const stations = ref<any[]>([])

const auditForm = reactive({
  id: '',
  status: 1,
  auditRemark: ''
})



const handleSearch = async () => {
  loading.value = true
  try {
    const res = await shippingAppointmentApi.getPage({
      current: pagination.current,
      size: pagination.size,
      userId: searchForm.userId,
      status: searchForm.status,
      stationId: searchForm.stationId
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  searchForm.userId = undefined
  searchForm.status = undefined
  searchForm.stationId = undefined
  pagination.current = 1
  handleSearch()
}

const handleViewDetail = async (row: ShippingAppointment) => {
  try {
    const res = await shippingAppointmentApi.getDetail(row.id!)
    detailData.value = res.data
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

const handleAudit = (row: ShippingAppointment) => {
  auditForm.id = row.id!
  auditForm.status = 1
  auditForm.auditRemark = ''
  auditDialogVisible.value = true
}

const handleAuditSubmit = async () => {
  try {
    await shippingAppointmentApi.audit(auditForm.id, auditForm.status, auditForm.auditRemark)
    ElMessage.success('审核成功')
    auditDialogVisible.value = false
    handleSearch()
  } catch (error) {
    ElMessage.error('审核失败')
  }
}



const loadStations = async () => {
  try {
    const res = await stationApi.getList()
    stations.value = res.data.records || []
  } catch (error) {
    ElMessage.error('加载快递站失败')
  }
}

onMounted(() => {
  handleSearch()
  loadStations()
})
</script>

<style scoped>
.shipping-admin-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  align-items: center;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
