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
          <h2>身份：研究生主管领导</h2>
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
            style="width:1040px"
            class="student-table"
        >
          <el-table-column prop="name" label="姓名" width="160"></el-table-column>
          <el-table-column prop="phone" label="联系方式" width="180"></el-table-column>
          <el-table-column prop="discipline" label="录取专业" width="200"></el-table-column>
          <el-table-column prop="graduateType" label="研究生类别" width="160"></el-table-column>
          <el-table-column prop="teacherName" label="录取导师" width="160"></el-table-column>
          <el-table-column prop="teacherPhone" label="导师电话" width="180"></el-table-column>
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
  data() {
    return {
      collegeName: '', // 学院名称
      admissionList: [
        // { name: '张三', phone: '13812345678', discipline: '计算机科学与技术', graduateType: '硕士', teacherName: '李四', teacherPhone: '13987654321' },
        // { name: '李四', phone: '13987654321', discipline: '软件工程', graduateType: '博士', teacherName: '王五', teacherPhone: '13876543210' },
        // { name: '王五', phone: '13876543210', discipline: '信息安全', graduateType: '硕士', teacherName: '赵六', teacherPhone: '13765432109' },
        // { name: '赵六', phone: '13765432109', discipline: '数据科学', graduateType: '博士', teacherName: '钱七', teacherPhone: '13654321098' },
        // { name: '钱七', phone: '13654321098', discipline: '人工智能', graduateType: '硕士', teacherName: '孙八', teacherPhone: '13543210987' },
        // { name: '孙八', phone: '13543210987', discipline: '物联网', graduateType: '博士', teacherName: '周九', teacherPhone: '13432109876' },
        // { name: '周九', phone: '13432109876', discipline: '网络工程', graduateType: '硕士', teacherName: '吴十', teacherPhone: '13321098765' },
        // { name: '吴十', phone: '13321098765', discipline: '电子信息', graduateType: '博士', teacherName: '郑十一', teacherPhone: '13210987654' },
        // { name: '郑十一', phone: '13210987654', discipline: '通信工程', graduateType: '硕士', teacherName: '王十二', teacherPhone: '13109876543' },
        // { name: '王十二', phone: '13109876543', discipline: '自动化', graduateType: '博士', teacherName: '李十三', teacherPhone: '13098765432' },
        // { name: '吴十', phone: '13321098765', discipline: '电子信息', graduateType: '博士', teacherName: '郑十一', teacherPhone: '13210987654' },
        // { name: '郑十一', phone: '13210987654', discipline: '通信工程', graduateType: '硕士', teacherName: '王十二', teacherPhone: '13109876543' },
        // { name: '王十二', phone: '13109876543', discipline: '自动化', graduateType: '博士', teacherName: '李十三', teacherPhone: '13098765432' },

      ], // 录取情况列表，每条数据包括：姓名、联系方式、录取专业、研究生类别、录取导师、导师电话等

      currentPage: 1, // 当前页
      pageSize: 10,   // 每页显示10条数据

      filteredAdmissionList: [],
      searchKeyword: '',
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
    filterStudents() {
      const keyword = this.searchKeyword.toLowerCase();
      this.filteredAdmissionList = this.admissionList.filter(item => {
        return (
            item.name.toLowerCase().includes(keyword) ||
            item.phone.toLowerCase().includes(keyword) ||
            item.discipline.toLowerCase().includes(keyword) ||
            item.graduateType.toLowerCase().includes(keyword) ||
            item.teacherName.toLowerCase().includes(keyword) ||
            item.teacherPhone.toLowerCase().includes(keyword)
        );
      });
      this.currentPage = 1; // 搜索后重置当前页
    },

    handleLogout() {
      // 登出逻辑
      this.$router.push('/');
    },

    addRequestInterceptor() {
      axios.interceptors.request.use(
          config => {
            const token = sessionStorage.getItem('graduateManager_token');
            if (token) {
              config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
          },
          error => Promise.reject(error)
      );
    },

    // 获取学院名称和录取情况
    async fetchAdmissionInfo() {
      try {
        const response = await axios.get('/api/graduateManager/admission');
        const data = response.data.data;
        this.collegeName = data.collegeName;
        this.admissionList = data.admissionList.map(item => {
          return {
            name: item.name,
            phone: item.phone,
            discipline: item.discipline,
            graduateType: item.graduateType,
            teacherName: item.teacherName,
            teacherPhone: item.teacherPhone
          };
        });
        this.filteredAdmissionList=this.admissionList;
        this.currentPage = 1;
      } catch (error) {
        this.$message.error('获取录取信息失败');
      }
    },

    handleCurrentChange(page) {
      this.currentPage = page;
    }
  },
  created() {
    this.addRequestInterceptor();
    this.fetchAdmissionInfo();
    this.filteredAdmissionList=this.admissionList;
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
