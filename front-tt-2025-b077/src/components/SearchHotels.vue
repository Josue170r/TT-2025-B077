<template>
  <nav class="top-navbar-container">
    <div class="navbar-header">
      <div class="menu-wrapper">
        <hamburgermenu @navigate="handleNavigation" />
      </div>
      <h1 class="navbar-title">Hoteles</h1>
      <div class="spacer"></div>
    </div>

    <div class="navbar-content d-flex justify-content-center py-2 px-3">
      <div class="search-container">
        <div
          class="search-wrapper"
          :class="{ focused: isFocused, 'has-value': query, loading: isLoading }"
        >
          <input
            type="text"
            class="search-input"
            placeholder="Busca el lugar de hospedaje de tu preferencia"
            v-model="query"
            @focus="handleFocus"
            @blur="handleBlur"
            @input="handleInput"
            @keydown.enter="performSearch"
            ref="searchInput"
          />
          <div class="search-icon-wrapper" @click="performSearch">
            <i v-if="!isLoading" class="fa-solid fa-bed search-icon"></i>
            <div v-else class="loading-spinner"></div>
          </div>
          <div class="search-effects">
            <div class="search-glow"></div>
          </div>
        </div>

        <transition name="dropdown">
          <div v-if="showSuggestions && suggestions.length > 0" class="suggestions-dropdown">
            <div
              v-for="(suggestion, index) in suggestions"
              :key="suggestion.placeId"
              class="suggestion-item"
              @click="selectSuggestion(suggestion)"
              :class="{ highlighted: index === selectedIndex }"
            >
              <i class="fa-solid fa-hotel suggestion-icon"></i>
              <div class="suggestion-content">
                <div class="suggestion-main">{{ suggestion.mainText }}</div>
                <div class="suggestion-secondary">{{ suggestion.secondaryText }}</div>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </nav>
</template>

<script>
import hamburgermenu from '@/components/hamburgermenu.vue'

export default {
  name: 'SearchHotels',
  components: {
    hamburgermenu,
  },
  data() {
    return {
      query: '',
      isFocused: false,
      isLoading: false,
      suggestions: [],
      selectedIndex: -1,
      showSuggestions: false,
    }
  },
  methods: {
    handleNavigation(route) {
      this.$emit('navigate', route)
    },
    handleFocus() {
      this.isFocused = true
      this.showSuggestions = this.suggestions.length > 0
    },
    handleBlur() {
      setTimeout(() => {
        this.isFocused = false
        this.showSuggestions = false
      }, 150)
    },
    handleInput() {
      if (this.query.length > 2) {
        this.isLoading = true
        setTimeout(() => {
          this.suggestions = [
            { placeId: 1, mainText: 'Hotel Palace', secondaryText: 'Centro Histórico' },
            { placeId: 2, mainText: 'Suites Reforma', secondaryText: 'Colonia Juárez' },
          ]
          this.isLoading = false
          this.showSuggestions = true
        }, 500)
      } else {
        this.suggestions = []
        this.showSuggestions = false
        this.isLoading = false
      }
    },
    performSearch() {
      if (this.query) {
        console.log('Buscando hoteles:', this.query)
        this.$emit('search-hotels', this.query)
      }
    },
    selectSuggestion(suggestion) {
      this.query = suggestion.mainText
      this.showSuggestions = false
      this.isFocused = false
      this.$emit('place-selected', suggestion)
      this.performSearch()
    },
  },
}
</script>

<style scoped>
.top-navbar-container {
  width: 100%;
  background-color: white;
}

.navbar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 20px 8px;
  border-bottom: 1px solid #e0e0e0;
}

.navbar-title {
  color: #1b515e !important;
  font-weight: 700 !important;
  letter-spacing: -0.5px;
  line-height: 1.2;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  font-size: 1.5rem;
  margin: 0;
  flex: 1;
  text-align: center;
}

