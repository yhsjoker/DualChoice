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
        <router-link class="navbar-item" to="/discipline-info"
          style="color:#00C5CD;text-decoration:overline"
        >学科信息</router-link>
        <router-link class="navbar-item" to="/teacher-info">导师信息</router-link>
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

  <div class="discipline-info-page">


    <el-row type="flex" justify="center" style="margin-top: 20px;">
      <el-col :span="14">
        <el-input
            placeholder="搜索学院/一级学科/二级学科名称"
            v-model="searchKeyword"
            clearable
            @input="filterSubjects"
            prefix-icon="el-icon-search"
        ></el-input>
      </el-col>
    </el-row>

    <el-row type="flex" justify="center" style="margin-top: 20px;">
      <el-col :span="14">
        <el-collapse v-model="activeCollegeNames" accordion>
          <div v-for="college in filteredData" :key="college.collegeName">
            <el-collapse-item :title="college.collegeName" :name="college.collegeName">
              <!-- 学院简介已移除，没有学院简介展示 -->

              <!-- 展示学院下所有一级学科 -->
              <div class="subjects-container" style="display: flex; flex-wrap: wrap; margin-top:20px;">
                <el-card
                    v-for="subject in college.subjects"
                    :key="subject.name"
                    class="subject-card"
                    style="width: 300px; margin: 10px;"
                >
                  <div slot="header" class="card-header" style="display: flex; justify-content: space-between; align-items: center;">
                    <span style="font-weight: bold;">{{ subject.name }}</span>
                  </div>
                  <!-- 直接展示一级学科简介 -->
                  <p style="font-size:14px; color:#666; margin-bottom:10px;">{{ subject.intro }}</p>

                  <!-- 展示二级学科 -->
                  <el-collapse>
                    <el-collapse-item title="查看二级学科" name="children">
                      <div v-if="subject.children && subject.children.length > 0">
                        <div v-for="child in subject.children" :key="child.name" style="margin-bottom:15px;">
                          <span style="font-weight:bold;">{{child.name}}</span>
                          <p style="font-size:13px; color:#666; margin:5px 0 0 0;">{{child.intro}}</p>
                        </div>
                      </div>
                      <div v-else>暂无二级学科信息</div>
                    </el-collapse-item>
                  </el-collapse>
                </el-card>
              </div>
            </el-collapse-item>
          </div>
        </el-collapse>
      </el-col>
    </el-row>
  </div>
</template>


<script>
import axios from 'axios';

export default {
  data() {
    return {
      allData: [],
      filteredData: [],
      searchKeyword: '',
      searchKeyword2: '', // 新增
      activeCollegeNames: []
    };
  },
  methods: {
    Logout() {
      this.$router.push('/');
    },
    async fetchData() {
      try {
        const response = await axios.get('/api/nav/subjects'); // 假设的接口
        this.allData = response.data.data.allData;
        this.filteredData = this.allData;
      } catch (error) {
        this.$message.error('获取学科信息失败');
      }
    },
    filterSubjects() {
      const keyword = this.searchKeyword.trim().toLowerCase();
      if (!keyword) {
        // 未输入搜索关键字时，展示全部数据
        this.filteredData = this.allData;
        return;
      }

      this.filteredData = this.allData.map(college => {
        // 过滤一级学科与二级学科
        const filteredSubjects = college.subjects.map(sub => {
          const matchedChildren = sub.children.filter(child =>
              child.name.toLowerCase().includes(keyword)
          );

          const matchSubjectName = sub.name.toLowerCase().includes(keyword);
          let newSubject = null;

          if (matchSubjectName || matchedChildren.length > 0) {
            newSubject = {
              ...sub,
              children: matchSubjectName ? sub.children : matchedChildren
            };
          }
          return newSubject;
        }).filter(s => s !== null);

        const matchCollegeName = college.collegeName.toLowerCase().includes(keyword);

        // 如果学院名匹配，或者学院下有匹配的学科，则保留该学院
        if (matchCollegeName || filteredSubjects.length > 0) {
          return {
            ...college,
            subjects: matchCollegeName ? college.subjects : filteredSubjects
          };
        } else {
          return null;
        }
      }).filter(c => c !== null);
    }
  },
  created() {
    this.fetchData();
    this.filteredData = this.allData;
  }
};
</script>

<style scoped>
.discipline-info-page {
  background: #f5f7fa;
  min-height: 100vh;
  padding-bottom: 50px;
}

.card-header span {
  font-weight: bold;
  font-size: 16px;
}

.subject-card {
  background: #ffffff;
  border-radius: 8px;
}

.el-list-item {
  font-size: 14px;
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
</style>
