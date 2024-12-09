<template>
  <div class="draw-lottery">
    <h1>抽签环节</h1>
    <div class="select-teacher">
      <span>选择老师:</span>
      <el-select v-model="currentTeacherId" placeholder="选择老师" @change="updateTeacherSubjects">
        <el-option
            v-for="teacher in teachers"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id">
        </el-option>
      </el-select>
    </div>

    <!-- 当前老师负责的二级学科及其名额 -->
    <el-table :data="currentTeacher.subjects" border style="width: 100%; margin-top: 20px;" :cell-style="{textAlign: 'center'}">
      <el-table-column prop="name" label="二级学科名称" header-align="center" align="center"></el-table-column>
      <el-table-column prop="academicQuota" label="学硕名额" header-align="center" align="center"></el-table-column>
      <el-table-column prop="professionalQuota" label="专硕名额" header-align="center" align="center"></el-table-column>
      <el-table-column prop="phdQuota" label="直博名额" header-align="center" align="center"></el-table-column>
    </el-table>

    <!-- 学生列表 -->
    <h3>待选择学生</h3>
    <el-table :data="students" border style="width: 100%; margin-top: 20px;" :cell-style="{textAlign: 'center'}">
      <el-table-column prop="name" label="姓名" header-align="center" align="center"></el-table-column>
      <el-table-column prop="type" label="研究生类型" header-align="center" align="center"></el-table-column>
      <el-table-column label="填报学科" header-align="center" align="center">
        <template #default="scope">
          <div v-for="(subject, index) in scope.row.subjects" :key="index">
            {{ subject }}
            <el-button
                size="small"
                @click="selectStudent(scope.row, subject)"
                :type="getButtonType(scope.row, subject)"
                :disabled="isSelectDisabled(scope.row, subject)">
              {{ getSelectButtonText(scope.row, subject) }}
            </el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" header-align="center" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="viewStudentDetail(scope.row.id)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 提交按钮 -->
    <el-button type="success" @click="submitSelections" style="margin-top: 20px;">提交选择</el-button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      teachers: [
        // {
        //   id: 1,
        //   name: "张伟",
        //   subjects: [
        //     {name: "计算机科学与技术", academicQuota: 5, professionalQuota: 3, phdQuota: 2},
        //     {name: "软件工程", academicQuota: 4, professionalQuota: 2, phdQuota: 1}
        //   ]
        // },
        // {
        //   id: 2,
        //   name: "李娜",
        //   subjects: [
        //     {name: "电子工程", academicQuota: 3, professionalQuota: 4, phdQuota: 0},
        //     {name: "通信工程", academicQuota: 2,professionalQuota: 3, phdQuota: 1}
        //   ]
        // }
      ],
      students: [
        // {
        //   id: 1,
        //   name: "王强",
        //   type: "学硕",
        //   subjects: ["计算机科学与技术", "电子工程", "软件工程", "通信工程"],
        //   selectedSubject: null
        // },
        // {
        //   id: 2,
        //   name: "赵丽",
        //   type: "专硕",
        //   subjects: ["软件工程", "通信工程", "计算机科学与技术", "电子工程"],
        //   selectedSubject: null
        // },
        // {
        //   id: 3,
        //   name: "陈明",
        //   type: "直博",
        //   subjects: ["电子工程", "计算机科学与技术", "通信工程", "软件工程"],
        //   selectedSubject: null
        // }
      ],
      selectedStudents: [],
      currentTeacherId: null,
    };
  },
  computed: {
    currentTeacher() {
      return this.teachers.find(teacher => teacher.id === this.currentTeacherId) || { subjects: [] };
    },
  },
  methods: {
    // 获取老师和学生数据
    async fetchData() {
      try {
        const teacherResponse = await axios.get('/api/teachers');
        this.teachers = teacherResponse.data.data.teachers;

        const studentResponse = await axios.get('/api/students');
        this.students = studentResponse.data.data.students;
      } catch (error) {
        this.$message.error('数据加载失败');
      }
    },

    // 更新当前老师负责的学科
    updateTeacherSubjects() {
      this.selectedStudents = [];
      this.students.forEach(student => {
        student.selectedSubject = null;
      });
    },

    // 选择学生
    selectStudent(student, subject) {
      if (student.selectedSubject) {
        this.$message.warning('该学生已被选择');
        return;
      }

      const quotaField = this.getQuotaField(student.type);
      const teacherSubject = this.currentTeacher.subjects.find(s => s.name === subject);

      if (!teacherSubject || teacherSubject[quotaField] <= 0) {
        this.$message.warning('名额不足，无法选择');
        return;
      }

      teacherSubject[quotaField] -= 1;
      this.selectedStudents.push({
        teacherId: this.currentTeacherId,
        teacherName: this.currentTeacher.name,
        studentId: student.id,
        studentName: student.name,
        subject: subject,
        type: student.type,
      });
      student.selectedSubject = subject;
    },

    // 获取名额字段
    getQuotaField(type) {
      switch(type) {
        case '学硕':
          return 'academicQuota';
        case '专硕':
          return 'professionalQuota';
        case '直博':
          return 'phdQuota';
        default:
          return 'academicQuota';
      }
    },

    getSelectButtonText(student, subject) {
      if (student.selectedSubject === subject) {
        return '已选择';
      }
      return '选择';
    },

    getButtonType(student, subject) {
      if (student.selectedSubject === subject) {
        return 'success';
      }
      return 'primary';
    },

    isSelectDisabled(student, subject) {
      if (student.selectedSubject) {
        return true;
      }
      const quotaField = this.getQuotaField(student.type);
      const teacherSubject = this.currentTeacher.subjects.find(s => s.name === subject);
      return !teacherSubject || teacherSubject[quotaField] <= 0;
    },

    viewStudentDetail(studentId) {
      this.$router.push({ name: 'StudentDetail', params: { id: studentId } });
    },

    async submitSelections() {
      if (this.selectedStudents.length === 0) {
        this.$message.warning('请选择学生后再提交');
        return;
      }

      try {
        await axios.post('/api/submitSelections', { selections: this.selectedStudents });
        this.$message.success('提交成功');
      } catch (error) {
        this.$message.error('提交失败，请重试');
      }
    },
  },
  created() {
    this.fetchData();
  },
};
</script>

<style scoped>
.draw-lottery {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 16px;
}

h1 {
  margin-top: 20px;
}

.select-teacher {
  display: flex;
  align-items: center;
  margin-top: 20px;
}

.select-teacher span {
  margin-right: 10px;
}

.el-select {
  width: 200px;
}

.el-table {
  width: 100%;
}

.el-button {
  margin-right: 10px;
}
</style>