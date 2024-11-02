<template>
  <div class="auth-container">
    <img class="logo-img" alt="Vue logo" src="../assets/mylogo.png">
    <h2>邮箱登录</h2>
    <form @submit.prevent="handleLogin">
      <div class="input-group">
        <input v-model="email" type="email" placeholder="邮箱" required />
      </div>
      <div class="input-group">
        <input v-model="password" type="password" placeholder="密码" required />
      </div>
      <button type="submit">登录</button>
    </form>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    <p>还没有账号? <a href="#" @click.prevent="goToRegister">注册</a></p>
    <p><a href="#" @click.prevent="goToForgotPassword">忘记密码?</a></p>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
import instance from '../http_no_auth'; // 确保引入 axios
import md5 from 'crypto-js/md5';

export default {
  data() {
    return {
      email: '',
      password: '',
      errorMessage: '' // 用于存储错误信息
    };
  },
  methods: {
    ...mapActions(['login']),
    async handleLogin() {
      this.errorMessage = ''; // 清空错误信息
      try {
        const hashedPassword = hashPassword(this.password, this.email); // 将密码加密
        const response = await instance.post('/xblog/public/login', {
          email: this.email,
          password: hashedPassword
        });
        if (response.data.success) {
          const userData = response.data.data.data;

          // 将 token、userData 存储在 sessionStorage 中
          const token = userData.token;
          sessionStorage.setItem('userData', JSON.stringify(userData));
          sessionStorage.setItem('token', JSON.stringify(token));
          sessionStorage.setItem("loginTime", new Date().getTime().toString());

          if(userData.role === 'user')
              this.$router.push('/home'); // 跳转到首页或其他页面
          else if(userData.role === 'admin')
            this.$router.push('/admin');
        } else {
          this.errorMessage = response.data.msg; // 设置错误信息
          console.error(response.data.msg);
        }
      } catch (error) {
        this.errorMessage = '请求失败，请稍后重试。'; // 设置请求失败的错误信息
        console.error('请求失败:', error);
      }
    },
    goToRegister() {
      this.$router.push('/register'); // 跳转到注册页面
    },
    goToForgotPassword() {
      this.$router.push('/fpwd'); // 跳转到忘记密码页面
    }
  }
};

function hashPassword(password, email) {
  // 使用邮箱名字和日期作为盐
  const salt = email;
  // 组合密码和盐，然后进行 MD5 加密
  return md5(password + salt).toString();
}

</script>

<style scoped>
.auth-container {
  display: flex;
  flex-direction: column; /* 垂直排列 */
  align-items: center; /* 水平居中 */
  justify-content: center; /* 垂直居中 */
  height: 100vh; /* 使容器高度占满视口 */
}

input {
  margin: 10px 0;
  padding: 10px;
  width: 200px;
}

button {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

p {
  margin-top: 10px;
}

.error-message {
  color: red; /* 错误信息的颜色 */
}
.logo-img {
  height: 100px; /* 设置 Logo 高度 */
  width: auto; /* 保持宽高比 */
}
</style>
