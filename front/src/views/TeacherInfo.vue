<template>
  <div class="teacher-info-page">
    <el-row type="flex" justify="center" style="margin-top: 20px;">
      <el-col :span="14">
        <h2 style="text-align: center; color: #333;">导师信息</h2>
      </el-col>
    </el-row>

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
                  <div slot="header" class="card-header" style="display: flex; flex-direction: column; align-items:flex-start;">
                    <span style="font-size:16px; font-weight:bold;">{{ teacher.name }}</span>
                    <span style="color:#666; font-size:14px;">{{ teacher.position }}</span>
                  </div>

                  <div style="margin-bottom:10px;">
                    <p style="font-size:14px; color:#666; margin:5px 0;">
                      电话<el-icon style="font-size:14px;vertical-align: middle;"><i class="el-icon-phone"></i></el-icon> {{ teacher.phone }}
                    </p>
                    <p style="font-size:14px; color:#666; margin:5px 0;">
                      邮箱<el-icon style="font-size:14px;vertical-align: middle;"><i class="el-icon-message"></i></el-icon> {{ teacher.email }}
                    </p>
                    <p style="margin:5px 0;">
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
            {name: "李老师", position: "教授", phone: "13800000000", email: "li@example.com", hasAdmissionQualification: true,
              intro: "李老师简介..."},
            {name: "王老师", position: "副教授", phone: "13900000000", email: "wang@example.com", hasAdmissionQualification: false,
              intro: "王老师简介..."}
          ]
        },
        {
          collegeName: "计算机学院",
          teachers: [
            {name: "张老师", position: "讲师", phone: "13600000000", email: "zhang@example.com", hasAdmissionQualification: true,
              intro: "张老师简介..."}
          ]
        }
      ],
      filteredData: [],
      searchKeyword: '',
      activeCollegeNames: [],
      introDialogVisible: false,
      selectedTeacherIntro: '',
      snippetLength: 60 // 简介摘要长度，可根据需求调整
    };
  },
  methods: {
    async fetchData() {
      try {
        const response = await axios.get('/api/nav/teachers'); // 假设的接口
        this.allData = response.data;
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
    // this.fetchData();
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
  font-size: 16px;
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
</style>
