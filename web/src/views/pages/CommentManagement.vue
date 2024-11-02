<template>
  <div>
    <h2>评论管理</h2>
    <!-- 评论管理内容 -->
    <button @click="fetchComments">获取评论列表</button>
    <ul>
      <li v-for="comment in comments" :key="comment.id">{{ comment.content }}</li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      comments: [],
    };
  },
  methods: {
    fetchComments() {
      // 调用 API 获取评论数据的逻辑
      this.comments = [
        { id: 1, content: '评论1' },
        { id: 2, content: '评论2' },
      ];
    },
  },
};
</script>
