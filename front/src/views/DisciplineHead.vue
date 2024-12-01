<template>
  <div class="discipline-head">
    <!-- 一级学科信息和名额指标 -->
    <div class="header">
      <el-card shadow="hover">
        <div class="header-content">
          <h2>{{ primarySubject }}</h2>
          <p>名额指标：<strong>{{ quotaIndicator }}</strong></p>
        </div>
      </el-card>
    </div>

    <!-- 二级学科和导师信息 -->
    <div class="content">
      <el-card
          shadow="never"
          class="secondary-card"
          v-for="(subject, index) in secondarySubjects"
          :key="index"
      >
        <div class="card-header">
          <h3>{{ subject }}</h3>
        </div>
        <el-table
            :data="teacherQuota[index]"
            class="center-table full-width-table"
            border
            stripe
        >
          <el-table-column prop="name" label="导师姓名" width="264px"
                           align="center"
                           header-align="center"
          ></el-table-column>

          <el-table-column label="学硕名额" width="261px" class-name="cell-center">
            <template #default="scope">
              <div class="cell-content">
              <el-input-number
                  v-model.number="scope.row.academicQuota"
                  :disabled="isReadOnly"
                  :min="0"
                  size="small"
                  @change="updateTotalQuota"
              ></el-input-number>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="专硕名额" width="261px" class-name="cell-center">
            <template #default="scope">
              <div class="cell-content">
              <el-input-number
                  v-model.number="scope.row.professionalQuota"
                  :disabled="isReadOnly"
                  :min="0"
                  size="small"
                  @change="updateTotalQuota"
                  class-name="cell-center"
              ></el-input-number>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="直博名额" width="261px">
            <template #default="scope">
              <div class="cell-content">
              <el-input-number
                  v-model.number="scope.row.phdQuota"
                  :disabled="isReadOnly"
                  :min="0"
                  size="small"
                  @change="updateTotalQuota"
                  class-name="cell-center"
              ></el-input-number>
              </div>
            </template>
          </el-table-column>

        </el-table>
      </el-card>
    </div>

    <!-- 提交按钮 -->
    <div class="footer" v-if="!isReadOnly">
      <el-button type="primary" @click="submitQuota" size="large"
      >提交名额分配</el-button
      >
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      primarySubject: '', // 一级学科名称（字符串）
      quotaIndicator: 0, // 名额指标（数字）
      secondarySubjects: [], // 二级学科列表（字符串数组）
      teacherQuota: [], // 导师名额列表（二维数组）
      isReadOnly: false, // 页面是否只读
    };
  },
  methods: {
    addRequestInterceptor() {
      axios.interceptors.request.use(
          (config) => {
            const token = sessionStorage.getItem('disciplineHead_token');
            if (token) {
              config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
          },
          (error) => Promise.reject(error)
      );
    },
    // 获取学科负责人信息
    async fetchSubjectInfo() {
      try {
        const response = await axios.get('/api/disciplineHead/info');
        const data = response.data.data;
        this.primarySubject = data.primarySubject;
        this.quotaIndicator = data.quotaIndicator;
        this.secondarySubjects = data.secondarySubjects;
        this.teacherQuota = data.teacherQuota;

        // 检查是否需要设置为只读
        this.checkQuotaEquality();
      } catch (error) {
        this.$message.error('获取学科信息失败');
      }
    },
    // 更新总名额数并检查是否只读
    updateTotalQuota() {
      this.checkQuotaEquality();
    },
    // 计算总名额并检查是否等于指标
    checkQuotaEquality() {
      const totalQuota = this.calculateTotalQuota();
      this.isReadOnly = totalQuota === this.quotaIndicator;
    },
    // 计算当前页面的总名额
    calculateTotalQuota() {
      let total = 0;
      this.teacherQuota.forEach((subjectQuota) => {
        subjectQuota.forEach((teacher) => {
          total +=
              (teacher.academicQuota || 0) +
              (teacher.professionalQuota || 0) +
              (teacher.phdQuota || 0);
        });
      });
      return total;
    },
    // 提交名额分配
    async submitQuota() {
      const totalQuota = this.calculateTotalQuota();
      if (totalQuota !== this.quotaIndicator) {
        const difference = totalQuota - this.quotaIndicator;
        if (difference > 0) {
          this.$message.error(
              `总名额超出指标 ${difference} 个，请调整后再提交`
          );
        } else {
          this.$message.error(
              `总名额少于指标 ${-difference} 个，请调整后再提交`
          );
        }
        return;
      }

      try {
        await axios.post('/api/disciplineHead/submitQuota', {
          primarySubject: this.primarySubject,
          secondarySubjects: this.secondarySubjects,
          teacherQuota: this.teacherQuota,
        });
        this.$message.success('名额分配提交成功');
        this.isReadOnly = true;
      } catch (error) {
        this.$message.error('提交失败，请重试');
      }
    },
  },
  created() {
    this.addRequestInterceptor();
    this.fetchSubjectInfo();
  },
};
</script>

<style scoped>
.discipline-head {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 20px;
  background-color: #f9f9f9;
}

.header {
  margin-bottom: 20px;
  width: 80%; /* 限制宽度，确保页面居中 */
  margin-left: auto;
  margin-right: auto;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h2 {
  font-size: 24px;
  margin: 0;
}

.header-content p {
  font-size: 18px;
  margin: 0;
}

.content {
  margin-top: 20px;
  width: 80%;
  margin-left: auto;
  margin-right: auto;
}

.full-width-table {
  width: 100%;
}

.secondary-card {
  margin-bottom: 30px;
}

.card-header {
  margin-bottom: 10px;
  text-align: center;
}

.card-header h3 {
  font-size: 20px;
  margin: 0;
}

::v-deep(.el-table) {
  table-layout: fixed;
}

::v-deep(.el-table th, .el-table td) {
  text-align: center;
}

.footer {
  text-align: center;
  margin-top: 20px;
}

::v-deep(.el-button) {
  width: 200px;
}

::v-deep(.cell-center) {
  text-align: center;
  vertical-align: middle;
}

.cell-content {
  display: flex;
  align-items: center;
  justify-content: center;
}

</style>
