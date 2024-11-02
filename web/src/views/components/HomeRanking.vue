<template>
  <div class="ranking-page">
    <Sidebar />
    <div class="ranking-content">
      <div class="ranking-container">
        <div class="ranking-module user-ranking">
          <h3>用户排名</h3>
          <ul>
            <li
                v-for="(user, index) in userRankings"
                :key="user.userId"
                class="user-item"
                :style="getUserStyle(index)"
            >
              <div class="rank-number">{{ index + 1 }}</div>
              <div class="avatar">{{ user.userName.charAt(0) }}</div>
              <div class="user-info">
                <span class="user-name">{{ user.userName }}</span>
                <span class="view-count">{{ user.viewCount }} 次浏览</span>
              </div>
            </li>
          </ul>
        </div>
        <div class="ranking-module article-ranking">
          <h3>博客排名</h3>
          <ul>
            <li
                v-for="(article, index) in articleRankings"
                :key="article.postId"
                class="article-item"
                :style="getArticleStyle(index)"
                @click="goToDetail(article.postId)"
            >
            <div class="rank-number">{{ index + 1 }}</div>
            <div class="article-info">
                <span class="tooltip">
                  <span class="article-title">{{ article.title }}</span>
                  <span class="tooltip-text">{{ article.title }}</span>
                </span>
              <div class="tags">
                  <span v-for="tag in article.tagNameList" :key="tag" class="tag tooltip">
                    {{ tag }}
                    <span class="tooltip-text">{{ tag }}</span>
                  </span>
              </div>
              <span class="view-count">{{ article.viewCount }} 次浏览</span>
            </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from "@/views/components/UserSiderBar.vue";
import instance from "@/http_no_auth";

export default {
  components: {
    Sidebar,
  },
  data() {
    return {
      userRankings: [],
      articleRankings: [],
    };
  },
  created() {
    this.fetchRankings();
  },
  methods: {
    async fetchRankings() {
      try {
        const userResponse = await instance.get('/xblog/public/user/view-rank');
        if (userResponse.data.success) {
          this.userRankings = userResponse.data.data.data;
        } else {
          console.error("用户排名数据获取失败:", userResponse.data.msg);
        }

        const articleResponse = await instance.get('/xblog/public/post/view-rank');
        if (articleResponse.data.success) {
          this.articleRankings = articleResponse.data.data.data;
        } else {
          console.error("文章排名数据获取失败:", articleResponse.data.msg);
        }
      } catch (error) {
        console.error("获取排名数据失败:", error);
      }
    },
    goToDetail(postId) {
      // 跳转到详情页的方法
      this.$router.push({ name: 'BlogDetail', params: { postId: postId } });
    },
    getUserStyle(index) {
      const opacity = 1 - index * 0.1; // 从1到0.1
      return {
        backgroundColor: `rgba(255, 255, 0, ${opacity})`, // 黄色渐变
      };
    },
    getArticleStyle(index) {
      const opacity = 1 - index * 0.1; // 从1到0.1
      return {
        backgroundColor: `rgba(255, 0, 0, ${opacity})`, // 红色渐变
      };
    },
  },
};
</script>

<style scoped>
.ranking-page {
  display: flex;
}

.ranking-content {
  margin-left: 125px;
  padding: 10px;
  flex-grow: 1;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.ranking-container {
  display: flex;
  justify-content: space-between;
}

.ranking-module {
  width: 48%;
  margin-bottom: 30px;
  margin-left: 10px;
  background-color: #ffffff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: auto;
}

.ranking-module h3 {
  margin-bottom: 10px;
  font-size: 1.5em;
  color: #333;
  border-bottom: 2px solid #007bff;
  padding-bottom: 10px;
}

.ranking-module ul {
  list-style: none;
  padding: 0;
}

.user-item,
.article-item {
  display: flex;
  align-items: center;
  padding: 10px 0; /* 增加上下内边距 */
  border-bottom: 1px solid #ddd;
  transition: background-color 0.3s;
  border-radius: 8px; /* 添加圆角 */
  margin-bottom: 5px; /* 增加间距 */
}

.user-item:hover,
.article-item:hover {
  background-color: #f1f1f1;
}

.rank-number {
  width: 40px; /* 调整序号宽度 */
  text-align: center;
  font-weight: bold;
  color: #007bff;
}

.avatar {
  margin-right: 15px; /* 增加右边距 */
  width: 50px; /* 调整头像大小 */
  height: 50px;
  border-radius: 50%;
  background-color: #007bff;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.5em; /* 调整字体大小 */
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
}

.article-info {
  flex-grow: 1;
  display: flex;
  flex-direction: column; /* 垂直排列信息 */
}

.article-title {
  flex-grow: 1;
  font-weight: bold;
  margin-bottom: 5px; /* 增加标题与标签的间距 */
}

.view-count {
  color: #5D6FFF;
  font-size: 0.9em;
  white-space: nowrap;
  margin-top: 5px; /* 增加浏览次数与标题的间距 */
  margin-right: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start; /* 左对齐 */
  margin-bottom: 5px; /* 增加标签与浏览次数的间距 */
}

.user-info {
  flex-grow: 1;
  display: flex;
  justify-content: space-between;
}

.user-name {
  text-align: center;
  flex-grow: 1;
}

.tag {
  display: inline-block;
  background-color: #e2e6ea;
  border-radius: 4px;
  padding: 5px 10px; /* 增加内边距 */
  margin-right: 5px;
  margin-top: 2px;
  font-size: 0.7em; /* 调整字体大小 */
  color: #225684;
}

/* 工具提示样式 */
.tooltip {
  position: relative;
  display: inline-block;
  cursor: pointer;
}

.tooltip .tooltip-text {
  visibility: hidden;
  width: 120px;
  background-color: rgba(0, 0, 0, 0.75);
  color: #fff;
  text-align: center;
  border-radius: 5px;
  padding: 5px;
  position: absolute;
  z-index: 1;
  bottom: 125%;
  left: 50%;
  margin-left: -60px;
  opacity: 0;
  transition: opacity 0.3s;
}

.tooltip:hover .tooltip-text {
  visibility: visible;
  opacity: 1;
}
</style>
