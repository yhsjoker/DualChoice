<template>
  <div class="discipline-info-page">
    <el-row type="flex" justify="center" style="margin-top: 20px;">
      <el-col :span="14">
        <h2 style="text-align: center; color: #333;">学科信息</h2>
      </el-col>
    </el-row>

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
      allData: [
        {
          collegeName: "信息科学与工程学院",
          subjects: [
            {
              name: "电子信息（全日制）",
              intro: "本一级学科简介...",
              children: [
                {name: "全日制计算机技术（01方向）", intro: "二级学科简介..."},
                {name: "全日制软件工程（02方向）", intro: "二级学科简介..."}
              ]
            },
            {
              name: "控制科学与工程（全日制）",
              intro: "另一个一级学科简介...",
              children: [
                {name: "智能控制与系统优化", intro: "二级学科简介..."}
              ]
            },
            {
              name: "控制科学与工程（全日制）",
              intro: "另一个一级学科简介...",
              children: [
                {name: "智能控制与系统优化", intro: "二级学科简介..."}
              ]
            }
          ]
        },
        {
          collegeName: "计算机学院",
          subjects: [
            {
              name: "计算机科学与技术",
              intro: "一级学科简介...",
              children: [
                {name: "人工智能", intro: "二级学科简介..."},
                {name: "网络安全", intro: "二级学科简介..."}
              ]
            }
          ]
        }
      ],
      filteredData: [],
      searchKeyword: '',
      activeCollegeNames: []
    };
  },
  methods: {
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
    // this.fetchData();
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
</style>
