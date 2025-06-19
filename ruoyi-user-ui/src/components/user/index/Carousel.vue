<template>
  <div class="carousel">
    <div class="carousel-container">
      <div class="carousel-item" v-for="(slide, index) in slides" :key="index"
           :class="{ active: currentSlide === index }">
        <img
            :src="slide"
            class="carousel-image"
            alt="轮播图"
            @contextmenu.prevent
            draggable="false"
        />
      </div>
      <div class="carousel-controls">
        <button @click="prevSlide">&lt;</button>
        <div class="carousel-indicators">
          <span
              v-for="(slide, index) in slides"
              :key="index"
              :class="['indicator', { active: currentSlide === index }]"
              @click="goToSlide(index)"
          ></span>
        </div>
        <button @click="nextSlide">&gt;</button>
      </div>
    </div>
  </div>
</template>

<script>
const images = import.meta.glob('@/assets/images/carousel/*.{jpg,png,jpeg,gif}', {eager: true})

export default {
  name: 'Carousel',
  data() {
    return {
      currentSlide: 0,
      slides: Object.values(images).map(img => img.default)
    }
  },
  methods: {
    nextSlide() {
      this.currentSlide = (this.currentSlide + 1) % this.slides.length
    },
    prevSlide() {
      this.currentSlide = (this.currentSlide - 1 + this.slides.length) % this.slides.length
    },
    goToSlide(index) {
      this.currentSlide = index
    }
  },
  mounted() {
    setInterval(this.nextSlide, 5000)
  }
}
</script>

<style scoped>
.carousel {
  width: 100%;
  position: relative;
  overflow: hidden;
  box-sizing: border-box;
}

.carousel-container {
  width: 100%;
  height: auto;
  position: relative;
}

.carousel-item {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  opacity: 0;
  transition: opacity 0.5s ease-in-out;
}

.carousel-item.active {
  opacity: 1;
  position: relative;
}

.carousel-controls {
  position: absolute;
  bottom: 10px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  opacity: 0;
  transition: opacity 0.3s ease;
  pointer-events: none;
}

/* 鼠标悬浮时显示控制按钮 */
.carousel:hover .carousel-controls {
  opacity: 1;
  pointer-events: auto;
}

.carousel-indicators {
  display: flex;
  gap: 5px;
}

.indicator {
  width: 10px;
  height: 10px;
  background-color: #ccc;
  border-radius: 50%;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.indicator.active {
  background-color: #333;
}

button {
  background: rgba(255, 255, 255, 0.7);
  border: none;
  font-size: 18px;
  cursor: pointer;
  user-select: none;
  padding: 2px 8px;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

button:hover {
  background: rgba(255, 255, 255, 0.9);
}

.carousel-image {
  width: 100%;
  max-height: 300px;
  object-fit: cover;
  border-radius: 8px;
  user-select: none;
  pointer-events: none;
}
</style>