<template>
  <div class="blog-detail-container">
    <el-row class="navbar">
      <el-col>
        <el-button type="text" @click="goBack" icon="el-icon-arrow-left">返回</el-button>
      </el-col>
      <el-col>
        <h1 class="blog-title">{{ blog.title }}</h1>
      </el-col>
    </el-row>

    <div class="blog-meta">
      <div class="meta-info">
        <span class="blog-author">作者: <span class="content">{{ blog.userName }} | </span></span>
        <span class="blog-status"> 状态: <span class="content">{{ getStatusDisplay(blog.status) }} | </span></span>
        <span class="blog-view"> 浏览量: <span class="content">{{ blog.viewCount }} | </span></span>
        <span class="blog-updated"> 更新时间: <span class="content">{{ formatLastUpdateTime(blog.lastUpdateTime) }} | </span></span>
        <span class="blog-tags" v-if="blog.tagNames"> 标签: <span class="content">{{ blog.tagNames }}</span></span>
      </div>
    </div>

    <div class="blog-content-container">
      <div class="content-mk">
        <div class="blog-content markdown-body" v-html="formattedContent"></div>
      </div>
      <div class="comment-section">
        <h2>评论</h2>
        <div class="comment-input-container">
          <el-input
              type="textarea"
              v-model="commentContent"
              placeholder="请输入评论内容"
              rows="1"
              class="comment-input"
          ></el-input>
          <el-button
              type="primary"
              @click="submitComment"
              :disabled="!isLoggedIn"
          >
            提交评论
          </el-button>
        </div>
        <div class="comments-list">
          <div v-for="(comment) in comments" :key="comment.commentId" class="comment-item">
            <strong>{{ comment.userName }}:</strong>
            <div
                :class="['comment-content', { expanded: comment.expanded }]"
                @click="toggleExpand(comment)"
            >
              {{ comment.content }}
            </div>
            <span class="comment-time">（{{ formatCommentTime(comment.createTime) }}）</span>
          </div>
        </div>
        <div v-if="!isLoggedIn" class="login-warning">
          请登录后评论
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import instance from '../../http_no_auth';
import { marked } from 'marked';
import 'github-markdown-css/github-markdown.css';

export default {
  data() {
    return {
      blog: {
        title: '',
        userName: '',
        status: '',
        lastUpdateTime: '',
        content: '',
        tagNames: '',
      },
      commentContent: '',
      comments: [], // 含有 expanded 属性的评论
      isLoggedIn: !!sessionStorage.getItem('userData') // 判断用户是否登录
    };
  },
  computed: {
    formattedContent() {
      return this.blog.content ? marked(this.blog.content) : '';
    },
  },
  methods: {
    fetchBlogDetail() {
      const postId = this.$route.params.postId;
      instance.post(`/xblog/public/post/one`, {
        postId: postId,
      })
          .then(response => {
            if (response.data.success) {
              this.blog = response.data.data.data;
              this.fetchComments(); // 获取评论
            } else {
              this.$message.error(response.data.msg);
            }
          })
          .catch(error => {
            console.error('获取博客详情失败:', error);
            this.$message.error('获取博客详情失败，请重试');
          });
    },
    fetchComments() {
      const postId = this.$route.params.postId;
      instance.post(`/xblog/public/comment/list`, { postId: postId })
          .then(response => {
            if (response.data.success) {
              this.comments = response.data.data.data.map(comment => ({
                ...comment,
                expanded: false // 初始化为未展开
              }));
              // 按评论时间从新到旧排序
              this.comments.sort((a, b) => new Date(b.createTime) - new Date(a.createTime));
            } else {
              this.$message.error(response.data.msg);
            }
          })
          .catch(error => {
            console.error('获取评论失败:', error);
          });
    },
    submitComment() {
      const postId = this.$route.params.postId;
      const userId = this.isLoggedIn ? JSON.parse(sessionStorage.getItem('userData')).userId : '2';

      if (!this.commentContent) {
        this.$message.warning('评论内容不能为空');
        return;
      }

      instance.post(`/xblog/public/comment/add`, {
        postId: postId,
        userId: userId,
        content: this.commentContent
      })
          .then(response => {
            if (response.data.success) {
              this.$message.success('评论提交成功');
              this.commentContent = ''; // 清空评论内容
              this.fetchComments(); // 重新获取评论
            } else {
              this.$message.error(response.data.msg);
            }
          })
          .catch(error => {
            console.error('提交评论失败:', error);
            this.$message.error('提交评论失败，请重试');
          });
    },
    toggleExpand(comment) {
      comment.expanded = !comment.expanded; // 切换展开状态
    },
    getStatusDisplay(status) {
      return status === '0' ? '草稿' : '已发布';
    },
    formatLastUpdateTime(lastUpdateTime) {
      const date = new Date(lastUpdateTime);
      return date.toLocaleString();
    },
    formatCommentTime(createTime) {
      const date = new Date(createTime);
      return date.toLocaleString(); // 可以自定义格式
    },
    goBack() {
      this.$router.go(-1);
    },
  },
  mounted() {
    this.fetchBlogDetail();
  },
};
</script>

<style scoped>
.blog-detail-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
}

.navbar {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.blog-title {
  font-size: 24px;
  margin-left: 20px;
  flex-grow: 1;
  color: black;
}

.blog-meta {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 15px;
}

.meta-info {
  display: flex;
  justify-content: center;
  font-size: 14px;
  color: #999;
  margin-bottom: 5px;
}

.blog-content-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.content-mk {
  width: 70%;
}

.comment-section {
  margin-top: 5px;
  padding: 10px;
  border: 2px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
  width: 28%;
}

.comment-input-container {
  display: flex;
  align-items: center;
  width: 100%;
}

.comment-input {
  margin-right: 10px;
  flex-grow: 1;
}

.comments-list {
  width: 100%;
}

.comment-item {
  margin-bottom: 10px;
  border-bottom: 1px solid #ddd;
  padding: 5px 0;
  text-align: left; /* 确保文本左对齐 */
}

.comment-content {
  max-height: 3em; /* 限制为三行 */
  overflow: hidden; /* 隐藏多余内容 */
  transition: max-height 0.3s ease; /* 动画效果 */
}

.comment-content.expanded {
  max-height: none; /* 展开后不限制高度 */
}

.login-warning {
  color: red;
  margin-top: 10px;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.markdown-body {
  margin-left: 20px;
  padding: 15px;
  text-align: justify;
  border: 2px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
  overflow: auto;
}
</style>
