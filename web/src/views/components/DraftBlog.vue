<template>
  <div class="draft-container">
    <el-row :gutter="20" class="draft-list">
      <el-col
          v-for="draft in paginatedDrafts"
          :key="draft.postId"
          :span="6"
      >
        <el-card class="draft-card" :body-style="{ padding: '20px' }">
          <h3 class="draft-title">{{ draft.title }}</h3>
          <p class="draft-author">作者: {{ draft.userName }}</p>
          <p class="draft-update-time">更新时间: {{ formatDate(draft.lastUpdateTime) }}</p>
          <div class="button-group">
            <el-button @click="editDraft(draft.postId)" size="small">编辑</el-button>
            <el-button @click="deleteDraft(draft)" type="danger" size="small">删除</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-pagination
        background
        v-if="total > 0"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :page-sizes="[8, 18, 24]"
        layout="total, prev, pager, next, jumper, sizes"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
        class="pagination"
    />
  </div>
</template>

<script>
import instance from '../../http';
import {ElMessage} from "element-plus";

export default {
  data() {
    return {
      drafts: [],
      currentPage: 1,
      pageSize: 8,
      total: 0,
    };
  },
  computed: {
    paginatedDrafts() {
      return this.drafts;
    },
  },
  methods: {
    async fetchDrafts(page = this.currentPage, size = this.pageSize) {
      try {
        const response = await instance.post('/xblog/user/post/drafts', {
          userId: JSON.parse(sessionStorage.getItem('userData')).userId,
          pageNum: page,
          pageSize: size,
        });

        if (response.data.success) {
          this.drafts = response.data.data.data.list || [];
          this.total = response.data.data.data.total || 0;
        } else {
          this.$message.error(response.data.msg);
        }
      } catch (error) {
        console.error('获取草稿失败:', error);
        this.$message.error('获取草稿失败，请重试');
      }
    },
    handlePageChange(page) {
      this.currentPage = page;
      this.fetchDrafts(this.currentPage, this.pageSize);
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.currentPage = 1; // 重置到第一页
      this.fetchDrafts(this.currentPage, this.pageSize); // 重新获取草稿
    },
    editDraft(postId) {
      this.$router.push({ name: 'PublishBlog', params: { postId } });
    },
    deleteDraft(draft) {
      this.$confirm('确定删除该草稿吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        instance.post(`/xblog/user/post/del`, {
          postId: draft.postId,
          tagIds: draft.tagIds,
          userId: sessionStorage.getItem('userData') ? JSON.parse(sessionStorage.getItem('userData')).userId : null,
        }).then(response => {
          if (response.data.success) {
            this.fetchDrafts(this.currentPage, this.pageSize); // 刷新草稿列表
            this.$message.success('草稿已删除');
          } else {
            this.$message.error(response.data.msg);
          }
        }).catch(error => {
          console.error('删除草稿失败:', error);
          this.$message.error('删除草稿失败，请重试');
        });
      }).catch(() => {
        // 处理用户点击取消的情况
        ElMessage.info('已取消删除');
      });
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleString();
    },
  },
  mounted() {
    this.fetchDrafts(); // 组件挂载时获取草稿列表
  },
};
</script>

<style scoped>
.draft-container {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.draft-title {
  font-size: 24px;
  margin-bottom: 15px;
  color: #333;
}

.draft-list {
  margin-bottom: 20px;
}

.draft-card {
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.draft-author,
.draft-update-time {
  color: #666;
}

.button-group {
  display: flex;
  justify-content: space-between;
}

.pagination {
  margin-top: 10px;
}
</style>
