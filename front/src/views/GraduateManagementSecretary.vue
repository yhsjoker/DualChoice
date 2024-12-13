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
          @input="filterLists"
      >
      </el-input>
      <el-button type="text" class="login-link" @click="Logout">退出登录</el-button>
    </div>
  </header>

  <div class="page-container">
    <aside class="sidebar">
      <el-card class="card">
        <div class="card-content">
          <h2>身份：研究生管理秘书</h2>
          <br></br>
          <h3>学院：{{ collegeName }}</h3>
          <br></br><br></br>
          <div class="button-group">
            <el-button
                @click="handleMenuSelect('students')"
                :type="activeTab === 'students' ? 'primary' : 'default'"
                size="large"
                class="sidebar-button"
            >
              考生
            </el-button>
            <el-button
                @click="handleMenuSelect('teachers')"
                :type="activeTab === 'teachers' ? 'primary' : 'default'"
                size="large"
                class="sidebar-button"
            >
              导师
            </el-button>
          </div>
        </div>
      </el-card>
    </aside>

      <!-- 右侧内容区域 -->
      <main class="content">
        <!-- 考生列表 -->
        <div v-if="activeTab === 'students'">
          <el-table :data="pagedStudents" style="width: 800px; font-size: 16px" size="large">
            <el-table-column prop="name" label="姓名" width="200" />
            <el-table-column prop="examNumber" label="准考证号" width="250" />
            <el-table-column prop="category" label="考生类别" width="200" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button
                    type="primary"
                    @click="viewStudent(scope.row)"
                    size="large"
                >查看</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页组件 -->
          <div>
            <el-pagination
                style="justify-content:center"
                background
                layout="prev, pager, next"
                :page-size="pageSize"
                :total="filteredList.length"
                :current-page.sync="currentPage"
                @current-change="handleCurrentChange"
            >
            </el-pagination>
          </div>

        </div>

        <!-- 导师列表 -->
        <div v-else-if="activeTab === 'teachers'">
          <el-table :data="pagedTeachers" style="width: 800px; font-size: 16px" size="large">
            <el-table-column prop="name" label="姓名" width="200" />
            <el-table-column prop="title" label="职称" width="200" />
            <el-table-column prop="phone" label="电话" width="250" />
            <el-table-column label="资格" width="150">
              <template #default="scope">
                <div v-if="scope.row.qualification === null">
                  <el-button
                      type="success"
                      size="small"
                      @click="setQualification(scope.row, '是')"
                  >是</el-button>
                  <el-button
                      type="danger"
                      size="small"
                      @click="setQualification(scope.row, '否')"
                  >否</el-button>
                </div>
                <div v-else>
                  <el-tag
                      :type="scope.row.qualification === '是' ? 'success' : 'danger'"
                      size="large"
                  >{{ scope.row.qualification }}</el-tag>
                </div>
              </template>
            </el-table-column>
          </el-table>

          <div>
            <el-pagination
                style="justify-content:center;"
                background
                layout="prev, pager, next"
                :page-size="pageSize"
                :total="filteredList.length"
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
  name: 'GraduateSecretary',
  data() {
    return {
      activeTab: 'students', // 当前激活的选项卡
      collegeName: '', // 学院信息
      students: [], // 考生列表
      teachers: [], // 导师列表

      currentPage: 1,    // 新增：当前页码
      pageSize: 10,       // 新增：每页显示10条数据

      searchKeyword: '',
      filteredList: [],
      filteredStudents: [],
      filteredTeachers: [],
    };
  },

  computed: {
    // 新增：基于students和currentPage计算当前页的学生数据
    pagedStudents() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredStudents.slice(start, end);
    },
    pagedTeachers() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredTeachers.slice(start, end);
    }
  },

  methods: {
    filterStudents(){
      const keyword = this.searchKeyword.trim().toLowerCase();
      this.filteredStudents = this.students.filter(item => {
        return (
            item.name.toLowerCase().includes(keyword) ||
            item.examNumber.toLowerCase().includes(keyword) ||
            item.category.toLowerCase().includes(keyword)
        );
      });
    },
    filterTeachers(){
      const keyword = this.searchKeyword.trim().toLowerCase();
      this.filteredTeachers = this.teachers.filter(item => {
        return (
            item.name.toLowerCase().includes(keyword) ||
            item.title.toLowerCase().includes(keyword) ||
            item.phone.toLowerCase().includes(keyword)
        );
      });
    },
    filterLists() {
      if(this.activeTab === 'students'){
        this.filterStudents();
        this.filteredList = this.filteredStudents;
      }else{
        this.filterTeachers();
        this.filteredList = this.filteredTeachers;
      }
      this.currentPage = 1;
    },

    Logout() {
      this.$router.push('/');
    },
    // 添加请求拦截器，加入 token
    addRequestInterceptor() {
      axios.interceptors.request.use(
          (config) => {
            const token = sessionStorage.getItem('graduateManagementSecretary_token');
            if (token) {
              config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
          },
          (error) => Promise.reject(error)
      );
    },
    // 获取学院信息
    async getCollege() {
      try {
        const response = await axios.get('/api/graduateManagementSecretary/getCollege');
        this.collegeName = response.data.data.collegeName;
      } catch (error) {
        this.$message.error('获取学院信息失败');
      }
    },
    // 获取考生列表
    async fetchStudents() {
      try {
        const response = await axios.get(`/api/graduateManagementSecretary/students`);
        this.students = response.data.data.students;
        this.filteredStudents = this.students.slice();
        this.filteredList = this.filteredStudents;
        this.currentPage = 1;
      } catch (error) {
        this.$message.error('获取考生列表失败');
      }
    },
    // 获取导师列表
    async fetchTeachers() {
      try {
        const response = await axios.get(`/api/graduateManagementSecretary/teachers`);
        this.teachers = response.data.data.teachers;
        this.filteredTeachers = this.teachers.slice();
        this.filteredList = this.filteredTeachers;
        this.currentPage = 1;
      } catch (error) {
        this.$message.error('获取导师列表失败');
      }
    },
    // 菜单切换
    async handleMenuSelect(key) {
      this.activeTab = key;
      if (key === 'students') {
        await this.fetchStudents();
      } else if (key === 'teachers') {
        await this.fetchTeachers();
      }
    },
    // 查看学生详细信息
    viewStudent(student) {
      this.$router.push({ name: 'StudentForm', params: { studentId: student.id } });
    },
    // 设置导师资格
    async setQualification(teacher, qualification) {
      try {
        await axios.put(`/api/graduateManagementSecretary/teachers/setQualification`, {
          id: teacher.id,
          qualification: qualification,
        });
        teacher.qualification = qualification;
        this.$message.success('设置成功');
      } catch (error) {
        this.$message.error('设置导师资格失败');
      }
    },

    handleCurrentChange(page) {
      // 切换分页时更新当前页
      this.currentPage = page;
    }
  },
  created() {
    this.addRequestInterceptor();
    this.getCollege();
    this.fetchStudents();
  },
};
</script>

<style scoped>
.page-container {
  display: flex;
  justify-content: center;
  min-height: 100vh;
  width: 100%;
  background-color: #f0f2f5;
}

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

.sidebar-content p {
  font-size: 16px;
  color: #333333;
  margin: 5px 0;
}

.sidebar-button {
  width: 100%;
  margin-bottom: 20px;
  font-size: 20px; /* 增大字体 */
}

.content {
  flex: 1;
  display: flex;
  align-content: center;
  justify-content: center;
}

.el-table th,
.el-table td {
  font-size: 18px; /* 增大字体 */
  text-align: center;
}

.el-table-column {
  text-align: center;
}

.el-button {
  font-size: 18px; /* 增大按钮字体 */
}

.dialog-footer {
  text-align: right;
}

.button-group {
  display: flex;
  flex-direction: column;
  align-items: center;
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
