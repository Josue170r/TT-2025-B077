<template>
  <div class="search-container">
    <div
      class="search-wrapper"
      :class="{ focused: isFocused, 'has-value': query, loading: isLoading }"
    >
      <input
        type="text"
        class="search-input"
        placeholder="Explora un lugar"
        v-model="query"
        @focus="handleFocus"
        @blur="handleBlur"
        @input="handleInput"
        @keydown.enter="performSearch"
        @keydown="handleKeyNavigation"
        ref="searchInput"
      />
      <div class="search-icon-wrapper" @click="performSearch">
        <i v-if="!isLoading" class="fa-solid fa-magnifying-glass search-icon"></i>
        <div v-else class="loading-spinner"></div>
      </div>
      <div class="search-effects">
        <div class="search-glow"></div>
      </div>
    </div>

    <div v-if="showSuggestions && suggestions.length > 0" class="suggestions-dropdown">
      <div
        v-for="(suggestion, index) in suggestions"
        :key="suggestion.placeId"
        class="suggestion-item"
        @click="selectSuggestion(suggestion)"
        :class="{ highlighted: index === selectedIndex }"
      >
        <i class="fa-solid fa-location-dot suggestion-icon"></i>
        <div class="suggestion-content">
          <div class="suggestion-main">{{ suggestion.mainText }}</div>
          <div class="suggestion-secondary">{{ suggestion.secondaryText }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapMutations } from 'vuex'

export default {
  name: 'SearchInput',
  props: {
    placeholder: {
      type: String,
      default: 'Explora un lugar',
    },
  },
  data() {
    return {
      query: '',
      isFocused: false,
      isLoading: false,
      suggestions: [],
      showSuggestions: false,
      selectedIndex: -1,
      debounceTimer: null,
    }
  },
  beforeUnmount() {
    clearTimeout(this.debounceTimer)
  },
  methods: {
    ...mapActions('places', {
      searchPlacesByName: 'searchPlacesByName',
    }),
    ...mapMutations('places', {
      setSelectedPlaceId: 'setSelectedPlaceId',
    }),
    handleFocus() {
      this.isFocused = true
      if (this.suggestions.length > 0) {
        this.showSuggestions = true
      }
    },

    handleBlur() {
      setTimeout(() => {
        this.isFocused = false
        this.showSuggestions = false
        this.selectedIndex = -1
      }, 200)
    },

    handleInput() {
      this.$emit('search-input', this.query)

      clearTimeout(this.debounceTimer)

      if (this.query.trim().length < 2) {
        this.suggestions = []
        this.showSuggestions = false
        return
      }

      this.debounceTimer = setTimeout(() => {
        this.fetchSuggestions()
      }, 300)
    },

    async fetchSuggestions() {
      if (!this.query.trim()) {
        return
      }

      this.isLoading = true

      try {
        const response = await this.searchPlacesByName(this.query.trim())

        if (response.data && response.data.data) {
          this.suggestions = response.data.data
          this.showSuggestions = this.suggestions.length > 0
        } else {
          this.suggestions = []
          this.showSuggestions = false
        }
      } catch (error) {
        console.error('Error al buscar lugares:', error)
        this.suggestions = []
        this.showSuggestions = false
        this.$emit('search-error', error)
      } finally {
        this.isLoading = false
      }
    },

    async performSearch() {
      console.log(this.query)
      if (!this.query.trim()) return

      this.isLoading = true
      this.showSuggestions = false

      try {
        if (this.selectedIndex >= 0 && this.suggestions[this.selectedIndex]) {
          const selectedPlace = this.suggestions[this.selectedIndex]
          this.$emit('place-selected', {
            place: selectedPlace,
            query: this.query,
          })
        } else if (this.suggestions.length > 0) {
          this.$emit('place-selected', {
            place: this.suggestions[0],
            query: this.query,
          })
        } else {
          const response = await this.searchPlacesByName(this.query.trim())

          if (response.data && response.data.data && response.data.data.length > 0) {
            this.$emit('place-selected', {
              place: response.data.data[0],
              query: this.query,
            })
          } else {
            this.$emit('search-error', new Error('No se encontraron resultados'))
          }
        }
      } catch (error) {
        console.error('Error en la búsqueda:', error)
        this.$emit('search-error', error)
      } finally {
        this.isLoading = false
      }
    },

    selectSuggestion(suggestion) {
      this.query = suggestion.description
      this.showSuggestions = false
      this.selectedIndex = -1
      this.setSelectedPlaceId(suggestion.placeId)
      this.$router.push({
        name: 'site_description',
        query: { from: 'home' },
      })
    },

    handleKeyNavigation(event) {
      if (!this.showSuggestions || this.suggestions.length === 0) return

      switch (event.key) {
        case 'ArrowDown':
          event.preventDefault()
          this.selectedIndex = Math.min(this.selectedIndex + 1, this.suggestions.length - 1)
          break
        case 'ArrowUp':
          event.preventDefault()
          this.selectedIndex = Math.max(this.selectedIndex - 1, -1)
          break
        case 'Enter':
          event.preventDefault()
          this.performSearch()
          break
        case 'Escape':
          this.showSuggestions = false
          this.selectedIndex = -1
          this.$refs.searchInput.blur()
          break
      }
    },
  },
}
</script>

<style scoped>
.search-container {
  position: relative;
  z-index: 9999;
}

