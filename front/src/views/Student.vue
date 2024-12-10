<template>
  <el-menu
      class="nav-bar"
      mode="horizontal"
      background-color="#FFFFFF"
      text-color="#333333"
      active-text-color="#409EFF"
      router
  >

    <el-menu-item index="/subject-info" class="custom-menu-item subject-info">学科信息</el-menu-item>
    <el-menu-item index="/teacher-info" class="custom-menu-item teacher-info">导师信息</el-menu-item>

    <el-menu-item index="logout" class="logout-button" @click="handleLogout">
      退出登录
    </el-menu-item>
  </el-menu>

  <el-container class="student-info">
    <el-header class="info-header">
      <h2>考生信息页面</h2>
    </el-header>
    <el-main>
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
        <!-- 所有信息都可编辑 -->
        <el-form-item label="复试学科专业" prop="subject">
          <el-input v-model="formData.subject" :disabled="isReadOnly" placeholder="请输入复试学科专业"></el-input>
        </el-form-item>
        <el-form-item label="准考证号" prop="examNumber">
          <el-input v-model="formData.examNumber" :disabled="isReadOnly" placeholder="请输入准考证号"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" :disabled="isReadOnly" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="考生类别" prop="studentType">
          <el-select v-model="formData.studentType" :disabled="isReadOnly" placeholder="请选择考生类别">
            <el-option label="应届生" value="应届生"></el-option>
            <el-option label="往届生" value="往届生"></el-option>
            <el-option label="同等学力" value="同等学力"></el-option>
            <el-option label="定向生" value="定向生"></el-option>
            <el-option label="非定向考生" value="非定向考生"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="本科毕业学校" prop="graduationSchool">
          <el-input v-model="formData.graduationSchool" :disabled="isReadOnly" placeholder="请输入本科毕业学校"></el-input>
        </el-form-item>
        <el-form-item label="毕业时间" prop="graduationTime">
          <el-date-picker v-model="formData.graduationTime" :disabled="isReadOnly" type="date" placeholder="请选择毕业时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="本科专业" prop="graduatedMajor">
          <el-input v-model="formData.graduatedMajor" :disabled="isReadOnly" placeholder="请输入本科专业"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" :disabled="isReadOnly" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="contact">
          <el-input v-model="formData.contact" :disabled="isReadOnly" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item label="紧急电话" prop="emergencyContact">
          <el-input v-model="formData.emergencyContact" :disabled="isReadOnly" placeholder="请输入紧急电话"></el-input>
        </el-form-item>
        <el-form-item label="生源地" prop="origin">
          <el-input v-model="formData.origin" :disabled="isReadOnly" placeholder="请输入生源地"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idNumber">
          <el-input v-model="formData.idNumber" :disabled="isReadOnly" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="研究生类型" prop="graduateType">
          <el-input v-model="formData.graduateType" :disabled="isReadOnly" placeholder="请输入研究生类型"></el-input>
        </el-form-item>
        <el-form-item label="报考导师优先意向">
          <el-form-item prop="supervisor1">
            <el-input v-model="formData.supervisor1" :disabled="isReadOnly" placeholder="一志愿" ></el-input>
          </el-form-item>
          <el-form-item prop="supervisor2">
            <el-input v-model="formData.supervisor2" :disabled="isReadOnly" placeholder="二志愿" ></el-input>
          </el-form-item>
          <el-form-item prop="supervisor3">
            <el-input v-model="formData.supervisor3" :disabled="isReadOnly" placeholder="三志愿" ></el-input>
          </el-form-item>
        </el-form-item>

        <!-- 特殊处理电子信息（全日制）专业 -->
        <template v-if="formData.subject === '电子信息（全日制）'">
          <el-form-item label="是否接受方向调整" prop="acceptAdjustment">
            <el-radio-group v-model="formData.acceptAdjustment" :disabled="isReadOnly">
              <el-radio label="是">是</el-radio>
              <el-radio label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <!-- 如果接受方向调整 -->
          <div v-if="formData.acceptAdjustment === '是'">
            <el-form-item label="选择专业顺序" prop="preferredSubjects">
              <el-select
                  v-if="Array.isArray(formData.preferredSubjects)"
                  v-model="formData.preferredSubjects[0]"
                  :disabled="isReadOnly"
                  placeholder="第一志愿"
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
                  v-if="Array.isArray(formData.preferredSubjects)"
                  v-model="formData.preferredSubjects[1]"
                  :disabled="isReadOnly"
                  placeholder="第二志愿"
                  style="margin-top: 10px; margin-left: 10px"
              >
                <el-option
                    v-for="item in formData.subSubjectOptions"
                    :label="item"
                    :value="item"
                    :key="item"
                ></el-option>
              </el-select>

              <el-select
                  v-if="Array.isArray(formData.preferredSubjects)"
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
                  v-if="Array.isArray(formData.preferredSubjects)"
                  v-model="formData.preferredSubjects[3]"
                  :disabled="isReadOnly"
                  placeholder="第四志愿"
                  style="margin-top: 10px; margin-left: 10px"
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
            <el-form-item label="选择一个二级学科" prop="singleSubSubject">
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
          <el-form-item label="拟报研究方向" prop="researchDirection">
            <el-select v-model="formData.researchDirection" :disabled="isReadOnly" placeholder="请选择拟报研究方向">
              <el-option
                  v-for="item in formData.subSubjectOptions"
                  :label="item"
                  :value="item"
                  :key="item"
              ></el-option>
            </el-select>
          </el-form-item>
        </template>

        <el-form-item label="上传个人简述" prop="personalStatement">
          <template v-if="isReadOnly">
            <div>
              <p>已上传的文件: {{ formData.personalStatementName }}</p>
              <a :href="formData.personalStatementUrl" download :title="formData.personalStatementName">下载已上传的PDF</a>
            </div>
          </template>
          <template v-else>
            <el-upload
                class="upload-demo"
                ref="upload"
                action="#"
                :auto-upload="false"
                :on-change="handleFileChange"
                :on-remove="handleFileRemove"
                :file-list="fileList"
                :limit="1"
                :on-exceed="handleExceed"
            >
              <el-button slot="trigger" size="small" type="primary">选择文件</el-button>
              <div slot="tip" class="el-upload__tip" style="width: 290px">
                只支持上传 PDF 文件
              </div>
            </el-upload>

            <div v-if="previewUrl" style="margin-top: 45px">
              <!-- 点击此链接在新标签页打开已选择的pdf -->
              <a :href="previewUrl" target="_blank" rel="noopener" style="margin-left:10px;">查看文件</a>
            </div>
          </template>
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
          <template v-if="!isReadOnly">
            <el-button type="primary" @click="submitForm">提交</el-button>
          </template>
          <template v-else>
            <el-button type="primary" @click="handleCheckStatus">查看状态</el-button>
          </template>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>

  <!-- 状态显示的对话框 -->
  <el-dialog
      title=""
      v-model="showStatusDialog"
      width="50%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      custom-class="status-dialog"
  >
    <div class="dialog-content">
      <template v-if="formData.status === '已录取'">
        <div class="status-header success">
          <i class="el-icon-circle-check success-icon"></i>
          <h2 class="success-text">恭喜你!</h2>
        </div>
        <p class="status-message">
          你已被 <span class="highlight">{{ formData.admissionMajor }}</span> 专业的 <span class="highlight">{{ formData.admissionTeacherName }}</span> 老师录取
        </p>
      </template>
      <template v-else>
        <div class="status-header info">
          <i class="el-icon-info info-icon"></i>
          <h2 class="info-text">当前状态</h2>
        </div>
        <p class="status-message">
          当前状态为：<span class="highlight">{{ formData.status }}</span>
        </p>
      </template>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="showStatusDialog = false">关闭</el-button>
    </span>
  </el-dialog>
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

        personalStatementFile: null,      // 前端要上传的PDF文件
        personalStatementName: '',        // 已上传文件的名称
        personalStatementUrl: '',         // 已上传文件的可访问链接

        status: '', // 状态（"提交阶段"、"审查阶段"、"已录取"、"未录取"）
        admissionTeacherName: '', // 已录取时返回录取导师姓名
        admissionMajor: '' // 已录取时返回录取专业
      },
      fileList:[],
      isReadOnly: false,
      flag: false,
      rules: {},
      previewUrl: '',
      showStatusDialog: false // 控制查看状态对话框显示
    };
  },
  methods: {
    handleFileChange(file, fileList) {
      const allowedType = 'application/pdf';

      if (file.raw.type === allowedType) {
        this.formData.personalStatementFile = file.raw;
        this.fileList = fileList;
        this.previewUrl = URL.createObjectURL(file.raw);
      } else {
        this.$message.error('只能上传 PDF 文件!');
        this.formData.personalStatementFile = null;
        this.fileList = fileList.filter(f => f.raw.type === allowedType);
        this.previewUrl = '';
      }
    },
    handleFileRemove(file, fileList){
      this.previewUrl = '';
      this.formData.personalStatementFile = null;
    },
    handleExceed(files, fileList) {
      this.$message.warning(`只能提交 1 个文件`);
    },

    async handleLogout() {
      this.$router.push('/');
    },

    async handleCheckStatus() {
      try {
        const response = await axios.get('/api/student/currentStatus');
        const data = response.data.data;
        this.formData.status = data.status || this.formData.status;
        this.formData.admissionTeacherName = data.admissionTeacherName || '';
        this.formData.admissionMajor = data.admissionMajor || '';

        // this.formData.status = '已录取';
        // this.formData.admissionTeacherName = '张三';
        // this.formData.admissionMajor = '计算机技术';

        this.showStatusDialog = true;
      } catch (error) {
        this.$message.error('获取状态信息失败');
      }
    },

    generateRules() {
      // 动态生成验证规则
      this.rules = {
        subject: [{ required: true, message: '请输入复试学科专业', trigger: 'blur' }],
        examNumber: [{ required: true, message: '请输入准考证号', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        studentType: [{ required: true, message: '请选择考生类别', trigger: 'change' }],
        graduationSchool: [{ required: true, message: '请输入本科毕业学校', trigger: 'blur' }],
        graduationTime: [{ required: true, message: '请选择毕业时间', trigger: 'change' }],
        graduatedMajor: [{ required: true, message: '请输入本科专业', trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        contact: [{ required: true, message: '请输入电话', trigger: 'blur' }],
        emergencyContact: [{ required: true, message: '请输入紧急电话', trigger: 'blur' }],
        origin: [{ required: true, message: '请输入生源地', trigger: 'blur' }],
        idNumber: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        graduateType: [{ required: true, message: '请输入研究生类型', trigger: 'blur' }],
        supervisor1: [{ required: true, message: '请输入一志愿导师', trigger: 'blur' }],
        supervisor2: [{ required: true, message: '请输入二志愿导师', trigger: 'blur' }],
        supervisor3: [{ required: true, message: '请输入三志愿导师', trigger: 'blur' }],
        acceptAdjustment: [{required: this.flag, message: '请选择是否接受方向调整', trigger: 'change'}],
        singleSubSubject: [{required: this.flag, message: '请选择一个二级学科', trigger: 'change'}],
        researchDirection: [{required: this.flag.isReadOnly, message: '请选择拟报研究方向', trigger: 'change'}],
        preferredSubjects: [{validator: this.validatePreferredSubjects.bind(this), trigger: 'change'}],
        personalStatementFile: {required: !this.isReadOnly, message: '请上传个人简述文件', trigger: 'change'}
      };
    },

    validatePreferredSubjects (rule, value, callback) {
      if (this.formData.acceptAdjustment === '是') {
        const selectedSubjects = value.filter((item) => item);
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
    },

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
        const data = response.data.data;

        // 将返回的数据填充到 formData 中
        this.formData = {
          ...this.formData,
          ...data,
          preferredSubjects: data.preferredSubjects || ['', '', '', ''], // 默认值
          singleSubSubject: data.singleSubSubject || '', // 默认值
          personalStatementUrl: data.personalStatementUrl || '',
          personalStatementName: data.personalStatementName || '',
        };

        // 如果准考证号不为空，则设置为只读
        this.isReadOnly = this.formData.examNumber !== null;

        if(this.formData.subject === '电子信息（全日制）'){
          this.flag = true;
        }

        // 动态生成验证规则
        await this.$nextTick(() => {
          this.generateRules();
        });

        // 重新验证表单
        await this.$nextTick(() => {
          this.$refs.formRef.clearValidate(); // 清空验证状态
          this.$refs.formRef.validate(); // 重新验证以同步状态
        });
      } catch (error) {
        this.$message.error('获取考生信息失败');
      }
    },

    async submitForm() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          if (!this.isReadOnly) {
            if (!this.formData.personalStatementFile) {
              this.$message.error('请上传个人简述文件');
              return;
            }
            if (this.formData.personalStatementFile.type !== 'application/pdf') {
              this.$message.error('上传的文件类型不正确，请上传 PDF 文件!');
              return;
            }
          }

          const submitData = new FormData();
          for (const key in this.formData) {
            if (this.formData.hasOwnProperty(key)) {
              if (key === 'personalStatementFile' && this.formData[key]) {
                submitData.append('personalStatementFile', this.formData[key]);
              } else if (key !== 'personalStatementFile') {
                // 将其他字段作为普通表单数据提交
                submitData.append(key, this.formData[key] ? this.formData[key] : '');
              }
            }
          }

          try {
            // 提交表单数据到后端
            await axios.post('/api/student/submitForm', submitData, {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            });
            this.$message.success('信息提交成功');
            // 重新获取考生信息，更新页面
            await this.fetchStudentInfo();
          } catch (error) {
            // console.error('提交表单时发生错误:', error); // 捕获完整的错误对象
            // if (error.response) {
            //   console.error('后端返回的错误:', error.response.data);
            // } else if (error.request) {
            //   console.error('未收到后端响应:', error.request);
            // } else {
            //   console.error('其他错误:', error.message);
            // }
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

.nav-bar {
  width: 100%;
  border-bottom: 2px solid #1E90FF !important; /* 下边框 */
}

/* 自定义菜单项样式 */
.custom-menu-item {
  text-align: center;
  font-size: 18px !important; /* 调整字体大小 */
  font-weight: bold !important;
}

/* 学科信息的特定样式 */
.subject-info {
  color: #FF69B4 !important; /* 例如，粉色 */
  margin-left: 25% !important;
}

/* 导师信息的特定样式 */
.teacher-info {
  color: #1E90FF !important; /* 例如，蓝色 */
  margin-left: 15% !important;
}

/* 退出登录按钮样式 */
.logout-button {
  cursor: pointer;
  margin-left: 15% !important;
}

.status-dialog .el-dialog__header {
  display: none; /* 隐藏默认标题栏，使我们自定义的内容更突出 */
}

.dialog-content {
  text-align: center;
  padding: 20px 10px;
}

.status-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.success-icon, .info-icon {
  font-size: 36px;
  margin-right: 10px;
}

.success-icon {
  color: #67C23A; /* 成功绿色 */
}

.info-icon {
  color: #909399; /* 中性灰色 */
}

.success-text {
  font-size: 24px;
  font-weight: bold;
  color: #67C23A;
  margin: 0;
}

.info-text {
  font-size: 24px;
  font-weight: bold;
  color: #909399;
  margin: 0;
}

.status-message {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
}

.highlight {
  font-weight: bold;
  color: #409EFF; /* 强调用蓝色 */
}

.dialog-footer {
  text-align: center;
}
</style>
