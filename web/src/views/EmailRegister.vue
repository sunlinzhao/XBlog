<template>
  <div class="auth-container">
    <img class="logo-img" alt="Vue logo" src="../assets/mylogo.png">
    <h2>邮箱注册</h2>
    <form @submit.prevent="handleRegister">
      <div class="input">
        <input v-model="email" type="email" placeholder="邮箱" required />
      </div>
      <div class="input">
        <input v-model="verificationCode" type="text" placeholder="验证码" required />
      </div>
      <div class="input">
        <input v-model="userName" type="text" placeholder="用户名" required />
      </div>
      <div class="input">
        <input v-model="password" type="password" placeholder="密码" required />
      </div>
      <div class="input">
        <input v-model="confirmPassword" type="password" placeholder="确认密码" required />
      </div>
      <div class="button-group">
        <button type="button" @click="sendVerificationCode" :disabled="isCountingDown">
          {{ isCountingDown ? countdown : '发送验证码' }}
        </button>
        <button type="submit">注册</button>
      </div>
    </form>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    <p>已经有账号? <a href="#" @click.prevent="goToLogin">登录</a></p>
  </div>
</template>

<script>
import instance from '../http_no_auth';
import md5 from 'crypto-js/md5';

export default {
  data() {
    return {
      email: '',
      verificationCode: '',
      userName: '',
      password: '',
      confirmPassword: '',
      errorMessage: '', // 用于存储错误信息
      isCountingDown: false,
      countdown: 60,
      countdownInterval: null
    };
  },
  methods: {
    async handleRegister() {
      // 清空错误信息
      this.errorMessage = '';

      // 验证每个输入框不能为空
      if (!this.email || !this.verificationCode || !this.userName || !this.password || !this.confirmPassword) {
        this.errorMessage = '所有字段都不能为空。';
        return;
      }

      // 检查密码是否一致
      if (this.password !== this.confirmPassword) {
        this.errorMessage = '两次输入的密码不一致，请重新输入。';
        return;
      }

      try {
        const hashedPassword = hashPassword(this.password, this.email); // 将密码加密
        const response = await instance.post('/xblog/public/register', {
          email: this.email,
          code: this.verificationCode,
          userName: this.userName,
          password: hashedPassword
        });

        if (response.data.success) {
          // 注册成功后跳转到登录页
          this.$router.push('/login');
        } else {
          this.errorMessage = response.data.msg; // 设置错误信息
          console.error(response.data.msg);
        }
      } catch (error) {
        console.error('请求失败:', error);
      }
    },
    goToLogin() {
      this.$router.push('/login'); // 跳转到登录页面
    },
    async sendVerificationCode() {
      if (!this.email) {
        this.errorMessage = '请填写邮箱';
        return;
      }

      this.errorMessage = '';

      // 检查邮箱是否合法
      if (!isValidEmail(this.email)) {
        this.errorMessage = '请输入合法的邮箱地址。';
        return;
      }
      // 发送验证码逻辑
      try {
        // 这里调用发送验证码的 API
        await instance.post('/xblog/public/sendCode', {
          email: this.email
        });

        // 开始倒计时
        this.isCountingDown = true;
        this.countdown = 60;
        this.countdownInterval = setInterval(() => {
          this.countdown--;
          if (this.countdown <= 0) {
            clearInterval(this.countdownInterval);
            this.isCountingDown = false;
          }
        }, 1000);
      } catch (error) {
        this.errorMessage = '发送验证码失败，请稍后重试。';
      }
    }
  }
};

function isValidEmail(email) {
  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return emailPattern.test(email);
}
function hashPassword(password, email) {
  // 使用邮箱名字作为盐
  const salt = email;
  // 组合密码和盐，然后进行 MD5 加密
  return md5(password + salt).toString();
}
</script>

<style scoped>
.auth-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

input {
  margin: 10px 0;
  padding: 10px;
  width: 200px;
}

.button-group {
  display: flex;
  justify-content: center; /* 中间对齐 */
  margin: 10px 0; /* 上下间距 */
}

.button-group button {
  padding: 10px 20px; /* 按钮内部间距 */
  width: 120px; /* 设置统一的宽度 */
  margin: 0 10px; /* 左右间隔 */
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px; /* 统一字体大小 */
}

.button-group button:hover {
  background-color: #45a049; /* 悬停时的背景色 */
}

.button-group button:disabled {
  background-color: #ccc; /* 禁用状态的背景色 */
  cursor: not-allowed; /* 禁用状态的光标样式 */
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
