<template>
  <el-container class="student-info">
    <el-header class="info-header">
      <h2>考生信息页面</h2>
    </el-header>
    <el-main>
      <el-form :model="formData" ref="formRef" label-width="120px">
        <!-- 所有信息都可编辑 -->
        <el-form-item label="复试学科专业" required>
          <el-input v-model="formData.subject" :disabled="isReadOnly" placeholder="请输入复试学科专业"></el-input>
        </el-form-item>
        <el-form-item label="准考证号" required>
          <el-input v-model="formData.examNumber" :disabled="isReadOnly" placeholder="请输入准考证号"></el-input>
        </el-form-item>
        <el-form-item label="姓名" required>
          <el-input v-model="formData.name" :disabled="isReadOnly" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="考生类别" required>
          <el-select v-model="formData.studentType" :disabled="isReadOnly" placeholder="请选择考生类别">
            <el-option label="应届生" value="应届生"></el-option>
            <el-option label="往届生" value="往届生"></el-option>
            <el-option label="同等学力" value="同等学力"></el-option>
            <el-option label="定向生" value="定向生"></el-option>
            <el-option label="非定向考生" value="非定向考生"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="本科毕业学校" required>
          <el-input v-model="formData.graduationSchool" :disabled="isReadOnly" placeholder="请输入本科毕业学校"></el-input>
        </el-form-item>
        <el-form-item label="毕业时间" required>
          <el-date-picker v-model="formData.graduationTime" :disabled="isReadOnly" type="date" placeholder="请选择毕业时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="本科专业" required>
          <el-input v-model="formData.graduatedMajor" :disabled="isReadOnly" placeholder="请输入本科专业"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" required>
          <el-input v-model="formData.email" :disabled="isReadOnly" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="电话" required>
          <el-input v-model="formData.contact" :disabled="isReadOnly" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item label="紧急电话" required>
          <el-input v-model="formData.emergencyContact" :disabled="isReadOnly" placeholder="请输入紧急电话"></el-input>
        </el-form-item>
        <el-form-item label="生源地" required>
          <el-input v-model="formData.origin" :disabled="isReadOnly" placeholder="请输入生源地"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" required>
          <el-input v-model="formData.idNumber" :disabled="isReadOnly" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="研究生类型" required>
          <el-input v-model="formData.graduateType" :disabled="isReadOnly" placeholder="请输入研究生类型"></el-input>
        </el-form-item>
        <el-form-item label="报考导师优先意向" required>
          <el-input v-model="formData.supervisor1" :disabled="isReadOnly" placeholder="一志愿"></el-input>
          <el-input v-model="formData.supervisor2" :disabled="isReadOnly" placeholder="二志愿" style="margin-top: 10px;"></el-input>
          <el-input v-model="formData.supervisor3" :disabled="isReadOnly" placeholder="三志愿" style="margin-top: 10px;"></el-input>
        </el-form-item>

        <!-- 特殊处理电子信息（全日制）专业 -->
        <template v-if="formData.subject === '电子信息（全日制）'">
          <el-form-item label="是否接受方向调整" required>
            <el-radio-group v-model="formData.acceptAdjustment" :disabled="isReadOnly" >
              <el-radio label="是">是</el-radio>
              <el-radio label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <!-- 如果接受方向调整 -->
          <div v-if="formData.acceptAdjustment === '是'">
            <el-form-item label="选择专业顺序" required>
              <el-select
                  v-model="formData.preferredSubjects[0]"
                  :disabled="isReadOnly"
                  placeholder="第一志愿"
              >
                <el-option
                    v-for="item in formData.subSubjectOptions"
                    :label="item"
                    :value="item"
                    :key="item"
                ></el-option>
              </el-select>

              <el-select
                  v-model="formData.preferredSubjects[1]"
                  :disabled="isReadOnly"
                  placeholder="第二志愿"
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
                  :disabled="isReadOnly"
                  placeholder="第三志愿"
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
                  :disabled="isReadOnly"
                  placeholder="第四志愿"
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
            <el-form-item label="选择一个二级学科" required>
              <el-select v-model="formData.singleSubSubject" :disabled="isReadOnly" placeholder="请选择二级学科">
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
          <el-form-item label="拟报研究方向" required>
            <el-select v-model="formData.researchDirection" placeholder="请选择拟报研究方向">
              <el-option
                  v-for="item in formData.subSubjectOptions"
                  :label="item"
                  :value="item"
                  :key="item"
              ></el-option>
            </el-select>
          </el-form-item>
        </template>

        <!-- 提交按钮 -->
        <el-form-item v-if="!isReadOnly">
          <el-button type="primary" @click="submitForm">提交</el-button>
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
        subject: '电子信息（全日制）', // 复试学科专业
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
        //subSubjectOptions: ['全日制计算机技术（01方向）', '全日制软件工程（02方向）', '全日制软件工程国际联合培养（03方向）',
        //  '全日制人工智能（04方向）', '全日制大数据技术与工程（05方向）'], // 电子信息专业下的五个二级学科
      },
      isReadOnly: false,
    };
  },
  methods: {
    addRequestInterceptor() {
      axios.interceptors.request.use(
          config => {
            const token = sessionStorage.getItem('student_token');
            if (token) {
              config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
          },
          error => Promise.reject(error)
      );
    },
    async fetchStudentInfo() {
      try {
        const response = await axios.get('/api/student/info');
        const data = response.data.data

        // 将返回的数据填充到 formData 中
        this.formData = { ...this.formData, ...data };

        // 如果身份证号不为空，则设置为只读
        this.isReadOnly = this.formData.examNumber !== '';
      } catch (error) {
        this.$message.error('获取考生信息失败');
      }
    },
    async submitForm() {
      // 表单验证规则
      const validatePreferredSubjects = (rule, value, callback) => {
        if (this.formData.acceptAdjustment === '是') {
          const selectedSubjects = this.formData.preferredSubjects.filter((item) => item);
          const hasDuplicates = new Set(selectedSubjects).size !== selectedSubjects.length;
          if (selectedSubjects.length < 4) {
            callback(new Error('请完整选择四个专业'));
          } else if (hasDuplicates) {
            callback(new Error('所选专业不能重复'));
          } else {
            callback();
          }
        } else {
          callback();
        }
      };

      // 定义表单验证规则
      // 设置验证规则
      this.$refs.formRef.rules = {
        subject: [{required: true, message: '请输入复试学科专业', trigger: 'blur'}],
        examNumber: [{required: true, message: '请输入准考证号', trigger: 'blur'}],
        name: [{required: true, message: '请输入姓名', trigger: 'blur'}],
        studentType: [{required: true, message: '请选择考生类别', trigger: 'change'}],
        graduationSchool: [{required: true, message: '请输入本科毕业学校', trigger: 'blur'}],
        graduationTime: [{required: true, message: '请选择毕业时间', trigger: 'change'}],
        graduatedMajor: [{required: true, message: '请输入本科专业', trigger: 'blur'}],
        email: [{required: true, message: '请输入邮箱', trigger: 'blur'}],
        contact: [{required: true, message: '请输入电话', trigger: 'blur'}],
        emergencyContact: [{required: true, message: '请输入紧急电话', trigger: 'blur'}],
        origin: [{required: true, message: '请输入生源地', trigger: 'blur'}],
        idNumber: [{required: true, message: '请输入身份证号', trigger: 'blur'}],
        graduateType: [{required: true, message: '请输入研究生类型', trigger: 'blur'}],
        supervisor1: [{required: true, message: '请输入一志愿导师', trigger: 'blur'}],
        supervisor2: [{required: true, message: '请输入二志愿导师', trigger: 'blur'}],
        supervisor3: [{required: true, message: '请输入三志愿导师', trigger: 'blur'}],
        acceptAdjustment: [{required: true, message: '请选择是否接受方向调整', trigger: 'change'}],
        singleSubSubject: [{required: true, message: '请选择一个二级学科', trigger: 'change'}],
        researchDirection: [{required: true, message: '请选择拟报研究方向', trigger: 'change'}],
        preferredSubjects: [{validator: validatePreferredSubjects, trigger: 'change'}],
      };

      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          try {
            // 提交表单数据到后端
            await axios.post('/api/student/submitForm', this.formData);
            this.$message.success('信息提交成功');
            // 重新获取考生信息，更新页面
            await this.fetchStudentInfo();
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
</style>
