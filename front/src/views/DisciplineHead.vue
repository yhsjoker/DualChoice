<template>
  <header class="navbar">
    <div class="navbar-left">
      <img src="@/assets/bjfu-logo.png" alt="北京林业大学logo" class="logo" />

      <div class="navbar-titles">
        <span class="navbar-title">北京林业大学</span>
        <span class="navbar-subtitle">研究生导师双选系统</span>
      </div>

      <div class="navbar-menu">
        <router-link class="navbar-item" to="/school-intro">学校简介</router-link>
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
  <div class="discipline-head-page">

    <aside class="sidebar">
      <el-card class="sidebar-card">
        <div class="sidebar-content">
          <h2>身份：学科负责人</h2>
          <br></br>
          <h3>负责的一级学科</h3>
          <el-tag type="primary" class="subject-tag">{{ primarySubject }}</el-tag>
          <br></br><br></br><br></br>
          <h3>名额指标：<strong>{{ quotaIndicator }}</strong></h3>
        </div>
      </el-card>
    </aside>

    <!-- 二级学科和导师信息 -->
    <main class="main-content">
    <div class="content">
      <el-card
          shadow="never"
          class="secondary-card"
          v-for="(item, index) in quota"
          :key="index"
      >
        <div class="card-header">
          <h3>{{ item.secondarySubjects }}</h3>
        </div>
        <el-table
            :data="item.teacherQuota"
            class="center-table full-width-table"
            border
            stripe
        >
          <el-table-column prop="teacherName" label="导师姓名" width="264px"
                           align="center"
                           header-align="center"
          ></el-table-column>

          <el-table-column label="学硕名额" width="261px" class-name="cell-center">
            <template #default="scope">
              <div class="cell-content">
              <el-input-number
                  v-model.number="scope.row.academicQuota"
                  :disabled="isReadOnly"
                  :min="0"
                  size="small"
              ></el-input-number>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="专硕名额" width="261px" class-name="cell-center">
            <template #default="scope">
              <div class="cell-content">
              <el-input-number
                  v-model.number="scope.row.professionalQuota"
                  :disabled="isReadOnly"
                  :min="0"
                  size="small"
                  class-name="cell-center"
              ></el-input-number>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="直博名额" width="261px">
            <template #default="scope">
              <div class="cell-content">
              <el-input-number
                  v-model.number="scope.row.phdQuota"
                  :disabled="isReadOnly"
                  :min="0"
                  size="small"
                  class-name="cell-center"
              ></el-input-number>
              </div>
            </template>
          </el-table-column>

        </el-table>
      </el-card>
    </div>

    <!-- 提交按钮 -->
    <div class="footer" v-if="!isReadOnly">
      <el-button type="primary" @click="submitQuota" size="large"
      >提交名额分配</el-button
      >
    </div>

    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      primarySubject: '', // 一级学科名称（字符串）
      quotaIndicator: 0, // 名额指标（数字）
      quota:[],
      isReadOnly: false, // 页面是否只读
      searchKeyword: '',
    };
  },
  methods: {
    Logout() {
      this.$router.push('/');
    },
    addRequestInterceptor() {
      axios.interceptors.request.use(
          (config) => {
            const token = sessionStorage.getItem('disciplineHead_token');
            if (token) {
              config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
          },
          (error) => Promise.reject(error)
      );
    },
    // 获取学科负责人信息
    async fetchSubjectInfo() {
      try {
        const response = await axios.get('/api/disciplineHead/info');
        const data = response.data.data;
        this.primarySubject = data.primarySubject;
        this.quotaIndicator = data.quotaIndicator;
        this.quota = data.quota;
        // this.secondarySubjects = data.secondarySubjects;
        // this.teacherQuota = data.teacherQuota;

        // 检查是否需要设置为只读
        // this.checkQuotaEquality();
      } catch (error) {
        this.$message.error('获取学科信息失败');
      }
    },
    // // 更新总名额数并检查是否只读
    // updateTotalQuota() {
    //   this.checkQuotaEquality();
    // },
    // // 计算总名额并检查是否等于指标
    // checkQuotaEquality() {
    //   const totalQuota = this.calculateTotalQuota();
    //   this.isReadOnly = totalQuota === this.quotaIndicator;
    // },
    // // 计算当前页面的总名额
    calculateTotalQuota() {
      let total = 0;
      this.quota.forEach((subject) => {
        subject.teacherQuota.forEach((teacher) => {
          total +=
              (teacher.academicQuota || 0) +
              (teacher.professionalQuota || 0) +
              (teacher.phdQuota || 0);
        });
      });
      return total;
    },
    // 提交名额分配
    async submitQuota() {
      const totalQuota = this.calculateTotalQuota();
      if (totalQuota !== this.quotaIndicator) {
        const difference = totalQuota - this.quotaIndicator;
        if (difference > 0) {
          this.$message.error(
              `当前名额数超出总指标 ${difference} 个，请调整后再提交`
          );
        } else {
          this.$message.error(
              `当前名额数相差总指标 ${-difference} 个，请调整后再提交`
          );
        }
        return;
      }

      try {
        await axios.put('/api/disciplineHead/submitQuota', {
          primarySubject: this.primarySubject,
          quota: this.quota,
        });
        this.$message.success('名额分配提交成功');
        this.isReadOnly = true;
      } catch (error) {
        this.$message.error('提交失败，请重试');
      }
    },
  },
  created() {
    this.addRequestInterceptor();
    this.fetchSubjectInfo();
  },
};
</script>

<style scoped>
.discipline-head-page {
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
}

.main-content {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
}

.content {

  display: flex;
  flex-direction: column;
  gap: 20px;
}

.full-width-table {
  width: 100%;
}

.secondary-card {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px; /* 减少间距 */
  box-shadow: 0 2px 10px rgba(0,0,0,0.1); /* 增加阴影 */
}

.card-header {
  margin-bottom: 10px;
  text-align: center;
}

.card-header h3 {
  font-size: 20px;
  margin: 0;
}

::v-deep(.el-table) {
  table-layout: fixed;
}

::v-deep(.el-table th, .el-table td) {
  text-align: center;
}

.footer {
  text-align: center;
  margin-top: 20px;
}

::v-deep(.el-button) {
  width: auto; /* 自适应宽度 */
  padding: 10px 20px;
  font-size: 16px;
}

::v-deep(.cell-center) {
  text-align: center;
  vertical-align: middle;
}

.cell-content {
  display: flex;
  align-items: center;
  justify-content: center;
}

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

::v-deep .el-table th {
  font-size: 18px; /* 你可以根据需要调整字体大小 */
  color: #3c8dbc;
  text-align: center;
}

::v-deep .el-table .cell{
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
