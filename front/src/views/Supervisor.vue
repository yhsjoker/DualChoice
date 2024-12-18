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
          @input="filterStudents"
      >
      </el-input>
      <el-button type="text" class="login-link" @click="handleLogout">退出登录</el-button>
    </div>
  </header>

  <div class="page-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <el-card class="card">
        <div class="card-content">
          <h2>身份：监督人员</h2>
          <br></br>
          <h3>学院：{{ collegeName }}</h3>
        </div>
      </el-card>
    </aside>

    <!-- 主内容区域 -->
    <main class="main-content">
      <div>
        <el-table
            :data="pagedData"
            size="medium"
            stripe
            border
            style="width:840px"
            class="student-table"
        >
          <el-table-column prop="name" label="姓名" width="160"></el-table-column>
          <el-table-column prop="examNumber" label="准考证号" width="180"></el-table-column>
          <el-table-column prop="graduateType" label="研究生类型" width="160"></el-table-column>
          <el-table-column prop="phone" label="电话" width="180"></el-table-column>
          <el-table-column prop="volunteerStatus" label="录取状态" width="160">
            <template #default="scope">
              <el-tag :type="getTagType(scope.row.volunteerStatus)">
                {{ scope.row.volunteerStatus }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>

        <div style="display:flex; justify-content:center; margin-top:20px;">
          <el-pagination
              background
              layout="prev, pager, next"
              :page-size="pageSize"
              :total="filteredAdmissionList.length"
              :current-page.sync="currentPage"
              @current-change="handleCurrentChange"
          >
          </el-pagination>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Supervisor',
  data() {
    return {
      collegeName: '', // 学院名称
      admissionList: [
        // { name: '张伟', examNumber: '202301001', graduateType: '硕士', phone: '13800138000', volunteerStatus: '已录取' },
        // { name: '李娜', examNumber: '202301002', graduateType: '博士', phone: '13900139000', volunteerStatus: '提交阶段' },
        // { name: '王强', examNumber: '202301003', graduateType: '硕士', phone: '13700137000', volunteerStatus: '未录取' },
        // { name: '陈丽', examNumber: '202301004', graduateType: '博士', phone: '13600136000', volunteerStatus: '审查阶段' },
        // { name: '张伟', examNumber: '202301001', graduateType: '硕士', phone: '13800138000', volunteerStatus: '已录取' },
        // { name: '李娜', examNumber: 'aaa', graduateType: '博士', phone: '13900139000', volunteerStatus: '提交阶段' },
        // { name: '王强', examNumber: '202AAA1003', graduateType: '硕士', phone: '13700137000', volunteerStatus: '未录取' },
        // { name: '陈丽', examNumber: '202301004', graduateType: '博士', phone: '13600136000', volunteerStatus: '审查阶段' },
        // { name: '张伟', examNumber: '202301001', graduateType: '硕士', phone: '13800138000', volunteerStatus: '已录取' },
        // { name: '李娜', examNumber: '202301002', graduateType: '博士', phone: '13900139000', volunteerStatus: '提交阶段' },
        // { name: '王强', examNumber: '202301003', graduateType: '硕士', phone: '13700137000', volunteerStatus: '未录取' },
        // { name: '陈丽', examNumber: '202301004', graduateType: '博士', phone: '13600136000', volunteerStatus: '审查阶段' },
      ], // 学生状态列表，每条数据包括：姓名、准考证号、研究生类型、电话、录取状态

      currentPage: 1, // 当前页
      pageSize: 10,   // 每页显示10条数据

      searchKeyword: '', // 搜索关键词
      filteredAdmissionList: [], // 过滤后的录取列表
    };
  },
  computed: {
    pagedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredAdmissionList.slice(start, end);
    }
  },
  methods: {
    handleLogout() {
      // 登出逻辑
      this.$router.push('/');
    },

    addRequestInterceptor() {
      axios.interceptors.request.use(
          config => {
            const token = sessionStorage.getItem('supervisor_token');
            if (token) {
              config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
          },
          error => Promise.reject(error)
      );
    },

    // 获取学院名称和学生状态
    async fetchAdmissionInfo() {
      try {
        const response = await axios.get('/api/supervisor/studentStatus');
        const data = response.data.data;
        this.collegeName = data.collegeName;
        this.admissionList = data.admissionList.map(item => {
          return {
            name: item.name,
            examNumber: item.examNumber,
            graduateType: item.graduateType,
            phone: item.phone,
            volunteerStatus: item.volunteerStatus
          };
        });
        this.filteredAdmissionList = this.admissionList;
        this.currentPage = 1;
      } catch (error) {
        this.$message.error('获取学生状态信息失败');
      }
    },

    handleCurrentChange(page) {
      this.currentPage = page;
    },

    getTagType(status) {
      switch(status) {
        case '已录取':
          return 'success';
        case '未录取':
          return 'danger';
        default:
          return 'warning';
      }
    },

    filterStudents() {
      const keyword = this.searchKeyword.trim().toLowerCase();
      if (!keyword) {
        this.filteredAdmissionList = this.admissionList;
      } else {
        this.filteredAdmissionList = this.admissionList.filter(student => {
          return (
              student.name.toLowerCase().includes(keyword) ||
              student.examNumber.toLowerCase().includes(keyword) ||
              student.graduateType.toLowerCase().includes(keyword) ||
              student.phone.toLowerCase().includes(keyword) ||
              student.volunteerStatus.toLowerCase().includes(keyword)
          );
        });
      }
      this.currentPage = 1; // 重置到第一页
    }
  },
  created() {
    this.addRequestInterceptor();
    this.fetchAdmissionInfo();
    this.filteredAdmissionList = this.admissionList;
    this.currentPage=1;
  },
};
</script>

