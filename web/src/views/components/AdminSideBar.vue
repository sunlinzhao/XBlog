<template>
  <aside class="sidebar">
    <div class="logo">
      <img alt="Vue logo" src="../../assets/mylogo.png" class="logo-image">
    </div>
    <h2>后台管理</h2>
    <ul>
      <li @click="navigate('UserManagement')">用户管理</li>
      <li @click="navigate('ArticleManagement')">文章管理</li>
      <li @click="navigate('CommentManagement')">评论管理</li>
      <li @click="navigate('TagManagement')">标签管理</li> <!-- 添加标签管理 -->
      <li @click="navigate('FeedbackManagement')">反馈管理</li> <!-- 添加反馈管理 -->
    </ul>
    <button @click="logout" class="logout-button">登出</button>
  </aside>
</template>

<script>
import instance from "@/http";

export default {
  methods: {
    navigate(view) {
      this.$emit('navigate', view);
    },
    async logout() {
      try {
        const response = await instance.post('/xblog/user/logout');

        if (response.data.success) {
          sessionStorage.clear();
          this.$router.push('/login');
        } else {
          this.$message.error(response.data.msg);
        }
      } catch (error) {
        console.error('登出失败:', error);
        this.$message.error('登出失败，请重试');
        this.$router.push('/login');
      }
    },
  }
};
</script>

<style scoped>
.sidebar {
  position: fixed; /* 固定定位 */
  width: 120px; /* 侧边栏宽度 */
  height: 95%; /* 高度占满整个视口 */
  background-color: #2c3e50; /* 深色背景 */
  color: #ecf0f1; /* 浅色字体 */
  padding: 20px;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.2); /* 加强阴影效果 */
  display: flex;
  flex-direction: column; /* 垂直排列侧边栏内容 */
  justify-content: space-between; /* 在空间中分布 */
}

.logo {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.logo-image {
  width: 80px; /* 设置logo大小 */
  height: auto; /* 自适应高度 */
}

.sidebar h2 {
  text-align: center;
  margin: 20px 0;
  font-size: 1.5rem; /* 增加标题字体大小 */
  color: #ecdbff; /* 标题颜色 */
}

.sidebar ul {
  list-style: none;
  padding: 0;
  flex-grow: 1; /* 让列表占满剩余空间 */
}

.sidebar li {
  padding: 12px 15px; /* 增加上下内边距 */
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s; /* 添加颜色过渡效果 */
  border-radius: 4px; /* 圆角效果 */
}

.sidebar li:hover {
  background-color: #34495e; /* 悬停时的颜色变化 */
  color: #ecdbff; /* 悬停时的字体颜色变化 */
}

.logout-button {
  margin-top: 20px;
  padding: 12px; /* 增加内边距 */
  background-color: #e74c3c; /* 登出按钮背景色 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  width: 100%; /* 按钮宽度100% */
  font-size: 1rem; /* 按钮字体大小 */
  transition: background-color 0.3s; /* 添加过渡效果 */
}

.logout-button:hover {
  background-color: #c0392b; /* 悬停时的背景色 */
}
</style>
