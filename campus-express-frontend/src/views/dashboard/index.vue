<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h2>数据统计</h2>
      <p class="module-info">毕业设计模块：数据统计模块</p>
    </div>
    
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { expressApi } from '@/api/express'
import { pickupApi } from '@/api/pickup'
import type { Pickup } from '@/types'

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

const recentPickups = ref<Pickup[]>([])

const fetchStatistics = async () => {
  try {
    const response = await expressApi.getStatistics()
    if (response.code === 200 && response.data) {
      stats.value.totalExpress = response.data.totalCount
      stats.value.pending = response.data.pendingCount
      stats.value.picked = response.data.pickedCount
      stats.value.stationCount = response.data.stationCount
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
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

onMounted(() => {
  fetchStatistics()
  fetchRecentPickups()
  initPieChart()
  initBarChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  pieChart?.dispose()
  barChart?.dispose()
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.dashboard-header {
  margin-bottom: 20px;
}

.dashboard-header h2 {
  margin-bottom: 5px;
  color: #303133;
}

.module-info {
  font-size: 14px;
  color: #909399;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.stat-content {
  padding: 10px 0;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  height: 400px;
}

.chart {
  width: 100%;
  height: 320px;
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
