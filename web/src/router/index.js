import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/EmailLogin.vue';
import Register from '../views/EmailRegister.vue';
import Home from '../views/BlogHome.vue';
import ForgotPwd from "@/views/ForgotPwd.vue";
import Admin from '@/views/AdminHome.vue';
import PublishBlog from '@/views/components/PublishBlog.vue';
import BlogDetail from "@/views/components/BlogDetail.vue";
import UnLogIn from "@/views/components/UnLogIn.vue";
import HomeRanking from "@/views/components/HomeRanking.vue";
import FeedBack from "@/views/components/FeedBack.vue";
import Community from "@/views/components/MyCommunity.vue";

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/register',
        name: 'Register',
        component: Register
    },
    {
        path: '/home',
        name: 'Home',
        component: Home,
        // meta: { requiresAuth: true, allowedRoles: ['user', 'admin'] }, // 需要认证的元信息
    },
    {
        path: '/fpwd',
        name: 'ForgotPassword',
        component: ForgotPwd
    },
    {
        path: '/admin',
        name: 'Admin',
        component: Admin,
        meta: { requiresAuth: true, allowedRoles: ['admin'] }, // 需要认证的元信息
    },
    {
        path: '/my-blogs',
        name: 'MyBlogs',
        component: () => import('../views/pages/user/MyBlogs.vue')
    },
    {
        path: '/publish/:postId?',
        name: 'PublishBlog',
        component: PublishBlog
    },
    {
        path: '/blog/:postId',
        name: 'BlogDetail',
        component: BlogDetail,
    },
    {
        path: '/unlog-in',
        name: 'UnLogIn',
        component: UnLogIn
    },
    {
        path: '/ranking',
        name: 'Ranking',
        component: HomeRanking
    },
    {
        path: '/feedback',
        name: 'Feedback',
        component: FeedBack,
    },
    {
        path: '/community',
        name: 'Community',
        component: Community,
    },

    // 其他路由
    {
        path: '/',
        redirect: '/home'
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// 添加路由守卫
router.beforeEach((to, from, next) => {
    const token = sessionStorage.getItem("token");
    // 获取并解析为对象
    const userDataStr = sessionStorage.getItem("userData");
    let userData = null;
    try {
        // 将字符串解析为对象
        userData = JSON.parse(userDataStr);
    } catch (error) {
        console.error("解析 userData 失败：", error);
    }
    // 确保 userRole 是一个数组，避免 undefined 问题
    const userRole = (userData && userData.role) ? userData.role : [];

    if (to.meta.requiresAuth && !token) {
        // 如果需要认证但用户未登录，重定向到登录页面
        next({ path: '/login' });
    } else
        if (to.meta.requiresAuth && token) {
        // 如果用户已登录，检查角色
        if (to.meta.allowedRoles && !to.meta.allowedRoles.some(role => userRole.includes(role))) {
            // 如果用户角色不在允许访问的角色列表中，重定向到首页
            next({ path: '/home' });
        } else {
            next(); // 允许访问
        }
    } else {
        next(); // 允许访问
    }
});


export default router;
