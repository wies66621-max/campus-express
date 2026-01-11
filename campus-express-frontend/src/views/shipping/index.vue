<template>
  <div class="shipping-container">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable>
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已拒绝" :value="2" />
            <el-option label="已取消" :value="3" />
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
          <span>我的寄件预约</span>
          <el-button type="primary" @click="handleCreate">新建预约</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="预约ID" width="80" />
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
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleViewDetail(row)">详情</el-button>
            <el-button 
              v-if="row.status === 0" 
              type="danger" 
              size="small" 
              @click="handleCancel(row)"
            >
              取消
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
        <el-divider content-position="left">寄件人信息</el-divider>
        <el-form-item label="姓名" prop="senderName">
          <el-input v-model="formData.senderName" placeholder="请输入寄件人姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="senderPhone">
          <el-input v-model="formData.senderPhone" placeholder="请输入寄件人手机号" />
        </el-form-item>
        <el-form-item label="地址" prop="senderAddress">
          <el-input v-model="formData.senderAddress" placeholder="请输入寄件人地址" />
        </el-form-item>

        <el-divider content-position="left">收件人信息</el-divider>
        <el-form-item label="姓名" prop="receiverName">
          <el-input v-model="formData.receiverName" placeholder="请输入收件人姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="receiverPhone">
          <el-input v-model="formData.receiverPhone" placeholder="请输入收件人手机号" />
        </el-form-item>
        <el-form-item label="地址" prop="receiverAddress">
          <el-input v-model="formData.receiverAddress" placeholder="请输入收件人地址" />
        </el-form-item>

        <el-divider content-position="left">包裹信息</el-divider>
        <el-form-item label="快递公司" prop="company">
          <el-select v-model="formData.company" placeholder="请选择快递公司">
            <el-option label="顺丰速运" value="顺丰速运" />
            <el-option label="中通快递" value="中通快递" />
            <el-option label="圆通速递" value="圆通速递" />
            <el-option label="申通快递" value="申通快递" />
            <el-option label="韵达速递" value="韵达速递" />
            <el-option label="邮政EMS" value="邮政EMS" />
          </el-select>
        </el-form-item>
        <el-form-item label="包裹类型" prop="packageType">
          <el-radio-group v-model="formData.packageType">
            <el-radio :label="0">普通</el-radio>
            <el-radio :label="1">易碎</el-radio>
            <el-radio :label="2">贵重</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="重量(kg)" prop="weight">
          <el-input-number v-model="formData.weight" :min="0" :max="100" :precision="2" :step="0.1" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="预约详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="预约ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { shippingAppointmentApi, type ShippingAppointment } from '@/api/shipping'

const loading = ref(false)
const tableData = ref<ShippingAppointment[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('新建寄件预约')
const detailVisible = ref(false)
const formRef = ref<FormInstance>()

const searchForm = reactive({
  status: undefined as number | undefined
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const formData = reactive<ShippingAppointment>({
  senderName: '',
  senderPhone: '',
  senderAddress: '',
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  company: '',
  packageType: 0,
  weight: 0,
  remark: ''
})

const detailData = ref<ShippingAppointment>({} as ShippingAppointment)

const formRules: FormRules = {
  senderName: [{ required: true, message: '请输入寄件人姓名', trigger: 'blur' }],
  senderPhone: [
    { required: true, message: '请输入寄件人手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  receiverName: [{ required: true, message: '请输入收件人姓名', trigger: 'blur' }],
  receiverPhone: [
    { required: true, message: '请输入收件人手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  company: [{ required: true, message: '请选择快递公司', trigger: 'change' }]
}

const handleSearch = async () => {
  loading.value = true
  try {
    const res = await shippingAppointmentApi.getMyPage({
      current: pagination.current,
      size: pagination.size,
      status: searchForm.status
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
  searchForm.status = undefined
  pagination.current = 1
  handleSearch()
}

const handleCreate = () => {
  dialogTitle.value = '新建寄件预约'
  Object.assign(formData, {
    senderName: '',
    senderPhone: '',
    senderAddress: '',
    receiverName: '',
    receiverPhone: '',
    receiverAddress: '',
    company: '',
    packageType: 0,
    weight: 0,
    remark: ''
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await shippingAppointmentApi.create(formData)
        ElMessage.success('预约创建成功')
        dialogVisible.value = false
        handleSearch()
      } catch (error) {
        ElMessage.error('预约创建失败')
      }
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
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

const handleCancel = async (row: ShippingAppointment) => {
  try {
    await ElMessageBox.confirm('确认取消该预约吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await shippingAppointmentApi.cancel(row.id!)
    ElMessage.success('取消成功')
    handleSearch()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消失败')
    }
  }
}

onMounted(() => {
  handleSearch()
})
</script>

<style scoped>
.shipping-container {
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
