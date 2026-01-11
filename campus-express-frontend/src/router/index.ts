import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', requiresAuth: true }
      },
      {
        path: 'express',
        name: 'Express',
        component: () => import('@/views/express/index.vue'),
        meta: { title: '快递管理', requiresAuth: true, roles: ['admin', 'courier'] }
      },
      {
        path: 'pickup',
        name: 'Pickup',
        component: () => import('@/views/pickup/index.vue'),
        meta: { title: '取件记录', requiresAuth: true, roles: ['admin', 'courier'] }
      },
      {
        path: 'station',
        name: 'Station',
        component: () => import('@/views/station/index.vue'),
        meta: { title: '快递站管理', requiresAuth: true, roles: ['admin'] }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '用户管理', requiresAuth: true, roles: ['admin'] }
      },
      {
        path: 'shipping',
        name: 'Shipping',
        component: () => import('@/views/shipping/index.vue'),
        meta: { title: '寄件预约', requiresAuth: true, roles: ['user'] }
      },
      {
        path: 'shipping-admin',
        name: 'ShippingAdmin',
        component: () => import('@/views/shipping/admin.vue'),
        meta: { title: '寄件审核', requiresAuth: true, roles: ['admin', 'courier'] }
      },
      {
        path: 'notice',
        name: 'Notice',
        component: () => import('@/views/notice/index.vue'),
        meta: { title: '公告管理', requiresAuth: true, roles: ['admin'] }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  const userRole = localStorage.getItem('userRole')

  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
    return
  }

  if (to.meta.requiresAuth && !token) {
    next('/login')
    return
  }

  if (to.meta.roles && Array.isArray(to.meta.roles)) {
    const allowedRoles = to.meta.roles as string[]
    if (!allowedRoles.includes(userRole || '')) {
      next('/dashboard')
      return
    }
  }

  next()
})

export default router
