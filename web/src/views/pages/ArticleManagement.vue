<template>
  <div>
    <h2>文章管理</h2>
    <!-- 文章管理内容 -->
    <button @click="fetchArticles">获取文章列表</button>
    <ul>
      <li v-for="article in articles" :key="article.id">{{ article.title }}</li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      articles: [],
    };
  },
  methods: {
    fetchArticles() {
      // 调用 API 获取文章数据的逻辑
      this.articles = [
        { id: 1, title: '文章1' },
        { id: 2, title: '文章2' },
      ];
    },
  },
};
</script>
