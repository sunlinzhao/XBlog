<template>
  <div class="app-container">
    <Sidebar @navigate="navigate" @logout="logout" />

    <main class="main-content">
      <div class="content">
        <component :is="currentView"></component>
      </div>
    </main>
  </div>
</template>

<script>
import Sidebar from './components/AdminSideBar.vue';
import UserManagement from './pages/UserManagement.vue';
import ArticleManagement from './pages/ArticleManagement.vue';
import CommentManagement from './pages/CommentManagement.vue';
import instance from '@/http';

export default {
  components: {
    Sidebar,
    UserManagement,
    ArticleManagement,
    CommentManagement,
  },
  data() {
    return {
      currentView: 'UserManagement', // 默认显示用户管理
    };
  },
  methods: {
    navigate(view) {
      this.currentView = view; // 切换视图
    },
    async logout() {
      try {
        const response = await instance.post('/xblog/user/logout');

        if (response.data.success) {
          sessionStorage.clear();
          this.isLoggedIn = false;
          this.$router.push('/login');
        } else {
          sessionStorage.clear();
          this.$router.push('/login');
        }
      } catch (error) {
        console.error('登出失败:', error);
        this.$message.error('登出失败，请重试');
      }
    },
  }
};
</script>

<style scoped>
html, body {
  height: 100%; /* 确保 html 和 body 高度为100% */
  margin: 0; /* 清除默认边距 */
}

.app-container {
  display: flex;
  height: 100%; /* 确保容器高度为100% */
  font-family: Arial, sans-serif; /* 使用清晰的字体 */
}

.main-content {
  flex: 1;
  height: 100%; /* 填充整个页面 */
  margin-left: 160px;
  padding: 20px;
  background-color: #ecf0f1; /* 主内容区域背景 */
  display: flex;
  flex-direction: column; /* 垂直排列主内容区域内容 */
}

.content {
  margin-top: 20px;
  padding: 20px;
  background-color: #ffffff; /* 内容区域背景 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 更显著的阴影效果 */
  flex-grow: 1; /* 让内容区域占满剩余空间 */
}
</style>
