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
        <router-link class="navbar-item" to="/teacher-info"
                     style="color:#00C5CD;text-decoration:overline"
        >导师信息</router-link>
      </div>
    </div>
    <div class="navbar-right">
      <el-input
          class="search-input"
          placeholder="搜索..."
          suffix-icon="el-icon-search"
          v-model="searchKeyword2"
      >
      </el-input>
      <el-button type="text" class="login-link" @click="Logout">首页</el-button>
    </div>
  </header>

  <div class="teacher-info-page">
    <el-row type="flex" justify="center" style="margin-top: 20px;">
      <el-col :span="14">
        <el-input
            placeholder="搜索学院/老师名称"
            v-model="searchKeyword"
            clearable
            @input="filterTeachers"
            prefix-icon="el-icon-search"
        ></el-input>
      </el-col>
    </el-row>

    <el-row type="flex" justify="center" style="margin-top: 20px;">
      <el-col :span="14">
        <el-collapse v-model="activeCollegeNames" accordion>
          <div v-for="college in filteredData" :key="college.collegeName">
            <el-collapse-item :title="college.collegeName" :name="college.collegeName">
              <!-- 展示该学院下所有老师 -->
              <div class="teachers-container" style="display: flex; flex-wrap: wrap; margin-top:20px;">
                <el-card
                    v-for="teacher in college.teachers"
                    :key="teacher.name"
                    class="teacher-card"
                    style="width: 280px; margin: 10px;"
                >


                  <div slot="header" class="card-header">
                    <!-- 导师姓名、职称和图片同时放在一行 -->
                    <div class="header-content">
                      <div class="text-info">
                        <span class="teacher-name">{{ teacher.name }}</span><br/>
                        <span class="teacher-position">{{ teacher.position }}</span>
                      </div>
                      <img
                          v-if="teacher.imageUrl"
                          :src="teacher.imageUrl"
                          alt="导师照片"
                          class="teacher-image-right"
                      />
                    </div>
                  </div>

                  <div style="margin-bottom:10px;">
                    <p style="font-size:15px; color:#666; margin:5px 0;">
                      电话<el-icon style="font-size:14px;vertical-align: middle;"><i class="el-icon-phone"></i></el-icon> {{ teacher.phone }}
                    </p>
                    <p style="font-size:15px; color:#666; margin:5px 0;">
                      邮箱<el-icon style="font-size:14px;vertical-align: middle;"><i class="el-icon-message"></i></el-icon> {{ teacher.email }}
                    </p>
                    <p style="font-size:15px">
                      <el-tag :type="teacher.hasAdmissionQualification ? 'success' : 'info'">
                        {{ teacher.hasAdmissionQualification ? '有招生资格' : '无招生资格' }}
                      </el-tag>
                    </p>
                  </div>

                  <div style="font-size:14px; color:#444; line-height:1.5;">
                    <!-- 简介摘要 -->
                    <p>简介： {{ getIntroSnippet(teacher.intro) }}</p>
                    <div v-if="teacher.intro.length > snippetLength">
                      <el-button type="text" @click="openIntroDialog(teacher)">查看简介</el-button>
                    </div>
                  </div>
                </el-card>
              </div>
            </el-collapse-item>
          </div>
        </el-collapse>
      </el-col>
    </el-row>

    <!-- 简介对话框 -->
    <el-dialog
        title="教师简介"
        v-model="introDialogVisible"
        width="50%"
    >
      <p style="white-space: pre-wrap;">{{ selectedTeacherIntro }}</p>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="introDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      allData: [
        {
          collegeName: "信息科学与工程学院",
          teachers: [
            {name: "李老师", position: "教授", imageUrl:'', phone: "13800000000", email: "li@example.com", hasAdmissionQualification: true,
              intro: "李老师简介..."},
            {name: "王老师", position: "副教授", imageUrl:'', phone: "13900000000", email: "wang@example.com", hasAdmissionQualification: false,
              intro: "王老师简介..."}
          ]
        },
        {
          collegeName: "计算机学院",
          teachers: [
            {name: "张老师", position: "讲师", imageUrl:'', phone: "13600000000", email: "zhang@example.com", hasAdmissionQualification: true,
              intro: "张老师简介..."}
          ]
        }
      ],
      filteredData: [],
      searchKeyword: '',
      searchKeyword2: '', // 新增
      activeCollegeNames: [],
      introDialogVisible: false,
      selectedTeacherIntro: '',
      snippetLength: 60 // 简介摘要长度，可根据需求调整
    };
  },
  methods: {
    Logout(){
      this.$router.push('/');
    },

    async fetchData() {
      try {
        const response = await axios.get('/api/nav/teachers'); // 假设的接口
        this.allData = response.data.data.allData;
        this.filteredData = this.allData;
      } catch (error) {
        this.$message.error('获取教师信息失败');
      }
    },
    filterTeachers() {
      const keyword = this.searchKeyword.trim().toLowerCase();
      if (!keyword) {
        this.filteredData = this.allData;
        return;
      }

      this.filteredData = this.allData.map(college => {
        const matchedTeachers = college.teachers.filter(teacher =>
            teacher.name.toLowerCase().includes(keyword)
        );
        const matchCollegeName = college.collegeName.toLowerCase().includes(keyword);

        if (matchCollegeName || matchedTeachers.length > 0) {
          return {
            ...college,
            teachers: matchCollegeName ? college.teachers : matchedTeachers
          };
        } else {
          return null;
        }
      }).filter(c => c !== null);
    },
    getIntroSnippet(intro) {
      if (intro.length > this.snippetLength) {
        return intro.slice(0, this.snippetLength) + '...';
      }
      return intro;
    },
    openIntroDialog(teacher) {
      this.selectedTeacherIntro = teacher.intro;
      this.introDialogVisible = true;
    }
  },
  created() {
    this.fetchData();
    this.filteredData = this.allData;
  }
};
</script>

<style scoped>
.teacher-info-page {
  background: #f5f7fa;
  min-height: 100vh;
  padding-bottom: 50px;
}

.card-header span {
  font-weight: bold;
}

.teacher-card {
  background: #ffffff;
  border-radius: 8px;
  padding-bottom: 10px;
}

.teacher-card p {
  margin: 0;
  line-height: 1.5;
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


.teacher-image {
  width: 60%;
  height: 180px; /* 可根据需求调整高度 */
  object-fit: cover; /* 确保图片按比例填满容器 */
  border-radius: 4px 4px 0 0; /* 图片圆角，可选 */
  margin-bottom: 10px; /* 图片与下面文字内容的间距 */
}

.card-header {
  /* 使用flex布局，让文字和图片在同一行对齐 */
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.text-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* 导师图片样式（右侧） */
.teacher-image-right {
  width: 60px; /* 可根据需要调整 */
  height: 60px; /* 可根据需要调整 */
  object-fit: cover;
  border-radius: 4px;
  margin-left:10px;
}

.teacher-name {
  font-size:20px;
  font-weight:bold;
}

.teacher-position {
  color:#666;
  font-size:16px;
  margin-top:4px;
}

</style>
