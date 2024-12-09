<template>
  <el-menu
      class="nav-bar"
      mode="horizontal"
      background-color="#FFFFFF"
      text-color="#333333"
      active-text-color="#409EFF"
      router
  >

    <el-menu-item index="/subject-info" class="custom-menu-item subject-info">学科信息</el-menu-item>
    <el-menu-item index="/teacher-info" class="custom-menu-item teacher-info">导师信息</el-menu-item>

    <el-menu-item index="logout" class="logout-button" @click="handleLogout">
      退出登录
    </el-menu-item>
  </el-menu>

  <div class="login-container">
    <el-form :model="loginForm" @submit.native.prevent="handleLogin">
      <el-form-item>
        <el-input v-model="loginForm.username" placeholder="用户名" size="large"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="loginForm.password" placeholder="密码" type="password" size="large"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleLogin" size="large" style="width: 100%">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    async handleLogout() {
      this.$router.push('/');
    },

    async handleLogin() {
      try {
        const response = await axios.post('/api/user/login', this.loginForm);
        const {user_identity, token} = response.data.data
        console.log(response.data.data)
        if (user_identity === 'Student') {
          sessionStorage.setItem('student_token', token);
          sessionStorage.setItem('student_identity', user_identity);
          this.$router.push('/student');
        } else if (user_identity === 'InterviewGroup') {
          sessionStorage.setItem('interview_token', token);
          sessionStorage.setItem('interview_identity', user_identity);
          this.$router.push('/interview-group');
        } else if (user_identity === 'DisciplineHead'){
          sessionStorage.setItem('disciplineHead_token', token);
          sessionStorage.setItem('disciplineHead_identity', user_identity);
          this.$router.push('/discipline-head');
        } else if(user_identity === 'GraduateManagementSecretary'){
          sessionStorage.setItem('graduateManagementSecretary_token', token);
          sessionStorage.setItem('graduateManagementSecretary_identity', user_identity);
          this.$router.push('/graduate-manage-group');
        } else if(user_identity === 'Teacher'){
          sessionStorage.setItem('teacher_token', token);
          sessionStorage.setItem('teacher_identity', user_identity);
          this.$router.push('/teacher');
        }
      } catch (error) {
        console.log(error)
        this.$message.error('登录失败，请检查用户名和密码。')
      }
    }
  }
}
</script>

<style>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.el-form form{
  width: 400px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-radius: 12px;
  background-color: #ffffff;
}

.nav-bar {
  width: 100%;
  border-bottom: 2px solid #1E90FF !important; /* 下边框 */
}

/* 自定义菜单项样式 */
.custom-menu-item {
  text-align: center;
  font-size: 18px !important; /* 调整字体大小 */
  font-family: '楷体', sans-serif;
  font-weight: bold !important;
}

/* 学科信息的特定样式 */
.subject-info {
  color: #FF69B4 !important; /* 例如，粉色 */
  margin-left: 25% !important;
}

/* 导师信息的特定样式 */
.teacher-info {
  color: #1E90FF !important; /* 例如，蓝色 */
  margin-left: 15% !important;
}

/* 退出登录按钮样式 */
.logout-button {
  cursor: pointer;
  margin-left: 15% !important;
  font-family: '楷体', sans-serif;
  font-size: 18px !important;
  font-weight: bold !important;
}
</style>