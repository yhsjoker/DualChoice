<template>
  <el-container class="student-info">
    <el-header class="info-header">
      <h2>{{ formData.name }}的复试信息</h2>
    </el-header>
    <el-main>
      <el-form :model="formData" label-width="120px">
        <!-- 所有表单项，设置为只读 -->
        <el-form-item label="复试学科专业">
          <el-input v-model="formData.subject" readonly></el-input>
        </el-form-item>
        <el-form-item label="准考证号">
          <el-input v-model="formData.examNumber" readonly></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="formData.name" readonly></el-input>
        </el-form-item>
        <el-form-item label="考生类别">
          <el-input v-model="formData.studentType" readonly></el-input>
        </el-form-item>
        <el-form-item label="本科毕业学校">
          <el-input v-model="formData.graduationSchool" readonly></el-input>
        </el-form-item>
        <el-form-item label="毕业时间">
          <el-date-picker v-model="formData.graduationTime" type="date" readonly></el-date-picker>
        </el-form-item>
        <el-form-item label="本科专业">
          <el-input v-model="formData.graduatedMajor" readonly></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="formData.email" readonly></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="formData.contact" readonly></el-input>
        </el-form-item>
        <el-form-item label="紧急电话">
          <el-input v-model="formData.emergencyContact" readonly></el-input>
        </el-form-item>
        <el-form-item label="生源地">
          <el-input v-model="formData.origin" readonly></el-input>
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="formData.idNumber" readonly></el-input>
        </el-form-item>
        <el-form-item label="研究生类型">
          <el-input v-model="formData.graduateType" readonly></el-input>
        </el-form-item>
        <el-form-item label="报考导师优先意向">
          <el-input v-model="formData.supervisor1" placeholder="一志愿" readonly></el-input>
          <el-input v-model="formData.supervisor2" placeholder="二志愿" readonly style="margin-top: 10px;"></el-input>
          <el-input v-model="formData.supervisor3" placeholder="三志愿" readonly style="margin-top: 10px;"></el-input>
        </el-form-item>

        <!-- 特殊处理电子信息（全日制）专业 -->
        <template v-if="formData.subject === '电子信息（全日制）'">
          <el-form-item label="是否接受方向调整">
            <el-radio-group v-model="formData.acceptAdjustment" disabled>
              <el-radio label="是">是</el-radio>
              <el-radio label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <!-- 如果接受方向调整 -->
          <div v-if="formData.acceptAdjustment === '是'">
            <el-form-item label="选择专业顺序">
              <el-select v-model="formData.preferredSubjects[0]" placeholder="第一志愿" disabled>
                <el-option
                    v-for="item in formData.subSubjectOptions"
                    :label="item"
                    :value="item"
                    :key="item"
                ></el-option>
              </el-select>
              <el-select
                  v-model="formData.preferredSubjects[1]"
                  placeholder="第二志愿"
                  disabled
                  style="margin-top: 10px;"
              >
                <el-option
                    v-for="item in formData.subSubjectOptions"
                    :label="item"
                    :value="item"
                    :key="item"
                ></el-option>
              </el-select>
              <el-select
                  v-model="formData.preferredSubjects[2]"
                  placeholder="第三志愿"
                  disabled
                  style="margin-top: 10px;"
              >
                <el-option
                    v-for="item in formData.subSubjectOptions"
                    :label="item"
                    :value="item"
                    :key="item"
                ></el-option>
              </el-select>
              <el-select
                  v-model="formData.preferredSubjects[3]"
                  placeholder="第四志愿"
                  disabled
                  style="margin-top: 10px;"
              >
                <el-option
                    v-for="item in formData.subSubjectOptions"
                    :label="item"
                    :value="item"
                    :key="item"
                ></el-option>
              </el-select>
            </el-form-item>
          </div>
          <!-- 如果不接受方向调整 -->
          <div v-else-if="formData.acceptAdjustment === '否'">
            <el-form-item label="选择一个二级学科">
              <el-select v-model="formData.singleSubSubject" placeholder="请选择二级学科" disabled>
                <el-option
                    v-for="item in formData.subSubjectOptions"
                    :label="item"
                    :value="item"
                    :key="item"
                ></el-option>
              </el-select>
            </el-form-item>
          </div>
        </template>

        <!-- 其他专业 -->
        <template v-else>
          <el-form-item label="拟报研究方向">
            <el-select v-model="formData.researchDirection" placeholder="请选择拟报研究方向" disabled>
              <el-option
                  v-for="item in formData.subSubjectOptions"
                  :label="item"
                  :value="item"
                  :key="item"
              ></el-option>
            </el-select>
          </el-form-item>
        </template>

        <el-divider></el-divider>
        <el-form-item label="复试时间">
          <el-date-picker
              v-model="formData.reExamTime"
              type="datetime"
              readonly
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="复试地点">
          <el-input
              v-model="formData.reExamLocation"
              readonly
          ></el-input>
        </el-form-item>
        <el-form-item label="综合评价">
          <el-input
              v-model="formData.overallEvaluation"
              readonly
          ></el-input>
        </el-form-item>
        <el-form-item label="外语听力及口语成绩">
          <el-input
              v-model="formData.englishScore"
              readonly
          ></el-input>
        </el-form-item>
        <el-form-item label="专业知识测试成绩">
          <el-input
              v-model="formData.professionalScore"
              readonly
          ></el-input>
        </el-form-item>
        <el-form-item label="综合素质面试成绩">
          <el-input
              v-model="formData.interviewScore"
              readonly
          ></el-input>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      formData: {
        subject: '', // 复试学科专业
        examNumber: '', // 准考证号
        name: '', // 姓名
        studentType: '', // 考生类别
        graduationSchool: '', // 本科毕业学校
        graduationTime: '', // 毕业时间
        graduatedMajor: '', // 本科专业
        email: '', // 邮箱
        contact: '', // 电话
        emergencyContact: '', // 紧急电话
        origin: '', // 生源地
        idNumber: '', // 身份证号
        supervisor1: '', // 一志愿导师
        supervisor2: '', // 二志愿导师
        supervisor3: '', // 三志愿导师
        acceptAdjustment: '', // 是否接受方向调整
        preferredSubjects: ['', '', '', ''], // 按顺序选择的四个专业
        singleSubSubject: '', // 不接受方向调整时选择的二级学科
        researchDirection: '', // 拟报研究方向（其他专业）
        graduateType: '', // 研究生类型
        subSubjectOptions: [], // 二级学科选项，从后端获取

        // 新增的复试信息
        reExamTime: '', // 复试时间
        reExamLocation: '', // 复试地点
        overallEvaluation: '', // 综合评价
        englishScore: '', // 外语听力及口语成绩
        professionalScore: '', // 专业知识测试成绩
        interviewScore: '', // 综合素质面试成绩
      },
    };
  },
  methods: {
    // 获取学生的复试表信息
    async fetchStudentInfo() {
      const studentId = this.$route.params.studentId; // 获取路由参数中的 studentId
      try {
        // 从后端获取学生信息
        const response = await axios.get(`/api/graduateManagementSecretary/studentForm/${studentId}`);
        const data = response.data.data;

        // 将返回的数据填充到 formData 中
        this.formData = { ...this.formData, ...data };

      } catch (error) {
        this.$message.error('获取学生信息失败');
      }
    },
  },

  created() {
    this.fetchStudentInfo();
  },
};
</script>

<style scoped>
.student-info {
  max-width: 900px;
  margin: 40px auto;
  background-color: #f9f9f9;
  border-radius: 12px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.info-header {
  background-color: #3c8dbc;
  color: #ffffff;
  text-align: center;
  padding: 15px;
  border-radius: 8px 8px 0 0;
}

h2 {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

::v-deep(.el-form-item__label) {
  font-size: 16px;
  font-weight: bold;
}

::v-deep(.el-input, .el-select, .el-date-picker, .el-checkbox-group, .el-radio-group) {
  font-size: 16px;
}

::v-deep(.el-button) {
  font-size: 18px;
  width: 100%;
  padding: 10px;
}

::v-deep(.el-divider) {
  margin: 30px 0;
}
</style>
