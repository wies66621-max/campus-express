<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h2>{{ userRole === 'admin' ? '数据统计' : '我的快递' }}</h2>
      <p class="module-info">{{ userRole === 'admin' ? '毕业设计模块：数据统计模块' : '毕业设计模块：用户快递管理模块' }}</p>
    </div>
    
    <template v-if="userRole === 'admin'">
      <el-row :gutter="20" class="stats-row">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalExpress }}</div>
              <div class="stat-label">总快递数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-value">{{ stats.pending }}</div>
              <div class="stat-label">待取件</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-value">{{ stats.picked }}</div>
              <div class="stat-label">已取件</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-value">{{ stats.stationCount }}</div>
              <div class="stat-label">快递站数量</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="charts-row">
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>快递状态分布</span>
              </div>
            </template>
            <div ref="pieChartRef" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="card-header">
                <span>每日取件数量变化</span>
              </div>
            </template>
            <div ref="barChartRef" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-card class="table-card">
        <template #header>
          <div class="card-header">
            <span>最近取件记录</span>
          </div>
        </template>
        <el-table :data="recentPickups" stripe>
          <el-table-column prop="expressId" label="快递ID" />
          <el-table-column prop="operatorId" label="操作员ID" />
          <el-table-column prop="pickupTime" label="取件时间" />
          <el-table-column prop="status" label="状态">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'info'">
                {{ row.status === 1 ? '已取件' : '待取件' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </template>

    <template v-else>
      <el-row :gutter="20" class="user-stats-row">
        <el-col :span="8">
          <el-card class="user-stat-card pending">
            <div class="user-stat-content">
              <div class="user-stat-icon">📦</div>
              <div class="user-stat-info">
                <div class="user-stat-value">{{ userStats.pendingCount }}</div>
                <div class="user-stat-label">我的待取快递</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="user-stat-card picked">
            <div class="user-stat-content">
              <div class="user-stat-icon">✅</div>
              <div class="user-stat-info">
                <div class="user-stat-value">{{ userStats.pickedCount }}</div>
                <div class="user-stat-label">我的已取快递</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="user-stat-card recent">
            <div class="user-stat-content">
              <div class="user-stat-icon">🕐</div>
              <div class="user-stat-info">
                <div class="user-stat-value">{{ userStats.lastPickupTime || '无' }}</div>
                <div class="user-stat-label">最近取件时间</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-card class="user-express-card">
        <template #header>
          <div class="card-header">
            <span>我的待取快递</span>
            <el-tag type="warning" v-if="userStats.pendingCount > 0">有 {{ userStats.pendingCount }} 件快递待取</el-tag>
          </div>
        </template>
        <el-empty v-if="pendingExpressList.length === 0" description="暂无待取快递" />
        <el-table v-else :data="pendingExpressList" stripe>
          <el-table-column prop="trackingNumber" label="快递单号" />
          <el-table-column prop="company" label="快递公司" />
          <el-table-column prop="stationName" label="站点" />
          <el-table-column prop="pickupCode" label="取件码">
            <template #default="{ row }">
              <el-tag type="primary" size="large">{{ row.pickupCode }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态">
            <template #default="{ row }">
              <el-tag :type="row.status === 0 ? 'warning' : 'success'">
                {{ row.status === 0 ? '待取件' : '已取件' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card class="user-history-card">
        <template #header>
          <div class="card-header">
            <span>我的最近取件记录</span>
          </div>
        </template>
        <el-empty v-if="pickedExpressList.length === 0" description="暂无取件记录" />
        <el-table v-else :data="pickedExpressList" stripe>
          <el-table-column prop="trackingNumber" label="快递单号" />
          <el-table-column prop="company" label="快递公司" />
          <el-table-column prop="pickupTime" label="取件时间" />
          <el-table-column prop="status" label="状态">
            <template #default>
              <el-tag type="success">已取件</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card class="notice-card">
        <template #header>
          <div class="card-header">
            <div class="header-left">
              <el-icon class="notice-header-icon" :size="20"><Bell /></el-icon>
              <span>系统公告</span>
            </div>
            <el-tag v-if="noticeList.length > 0" type="info" size="small">{{ noticeList.length }} 条公告</el-tag>
          </div>
        </template>
        <el-empty v-if="noticeList.length === 0" description="暂无公告" :image-size="100" />
        <div v-else class="notice-list">
          <div
            v-for="(notice, index) in noticeList"
            :key="notice.id"
            class="notice-item"
            :style="{ animationDelay: `${index * 0.1}s` }"
            @click="handleViewNotice(notice)"
          >
            <div class="notice-left">
              <div class="notice-icon">
                <el-icon :size="20"><Notification /></el-icon>
              </div>
              <div class="notice-content">
                <div class="notice-title">{{ notice.title }}</div>
                <div class="notice-preview">{{ notice.content?.substring(0, 50) }}{{ (notice.content?.length || 0) > 50 ? '...' : '' }}</div>
              </div>
            </div>
            <div class="notice-right">
              <div class="notice-time">{{ formatDate(notice.createTime) }}</div>
              <el-icon class="notice-arrow"><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </el-card>
    </template>

    <el-dialog
      v-model="noticeDialogVisible"
      title="公告详情"
      width="600px"
    >
      <div v-if="currentNotice" class="notice-detail">
        <h3 class="notice-detail-title">{{ currentNotice.title }}</h3>
        <div class="notice-detail-meta">
          <span>发布时间：{{ currentNotice.createTime }}</span>
          <span v-if="currentNotice.creatorName">发布人：{{ currentNotice.creatorName }}</span>
        </div>
        <div class="notice-detail-content">{{ currentNotice.content }}</div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import * as echarts from 'echarts'
import { Bell, Notification, ArrowRight } from '@element-plus/icons-vue'
import { expressApi } from '@/api/express'
import { pickupApi } from '@/api/pickup'
import { noticeApi } from '@/api/notice'
import { useUserStore } from '@/store/modules/user'
import type { Pickup, Express } from '@/types'
import type { Notice } from '@/api/notice'

const userStore = useUserStore()
const userRole = computed(() => userStore.user?.role || 'user')

const pieChartRef = ref<HTMLElement>()
const barChartRef = ref<HTMLElement>()
let pieChart: echarts.ECharts | null = null
let barChart: echarts.ECharts | null = null

const stats = ref({
  totalExpress: 0,
  pending: 0,
  picked: 0,
  stationCount: 0
})

const userStats = ref({
  pendingCount: 0,
  pickedCount: 0,
  lastPickupTime: ''
})

const recentPickups = ref<Pickup[]>([])
const pendingExpressList = ref<Express[]>([])
const pickedExpressList = ref<Express[]>([])
const noticeList = ref<Notice[]>([])
const noticeDialogVisible = ref(false)
const currentNotice = ref<Notice | null>(null)

const fetchStatistics = async () => {
  try {
    const response = await expressApi.getStatistics()
    if (response.code === 200 && response.data) {
      stats.value.totalExpress = response.data.totalCount
      stats.value.pending = response.data.pendingCount
      stats.value.picked = response.data.pickedCount
      stats.value.stationCount = response.data.stationCount
      updatePieChart()
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

const updatePieChart = () => {
  if (!pieChart) return
  pieChart.setOption({
    series: [
      {
        name: '快递状态',
        type: 'pie',
        radius: '50%',
        data: [
          { value: stats.value.pending, name: '待取件' },
          { value: stats.value.picked, name: '已取件' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })
}

const fetchUserExpressData = async () => {
  try {
    const response = await expressApi.getMyExpress(1, 100)
    
    if (response.code === 200 && response.data) {
      const allExpress = response.data.records || []
      pendingExpressList.value = allExpress.filter((e: Express) => e.status === 0).slice(0, 5)
      pickedExpressList.value = allExpress.filter((e: Express) => e.status === 1).slice(0, 5)
      
      userStats.value.pendingCount = pendingExpressList.value.length
      userStats.value.pickedCount = pickedExpressList.value.length
      
      if (pickedExpressList.value.length > 0) {
        userStats.value.lastPickupTime = pickedExpressList.value[0].updateTime?.split(' ')[0] || ''
      }
    }
  } catch (error: any) {
    console.error('获取用户快递数据失败:', error?.message || error)
    
    if (error?.message?.includes('用户不存在') || error?.response?.data?.message?.includes('用户不存在')) {
      console.warn('用户信息可能已过期，尝试重新获取用户信息')
      try {
        await userStore.getUserInfo()
        await fetchUserExpressData()
      } catch (retryError) {
        console.error('重新获取用户信息失败:', retryError)
      }
    }
  }
}

const fetchRecentPickups = async () => {
  try {
    const response = await pickupApi.getList({ pageNum: 1, pageSize: 5 })
    if (response.code === 200 && response.data) {
      recentPickups.value = response.data.records
    }
  } catch (error) {
    console.error('获取取件记录失败:', error)
  }
}

const initPieChart = () => {
  if (!pieChartRef.value) return
  pieChart = echarts.init(pieChartRef.value)
  pieChart.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '快递状态',
        type: 'pie',
        radius: '50%',
        data: [
          { value: stats.value.pending, name: '待取件' },
          { value: stats.value.picked, name: '已取件' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })
}

const initBarChart = () => {
  if (!barChartRef.value) return
  barChart = echarts.init(barChartRef.value)
  barChart.setOption({
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '取件数量',
        type: 'bar',
        data: [120, 200, 150, 80, 70, 110, 130]
      }
    ]
  })
}

const handleResize = () => {
  pieChart?.resize()
  barChart?.resize()
}

const fetchNoticeList = async () => {
  try {
    const response = await noticeApi.getList({ limit: 5 })
    if (response.code === 200 && response.data) {
      noticeList.value = response.data
    }
  } catch (error) {
    console.error('获取公告列表失败:', error)
  }
}

const formatDate = (dateStr: string | undefined) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  
  if (days === 0) {
    const hours = Math.floor(diff / (1000 * 60 * 60))
    if (hours === 0) {
      const minutes = Math.floor(diff / (1000 * 60))
      return minutes < 1 ? '刚刚' : `${minutes}分钟前`
    }
    return `${hours}小时前`
  } else if (days === 1) {
    return '昨天'
  } else if (days < 7) {
    return `${days}天前`
  } else {
    return dateStr.split(' ')[0]
  }
}

const handleViewNotice = async (notice: Notice) => {
  try {
    const response = await noticeApi.getDetail(notice.id!)
    if (response.code === 200 && response.data) {
      currentNotice.value = response.data
      noticeDialogVisible.value = true
    } else {
      console.error('获取公告详情失败:', response.message)
    }
  } catch (error) {
    console.error('获取公告详情失败:', error)
  }
}

onMounted(async () => {
  await userStore.initUserInfo()
  
  if (userRole.value === 'admin') {
    fetchStatistics()
    fetchRecentPickups()
    initPieChart()
    initBarChart()
    window.addEventListener('resize', handleResize)
  } else {
    await fetchUserExpressData()
    await fetchNoticeList()
  }
})

onUnmounted(() => {
  pieChart?.dispose()
  barChart?.dispose()
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.dashboard-container {
  padding: 24px;
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.dashboard-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f0f0f0;
}

.dashboard-header h2 {
  margin-bottom: 8px;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.module-info {
  font-size: 13px;
  color: #909399;
  font-weight: 400;
}

.stats-row {
  margin-bottom: 24px;
}

.stat-card {
  text-align: center;
  border-radius: 12px;
  transition: all 0.3s ease;
  cursor: pointer;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #409EFF 0%, #79bbff 100%);
}

.stat-content {
  padding: 24px 16px;
  position: relative;
}

.stat-value {
  font-size: 36px;
  font-weight: 700;
  background: linear-gradient(135deg, #409EFF 0%, #337ecc 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 12px;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.charts-row {
  margin-bottom: 24px;
}

.chart-card {
  height: 420px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.chart-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.chart {
  width: 100%;
  height: 340px;
}

.table-card {
  margin-bottom: 24px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.table-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
  color: #303133;
}

.user-stats-row {
  margin-bottom: 24px;
}

.user-stat-card {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 12px;
  overflow: hidden;
  position: relative;
}

.user-stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  border-radius: 12px 12px 0 0;
}

.user-stat-card.pending::before {
  background: linear-gradient(90deg, #E6A23C 0%, #f3d19e 100%);
}

.user-stat-card.picked::before {
  background: linear-gradient(90deg, #67C23A 0%, #95d475 100%);
}

.user-stat-card.recent::before {
  background: linear-gradient(90deg, #409EFF 0%, #79bbff 100%);
}

.user-stat-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.15);
}

.user-stat-content {
  display: flex;
  align-items: center;
  padding: 28px 24px;
}

.user-stat-icon {
  font-size: 52px;
  margin-right: 20px;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.1));
  transition: transform 0.3s ease;
}

.user-stat-card:hover .user-stat-icon {
  transform: scale(1.1) rotate(5deg);
}

.user-stat-info {
  flex: 1;
}

.user-stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 8px;
  line-height: 1.2;
}

.user-stat-label {
  font-size: 14px;
  color: #909399;
  font-weight: 500;
}

.user-express-card,
.user-history-card {
  margin-bottom: 24px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.user-express-card:hover,
.user-history-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.notice-card {
  margin-bottom: 24px;
  border-radius: 12px;
  transition: all 0.3s ease;
  overflow: hidden;
}

.notice-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.notice-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
  color: #303133;
}

.notice-card .header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.notice-header-icon {
  color: #409EFF;
}

.notice-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notice-item {
  padding: 16px;
  border-radius: 12px;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid #e9ecef;
  animation: slideIn 0.4s ease-out backwards;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.notice-item:hover {
  background: linear-gradient(135deg, #e9ecef 0%, #f8f9fa 100%);
  transform: translateX(8px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border-color: #409EFF;
}

.notice-left {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
}

.notice-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.notice-content {
  flex: 1;
  min-width: 0;
}

.notice-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notice-preview {
  font-size: 13px;
  color: #909399;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notice-right {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.notice-time {
  font-size: 12px;
  color: #909399;
  padding: 4px 8px;
  background: #f0f2f5;
  border-radius: 6px;
}

.notice-arrow {
  color: #c0c4cc;
  transition: all 0.3s ease;
}

.notice-item:hover .notice-arrow {
  color: #409EFF;
  transform: translateX(4px);
}

.notice-detail-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
}

.notice-detail-meta {
  display: flex;
  gap: 24px;
  margin-bottom: 20px;
  font-size: 14px;
  color: #909399;
}

.notice-detail-content {
  font-size: 15px;
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
  word-wrap: break-word;
}

:deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

:deep(.el-dialog__header) {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
  padding: 20px 24px;
}

:deep(.el-dialog__title) {
  color: white;
  font-size: 18px;
  font-weight: 600;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
  font-size: 20px;
}

:deep(.el-dialog__body) {
  padding: 24px;
}

:deep(.notice-detail) {
  padding: 8px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background: linear-gradient(180deg, #fafafa 0%, #f5f5f5 100%);
  font-weight: 600;
  color: #303133;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #fafafa;
}

:deep(.el-table tbody tr:hover > td) {
  background-color: #f0f7ff !important;
}

:deep(.el-tag) {
  border-radius: 6px;
  padding: 4px 12px;
  font-weight: 500;
  border: none;
}

:deep(.el-tag--primary) {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
  color: white;
}

:deep(.el-tag--success) {
  background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);
  color: white;
}

:deep(.el-tag--warning) {
  background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);
  color: white;
}

:deep(.el-empty) {
  padding: 40px 0;
}

:deep(.el-empty__description) {
  color: #909399;
  font-size: 14px;
}

@media (max-width: 1200px) {
  .dashboard-container {
    padding: 20px;
  }
  
  .stat-value {
    font-size: 32px;
  }
  
  .user-stat-value {
    font-size: 28px;
  }
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 16px;
  }
  
  .dashboard-header h2 {
    font-size: 20px;
  }
  
  .stat-value {
    font-size: 28px;
  }
  
  .user-stat-content {
    padding: 20px 16px;
  }
  
  .user-stat-icon {
    font-size: 40px;
    margin-right: 16px;
  }
  
  .user-stat-value {
    font-size: 24px;
  }
  
  .chart-card {
    height: 360px;
  }
  
  .chart {
    height: 280px;
  }
}

@media (max-width: 480px) {
  .dashboard-container {
    padding: 12px;
  }
  
  .dashboard-header h2 {
    font-size: 18px;
  }
  
  .module-info {
    font-size: 12px;
  }
  
  .stat-value {
    font-size: 24px;
  }
  
  .stat-label {
    font-size: 12px;
  }
  
  .user-stat-content {
    padding: 16px 12px;
  }
  
  .user-stat-icon {
    font-size: 36px;
    margin-right: 12px;
  }
  
  .user-stat-value {
    font-size: 20px;
  }
  
  .user-stat-label {
    font-size: 12px;
  }
}
</style>
