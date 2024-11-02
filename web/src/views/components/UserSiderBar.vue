<template>
  <aside class="sidebar">
    <div class="logo">
      <img alt="Vue logo" src="../../assets/mylogo.png" class="logo-image">
    </div>
    <h2>菜单</h2>
    <ul>
      <li @click="navigateToHome">首页</li>
      <li @click="navigateToMyBlogs">我的博客</li>
      <li @click="navigateToRanking">排名</li>
      <li @click="navigateToCommunity">社区</li>
      <li @click="navigateToFeedback">反馈</li>
    </ul>
    <button @click="handleAuth" class="logout-button">
      {{ isLoggedIn ? '登出' : '登录' }}
    </button>
  </aside>
</template>

<script>
import instance from "@/http";

export default {
  data() {
    return {
      isLoggedIn: false,
    };
  },
  created() {
    this.checkLoginStatus();
  },
  methods: {
    navigateToHome() {
      this.$router.push('/home');
    },
    navigateToMyBlogs() {
      this.checkLoginAndNavigate('/my-blogs');
    },
    navigateToRanking() {
      this.$router.push('/ranking');
    },
    navigateToCommunity() {
      this.$router.push('/community');
    },
    navigateToFeedback() {
      this.$router.push('/feedback');
    },
    async checkLoginStatus() {
      this.isLoggedIn = !!sessionStorage.getItem('userData');
    },
    async checkLoginAndNavigate(route) {
      if (this.isLoggedIn) {
        this.$router.push(route);
      } else {
        this.$router.push('/unlog-in');
      }
    },
    async handleAuth() {
      if (this.isLoggedIn) {
        await this.logout();
      } else {
        this.$router.push('/login');
      }
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
  },
};
</script>

<style scoped>
.sidebar {
  position: fixed;
  width: 95px;
  height: 95%; /* 确保侧边栏高度占满 */
  background-color: #2c3e50; /* 深色背景 */
  color: #ecf0f1; /* 字体颜色 */
  padding: 20px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  top: 0;
  left: 0;
  z-index: 10;
}

.logo {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.logo-image {
  width: 70px; /* 设置logo大小 */
  height: auto; /* 自适应高度 */
}

.sidebar h2 {
  text-align: center;
  margin: 20px 0;
  font-size: 1.5rem; /* 增加标题字体大小 */
}

.sidebar ul {
  list-style: none;
  padding: 0;
  flex-grow: 1;
}

.sidebar li {
  padding: 10px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s; /* 增加颜色过渡 */
}

.sidebar li:hover {
  background-color: #34495e; /* 悬停时的颜色变化 */
  color: #ecdbff; /* 悬停时的字体颜色变化 */
}

.logout-button {
  margin-top: 20px;
  padding: 10px 0;
  background-color: #e74c3c; /* 登出按钮颜色 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  width: 100%;
  font-size: 1rem; /* 增加按钮字体大小 */
  transition: background-color 0.3s; /* 增加按钮悬停效果 */
}

.logout-button:hover {
  background-color: #c0392b; /* 悬停时的按钮颜色 */
}
</style>
