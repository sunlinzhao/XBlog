<template>
  <div class="reset-password-container">
    <h2>重置密码</h2>
    <form @submit.prevent="resetPassword">
      <div class="input-group">
        <input v-model="email" type="email" placeholder="邮箱" required />
      </div>
      <div class="input-group">
        <input v-model="verificationCode" type="text" placeholder="验证码" required />
      </div>
      <div class="input-group">
        <input v-model="newPassword" type="password" placeholder="新密码" required />
      </div>
      <div class="input-group">
        <input v-model="confirmPassword" type="password" placeholder="确认密码" required />
      </div>
      <div class="button-group">
        <button type="button" @click="sendVerificationCode" :disabled="isCountingDown">
          {{ isCountingDown ? countdown : '发送验证码' }}
        </button>
        <button type="submit">确认修改</button>
      </div>
    </form>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    <p><a href="#" @click.prevent="goToLogin">返回登录</a></p>
  </div>
</template>

<script>
import instance from '../http_no_auth';
import md5 from "crypto-js/md5";
export default {
  data() {
    return {
      email: '',
      verificationCode: '',
      newPassword: '',
      confirmPassword: '',
      errorMessage: '',
      isCountingDown: false,
      countdown: 60,
      countdownInterval: null,
    };
  },
  methods: {
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
        const response = await instance.post('/xblog/public/sendCode', {
          email: this.email
        });
        if (response.data.success){
          this.errorMessage = response.data.msg;
          console.log(response.data.msg);
        }
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
    },
    async resetPassword() {
      // 清空错误信息
      this.errorMessage = '';

      // 判断两次输入的密码是否一致
      if (this.newPassword !== this.confirmPassword) {
        this.errorMessage = '两次输入的密码不一致，请重新输入。';
        return; // 不发送请求
      }

      // 发送重置密码请求
      try {
        const hashedPassword = hashPassword(this.newPassword, this.email); // 将密码加密
        const response = await instance.post('/xblog/public/resetPwd', { // 在这里添加 await
          email: this.email, // 传递邮箱
          newPassword: hashedPassword, // 传递新密码
          code: this.verificationCode // 传递验证码
        });

        if (response.data.success) {
          // 处理成功逻辑，例如跳转到登录页面
          this.errorMessage = '重置密码成功，3 秒后返回到登录页面...';
          setTimeout(() => {
            this.goToLogin(); // 跳转到登录页面
          }, 3000); // 3秒后跳转
        } else {
          this.errorMessage = response.data.msg; // 设置错误信息
        }
      } catch (error) {
        this.errorMessage = '重置密码请求失败，请稍后重试。'; // 设置请求失败的错误信息
        console.error('请求失败:', error);
      }
    },
    goToLogin() {
      this.$router.push('/login');
    },
  },
  // beforeDestroy() {
  //   if (this.countdownInterval) {
  //     clearInterval(this.countdownInterval);
  //   }
  // },

};
function isValidEmail(email) {
  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return emailPattern.test(email);
}
function hashPassword(password, email) {
  // 使用邮箱名字和日期作为盐
  const salt = email;
  // 组合密码和盐，然后进行 MD5 加密
  return md5(password + salt).toString();
}
</script>

<style scoped>
.reset-password-container {
  display: flex;
  flex-direction: column;
  align-items: center; /* 垂直居中 */
}

.input-group {
  display: flex;
  justify-content: center; /* 水平居中 */
  margin: 10px 0; /* 保留上下间距 */
}

input {
  padding: 10px;
  width: 200px; /* 设置固定宽度 */
  border: 1px solid #ccc;
  border-radius: 4px;
  margin: 0; /* 不留边距 */
  box-sizing: border-box; /* 包括边框和内边距在宽度计算内 */
}

.error-message {
  color: red; /* 错误信息的颜色 */
}

p {
  margin-top: 10px; /* 段落之间的间距 */
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
</style>
