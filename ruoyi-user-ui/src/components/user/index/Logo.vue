<template>
  <div class="logo-container">
    <!-- 自适应缩放的logo图片 -->
    <img src="/wyt.png" alt="Logo" class="logo-img"/>
  </div>
</template>

<script>
export default {
  name: 'Logo'
}
</script>

<style scoped>
.logo-container {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 12px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 150px;
  width: 100%;
  overflow: hidden;
}

.logo-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

/* 其他可选方案 - 如果需要保持完整显示（不裁剪） */
/*
.logo-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 8px;
}
*/

/* 如果希望图片保持原始比例但尽可能大 */
/*
.logo-img {
  max-width: 100%;
  max-height: 100%;
  width: auto;
  height: auto;
  object-fit: scale-down;
  border-radius: 8px;
}
*/
</style>