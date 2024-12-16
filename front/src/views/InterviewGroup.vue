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
          @input="filterStudents"
      >
      </el-input>
      <el-button type="text" class="login-link" @click="Logout">退出登录</el-button>
    </div>
  </header>

  <div class="page-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <el-card class="card">
        <div class="card-content">
          <h2>身份：复试组</h2>
          <br></br>
          <h3>负责的一级学科</h3>
          <el-tag type="primary" class="subject-tag">{{ primarySubject }}</el-tag>
        </div>
      </el-card>
    </aside>

    <!-- 主内容区域 -->
    <main class="main-content">
      <div>
      <el-table
          :data="pagedStudents"
          size="medium"
          stripe
          border
          style="width:910px"
          class="student-table"
      >
        <el-table-column prop="name" label="姓名" width="220"></el-table-column>
        <el-table-column prop="examNumber" label="准考证号" width="250"></el-table-column>
        <el-table-column prop="studentType" label="考生类别" width="220"></el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button
                type="primary"
                @click="openDialog(scope.row)"
                icon="el-icon-edit"
                class="action-button"
            >
              填写复试信息
            </el-button>
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

      <!-- 复试信息的对话框 -->
      <el-dialog
          v-model="dialogVisible"
          :title="`${currentStudent.name} 的复试信息`"
          width="600px"
      >
        <el-form
            :model="formData"
            :rules="rules"
            ref="formRef"
            label-width="160px"
            class="re-exam-form"
        >
          <el-form-item label="复试时间" prop="reExamTime">
            <el-date-picker
                v-model="formData.reExamTime"
                type="datetime"
                placeholder="请选择复试时间"
                :disabled="isReadOnly"
                style="width: 100%;"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="复试地点" prop="reExamLocation">
            <el-input
                v-model="formData.reExamLocation"
                placeholder="请输入复试地点"
                :disabled="isReadOnly"
                style="width: 100%;"
            ></el-input>
          </el-form-item>
          <el-form-item label="综合评价" prop="overallEvaluation">
            <el-input
                v-model="formData.overallEvaluation"
                placeholder="请输入综合评价"
                :disabled="isReadOnly"
                style="width: 100%;"
            ></el-input>
          </el-form-item>
          <el-form-item label="外语听力及口语成绩" prop="englishScore">
            <el-input
                v-model="formData.englishScore"
                placeholder="请输入成绩"
                :disabled="isReadOnly"
                style="width: 100%;"
            ></el-input>
          </el-form-item>
          <el-form-item label="专业知识测试成绩" prop="professionalScore">
            <el-input
                v-model="formData.professionalScore"
                placeholder="请输入成绩"
                :disabled="isReadOnly"
                style="width: 100%;"
            ></el-input>
          </el-form-item>
          <el-form-item label="综合素质面试成绩" prop="interviewScore">
            <el-input
                v-model="formData.interviewScore"
                placeholder="请输入成绩"
                :disabled="isReadOnly"
                style="width: 100%;"
            ></el-input>
          </el-form-item>




          <el-form-item label="电子签名" prop="signatureFile">
            <template v-if="isReadOnly">
              <div v-if="formData.signatureUrl">
                <img :src="formData.signatureUrl" alt="电子签名" class="signature-image" />
                <p>已上传的签名</p>
              </div>
              <div v-else>
                <p>未上传电子签名</p>
              </div>
            </template>
            <template v-else>
              <el-upload
                  class="upload-demo"
                  ref="signatureUpload"
                  action="#"
                  :auto-upload="false"
                  :on-change="handleSignatureChange"
                  :on-remove="handleSignatureRemove"
                  :file-list="signatureFileList"
                  :limit="1"
                  :on-exceed="handleSignatureExceed"
                  accept=".jpg,.jpeg,.png"
              >
                <el-button slot="trigger" size="small" type="primary">选择签名</el-button>
                <div slot="tip" class="el-upload__tip" style="width: 290px">
                  只支持上传 JPG 或 PNG 图片
                </div>
              </el-upload>

              <div v-if="signaturePreviewUrl" class="signature-preview">
                <!-- 点击此链接在新标签页打开已选择的图片 -->
                <a :href="signaturePreviewUrl" target="_blank" rel="noopener" style="margin-left:10px;">查看签名</a>
              </div>
            </template>
          </el-form-item>





        </el-form>
        <div slot="footer" class="dialog-footer" v-if="!isReadOnly">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">提交</el-button>
        </div>
      </el-dialog>
    </main>

  </div>
</template>

<script>
import axios from 'axios';
import moment from 'moment';

