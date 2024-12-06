<template>
  <div class="teacher-page">
    <!-- 左侧老师信息栏 -->
    <div class="teacher-info">
      <el-card shadow="hover">
        <h2>{{ teacherInfo.name }}</h2>
        <p>学院：{{teacherInfo.college}}</p>
        <p>职称：{{ teacherInfo.title }}</p>
        <p>电话：{{ teacherInfo.phone }}</p>
        <p>邮箱：{{ teacherInfo.email }}</p>
        <p>当前选择轮次：{{teacherInfo.round}}</p>
      </el-card>
    </div>
    <!-- 右侧二级学科及学生列表 -->
    <div class="subjects-container">
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
          <el-table-column prop="name" label="姓名"></el-table-column>
          <el-table-column prop="type" label="研究生类型"></el-table-column>
          <el-table-column prop="volunteerLevel" label="志愿级别"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" size="small" @click="viewStudent(scope.row.id)">查看</el-button>
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
        <el-button type="primary" size="large" @click="submitSelections">确认提交</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      teacherInfo: {
        name: '神农氏',
        college: '信息学院',
        email: 'shennong@qq.com',
        phone: '13379096699',
        title: '教授',
        round: '第一轮',
        secondarySubjects: [
          {
            name: "计算机科学与技术",
            academicQuota: 3,
            professionalQuota: 2,
            phdQuota: 0,
            students: [
              { id: 1, name: "李明", type: "直博", volunteerLevel: "第一志愿", isSelected: false },
              { id: 2, name: "王红", type: "专硕", volunteerLevel: "第一志愿", isSelected: false },
              { id: 3, name: "赵刚", type: "直博", volunteerLevel: "第一志愿", isSelected: false },
              { id: 4, name: "孙晓", type: "学硕", volunteerLevel: "第一志愿", isSelected: false },
            ]
          },
          {
            name: "电子工程",
            academicQuota: 2,
            professionalQuota: 3,
            phdQuota: 2,
            students: [
              { id: 5, name: "刘涛", type: "学硕", volunteerLevel: "第一志愿", isSelected: false },
              { id: 6, name: "周宇", type: "专硕", volunteerLevel: "第一志愿", isSelected: false },
              { id: 7, name: "陈建", type: "学硕", volunteerLevel: "第一志愿", isSelected: false },
              { id: 8, name: "杨丽", type: "专硕", volunteerLevel: "第一志愿", isSelected: false },
              { id: 9, name: "郑阳", type: "学硕", volunteerLevel: "第一志愿", isSelected: false },
            ]
          }
        ]
      },
      selectedStudents: []
    };
  },
  methods: {
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
        // const response = await axios.get('/api/teacher/info');
        // const data = response.data.data
        // this.teacherInfo = {...this.teacherInfo, ...data};

      } catch (error) {
        this.$message.error('获取老师信息失败');
      }
    },
    viewStudent(studentId) {
      this.$router.push({ name: 'StudentForm', params: { id: studentId } });
    },
    selectStudent(student, subject) {
      let quotaField;
      switch(student.type) {
        case '学硕':
          quotaField = 'academicQuota';
          break;
        case '专硕':
          quotaField = 'professionalQuota';
          break;
        case '直博':
          quotaField = 'phdQuota';
          break;
        default:
          return;
      }
      if (subject[quotaField] > 0) {
        subject[quotaField]--;
        this.selectedStudents.push(student.id);
        student.isSelected = true;
      } else {
        this.$message.warning('该类型的名额已满，无法选择该学生');
      }
    },
    isQuotaAvailable(student, subject) {
      let quotaField;
      switch(student.type) {
        case '学硕':
          quotaField = 'academicQuota';
          break;
        case '专硕':
          quotaField = 'professionalQuota';
          break;
        case '直博':
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
          name: subject.name,
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
    // this.fetchTeacherInfo();
  },
};
</script>

<style scoped>
.teacher-page {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  margin: 20px;
  font-family: 'Roboto', Arial, sans-serif; /* 统一字体 */
}
.teacher-info {
  width: 15%;
  margin-left: 5% !important;
  margin-right: 2%;
}
.subjects-container {
  flex: 1;
}
.subject-card {
  margin-bottom: 20px;
}
.student-table {
  width: 100%;
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
</style>