<style scoped>
.page-container {
  display: flex;
  min-height: 100vh;
  background-color: #f0f2f5;
}

/* 侧边栏样式 */
.sidebar {
  width: 250px;
  background-color: #ffffff;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.card {
  border: none;
  margin-bottom: 30px;
}

.card-content {
  text-align: center;
}

.card-content h2 {
  color: #409EFF;
  margin-bottom: 10px;
}

.card-content h3 {
  font-size: 20px;
}

.subject-tag {
  font-size: 20px;
}

/* 主内容区域样式 */
.main-content {
  flex: 1;
  display: flex;
  align-content: center;
  justify-content: center;
}

/* 表格样式 */
.student-table {
  justify-content: center;
  background-color: #ffffff;
  border-radius: 8px;
  overflow: hidden;
  font-family: 'Microsoft YaHei', 'Helvetica Neue', Arial, sans-serif;
  font-size: 16px;
}

.student-table .el-table__header {
  background-color: #409EFF;
  color: #ffffff;
}

.student-table .el-table__header th {
  font-size: 18px;
  font-weight: bold;
  font-family: 'Microsoft YaHei', 'Helvetica Neue', Arial, sans-serif;
  text-align: center;
}

.student-table .el-table__body tr:nth-child(odd) {
  background-color: #f9fafc;
}

.student-table {
  border-radius: 4px;
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
  text-decoration: none;
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
  color: #00C5CD;
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

/* 响应式设计 */
@media (max-width: 1024px) {
  .sidebar {
    display: none;
  }

  .main-content {
    padding: 20px;
  }
}

@media (max-width: 768px) {
  .main-content {
    padding: 10px;
  }

  .student-table {
    width: 100%;
  }

  .el-table th,
  .el-table td {
    font-size: 14px;
  }

  .navbar-menu {
    margin-left: 20px;
  }

  .navbar-item {
    margin-left: 20px;
    font-size: 16px;
  }
}

::v-deep .el-table th {
  font-size: 20px; /* 你可以根据需要调整字体大小 */
  color: #3c8dbc;
  text-align: center;
}

::v-deep .el-table .cell{
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
