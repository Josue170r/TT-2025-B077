<template>
  <div class="search-container">
    <div class="search-wrapper" :class="{ 'focused': isFocused, 'has-value': query }">
      <div class="search-icon-wrapper">
        <i class="fa-solid fa-magnifying-glass"></i>
      </div>
      <input
        type="text"
        class="search-input"
        placeholder="Explora un lugar"
        v-model="query"
        @focus="handleFocus"
        @blur="handleBlur"
        @input="handleInput"
      />
      <div class="search-effects">
        <div class="search-glow"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SearchInput",
  data() {
    return {
      query: "",
      isFocused: false
    };
  },
  methods: {
    handleFocus() {
      this.isFocused = true;
    },
    handleBlur() {
      this.isFocused = false;
    },
    handleInput() {
      // Emite el valor para que el componente padre pueda usarlo
      this.$emit('search-input', this.query);
    }
  }
};
</script>

<style scoped>
.search-container {
  width: 100%;
  max-width: 100%;
  margin: 0;
  padding: 0;
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
  border-color: #ABCD9E;
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 6px 25px rgba(171, 205, 158, 0.25);
}

.search-wrapper.has-value {
  background: rgba(255, 255, 255, 0.95);
}

.search-icon-wrapper {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.search-icon {
  color: #1B515E;
  font-size: 1.1rem;
  opacity: 0.6;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.search-wrapper.focused .search-icon {
  color: #ABCD9E;
  opacity: 1;
  transform: scale(1.1);
}

.search-input {
  width: 100%;
  height: 100%;
  border: none;
  outline: none;
  background: transparent;
  padding: 0 50px 0 45px;
  font-size: 14px;
  font-weight: 500;
  color: #1B515E;
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
  background: linear-gradient(45deg, 
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

/* Responsive */
@media (max-width: 576px) {
  .search-wrapper {
    height: 42px;
  }
  
  .search-input {
    font-size: 13px;
    padding: 0 40px 0 40px;
  }
  
  .search-icon-wrapper {
    left: 12px;
  }
  
  .search-icon {
    font-size: 1rem;
  }
}

@media (min-width: 768px) {
  .search-wrapper {
    height: 52px;
    max-width: 1000px;
  }
  
  .search-input {
    font-size: 16px;
    padding: 0 60px 0 55px;
  }
  
  .search-icon-wrapper {
    left: 20px;
  }
  
  .search-icon {
    font-size: 1.3rem;
  }
}

@media (min-width: 1200px) {
  .search-wrapper {
    height: 58px;
    border-radius: 30px;
  }
  
  .search-input {
    font-size: 18px;
    padding: 0 65px 0 60px;
    letter-spacing: 0.4px;
  }
  
  .search-icon-wrapper {
    left: 22px;
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
    height: 64px;
    border-radius: 32px;
    max-width: 1000px;
    margin: 0 auto;
  }
  
  .search-input {
    font-size: 20px;
    padding: 0 70px 0 65px;
    letter-spacing: 0.5px;
  }
  
  .search-icon-wrapper {
    left: 25px;
  }
  
  .search-icon {
    font-size: 1.5rem;
  }
  
  .search-glow {
    border-radius: 32px;
  }
}
</style>