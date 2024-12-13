import { createRouter, createWebHistory } from 'vue-router'

// 导入组件
import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/Login.vue'
import Student from "../views/Student.vue";
import InterviewGroup from "../views/InterviewGroup.vue";
import DisciplineHead from "../views/DisciplineHead.vue";
import GraduateManagementSecretary from "../views/GraduateManagementSecretary.vue";
import StudentForm from "../views/StudentForm.vue";
import Teacher from "../views/Teacher.vue";
import DrawLottery from "../views/DrawLottery.vue";
import DisciplineInfo from "../views/DisciplineInfo.vue";
import TeacherInfo from "../views/TeacherInfo.vue";
import GraduateManager from "../views/GraduateManager.vue";
import Supervisor from "../views/Supervisor.vue";
import DisciplineSecretary from "../views/DisciplineSecretary.vue";
// import TeacherInfo from '../views/TeacherInfo.vue';
// import Contact from '../views/Contact.vue';

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
    component: DisciplineHead,
    meta: { requiresAuth: true, role: 'discipline-head' }
  },
  {
    path: '/graduate-manage-group',
    name: 'GraduateManagementSecretary',
    component: GraduateManagementSecretary,
    meta: { requiresAuth: true, role: 'graduate-manage-group' }
  },
  {
    path: '/student-form/:studentId',
    name: 'StudentForm',
    component: StudentForm,
    meta: { requiresAuth: true, role: 'student-form' }
  },
  {
    path: '/teacher',
    name: 'Teacher',
    component: Teacher,
    meta: { requiresAuth: true, role: 'teacher' }
  },
  {
    path: '/draw-lottery',
    name: 'DrawLottery',
    component: DrawLottery,
    meta: { requiresAuth: true, role: 'draw-lottery' }
  },
  {
    path: '/discipline-info',
    name: 'DisciplineInfo',
    component: DisciplineInfo,
  },
  {
    path: '/teacher-info',
    name: 'teacher-info',
    component: TeacherInfo,
  },
  {
    path: '/graduate-manager',
    name: 'GraduateManager',
    component: GraduateManager,
    meta: { requiresAuth: true, role: 'graduate-manager' }
  },
  {
    path: '/supervisor',
    name: 'Supervisor',
    component: Supervisor,
    meta: { requiresAuth: true, role: 'supervisor' }
  },
  {
    path: '/disciplineSecretary',
    name: 'DisciplineSecretary',
    component: DisciplineSecretary,
    meta: { requiresAuth: true, role: 'disciplineSecretary' }
  }
  // {
  //   path: '/contact',
  //   name: 'contact',
  //   component: Contact,
  // },
]

// 创建并导出路由实例
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})


export default router
