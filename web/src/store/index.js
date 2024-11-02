import { createStore } from 'vuex';

// vuex 每次刷新页面就清空了，这里不用来保存用户登录状态
const store = createStore({
    state() {
        return {
            user: null, // 用户信息
            // 其他状态
        };
    },
    getters: {
        isLoggedIn(state) {
            return !!state.user; // 返回用户是否已登录
        },
        getUser(state) {
            return state.user; // 返回用户信息
        },
    },
    mutations: {
        SET_USER(state, userData) {
            state.user = userData; // 设置用户信息
        },
    },
    actions: {
        login({ commit }, userData) {
            commit('SET_USER', userData); // 更新用户信息
        },
    },
});

export default store;