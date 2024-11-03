<template>
  <div class="feedback-container">
    <sidebar></sidebar>
    <div class="feedback-content">
      <h1>反馈意见</h1>
      <el-input
          type="textarea"
          v-model="feedbackContent"
          placeholder="请输入您的反馈意见"
          rows="5"
          class="feedback-input"
      ></el-input>
      <el-button type="primary" @click="sendFeedback">发送</el-button>
      <p class="github-link">
        <a :href="githubLink" target="_blank">查看GitHub</a>
      </p>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/views/components/UserSiderBar.vue';
import instance from "@/http_no_auth";

export default {
  components: {
    Sidebar,
  },
  data() {
    return {
      feedbackContent: '',
      githubLink: 'https://github.com/sunlinzhao/XBlog',
      isLoggedIn: !!sessionStorage.getItem('userData') // 判断用户是否登录
    };
  },
  methods: {
    async sendFeedback() {
      if (!this.feedbackContent) {
        this.$message.warning('反馈内容不能为空');
        return;
      }
      try {
        const response = await instance.post('/xblog/public/feedback', {
          userId: this.isLoggedIn ? JSON.parse(sessionStorage.getItem('userData')).userId : '2',
          content: this.feedbackContent,
        });
        if (response.data.success) {
          this.$message.success('反馈提交成功');
          this.feedbackContent = ''; // 清空输入框
        } else {
          this.$message.error('反馈提交失败，请重试');
        }
      } catch (error) {
        console.error('发送反馈失败:', error);
        this.$message.error('发送反馈失败，请重试');
      }
    },
  },
};
</script>

<style scoped>
.feedback-container {
  display: flex;
}

.feedback-content {
  flex-grow: 1;
  padding: 20px;
  background-color: #fff;
  margin-left: 150px; /* 确保不与侧边栏重叠 */
}

.feedback-input {
  margin-bottom: 20px;
}

.github-link {
  margin-top: 20px;
}
</style>
