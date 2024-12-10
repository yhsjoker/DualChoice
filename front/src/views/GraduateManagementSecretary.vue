<template>
  <div class="page-container">
    <div class="graduate-secretary">
      <!-- 左侧菜单栏 -->
      <div class="sidebar">
        <h2>学院：{{ college }}</h2>
        <div class="button-group">
        <el-button
            @click="handleMenuSelect('students')"
            :type="activeTab === 'students' ? 'primary' : 'default'"
            size="large"
            class="sidebar-button"
        >考生</el-button>
        </div>
        <div class="button-group">
        <el-button
            @click="handleMenuSelect('teachers')"
            :type="activeTab === 'teachers' ? 'primary' : 'default'"
            size="large"
            class="sidebar-button"
        >导师</el-button>
        </div>
      </div>

      <!-- 右侧内容区域 -->
      <div class="content">
        <!-- 考生列表 -->
        <div v-if="activeTab === 'students'">
          <el-table :data="students" style="width: 100%" size="large">
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

        </div>

        <!-- 导师列表 -->
        <div v-else-if="activeTab === 'teachers'">
          <el-table :data="teachers" style="width: 100%" size="large">
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
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'GraduateSecretary',
  data() {
    return {
      activeTab: '', // 当前激活的选项卡
      collegeName: '', // 学院信息
      students: [], // 考生列表
      teachers: [], // 导师列表
    };
  },
  methods: {
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
      } catch (error) {
        this.$message.error('获取考生列表失败');
      }
    },
    // 获取导师列表
    async fetchTeachers() {
      try {
        const response = await axios.get(`/api/graduateManagementSecretary/teachers`);
        this.teachers = response.data.data.teachers;
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
          teacherId: teacher.id,
          qualification: qualification,
        });
        teacher.qualification = qualification;
        this.$message.success('设置成功');
      } catch (error) {
        this.$message.error('设置导师资格失败');
      }
    },
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
  width: 100%;
}

.graduate-secretary {
  display: flex;
  width: 80%; /* 控制整体宽度，可根据需要调整 */
}

.sidebar {
  width: 250px; /* 加宽 */
  padding: 20px;
}

.sidebar h2 {
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
}

.sidebar-button {
  width: 100%;
  margin-bottom: 20px;
  font-size: 20px; /* 增大字体 */
}

.content {
  flex: 1;
  padding: 20px;
}

.el-table th,
.el-table td {
  font-size: 18px; /* 增大字体 */
  text-align: center;
}

::v-deep(.el-table-column) {
  text-align: center;
}

::v-deep(.el-button) {
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

</style>
