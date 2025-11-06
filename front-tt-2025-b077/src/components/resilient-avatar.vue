<template>
  <v-avatar :size="size" :class="avatarClass" :style="avatarStyle">
    <v-img v-if="!useDefault && imageUrl" :src="currentImageUrl" @error="handleError" :alt="alt">
      <template v-slot:placeholder>
        <div class="d-flex align-center justify-center fill-height">
          <v-icon :icon="defaultIcon" :size="iconSize" color="grey-lighten-1"></v-icon>
        </div>
      </template>
    </v-img>

    <v-icon v-else :icon="defaultIcon" :size="iconSize" :color="iconColor"></v-icon>
  </v-avatar>
</template>

<script>
export default {
  name: 'ResilientAvatar',
  props: {
    src: {
      type: String,
      default: null,
    },
    size: {
      type: [String, Number],
      default: 50,
    },
    avatarClass: {
      type: String,
      default: '',
    },
    avatarStyle: {
      type: [String, Object],
      default: '',
    },
    defaultIcon: {
      type: String,
      default: 'mdi-account',
    },
    iconColor: {
      type: String,
      default: 'grey-lighten-1',
    },
    alt: {
      type: String,
      default: 'Avatar',
    },
    maxRetries: {
      type: Number,
      default: 2,
    },
    retryDelay: {
      type: Number,
      default: 1000,
    },
  },

  data() {
    return {
      useDefault: false,
      imageUrl: null,
      currentImageUrl: null,
      retryCount: 0,
      hasError: false,
    }
  },

  computed: {
    iconSize() {
      const size = typeof this.size === 'string' ? parseInt(this.size) : this.size
      return Math.floor(size * 0.6)
    },
  },

  watch: {
    src: {
      immediate: true,
      handler(newSrc) {
        this.resetAndLoadImage(newSrc)
      },
    },
  },

  methods: {
    resetAndLoadImage(src) {
      this.useDefault = false
      this.retryCount = 0
      this.hasError = false

      if (!src) {
        this.useDefault = true
        return
      }

      this.imageUrl = src
      this.currentImageUrl = this.processGoogleUrl(src)
    },

    processGoogleUrl(url) {
      if (!url || !url.includes('googleusercontent.com')) {
        return url
      }
      const encodedUrl = encodeURIComponent(url)
      const baseURL = import.meta.env.DEV
        ? 'http://127.0.0.1:8080/api/'
        : import.meta.env.VITE_API_BACK
      return `${baseURL}place/proxy-image?url=${encodedUrl}`
    },

    async handleError() {
      this.hasError = true
      this.retryCount++

      if (this.retryCount <= this.maxRetries) {
        const delay = this.retryDelay * this.retryCount

        await new Promise((resolve) => setTimeout(resolve, delay))

        if (this.imageUrl.includes('googleusercontent.com')) {
          const currentSize = this.getCurrentImageSize()
          const newSize = Math.max(Math.floor(currentSize / 1.5), 24)

          this.currentImageUrl = this.imageUrl
            .replace(/=s\d+/, `=s${newSize}`)
            .replace(/=w\d+/, `=w${newSize}`)

          this.currentImageUrl += `${this.currentImageUrl.includes('?') ? '&' : '?'}retry=${this.retryCount}&t=${Date.now()}`
        }
      } else {
        this.useDefault = true
      }
    },

    getCurrentImageSize() {
      if (!this.currentImageUrl) return 64

      const match = this.currentImageUrl.match(/=s(\d+)/)
      return match ? parseInt(match[1]) : 64
    },

    retry() {
      if (this.imageUrl) {
        this.resetAndLoadImage(this.imageUrl)
      }
    },
  },
}
</script>

<style scoped>
.v-avatar {
  transition: all 0.3s ease;
}

.v-avatar:hover {
  transform: scale(1.05);
}

.fill-height {
  height: 100% !important;
}
</style>
