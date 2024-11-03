import axios from 'axios';
import router from '@/router'; // 根据实际路径调整

const instance = axios.create({
    baseURL: 'http://139.155.158.230:17010', // 设置基本 URL
    timeout: 10000, // 设置请求超时时间
    withCredentials: true, // 允许发送 Cookie
});

// 可选：添加请求拦截器
instance.interceptors.request.use(config => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 中获取 token
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`; // 将 token 添加到请求头中
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// 可选：添加响应拦截器
instance.interceptors.response.use(response => {
    const authorization = response.headers['authorization']; // 从响应头中获取 Authorization 字段
    if (authorization) {
        const token = authorization.split(' ')[1]; // 获取 Bearer token
        sessionStorage.setItem('token', token); // 存储 token 到 sessionStorage
    }
    return response;
}, error => {
    console.log('进入错误响应拦截器', error);
    if (error.response && error.response.status === 401) {
        console.log('Token 过期');
        sessionStorage.clear(); // 清除失效的 token
        router.push('/login'); // 使用 Vue Router 跳转到登录页面
    }
    return Promise.reject(error);
});

export default instance;