export default {
  name: 'ReExamGroup',
  data() {
    return {
      primarySubject: '', // 复试组负责的一级学科
      studentList: [], // 学生列表
      dialogVisible: false, // 对话框显示状态
      currentStudent: {}, // 当前选中的学生
      formData: {
        id: '',
        reExamTime: '',
        reExamLocation: '',
        overallEvaluation: '',
        englishScore: '',
        professionalScore: '',
        interviewScore: '',

        signatureFile: null,          // 前端要上传的签名图片文件
        signatureUrl: '',             // 已上传签名文件的可访问链接
      },

      searchKeyword: '',
      filteredList: [],

      signatureFileList: [],          // 签名文件列表
      signaturePreviewUrl: '',        // 签名图片预览URL
      isReadOnly: false, // 表单是否只读
      rules: {
        reExamTime: [{ required: true, message: '请选择复试时间', trigger: 'change' }],
        reExamLocation: [{ required: true, message: '请输入复试地点', trigger: 'change' }],
        overallEvaluation: [{ required: true, message: '请输入综合评价', trigger: 'change' }],
        englishScore: [{ required: true, message: '请输入外语成绩', trigger: 'change' }],
        professionalScore: [{ required: true, message: '请输入专业知识成绩', trigger: 'change' }],
        interviewScore: [{ required: true, message: '请输入面试成绩', trigger: 'change' }],
        signatureFile: [{ required: true, message: '请上传个人签名', trigger: 'change' }],
      },

      currentPage: 1,    // 新增：当前页码
      pageSize: 10,       // 新增：每页显示10条数据
    };
  },

  computed: {
    // 新增：基于students和currentPage计算当前页的学生数据
    pagedStudents() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredList.slice(start, end);
    },
  },

  methods: {
    filterStudents() {
      const keyword = this.searchKeyword.toLowerCase();
      this.filteredList = this.studentList.filter(item => {
        return (
            item.name.toLowerCase().includes(keyword) ||
            item.examNumber.toLowerCase().includes(keyword) ||
            item.studentType.toLowerCase().includes(keyword)
        );
      });
      this.currentPage = 1; // 搜索后重置当前页
    },

    // 处理签名文件变化
    handleSignatureChange(file, fileList) {
      const allowedTypes = ['image/jpg', 'image/png'];
      if (allowedTypes.includes(file.raw.type)) {
        this.formData.signatureFile = file.raw;
        this.signatureFileList = fileList;
        this.signaturePreviewUrl = URL.createObjectURL(file.raw);
      } else {
        this.$message.error('只能上传 JPG 或 PNG 图片!');
        this.formData.signatureFile = null;
        this.signatureFileList = fileList.filter(f => allowedTypes.includes(f.raw.type));
        this.signaturePreviewUrl = '';
      }
    },

    // 处理签名文件移除
    handleSignatureRemove(file, fileList) {
      this.signaturePreviewUrl = '';
      this.formData.signatureFile = null;
    },

    // 处理签名文件超限
    handleSignatureExceed(files, fileList) {
      this.$message.warning('只能提交 1 个签名文件');
    },


    Logout() {
      this.$router.push('/');
    },
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
        // console.log(data);
        this.primarySubject = data.primarySubject;
        this.studentList = data.studentList.map(student => {
          return {
            name: student.name,
            examNumber: student.examNumber,
            studentType: student.studentType || '暂未填写', // 如果为空，标注“暂未填写”
            id: student.id, // 用于后续请求
          };
        });
        this.filteredList = this.studentList;
        this.currentPage=1;
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
      try {
        // 从后端获取该学生的复试信息
        const response = await axios.get(`/api/interviewGroup/reExamInfo/${student.id}`);
        if (response.data.data) {
          Object.assign(this.formData, response.data.data);
          // 如果复试地点不为空，设置为只读模式
          if (this.formData.reExamLocation) {
            this.isReadOnly = true;
          }

          if (this.formData.signatureUrl) {
            this.signaturePreviewUrl = this.formData.signatureUrl;
            this.signatureFileList = [];
          }
        }
      } catch (error) {
        this.$message.error('获取复试信息失败');
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {


          if (!this.isReadOnly) {
            if (!this.formData.signatureFile) {
              this.$message.error('请上传电子签名图片');
              return;
            }
          }

          this.formData.id = this.currentStudent.id;
          this.formData.reExamTime = moment(this.currentStudent.reExamTime).format('YYYY-MM-DD hh:mm:ss');

          const submitData = new FormData();
          for (const key in this.formData) {
            if (this.formData.hasOwnProperty(key)) {
              if (key === 'signatureFile' && this.formData[key]) {
                submitData.append('signatureFile', this.formData[key]);
              } else if (key !== 'signatureFile') {
                // 将其他字段作为普通表单数据提交
                submitData.append(key, this.formData[key] ? this.formData[key] : '');
              }
            }
          }


          try {
            // 提交数据到后端
            await axios.post(`/api/interviewGroup/updateReExamInfo`, submitData,{

            });
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

    handleCurrentChange(page) {
      // 切换分页时更新当前页
      this.currentPage = page;
    }
  },
  created() {
    this.addRequestInterceptor();
    this.fetchGroupInfo();
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

.student-table{
  border-radius: 4px;
}

/* 操作按钮样式 */
.action-button {
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 对话框样式 */
.el-dialog__header {
  background-color: #409EFF;
  color: #ffffff;
  border-bottom: none;
}

.el-dialog__title {
  font-size: 18px;
  font-weight: bold;
}

.re-exam-form {
  padding: 20px 0;
}

.el-form-item {
  margin-bottom: 20px;
}


.el-button--primary {
  background-color: #409EFF;
  border-color: #409EFF;
}

.el-button--primary:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

.el-button:hover {
  opacity: 0.9;
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
  .main-header h2 {
    font-size: 22px;
  }

  .el-table th,
  .el-table td {
    font-size: 14px;
  }

  .action-button {
    padding: 0 5px;
  }
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

.dialog-footer {
  text-align: center;
}

.signature-image{
  max-width: 100%;
  height: auto;
  margin-top: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.signature-preview {
  margin-top: 10px;
}

::v-deep .el-table th {
  font-size: 20px; /* 你可以根据需要调整字体大小 */
  color: #3c8dbc;
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
