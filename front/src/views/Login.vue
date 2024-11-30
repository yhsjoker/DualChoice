<template>
  <div class="login-container">
    <el-form :model="loginForm" @submit.native.prevent="handleLogin">
      <el-form-item>
        <el-input v-model="loginForm.username" placeholder="用户名" size="large"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="loginForm.password" placeholder="密码" type="password" size="large"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleLogin" size="large">登录</el-button>
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
        userName: '',
        password: ''
      }
    }
  },
  methods: {
    async handleLogin() {
      try {
        //console.log(this.loginForm)
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
        } else {
          this.$router.push('/dashboard');
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

.el-input input {
  font-size: 16px;
  padding: 10px 15px;
}

.el-button button{
  width: 100%;
  font-size: 18px;
  padding: 12px;
}
</style>
