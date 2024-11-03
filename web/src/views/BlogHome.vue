<template>
  <div class="app-container">
    <Sidebar />
    <main class="main-content">
      <header class="header">
        <div class="user-info">
          <span class="welcome">欢迎，{{ username }} | </span>
          <span>当前在线人数: <strong>{{ onlineUsers }}</strong> | </span>
          <span>总浏览量: <strong>{{ totalViews }}</strong> | </span>
          <span>已登录时长: <strong>{{ loggedInDuration }}</strong></span>
        </div>
        <div class="slogan-container">
          <div class="slogan" ref="slogan" @animationend="updateSlogan">{{ currentSlogan }}</div>
        </div>
      </header>
      <div class="content">
        <!-- 图片轮播 -->
        <div class="carousel">
          <div class="carousel-inner" :style="{ transform: 'translateX(' + (-currentImage * 100) + '%)' }">
            <div class="carousel-item" v-for="(image, index) in images" :key="index">
              <img :src="image" alt="轮播图" />
            </div>
          </div>
          <button class="prev" @click="prevImage">❮</button>
          <button class="next" @click="nextImage">❯</button>
          <div class="image-counter">{{ currentImage + 1 }} / {{ images.length }}</div>
        </div>

        <div class="dashboard">
          <h2>发布数量统计</h2>
          <div class="chart-container" ref="lineChart"></div>
          <h2>标签</h2>
          <div id="word-cloud" class="word-cloud"></div>
        </div>
        <router-view></router-view>
      </div>
    </main>
  </div>
</template>

<script>
import Sidebar from './components/UserSiderBar.vue';
import instance from "@/http_no_auth";
import WordCloud from 'wordcloud'; // 确保正确引入词云库
import * as echarts from 'echarts';

