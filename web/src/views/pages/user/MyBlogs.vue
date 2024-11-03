<template>
  <div class="my-blogs-container">
    <div class="sub-navbar">
      <el-button class="home-button" @click="goHome" type="primary">首页</el-button>
      <div class="nav-items">
        <span
            :class="{'active': currentTab === 'blogList'}"
            @click="switchTab('blogList')"
        >
          我的博客
        </span>
        <span
            :class="{'active': currentTab === 'publishBlog'}"
            @click="switchTab('publishBlog')"
        >
          博客发布
        </span>
        <span
            :class="{'active': currentTab === 'drafts'}"
            @click="switchTab('drafts')"
        >
          草稿箱
        </span>
      </div>
    </div>

    <div class="tab-content">
      <BlogList v-if="currentTab === 'blogList'" @switchTab="switchTab" />
      <PublishBlog v-if="currentTab === 'publishBlog'" />
      <DraftBlog v-if="currentTab === 'drafts'" />
    </div>
  </div>
</template>

<script>
import BlogList from '../../components/BlogList.vue';
import PublishBlog from '../../components/PublishBlog.vue';
import DraftBlog from '../../components/DraftBlog.vue';
import { ElButton } from 'element-plus';

export default {
  components: {
    BlogList,
    PublishBlog,
    DraftBlog,
    ElButton
  },
  data() {
    return {
      currentTab: 'blogList' // 初始选中的模块
    };
  },
  methods: {
    switchTab(tab) {
      this.currentTab = tab;
    },
    goHome() {
      this.$router.push('/'); // 假设首页的路径是根路径
    }
  }
};
</script>

<style scoped>
.my-blogs-container {
  padding: 20px;
}

.sub-navbar {
  display: flex;
  justify-content: space-between; /* 两端对齐 */
  align-items: center; /* 垂直居中 */
  margin-bottom: 20px;
  background-color: #34495e;
  padding: 10px 20px; /* 增加内边距 */
  border-radius: 5px;
  color: white;
}

.home-button {
  margin-right: auto; /* 将按钮推到左侧 */
}

.nav-items {
  display: flex; /* 水平布局 */
}

.nav-items span {
  cursor: pointer;
  padding: 10px 20px;
}

.nav-items .active {
  background-color: #2c3e50;
  border-radius: 5px;
}

.tab-content {
  padding: 20px;
  background-color: #ffffff;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}
</style>