.search-wrapper {
  position: relative;
  width: 100%;
  height: 45px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 25px;
  border: 2px solid rgba(171, 205, 158, 0.3);
  backdrop-filter: blur(10px);
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  overflow: hidden;
  box-shadow: 0 2px 15px rgba(27, 81, 94, 0.08);
}

.search-wrapper:hover {
  border-color: rgba(171, 205, 158, 0.6);
  transform: translateY(-1px);
  box-shadow: 0 4px 20px rgba(27, 81, 94, 0.12);
}

.search-wrapper.focused {
  border-color: #abcd9e;
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 6px 25px rgba(171, 205, 158, 0.25);
}

.search-wrapper.has-value {
  background: rgba(255, 255, 255, 0.95);
}

.search-wrapper.loading {
  border-color: rgba(171, 205, 158, 0.6);
}

.search-icon-wrapper {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.search-icon-wrapper:hover {
  transform: translateY(-50%) scale(1.1);
}

.search-icon {
  color: #1b515e;
  font-size: 1.1rem;
  opacity: 0.6;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.search-wrapper.focused .search-icon {
  color: #1b515e;
  opacity: 1;
  transform: scale(1.1);
}

.search-input {
  width: 100%;
  height: 100%;
  border: none;
  outline: none;
  background: transparent;
  padding: 0 50px 0 20px;
  font-size: 14px;
  font-weight: 500;
  color: #1b515e;
  letter-spacing: 0.3px;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.search-input::placeholder {
  color: rgba(27, 81, 94, 0.5);
  font-weight: 400;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.search-wrapper.focused .search-input::placeholder {
  color: rgba(171, 205, 158, 0.7);
  transform: translateX(2px);
}

.search-effects {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.search-glow {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: linear-gradient(
    45deg,
    rgba(171, 205, 158, 0.1),
    rgba(139, 182, 138, 0.1),
    rgba(171, 205, 158, 0.1)
  );
  border-radius: 25px;
  opacity: 0;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  filter: blur(8px);
}

.search-wrapper.focused .search-glow {
  opacity: 1;
  animation: glowPulse 2s ease-in-out infinite alternate;
}

@keyframes glowPulse {
  0% {
    transform: scale(1);
    opacity: 0.5;
  }
  100% {
    transform: scale(1.02);
    opacity: 0.8;
  }
}

/* Spinner de carga */
.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(27, 81, 94, 0.3);
  border-top: 2px solid #1b515e;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.suggestions-dropdown {
  position: absolute;
  top: calc(100% + 5px);
  left: 0;
  right: 0;
  background: white;
  border-radius: 15px;
  box-shadow: 0 8px 25px rgba(27, 81, 94, 0.15);
  border: 1px solid rgba(171, 205, 158, 0.2);
  max-height: 300px;
  overflow-y: auto;
  z-index: 999999999 !important;
  margin-top: 0;
  backdrop-filter: blur(10px);
}

.suggestion-item {
  padding: 12px 16px;
  border-bottom: 1px solid rgba(171, 205, 158, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 12px;
}

.suggestion-item:hover,
.suggestion-item.highlighted {
  background: rgba(171, 205, 158, 0.1);
  transform: translateX(4px);
}

.suggestion-item:last-child {
  border-bottom: none;
  border-radius: 0 0 15px 15px;
}

.suggestion-item:first-child {
  border-radius: 15px 15px 0 0;
}

.suggestion-icon {
  color: #1b515e;
  font-size: 14px;
  opacity: 0.7;
}

.suggestion-content {
  flex: 1;
}

.suggestion-main {
  font-weight: 500;
  color: #1b515e;
  font-size: 14px;
  margin-bottom: 2px;
}

.suggestion-secondary {
  font-size: 12px;
  color: rgba(27, 81, 94, 0.6);
}

/* Ocultar scrollbar completamente */
.suggestions-dropdown {
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE y Edge */
}

.suggestions-dropdown::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

/* Responsive */
@media (max-width: 576px) {
  .search-wrapper {
    height: 42px;
    border-radius: 20px !important;
  }

  .search-input {
    font-size: 13px;
    padding: 0 40px 0 15px;
  }

  .search-icon-wrapper {
    right: 12px;
  }

  .search-icon {
    font-size: 1rem;
  }
}

@media (min-width: 768px) {
  .search-wrapper {
    height: 50px;
    max-width: 1000px;
  }

  .search-input {
    font-size: 16px;
    padding: 0 60px 0 25px;
  }

  .search-icon-wrapper {
    right: 20px;
  }

  .search-icon {
    font-size: 1.3rem;
  }
}

@media (min-width: 1200px) {
  .search-wrapper {
    height: 35px;
    border-radius: 20px;
  }

  .search-input {
    font-size: 18px;
    padding: 0 65px 0 30px;
    letter-spacing: 0.4px;
  }

  .search-icon-wrapper {
    right: 22px;
  }

  .search-icon {
    font-size: 1.4rem;
  }

  .search-glow {
    border-radius: 30px;
  }
}

@media (min-width: 1400px) {
  .search-wrapper {
    height: 38px;
    border-radius: 20px;
    margin: 0 auto;
  }

  .search-input {
    font-size: 20px;
    padding: 0 70px 0 35px;
    letter-spacing: 0.5px;
  }

  .search-icon-wrapper {
    right: 25px;
  }

  .search-icon {
    font-size: 1.5rem;
  }

  .search-glow {
    border-radius: 32px;
  }
}
</style>
