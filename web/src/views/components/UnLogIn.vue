<template>
  <div class="login-prompt">
    <h1>您还未登录</h1>
    <p>请登录使用该功能！</p>
    <button @click="navigateToLogin" class="login-button">去登录</button>
  </div>
</template>

<script>
export default {
  methods: {
    navigateToLogin() {
      this.$router.push('/login'); // 跳转到登录页面
    },
  },
};
</script>

<style scoped>
.login-prompt {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh; /* 全屏居中 */
  background-color: #f7f7f7; /* 背景颜色 */
  color: #333; /* 字体颜色 */
}

.login-button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #3498db; /* 按钮背景色 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.login-button:hover {
  background-color: #2980b9; /* 悬停时的背景色 */
}
</style>
