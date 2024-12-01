import { createRouter, createWebHistory } from 'vue-router'

// 导入组件
import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/Login.vue'
import Student from "../views/Student.vue";
import InterviewGroup from "../views/InterviewGroup.vue";
import Supervisor from "../views/DisciplineHead.vue";
import GraduateManagementSecretary from "../views/GraduateManagementSecretary.vue";
import StudentForm from "../views/StudentForm.vue";

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/student',
    name: 'Student',
    component: Student,
    meta: { requiresAuth: true, role: 'student' }
  },
  {
    path: '/interview-group',
    name: 'InterviewGroup',
    component: InterviewGroup,
    meta: { requiresAuth: true, role: 'interview-group' }
  },
  {
    path: '/discipline-head',
    name: 'DisciplineHead',
    component: Supervisor,
    meta: { requiresAuth: true, role: 'discipline-head' }
  },
  {
    path: '/graduate-manage-group',
    name: 'GraduateManagementSecretary',
    component: GraduateManagementSecretary,
    meta: { requiresAuth: true, role: 'graduate-manage-group' }
  },
  {
    path: '/student-form',
    name: 'StudentForm',
    component: StudentForm,
    meta: { requiresAuth: true, role: 'student-form' }
  }
]

// 创建并导出路由实例
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
