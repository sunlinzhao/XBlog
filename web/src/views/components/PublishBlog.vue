<template>
  <div class="publish-blog">
    <div class="form-group center">
      <label>标题:</label>
      <input type="text" v-model="title" placeholder="请输入标题" required />
    </div>
    <div class="editor-preview-container">
      <div class="editor">
        <label>编辑器</label>
        <textarea v-model="content" placeholder="支持Markdown格式" required></textarea>
      </div>
      <div class="preview">
        <label>预览</label>
        <div class="markdown-preview markdown-body" v-html="compiledMarkdown"></div>
      </div>
    </div>
    <div class="form-group center">
      <label>添加标签:</label>
      <input type="text" v-model="tagNames" placeholder="请输入标签，多个标签用逗号分隔" />
    </div>
    <div class="form-group center">
      <label>状态:</label>
      <select v-model="status">
        <option value="0">存草稿</option>
        <option value="1">发布</option>
      </select>
    </div>
    <div class="form-group center">
      <label>上传Markdown文件:</label>
      <input type="file" @change="handleFileUpload" accept=".md" />
    </div>
    <div class="center">
      <button type="button" class="submit-button" @click="submitBlog">发布</button>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus';
import MarkdownIt from 'markdown-it';
import instance from "@/http";

export default {
  props: {
    postId: {
      type: Number,
      required: false
    }
  },
  data() {
    return {
      title: '',
      content: '',
      status: '1',
      tagNames: '',
      md: new MarkdownIt(),
      selectedFile: null // 新增用于存储选中的文件
    };
  },
  computed: {
    compiledMarkdown() {
      return this.md.render(typeof this.content === 'string' ? this.content : '');
    }
  },
  methods: {
    handleFileUpload(event) {
      const file = event.target.files[0]; // 获取选中的文件
      if (file && file.type !== 'text/markdown' && !file.name.endsWith('.md')) {
        ElMessage.error('只能上传 .md 文件！');
        this.selectedFile = null; // 重置文件选择
        return;
      }
      this.selectedFile = file; // 存储选中的文件
    },
    async fetchBlog() {
      const postId = this.$route.params.postId;
      if (postId) {
        try {
          const userDataObj = sessionStorage.getItem('userData');
          const userData = JSON.parse(userDataObj);
          const userId = userData.userId;

          const response = await instance.post('/xblog/public/post/one', {
            userId: userId,
            postId: postId
          });

          if (response.data.success) {
            const blogData = response.data.data.data;
            this.title = blogData.title;
            this.content = blogData.content || '';
            this.status = blogData.status.toString();
            this.tagNames = blogData.tagNames || '';
          } else {
            ElMessage.error(response.data.msg);
          }
        } catch (error) {
          console.error('获取博客内容失败:', error);
          ElMessage.error('获取博客内容失败，请重试！');
        }
      }
    },
    async submitBlog() {
      try {
        const userDataObj = sessionStorage.getItem('userData');
        const userData = JSON.parse(userDataObj);
        const userId = userData.userId;
        const postId = this.$route.params.postId;

        const payload = {
          userId: userId,
          postId: postId || '',
          title: this.title,
          tagNames: this.tagNames,
          status: this.status,
          content: this.content
        };

        if (this.selectedFile) { // 如果选择了文件
          const formData = new FormData();
          formData.append('file', this.selectedFile); // 将文件添加到 FormData
          formData.append('userId', userId);
          formData.append('postId', postId || '');
          formData.append('title', this.title);
          formData.append('tagNames', this.tagNames);
          formData.append('status', this.status);

          // 上传文件
          const uploadResponse = await instance.post('/xblog/user/post/uploadFile', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });

          if (uploadResponse.data.success) {
            this.resetForm();
            ElMessage.success('博客发布成功!');
          } else {
            ElMessage.error(uploadResponse.data.msg);
          }
        } else { // 如果没有选择文件
          const response = await instance.post('/xblog/user/post/upload', {...payload});
          if (response.data.success) {
            this.resetForm();
            ElMessage.success('博客发布成功!');
          } else {
            ElMessage.error(response.data.msg);
          }
        }
      } catch (error) {
        console.error('博客发布失败:', error);
        ElMessage.error('博客发布失败，请重试!');
      }
    },
    resetForm() {
      this.title = '';
      this.content = '';
      this.status = '1';
      this.tagNames = '';
      this.selectedFile = null; // 重置文件选择
    }
  },
  watch: {
    '$route.params.postId': 'fetchBlog',
  },
  mounted() {
    this.fetchBlog();
  }
};
</script>

<style scoped>
/* 保留原有样式 */
.publish-blog {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.editor-preview-container {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin: 20px 0;
  height: 80vh;
}

.editor, .preview {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  overflow: auto;
  width: 50%;
}

.editor label, .preview label {
  font-weight: bold;
  margin-bottom: 10px;
}

textarea {
  width: 100%;
  height: 100%;
  padding: 10px;
  resize: none;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-family: Arial, sans-serif;
  font-size: 14px;
}

input[type="text"], select, input[type="file"] {
  width: 300px;
  padding: 10px;
  margin-top: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.submit-button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-button:hover {
  background-color: #45a049;
}

.markdown-preview {
  width: 100%;
  max-height: 80vh;
  padding: 20px;
  overflow: auto;
  border: 1px solid #ddd;
  background-color: #ffffff;
  font-family: Arial, sans-serif;
  color: #333;
  line-height: 1.6;
  border-radius: 8px;
  text-align: left;
}

.markdown-preview img {
  max-width: 80%;
  height: auto;
  display: block;
  margin: 0 auto;
}
</style>