export default {
  components: {
    Sidebar,
  },
  data() {
    return {
      loginTime: null,
      timer: null,
      autoSwitchTimer: null,
      loggedInDuration: '0 分钟',
      onlineUsers: 0,
      totalViews: 0,
      slogans: [
        '红衣佳人白衣友，朝与同歌暮同酒',
        '生活如诗，时光如歌',
        '每一天都是新的开始',
        '分享生活，分享快乐',
        '勇敢追梦，无畏风雨',
      ],
      currentSlogan: '',
      sloganIndex: 0,
      images: [
        'https://s2.loli.net/2024/11/02/aWkbzNx2KCVYlet.png',
        'https://s2.loli.net/2024/11/02/XQCmRSkuLGg6tVI.png',
        'https://s2.loli.net/2024/11/02/5bOUyxMPI6zKl1J.png',
        'https://s2.loli.net/2024/11/02/TY8MzGhQ3sKLWZI.png',
        'https://s2.loli.net/2024/11/02/I6pnBhU8O5XDd3S.png',
      ],
      currentImage: 0,
      wordCloudData: [
        { text: "Vue", weight: 15 },
        { text: "JavaScript", weight: 10 },
        { text: "CSS", weight: 8 },
        { text: "HTML", weight: 7 },
        { text: "React", weight: 6 },
        { text: "Node.js", weight: 5 },
        { text: "Spring", weight: 4 },
        { text: "MySQL", weight: 3 },
        { text: "API", weight: 2 },
        { text: "Security", weight: 1 },
      ],
      lineChart: {
        type: 'line', // 或其他类型
        data: {
          labels: [],
          datasets: []
        },
        options: {}
      },
      lineData: []
    };
  },
  computed: {
    username() {
      const userDataStr = sessionStorage.getItem("userData");
      let userData = null;
      try {
        userData = JSON.parse(userDataStr);
      } catch (error) {
        console.error("解析 userData 失败：", error);
      }
      return userData ? userData.userName : '游客';
    },
  },
  mounted() {
    this.refreshInfo(); // 初始信息刷新
    this.timer = setInterval(this.refreshInfo, 60000); // 每60秒刷新信息

    if (sessionStorage.getItem("userData")) {
      const loginTimeStr = sessionStorage.getItem("loginTime");
      this.loginTime = loginTimeStr ? parseInt(loginTimeStr, 10) : null; // 解析为数字
      const durationInMinutes = Math.floor((new Date().getTime() - this.loginTime) / 1000 / 60);
      this.loggedInDuration = `${durationInMinutes} 分钟`;
    }

    this.updateSlogan(); // 初始化标语
    this.startAutoSwitch(); // 启动自动切换
    this.$nextTick(() => {
      this.fetchWordCloudData(); // 获取词云数据.
    });
    this.fetchArticleData(); // 获取文章数据
  },
  beforeUnmount() {
    clearInterval(this.timer); // 清除定时器
    clearInterval(this.autoSwitchTimer); // 清除自动切换定时器
  },
  methods: {
    refreshInfo() {
      if (this.loginTime) {
        const currentTime = new Date().getTime();
        const durationInMinutes = Math.floor((currentTime - this.loginTime) / 1000 / 60);
        this.loggedInDuration = `${durationInMinutes} 分钟`;
      }
      this.fetchOnlineCount();
      this.getTotalViews();
    },
    updateSlogan() {
      this.currentSlogan = this.slogans[this.sloganIndex];
      this.sloganIndex = (this.sloganIndex + 1) % this.slogans.length;

      this.$nextTick(() => {
        const sloganEl = this.$refs.slogan;
        sloganEl.classList.remove('scroll');
        void sloganEl.offsetWidth;
        sloganEl.classList.add('scroll');
      });
    },
    prevImage() {
      this.currentImage = (this.currentImage - 1 + this.images.length) % this.images.length;
      this.resetAutoSwitch();
    },
    nextImage() {
      this.currentImage = (this.currentImage + 1) % this.images.length;
      this.resetAutoSwitch();
    },
    startAutoSwitch() {
      this.autoSwitchTimer = setInterval(() => {
        this.nextImage();
      }, 5000);
    },
    resetAutoSwitch() {
      clearInterval(this.autoSwitchTimer);
      this.startAutoSwitch();
    },
    async fetchOnlineCount() {
      try {
        const response = await instance.get('/xblog/public/online-count');
        if (response.data.success) {
          this.onlineUsers = response.data.data.data.count;
        } else {
          this.$message.error(response.data.msg);
        }
      } catch (error) {
        console.error('获取在线人数失败:', error);
      }
    },
    async getTotalViews() {
      try {
        const response = await instance.get('/xblog/public/visit-count');
        if (response.data.success) {
          this.totalViews = response.data.data.data;
        } else {
          this.$message.error(response.data.msg);
        }
      } catch (error) {
        console.error('获取浏览量失败:', error);
      }
    },
    async fetchWordCloudData() {
      try {
        const response = await instance.get('/xblog/public/tag/postNum');
        if (response.data.success) {
          const tags = response.data.data.data;

          const totalPostNum = tags.reduce((sum, tag) => sum + tag.postNum, 0);

          this.wordCloudData = tags.map(tag => ({
            text: tag.tagName,
            weight: (tag.postNum / totalPostNum) * 100
          }));

          this.drawWordCloud();
        } else {
          this.$message.error(response.data.msg);
        }
      } catch (error) {
        console.error('获取词云数据失败:', error);
      }
    },
    drawWordCloud() {
      const wordCloudElement = document.getElementById('word-cloud');

      // 获取容器的宽度和高度
      const width = wordCloudElement.clientWidth;
      const height = wordCloudElement.clientHeight;

      // 绘制词云
      this.renderWordCloud(wordCloudElement, width, height);

      // 监听窗口resize事件
      window.addEventListener('resize', () => {
        this.renderWordCloud(wordCloudElement, wordCloudElement.clientWidth, wordCloudElement.clientHeight);
      });
    },

    renderWordCloud(element, width, height) {
      WordCloud(element, {
        list: this.wordCloudData.map(item => [item.text, item.weight]),
        gridSize: 10,
        weightFactor: 10,
        color: () => `hsl(${Math.random() * 360}, 100%, 50%)`,
        rotateRatio: 0.5,
        backgroundColor: '#ffffff',
        width: width,  // 设置canvas宽度
        height: height, // 设置canvas高度
        minSize: 5,    // 可以设置最小字体大小
        maxSize: 100,  // 可以设置最大字体大小
      });
    },
    async fetchArticleData() {
      try {
        const response = await instance.get('/xblog/public/post/publish-count');
        if (response.data.success) {
          const data = response.data.data.data;
          const labels = data.map(item => item.date);
          const counts = data.map(item => item.count);

          // 更新折线图
          this.initLineChart(labels, counts);
        } else {
          this.$message.error(response.data.msg);
        }
      } catch (error) {
        console.error('获取文章数据失败:', error);
      }
    },
    initLineChart(labels, counts) {
      const lineChartElement = this.$refs.lineChart;
      const lineChart = echarts.init(lineChartElement);

      const option = {
        title: {
          text: ''
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '10%',    // 左边距
          right: '10%',   // 右边距
          top: '10%',     // 上边距
          bottom: '10%'   // 下边距
        },
        xAxis: {
          type: 'category',
          data: labels,
          boundaryGap: false // 确保第一个点和最后一个点在图表的边缘
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '发布数量',
          type: 'line',
          data: counts,
          smooth: true,
          itemStyle: {
            color: '#ACEEFF'
          },
          areaStyle: {}
        }]
      };

      lineChart.setOption(option);

      // 监听窗口resize事件，自动调整图表大小
      window.addEventListener('resize', () => {
        lineChart.resize();
      });
    },
  }
};
</script>

