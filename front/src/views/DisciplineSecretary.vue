<template>
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
      <el-button type="text" class="login-link" @click="Logout">退出登录</el-button>
    </div>
  </header>

  <div class="discipline-secretary-page">
    <aside class="sidebar">
      <el-card class="sidebar-card">
        <div class="sidebar-content">
          <h2>身份：学科秘书</h2>
          <br/>
          <h3>负责的一级学科：</h3>
          <el-tag type="primary" class="subject-tag">{{ primarySubject }}</el-tag>
          <br/><br/>
          <!-- 新增的抽签阶段按钮 -->
          <el-button type="primary" class="lottery-button" @click="goToLottery">抽签阶段</el-button>
        </div>
      </el-card>
    </aside>

    <!-- 主内容区域 -->
    <main class="main-content">
      <!-- 两个卡片 -->
      <div class="cards-container">
        <!-- 考生进度提醒卡片 -->
        <el-card shadow="never" class="progress-card">
          <div class="card-header">
            <h3>考生进度提醒</h3>
          </div>
          <div class="button-group">
            <el-button type="primary" size="large">提交阶段通知</el-button>
            <el-button type="primary" size="large">审查阶段通知</el-button>
            <el-button type="primary" size="large">已录取通知</el-button>
            <el-button type="primary" size="large">未录取通知</el-button>
          </div>
        </el-card>

        <!-- 老师进度提醒卡片 -->
        <el-card shadow="never" class="progress-card">
          <div class="card-header">
            <h3>老师进度提醒</h3>
          </div>
          <div class="button-group">
            <el-button type="success" size="large">录取阶段通知</el-button>
            <el-button type="success" size="large">抽签阶段通知</el-button>
          </div>
        </el-card>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      primarySubject: '', // 一级学科名称
      searchKeyword: '',  // 搜索关键词（如需要）
    };
  },
  methods: {
    Logout() {
      this.$router.push('/');
    },
    addRequestInterceptor() {
      axios.interceptors.request.use(
          (config) => {
            const token = sessionStorage.getItem('disciplineSecretary_token');
            if (token) {
              config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
          },
          (error) => Promise.reject(error)
      );
    },
    async fetchSubjectInfo() {
      try {
        const response = await axios.get('/api/disciplineSecretary/info');
        const data = response.data.data;
        this.primarySubject = data.primarySubject;
      } catch (error) {
        this.$message.error('获取一级学科信息失败');
      }
    },
    goToLottery() {
      this.$router.push('/draw-lottery');
    }
  },
  created() {
    this.addRequestInterceptor();
    this.fetchSubjectInfo();
  },
};
</script>

<style scoped>
.discipline-secretary-page {
  display: flex;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.sidebar {
  width: 250px;
  background-color: #ffffff;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.sidebar-card {
  border: none;
}

.sidebar-content {
  text-align: center;
}

.sidebar-content h2 {
  color: #409EFF;
  margin-bottom: 10px;
}

.sidebar-content h3 {
  font-size: 20px;
}

.subject-tag {
  font-size: 20px;
  margin-top: 10px;
}

/* 抽签阶段按钮 */
.lottery-button {
  margin-top: 30px;
  width: 100%;
  font-size: 18px;
}

.main-content {
  flex: 1;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding: 30px;
}

.cards-container {
  display: flex;
  flex-direction: row;
  gap: 40px;
}

.progress-card {
  width: 400px;
  background: #ffffff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.card-header {
  text-align: center;
  margin-bottom: 20px;
}

.card-header h3 {
  font-size: 22px;
  margin: 0;
  color: #333;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.button-group .el-button {
  margin: 0;
}

/* 顶部导航栏样式 */
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
</style>
