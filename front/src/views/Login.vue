<template>
  <div class="page-container">
    <!-- 导航栏 -->
    <header class="navbar">
      <div class="navbar-left">
        <img src="@/assets/bjfu-logo.png" alt="北京林业大学logo" class="logo" />

        <div class="navbar-titles">
          <span class="navbar-title">北京林业大学</span>
          <span class="navbar-subtitle">研究生导师双选系统</span>
        </div>

        <div class="navbar-menu">
          <a class="navbar-item" href="http://www.bjfu.edu.cn/index.htm" target="_blank" rel="noopener noreferrer">学校简介</a>
          <router-link class="navbar-item" to="/discipline-info">学科信息</router-link>
          <router-link class="navbar-item" to="/teacher-info">导师信息</router-link>
        </div>
      </div>
      <div class="navbar-right">
        <el-input
            class="search-input"
            placeholder="搜索..."
            suffix-icon="el-icon-search"
            v-model="searchKeyword"
        >
        </el-input>
        <el-button type="text" class="login-link" @click="goLogin">登录</el-button>
      </div>
    </header>

    <!-- 中间内容区域 -->
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
      },
      searchKeyword: '', // 新增
    }
  },
  methods: {
    goLogin() {
      this.$router.push('/')
    },

    async handleLogin() {
      try {
        const response = await axios.post('/api/user/login', this.loginForm);
        const {user_identity, token} = response.data.data

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
        } else if(user_identity === 'GraduateManager'){
          sessionStorage.setItem('graduateManager_token', token);
          sessionStorage.setItem('graduateManager_identity', user_identity);
          this.$router.push('/graduate-manager');
        } else if(user_identity === 'Supervisor'){
          sessionStorage.setItem('supervisor_token', token);
          sessionStorage.setItem('supervisor_identity', user_identity);
          this.$router.push('/supervisor');
        } else if(user_identity === 'DisciplineSecretary'){
          sessionStorage.setItem('disciplineSecretary_token', token);
          sessionStorage.setItem('disciplineSecretary_identity', user_identity);
          this.$router.push('/disciplineSecretary');
        }
      } catch (error) {
        console.log(error)
        this.$message.error('登录失败，请检查用户名和密码。')
      }
    }
  }
}
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 导航栏样式 */
.navbar {
  height: 70px;
  display: flex;
  align-items: center;
  padding: 0 50px;
  background-color: #FFFFE0;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  justify-content: space-between;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.navbar-menu {
  display: flex;
  margin-left: 40px;
}

.logo {
  height: 50px;
  margin-right: 20px;
}

.navbar-titles {
  display: flex;
  flex-direction: column;
}

.navbar-title {
  font-size: 20px;
  font-weight: bold;
  margin-right: 10px;
  color: #228B22;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.navbar-subtitle {
  font-size: 14px;
  color: #666666;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.navbar-item {
  font-size: 20px;
  margin-left: 140px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #333333;
  cursor: pointer;
  transition: color 0.3s, background-color 0.3s;
  text-decoration:none;
  font-weight: bold;
  position: relative;
}

.navbar-item::after {
  content: '';
  display: block;
  width: 0;
  height: 2px;
  background: #00C5CD;
  transition: width 0.3s;
  position: absolute;
  bottom: -5px;
  left: 0;
}

.navbar-item:hover {
  color:#00C5CD;
}

.navbar-item:hover::after {
  width: 100%;
}

.navbar-right {
  display: flex;
  align-items: center;
}

.search-input {
  margin-right: 20px;
}

.login-link {
  color: #409EFF;
  font-weight: bold;
}

.login-link:hover {
  color: #00C5CD;
}



.login-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-form form {
  width: 500px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-radius: 12px;
  background-color: #ffffff;
}

.el-input input {
  border: 1px solid #ccc;
  border-radius: 4px;
}

.el-input.is-focused input {
  border-color: #409EFF;
}
</style>
