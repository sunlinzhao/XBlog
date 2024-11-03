import { createApp } from 'vue'; // 使用 createApp 导入
import App from './App.vue';
import router from './router'; // 引入路由
import http from './http'; // 导入 Axios 实例
import store from './store';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
// import heartbeat from './heartbeat';


const app = createApp(App); // 创建 Vue 应用
app.config.globalProperties.$http = http; // 将 Axios 实例添加到全局属性
app.use(ElementPlus); // 使用 ElementPlus
app.use(store); // 使用 Vuex
app.use(router); // 使用路由
// 这里改成使用 HttpSession 实现统计在线人数
// app.use(heartbeat); // 使用心跳检测
app.mount('#app'); // 挂载应用