<style scoped>
html, body {
  height: 100%;
  margin: 0;
}

.app-container {
  display: flex;
  height: 100%;
  font-family: Arial, sans-serif;
  background-color: #ecf0f1;
  overflow-y: scroll;
  background-image: linear-gradient(to bottom, #ecf0f1, #ecf0f1);
}

.main-content {
  margin-left: 125px; /* 留出侧边栏的宽度 */
  width: 100%;
  flex-grow: 1;
  padding: 20px;
  background-color: #ecf0f1;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  border-bottom: 1px solid #ccc;
  justify-content: space-between;
}

.user-info {
  display: flex;
  align-items: center;
  white-space: nowrap;
}

.slogan-container {
  margin-left: auto;
  overflow: hidden;
  height: 1.5em;
  display: flex;
  align-items: center;
}

.slogan {
  color: #e74c3c;
  font-size: 1.2em;
  white-space: nowrap;
  transition: transform 0.5s ease;
}

.slogan.scroll {
  animation: scroll 5s linear;
}

@keyframes scroll {
  0% {
    transform: translateX(100%);
  }
  100% {
    transform: translateX(-100%);
  }
}

.carousel {
  position: relative;
  overflow: hidden;
  width: 100%;
  height: 650px;
  border-radius: 10px;
  margin-bottom: 5px;
}

.carousel-inner {
  display: flex;
  transition: transform 0.5s ease;
}

.carousel-item {
  min-width: 100%;
  height: 100%;
}

.carousel img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-counter {
  position: absolute;
  top: 10px; /* 距离顶部 10px */
  left: 50%;
  transform: translateX(-50%); /* 水平居中 */
  color: #333;
  font-size: 1em;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 4px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  z-index: 1; /* 确保在图片上方 */
}

button.prev, button.next {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(255, 255, 255, 0.7);
  border: none;
  cursor: pointer;
  font-size: 2em;
}

button.prev {
  left: 10px;
}

button.next {
  right: 10px;
}

.content {
  margin-top: 5px;
  padding: 5px;
  background-color: #ffffff;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  flex-grow: 1;
}
.dashboard {
  display: flex;
  justify-content: space-between;
}

.chart-container {
  width: 48%;
  height: 400px;
  background-color: #ffffff;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.word-cloud {
  display: flex;
  width: 48%;
  height: 400px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #ffffff;
}
.dashboard h2 {
  /*margin: 10px 0;*/
  font-size: 0.5em; /* 调整标题大小 */
  color: #333; /* 调整标题颜色 */
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
  background-color: #ffffff;
  border-radius: 4px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  padding: 5px 10px;
  display: inline-block;
  position: relative;
  /* 竖排 */
  writing-mode: vertical-rl;
}
</style>