.menu-wrapper {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-wrapper :deep(svg),
.menu-wrapper :deep(i) {
  color: #1b515e !important;
  font-size: 28px;
}

.spacer {
  width: 40px;
  flex-shrink: 0;
}

.navbar-content {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.search-container {
  position: relative;
  width: 100%;
  max-width: 650px;
  margin: 0;
}

.search-wrapper {
  display: flex;
  align-items: center;
  border: 2px solid #e0e0e0;
  border-radius: 50px;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  background-color: #ffffff;
  overflow: hidden;
  position: relative;
}

.search-wrapper.focused {
  border-color: #b0d4a1;
  background-color: white;
  box-shadow: 0 4px 12px rgba(53, 170, 6, 0.15);
  transform: translateY(-1px);
}

.search-wrapper.has-value {
  border-color: #b0d4a1;
}

.search-input {
  flex-grow: 1;
  border: none;
  padding: 12px 18px;
  font-size: 1rem;
  background-color: transparent;
  outline: none;
  color: #1b515e;
  font-weight: 400;
}

.search-input::placeholder {
  color: #1b515e;
  transition: color 0.3s;
}

.search-wrapper.focused .search-input::placeholder {
  color: #1b515e;
}

.search-icon-wrapper {
  padding: 12px 18px;
  cursor: pointer;
  color: #b0d4a1;
  transition: all 0.3s;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-icon-wrapper:hover {
  color: #b0d4a1;
  transform: scale(1.1);
}

.search-icon {
  font-size: 1.2rem;
  transition: transform 0.3s;
}

.search-icon-wrapper:active .search-icon {
  transform: scale(0.9);
}

.search-effects {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  overflow: hidden;
  border-radius: 50px;
}

.search-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(238, 243, 236, 0.1) 0%, transparent 70%);
  opacity: 0;
  transition: opacity 0.3s;
}

.search-wrapper.focused .search-glow {
  opacity: 1;
}

.loading-spinner {
  width: 1.2rem;
  height: 1.2rem;
  border: 2px solid rgba(53, 170, 6, 0.2);
  border-top-color: #b0d4a1;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.suggestions-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  left: 0;
  right: 0;
  z-index: 10;
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  margin-top: 0;
  overflow: hidden;
  border: 1px solid #e0e0e0;
}

.suggestion-item {
  display: flex;
  align-items: center;
  padding: 12px 18px;
  cursor: pointer;
  transition: all 0.2s;
  border-bottom: 1px solid #f5f5f5;
}

.suggestion-item:last-child {
  border-bottom: none;
}

.suggestion-item:hover,
.suggestion-item.highlighted {
  background-color: #f0f9ec;
  padding-left: 22px;
}

.suggestion-icon {
  color: #b0d4a1;
  margin-right: 12px;
  flex-shrink: 0;
  font-size: 1.1rem;
}

.suggestion-content {
  flex-grow: 1;
}

.suggestion-main {
  font-weight: 600;
  color: #333;
  font-size: 0.95rem;
  margin-bottom: 2px;
}

.suggestion-secondary {
  font-size: 0.85rem;
  color: #777;
}

.dropdown-enter-active {
  animation: slideDown 0.3s ease-out;
}

.dropdown-leave-active {
  animation: slideDown 0.2s ease-in reverse;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Tablets */
@media (max-width: 768px) {
  .navbar-header {
    padding: 10px 15px 6px;
  }

  .navbar-title {
    font-size: 1.3rem;
  }

  .navbar-content {
    padding: 8px 12px !important;
  }

  .search-container {
    max-width: 100%;
  }

  .search-input {
    font-size: 0.95rem;
    padding: 10px 15px;
  }

  .search-icon-wrapper {
    padding: 10px 15px;
  }

  .suggestion-item {
    padding: 10px 15px;
  }

  .menu-wrapper :deep(svg),
  .menu-wrapper :deep(i) {
    font-size: 24px;
  }
}

/* Mobile */
@media (max-width: 576px) {
  .navbar-header {
    padding: 8px 12px 5px;
  }

  .navbar-title {
    font-size: 1.2rem;
  }

  .navbar-content {
    padding: 6px 10px !important;
  }

  .menu-wrapper {
    width: 36px;
    height: 36px;
  }

  .spacer {
    width: 36px;
  }

  .search-wrapper {
    border-radius: 40px;
  }

  .search-input {
    font-size: 0.9rem;
    padding: 9px 12px;
  }

  .search-input::placeholder {
    font-size: 0.85rem;
  }

  .search-icon-wrapper {
    padding: 9px 12px;
  }

  .search-icon {
    font-size: 1.1rem;
  }

  .suggestions-dropdown {
    border-radius: 12px;
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  }

  .suggestion-item {
    padding: 10px 12px;
  }

  .suggestion-item:hover,
  .suggestion-item.highlighted {
    padding-left: 16px;
  }

  .suggestion-icon {
    font-size: 1rem;
    margin-right: 10px;
  }

  .suggestion-main {
    font-size: 0.9rem;
  }

  .suggestion-secondary {
    font-size: 0.8rem;
  }

  .menu-wrapper :deep(svg),
  .menu-wrapper :deep(i) {
    font-size: 22px;
  }
}

/* Mobile pequeño */
@media (max-width: 375px) {
  .navbar-title {
    font-size: 1.1rem;
  }

  .menu-wrapper {
    width: 32px;
    height: 32px;
  }

  .spacer {
    width: 32px;
  }

  .menu-wrapper :deep(svg),
  .menu-wrapper :deep(i) {
    font-size: 20px;
  }
}
</style>