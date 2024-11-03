<template>
  <div class="community-container">
    <sidebar></sidebar>
    <div class="community-content">
      <h1 class="title">社区</h1>
      <div class="search-container">
        <el-input
            v-model="searchTitle"
            placeholder="搜索文章标题"
            class="search-input"
        ></el-input>
        <el-input
            v-model="searchUsername"
            placeholder="搜索作者"
            class="search-input"
        ></el-input>
        <el-input
            v-model="searchTag"
            placeholder="搜索标签"
            class="search-input"
        ></el-input>
        <el-button type="primary" @click="searchArticles" class="search-button">搜索</el-button>
      </div>
      <div class="article-list">
        <h2>搜索结果</h2>
        <div v-if="articles.length === 0" class="no-results">
          暂无匹配结果
        </div>
        <div v-for="article in articles" :key="article.postId" class="article-item" @click="navigateToArticle(article.postId)">
          <h3 class="article-title">{{ article.title }}</h3>
          <p class="article-meta">作者: {{ article.userName }} | 标签: {{ article.tagNames }}</p>
          <p class="article-stats">浏览量: {{ article.viewCount }} | 更新时间: {{ formatDate(article.lastUpdateTime) }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/views/components/UserSiderBar.vue';
import instance from "@/http_no_auth";

export default {
  components: {
    Sidebar,
  },
  data() {
    return {
      searchTitle: '',
      searchUsername: '',
      searchTag: '',
      articles: [],
    };
  },
  methods: {
    async searchArticles() {
      try {
        const response = await instance.post('/xblog/public/post/search', {
          title: this.searchTitle,
          username: this.searchUsername,
          tagName: this.searchTag,
        });

        if (response.data.success) {
          this.articles = response.data.data.data; // 更新文章列表
        } else {
          this.$message.error('搜索失败，请重试');
        }
      } catch (error) {
        console.error('搜索文章失败:', error);
        this.$message.error('搜索文章失败，请重试');
      }
    },
    navigateToArticle(postId) {
      this.$router.push({ name: 'BlogDetail', params: { postId } });
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleString(); // 格式化日期
    },
  },
};
</script>

<style scoped>
.community-container {
  display: flex;
  flex-wrap: nowrap;
}

.community-content {
  flex-grow: 1;
  padding: 20px;
  background-color: #f9f9f9;
  margin-left: 130px; /* 确保不与侧边栏重叠 */
  border-radius: 8px; /* 圆角效果 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

.title {
  font-size: 2rem;
  margin-bottom: 20px;
  color: #333;
}

.search-container {
  display: flex;
  margin-bottom: 20px;
}

.search-input {
  margin-right: 10px;
  flex: 1; /* 使输入框占据剩余空间 */
}

.search-button {
  height: 38px; /* 确保按钮高度一致 */
}

.article-list {
  margin-top: 20px;
}

.article-item {
  padding: 15px;
  border-radius: 5px; /* 圆角效果 */
  border: 1px solid #ddd;
  margin-bottom: 10px;
  cursor: pointer;
  background-color: #fff; /* 背景色 */
  transition: background-color 0.3s, transform 0.2s;
}

.article-item:hover {
  background-color: #f0f8ff; /* 悬停背景色 */
  transform: translateY(-2px); /* 上移效果 */
}

.article-title {
  font-size: 1.5rem;
  color: #007BFF; /* 链接颜色 */
  margin: 0;
}

.article-meta,
.article-stats {
  color: #666; /* 元数据颜色 */
  margin: 5px 0; /* 上下间距 */
}

.no-results {
  color: #999;
  text-align: center; /* 使文本居中 */
  font-size: 1.2rem;
}

/* 媒体查询示例 */
@media (max-width: 768px) {
  .community-content {
    margin-left: 0; /* 小屏幕时取消边距 */
  }

  .article-item {
    padding: 10px; /* 调整小屏幕的内边距 */
  }

  .title {
    font-size: 1.5rem; /* 调整标题字体大小 */
  }
}
</style>
