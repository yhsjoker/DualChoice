<template>
  <div class="page-container">

  <div class="re-exam-group">
    <h2>复试组负责的一级学科：{{ primarySubject }}</h2>
    <el-table :data="studentList" size="large" style="width: 100%">
      <el-table-column prop="name" label="姓名" width="220"></el-table-column>
      <el-table-column prop="examNumber" label="准考证号" width="250"></el-table-column>
      <el-table-column prop="studentType" label="考生类别" width="220"></el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button
              type="primary"
              @click="openDialog(scope.row)"
              size="mini"
          >填写复试信息</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 复试信息的对话框 -->
    <el-dialog
        v-model = "dialogVisible"
        :title="`填写 ${currentStudent.name} 的复试信息`"
        width="500px"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="150px">
        <el-form-item label="复试时间" prop="reExamTime">
          <el-date-picker
              v-model="formData.reExamTime"
              type="datetime"
              placeholder="请选择复试时间"
              :disabled="isReadOnly"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="复试地点" prop="reExamLocation">
          <el-input
              v-model="formData.reExamLocation"
              placeholder="请输入复试地点"
              :disabled="isReadOnly"
          ></el-input>
        </el-form-item>
        <el-form-item label="综合评价" prop="overallEvaluation">
          <el-input
              v-model="formData.overallEvaluation"
              placeholder="请输入综合评价"
              :disabled="isReadOnly"
          ></el-input>
        </el-form-item>
        <el-form-item label="外语听力及口语成绩" prop="englishScore">
          <el-input
              v-model="formData.englishScore"
              placeholder="请输入成绩"
              :disabled="isReadOnly"
          ></el-input>
        </el-form-item>
        <el-form-item label="专业知识测试成绩" prop="professionalScore">
          <el-input
              v-model="formData.professionalScore"
              placeholder="请输入成绩"
              :disabled="isReadOnly"
          ></el-input>
        </el-form-item>
        <el-form-item label="综合素质面试成绩" prop="interviewScore">
          <el-input
              v-model="formData.interviewScore"
              placeholder="请输入成绩"
              :disabled="isReadOnly"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="!isReadOnly">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </div>
    </el-dialog>
  </div>

  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ReExamGroup',
  data() {
    return {
      primarySubject: '计算机科学与技术', // 复试组负责的一级学科
      studentList: [
        {
          "id": "1",
          "name": "张三",
          "examNumber": "123456789",
          "studentType": "应届生"
        },
        {
          "id": "2",
          "name": "李四",
          "examNumber": "987654321",
          "studentType": ''
        }
      ], // 学生列表
      dialogVisible: false, // 对话框显示状态
      currentStudent: {}, // 当前选中的学生
      formData: {
        reExamTime: '',
        reExamLocation: '',
        overallEvaluation: '',
        englishScore: '',
        professionalScore: '',
        interviewScore: '',
      },
      isReadOnly: false, // 表单是否只读
      rules: {
        reExamTime: [{ required: true, message: '请选择复试时间', trigger: 'change' }],
        reExamLocation: [{ required: true, message: '请输入复试地点', trigger: 'blur' }],
        overallEvaluation: [{ required: true, message: '请输入综合评价', trigger: 'blur' }],
        englishScore: [{ required: true, message: '请输入外语成绩', trigger: 'blur' }],
        professionalScore: [{ required: true, message: '请输入专业知识成绩', trigger: 'blur' }],
        interviewScore: [{ required: true, message: '请输入面试成绩', trigger: 'blur' }],
      },
    };
  },
  methods: {
    addRequestInterceptor() {
      axios.interceptors.request.use(
          config => {
            const token = sessionStorage.getItem('interview_token');
            if (token) {
              config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
          },
          error => Promise.reject(error)
      );
    },
    // 获取复试组信息和学生列表
    async fetchGroupInfo() {
      try {
        const response = await axios.get('/api/interviewGroup/list');
        const data = response.data.data;
        this.primarySubject = data.primarySubject;
        this.studentList = data.studentList.map(student => {
          return {
            name: student.name,
            examNumber: student.examNumber,
            studentType: student.studentType || '暂未填写', // 如果为空，标注“暂未填写”
            id: student.id, // 用于后续请求
          };
        });
      } catch (error) {
        this.$message.error('获取复试组信息失败');
      }
    },
    // 打开对话框
    async openDialog(student) {
      this.currentStudent = student;
      this.dialogVisible = true;
      this.isReadOnly = false; // 默认可编辑
      // 初始化表单数据
      this.formData = {
        reExamTime: "2023-10-01 09:00:00",
        reExamLocation: "教室101",
        overallEvaluation: "优秀",
        englishScore: "90",
        professionalScore: "95",
        interviewScore: "93"
      };
      // try {
      //   // 从后端获取该学生的复试信息
      //   const response = await axios.get(`/api/interviewGroup/reExamInfo/${student.id}`);
      //   if (response.data.data) {
      //     Object.assign(this.formData, response.data.data);
      //     // 如果复试地点不为空，设置为只读模式
      //     if (this.formData.reExamLocation) {
      //       this.isReadOnly = true;
      //     }
      //   }
      // } catch (error) {
      //   this.$message.error('获取复试信息失败');
      // }
    },
    // 提交表单
    submitForm() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          try {
            // 提交数据到后端
            await axios.post(`/api/interviewGroup/updateReExamInfo/${this.currentStudent.id}`, this.formData);
            this.$message.success('复试信息提交成功');
            this.dialogVisible = false;
          } catch (error) {
            this.$message.error('提交失败，请重试');
          }
        } else {
          this.$message.warning('请完善表单信息');
        }
      });
    },
  },
  created() {
    this.addRequestInterceptor();
    this.fetchGroupInfo();
  },
};
</script>

<style scoped>
.re-exam-group {
  padding: 20px;
}

.dialog-footer {
  text-align: right;
}

h2 {
  margin-bottom: 20px;
  font-size: 24px;
}

::v-deep(.el-table th,.el-table td,.el-table-column) {
  font-size: 20px;
}

::v-deep(.el-table .el-button) {
  margin-right: 10px;
}

.page-container {
  display: flex;
  justify-content: center;
  width: 100%;
}
</style>
