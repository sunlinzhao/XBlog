import axios from 'axios';

const instance = axios.create({
    baseURL: 'http://139.155.158.230:17010', // 设置基本 URL
    timeout: 10000, // 设置请求超时时间
    withCredentials: true, // 允许发送 Cookie
});

// 可选：添加请求拦截器
instance.interceptors.request.use(config => {
    // 在请求发送之前做些什么
    return config;
}, error => {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 可选：添加响应拦截器
instance.interceptors.response.use(response => {
    // 对响应数据做点什么
    return response;
}, error => {
    // 对响应错误做点什么
    return Promise.reject(error);
});

export default instance;
