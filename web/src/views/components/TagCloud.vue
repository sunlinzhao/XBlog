<!-- components/TagCloud.vue -->
<template>
  <div class="tag-cloud">
    <div class="tag" v-for="tag in tags" :key="tag.name" :class="tag.className">
      {{ tag.name }}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    tags: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style scoped>
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 5px;
}

.tag {
  padding: 5px 10px;
  border-radius: 4px;
  background-color: #95a5a6;
  color: #fff;
  cursor: pointer;
  user-select: none;
  transition: background-color 0.3s;
}

.tag:hover {
  background-color: #3498db;
}
</style>