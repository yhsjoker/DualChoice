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
  <div class="teacher-page">
    <aside class="sidebar">
      <el-card class="card">
        <div class="card-content">
          <h2>身份：导师</h2>

          <img v-if="teacherInfo.imageUrl" :src="teacherInfo.imageUrl" class="image" alt="导师照片"/>

          <p>姓名：{{ teacherInfo.name }}</p>
          <p>学院：{{teacherInfo.college}}</p>
          <p>职称：{{ teacherInfo.title }}</p>
          <p>电话：{{ teacherInfo.phone }}</p>
          <p>邮箱：{{ teacherInfo.email }}</p>
          <br></br><br></br>
          <h3>当前选择轮次：第{{teacherInfo.round}}轮</h3>
        </div>
      </el-card>
    </aside>

    <!-- 右侧二级学科及学生列表 -->
    <main class="subjects-container">
      <el-card v-for="subject in teacherInfo.secondarySubjects" :key="subject.name" class="subject-card">
        <div slot="header" class="subject-header">
          <div class="centered-info">
            <h3>{{ subject.name }}</h3>
            <p class="quota-info">
              <span>学硕名额：{{ subject.academicQuota }}</span>
              <span>专硕名额：{{ subject.professionalQuota }}</span>
              <span>直博名额：{{ subject.phdQuota }}</span>
            </p>
          </div>
        </div>

        <el-table :data="subject.students" class="student-table">
          <el-table-column prop="studentName" label="姓名"></el-table-column>
          <el-table-column prop="graduateType" label="研究生类型"></el-table-column>
          <el-table-column prop="studentPhone" label="联系方式"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" size="small" @click="viewStudent(scope.row.studentId)">查看</el-button>
              <el-button
                  type="success"
                  size="small"
                  @click="selectStudent(scope.row, subject)"
                  :disabled="isQuotaAvailable(scope.row, subject) || scope.row.isSelected">
                {{ scope.row.isSelected ? '已选择该学生' : '选择' }}</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <div class="footer">
        <el-button type="primary" size="large"
                   :disabled="teacherInfo.round>=4"
                   @click="submitSelections">确认提交</el-button>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      teacherInfo: {
        // name: '神农氏',
        // college: '信息学院',
        // email: 'shennong@qq.com',
        // phone: '13379096699',
        // title: '教授',
        // round: '第一轮',
        // imageUrl: '',
        // secondarySubjects: [
        //   {
        //     name: "计算机科学与技术",
        //     academicQuota: 3,
        //     professionalQuota: 2,
        //     phdQuota: 0,
        //     students: [
        //       { id: 1, name: "李明", type: "直博型", volunteerLevel: "第一志愿", isSelected: false },
        //       { id: 2, name: "王红", type: "专业型", volunteerLevel: "第一志愿", isSelected: false },
        //       { id: 3, name: "赵刚", type: "直博型", volunteerLevel: "第一志愿", isSelected: false },
        //       { id: 4, name: "孙晓", type: "学术型", volunteerLevel: "第一志愿", isSelected: false },
        //     ]
        //   },
        //   {
        //     name: "电子工程",
        //     academicQuota: 2,
        //     professionalQuota: 3,
        //     phdQuota: 2,
        //     students: [
        //       { id: 5, name: "刘涛", type: "学术型", volunteerLevel: "第一志愿", isSelected: false },
        //       { id: 6, name: "周宇", type: "专业型", volunteerLevel: "第一志愿", isSelected: false },
        //       { id: 7, name: "陈建", type: "学术型", volunteerLevel: "第一志愿", isSelected: false },
        //       { id: 8, name: "杨丽", type: "专业型", volunteerLevel: "第一志愿", isSelected: false },
        //       { id: 9, name: "郑阳", type: "学术型", volunteerLevel: "第一志愿", isSelected: false },
        //     ]
        //   }
        // ]
      },
      selectedStudents: [],
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
            const token = sessionStorage.getItem('teacher_token');
            if (token) {
              config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
          },
          (error) => Promise.reject(error)
      );
    },

    async fetchTeacherInfo() {
      try {
        const response = await axios.get('/api/teacher/info');
        const data = response.data.data
        this.teacherInfo = {...this.teacherInfo, ...data};
      } catch (error) {
        this.$message.error('获取老师信息失败');
      }
    },
    viewStudent(studentId) {
      this.$router.push({ name: 'StudentForm', params: { studentId: studentId } });
    },
    selectStudent(student, subject) {
      let quotaField;
      switch(student.graduateType) {
        case '学术型':
          quotaField = 'academicQuota';
          break;
        case '专业型':
          quotaField = 'professionalQuota';
          break;
        case '直博型':
          quotaField = 'phdQuota';
          break;
        default:
          return;
      }
      if (subject[quotaField] > 0) {
        subject[quotaField]--;
        this.selectedStudents.push(student.studentId);
        student.isSelected = true;
      } else {
        this.$message.warning('该类型的名额已满，无法选择该学生');
      }
    },
    isQuotaAvailable(student, subject) {
      let quotaField;
      switch(student.graduateType) {
        case '学术型':
          quotaField = 'academicQuota';
          break;
        case '专业型':
          quotaField = 'professionalQuota';
          break;
        case '直博型':
          quotaField = 'phdQuota';
          break;
        default:
          return true;
      }
      return subject[quotaField] <= 0;
    },
    async submitSelections() {
      const submissionData = {
        secondarySubjects: this.teacherInfo.secondarySubjects.map(subject => ({
          disciplineName: subject.name,
          academicQuota: subject.academicQuota,
          professionalQuota: subject.professionalQuota,
          phdQuota: subject.phdQuota,
        })),
        selectedStudents: this.selectedStudents
      };

      console.log(submissionData);
      try {
        // 提交表单数据到后端
        await axios.put('/api/teacher/submitSelections', submissionData);
        this.$message.success('信息提交成功');
      } catch (error) {
        this.$message.error('提交失败，请重试');
      }
    }
  },
  created() {
    this.addRequestInterceptor();
    this.fetchTeacherInfo();
  },
};
</script>

<style scoped>
.teacher-page {
  display: flex;
  margin: 20px;
  font-family: 'Roboto', Arial, sans-serif; /* 统一字体 */
  min-height: 100vh;
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
}

.card-content {
  text-align: center;
}

.card-content h2 {
  color: #409EFF;
  margin-bottom: 50px;
}
.card-content h3 {
  color: #BC8F8F;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 50px;
}

.card-content p {
  font-size: 18px;
  color: #CD9B1D;
  margin-bottom: 10px;
}

.subjects-container {
  flex: 1;
}
.subject-card {
  margin-bottom: 20px;
}
.student-table {
  width: auto;
}
.footer {
  text-align: center;
}
.centered-info {
  text-align: center;
}
.quota-info span {
  margin-right: 20%; /* 增加每个 span 之间的间距 */
}

.quota-info span:last-child {
  margin-right: 0; /* 移除最后一个 span 的右边距 */
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

image {
  max-width: 100%;
  height: auto;
  margin-top: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
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