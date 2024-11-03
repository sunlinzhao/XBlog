<template>
  <div class="blog-list-container">
    <el-card
        v-for="blog in blogs"
        :key="blog.postId"
        class="blog-card"
        @click="goToDetail(blog.postId)"
    >
    <div class="blog-info">
      <div class="blog-header">
        <h4 class="blog-title" :title="blog.title">{{ blog.title }}</h4>
        <div class="blog-actions">
          <el-button @click.stop="editBlog(blog.postId)" type="primary" class="edit-button">修改</el-button>
          <el-button @click.stop="deleteBlog(blog)" type="danger" class="delete-button">删除</el-button>
        </div>
      </div>
      <div class="blog-details">
          <span class="blog-author" :title="'作者: ' + blog.userName">
            <span class="label">作者:</span>
            <span class="value">{{ truncateText(blog.userName, 15) }}</span>
          </span>
        <span class="blog-status" :title="'状态: ' + getStatusDisplay(blog.status)">
            <span class="label">状态:</span>
            <span class="value">{{ getStatusDisplay(blog.status) }}</span>
          </span>
        <span class="blog-tags" :title="'标签: ' + blog.tagNames">
            <span class="label">标签:</span>
            <span class="value">{{ truncateText(blog.tagNames, 20) }}</span>
          </span>
        <span class="blog-tags" :title="'浏览量: ' + blog.viewCount">
            <span class="label">浏览量:</span>
            <span class="value">{{ truncateText(blog.viewCount, 20) }}</span>
          </span>
        <span class="blog-updated" :title="'更新时间: ' + formatLastUpdateTime(blog.lastUpdateTime)">
            <span class="label">更新时间:</span>
            <span class="value">{{ formatLastUpdateTime(blog.lastUpdateTime) }}</span>
          </span>
      </div>
    </div>
    </el-card>

    <el-pagination
        background
        v-if="total > 0"
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        @update:current-page="pageChange"
        @update:page-size="pageChange"
        :page-sizes="[5, 10, 15]"
        layout="total, prev, pager, next, jumper, sizes"
        class="pagination"
    />
  </div>
</template>

<script>
import instance from '../../http';
import { ElMessage, ElCard, ElButton, ElPagination, ElMessageBox } from 'element-plus';

export default {
  components: {
    ElCard,
    ElButton,
    ElPagination,
  },
  data() {
    return {
      blogs: [],
      currentPage: 1,
      pageSize: 5,
      total: 0,
    };
  },
  methods: {
    fetchBlogs() {
      const userId = sessionStorage.getItem('userData') ? JSON.parse(sessionStorage.getItem('userData')).userId : null;
      instance.post('/xblog/user/post/page', {
        userId: userId,
        pageNum: this.currentPage,
        pageSize: this.pageSize,
      })
          .then(response => {
            if (response.data.success) {
              this.blogs = response.data.data.data.list || []; // 确保有默认值
              this.total = response.data.data.data.total || 0; // 确保有默认值
            } else {
              ElMessage.error(response.data.msg);
            }
          })
          .catch(error => {
            console.error('获取博客列表失败:', error);
          });
    },
    goToDetail(postId) {  // 跳转到详情页的方法
      this.$router.push({ name: 'BlogDetail', params: { postId: postId } });
    },
    editBlog(postId) {
      this.$router.push({ name: 'PublishBlog', params: { postId: postId } });
    },
    deleteBlog(blog) {
      ElMessageBox.confirm('你确定要删除这篇博客吗?', '确认删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
          .then(() => {
            instance.post(`/xblog/user/post/del`, {
              postId: blog.postId,
              tagIds: blog.tagIds,
              userId: sessionStorage.getItem('userData') ? JSON.parse(sessionStorage.getItem('userData')).userId : null,
            })
                .then(() => {
                  this.blogs = this.blogs.filter(item => item.postId !== blog.postId);
                  this.total--;
                  ElMessage.success('删除成功');
                })
                .catch(error => {
                  console.error('删除博客失败:', error);
                  ElMessage.error('删除失败，请重试');
                });
          })
          .catch(() => {
            ElMessage.info('已取消删除');
          });
    },
    getStatusDisplay(status) {
      return status === '0' ? '草稿' : '已发布';
    },
    pageChange(page) {
      this.currentPage = page;
      this.fetchBlogs();
    },
    formatLastUpdateTime(lastUpdateTime) {
      const date = new Date(lastUpdateTime);
      return date.toLocaleString(); // 格式化为本地时间字符串
    },
    truncateText(text, maxLength) {
      if (text.length > maxLength) {
        return text.slice(0, maxLength) + '...'; // 添加省略号
      }
      return text;
    },
  },
  mounted() {
    this.fetchBlogs();
  },
};
</script>

<style scoped>
.blog-list-container {
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
}

.blog-card {
  margin-bottom: 4px; /* 增加底部间距 */
  padding: 2px; /* 增加内边距 */
  border: 3px solid #e0e0e0;
  border-radius: 5px;
  transition: transform 0.2s;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.blog-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.blog-info {
  display: flex;
  flex-direction: column;
}

.blog-header {
  display: flex;
  justify-content: space-between; /* 均匀排列标题和按钮 */
  align-items: center; /* 垂直居中对齐 */
}

.blog-title {
  font-size: 18px; /* 增大标题字体大小 */
  margin: 0;
  color: #34495e;
  overflow: hidden; /* 隐藏溢出文本 */
  white-space: nowrap; /* 不换行 */
  text-overflow: ellipsis; /* 省略号 */
}

.blog-details {
  display: flex; /* 水平排列作者、状态、标签和更新时间 */
  font-size: 12px; /* 增加详情字体大小 */
  color: #666;
  align-items: baseline; /* 垂直居中对齐 */
  justify-content: space-between; /* 保持标签的左右对齐 */
  margin-top: 5px; /* 上边距 */
}

.label {
  font-weight: bold; /* 加粗标签 */
  color: #333; /* 标签颜色 */
  margin-right: 5px; /* 标签与内容间的间距 */
}

.value {
  color: #666; /* 内容颜色 */
  margin-right: 15px; /* 标签内容间距 */
  overflow: hidden; /* 隐藏溢出文本 */
  white-space: nowrap; /* 不换行 */
  text-overflow: ellipsis; /* 省略号 */
}

.blog-updated {
  color: #666; /* 更新时间文本颜色 */
  text-align: right; /* 右对齐 */
}

.edit-button,
.delete-button {
  font-size: 14px; /* 增加按钮字体大小 */
  margin-left: 5px; /* 减少左侧间距 */
}

.edit-button:hover,
.delete-button:hover {
  opacity: 0.8;
}

.pagination {
  margin-top: 20px; /* 为分页组件添加上边距 */
  text-align: center; /* 中心对齐 */
}
</style>
