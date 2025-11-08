<template>
  <div class="floating-controls d-flex align-items-center gap-3">
    <div class="route-card flex-fill p-3">
      <div class="d-flex align-items-center mb-1 position-relative">
        <span class="icon origin me-3 d-inline-block"></span>
        <input
          v-model="localOrigin"
          placeholder="Tu ubicación"
          class="form-control route-input border-0"
        />
        <div v-if="showOriginSuggestions" class="autocomplete-dropdown origin-dropdown">
          <div
            v-for="suggestion in originSuggestions"
            :key="suggestion.place_id"
            class="autocomplete-item"
            @click="selectOrigin(suggestion)"
          >
            {{ suggestion.description }}
          </div>
        </div>
      </div>

      <div class="d-flex align-items-center justify-content-between my-1">
        <span class="route-line flex-fill me-3"></span>
        <button class="btn swap-btn p-2" @click="handleSwap">⇆</button>
      </div>

      <div class="d-flex align-items-center position-relative">
        <span class="icon destination me-3">
          <i class="fa-solid fa-location-dot"></i>
        </span>
        <input
          v-model="localDestination"
          placeholder="Destino"
          class="form-control route-input border-0"
        />
        <button
          v-if="localDestination"
          class="btn-clear-destination p-0"
          @click="clearDestination"
          type="button"
          title="Limpiar destino"
        >
          ✕
        </button>
        <div v-if="showDestinationSuggestions" class="autocomplete-dropdown destination-dropdown">
          <div
            v-for="suggestion in destinationSuggestions"
            :key="suggestion.place_id"
            class="autocomplete-item"
            @click="selectDestination(suggestion)"
          >
            {{ suggestion.description }}
          </div>
        </div>
      </div>
    </div>

    <button
      class="btn search-btn-circular d-flex align-items-center justify-content-center"
      @click="$emit('search')"
      :disabled="disabled"
      :class="{ disabled: disabled }"
    >
      🔍
    </button>
  </div>
</template>

<script>
export default {
  name: 'RouteInputs',
  props: {
    origin: {
      type: String,
      default: '',
    },
    destination: {
      type: String,
      default: '',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    suppressDestinationSearch: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      localOrigin: this.origin,
      localDestination: this.destination,
      autocompleteService: null,
      originSuggestions: [],
      destinationSuggestions: [],
      showOriginSuggestions: false,
      showDestinationSuggestions: false,
      isSelectingOrigin: false,
      isSelectingDestination: false,
    }
  },
  watch: {
    origin(newVal) {
      this.localOrigin = newVal
    },
    destination(newVal) {
      this.localDestination = newVal
    },
    localOrigin(newVal) {
      this.$emit('update:origin', newVal)

      if (this.isSelectingOrigin) {
        this.isSelectingOrigin = false
        return
      }

      if (newVal && newVal.length > 2 && newVal !== 'Tu ubicación') {
        this.getOriginSuggestions(newVal)
      } else {
        this.originSuggestions = []
        this.showOriginSuggestions = false
      }
    },
    localDestination(newVal) {
      this.$emit('update:destination', newVal)

      if (this.isSelectingDestination) {
        this.isSelectingDestination = false
        return
      }

      if (this.suppressDestinationSearch) {
        this.showDestinationSuggestions = false
        this.destinationSuggestions = []
        return
      }

      if (newVal && newVal.length > 2) {
        this.getDestinationSuggestions(newVal)
      } else {
        this.destinationSuggestions = []
        this.showDestinationSuggestions = false
      }
    },
    suppressDestinationSearch(newVal) {
      if (newVal) {
        this.showDestinationSuggestions = false
        this.destinationSuggestions = []
      }
    },
  },
  mounted() {
    this.initAutocomplete()
    document.addEventListener('click', this.handleClickOutside)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside)
  },
  methods: {
    initAutocomplete() {
      if (window.google && window.google.maps && window.google.maps.places) {
        this.autocompleteService = new window.google.maps.places.AutocompleteService()
      }
    },
    getOriginSuggestions(input) {
      if (!this.autocompleteService) {
        this.initAutocomplete()
      }

      if (this.autocompleteService) {
        this.autocompleteService.getPlacePredictions(
          {
            input,
            componentRestrictions: { country: 'mx' },
          },
          (predictions) => {
            this.originSuggestions = predictions || []
            this.showOriginSuggestions = this.originSuggestions.length > 0
          }
        )
      }
    },
    getDestinationSuggestions(input) {
      if (!this.autocompleteService) {
        this.initAutocomplete()
      }

      if (this.autocompleteService) {
        this.autocompleteService.getPlacePredictions(
          {
            input,
            componentRestrictions: { country: 'mx' },
          },
          (predictions) => {
            this.destinationSuggestions = predictions || []
            this.showDestinationSuggestions = this.destinationSuggestions.length > 0
          }
        )
      }
    },
    selectOrigin(suggestion) {
      this.isSelectingOrigin = true
      this.localOrigin = suggestion.description
      this.showOriginSuggestions = false
      this.originSuggestions = []
    },
    selectDestination(suggestion) {
      this.isSelectingDestination = true
      this.localDestination = suggestion.description
      this.showDestinationSuggestions = false
      this.destinationSuggestions = []
    },
    clearDestination() {
      this.localDestination = ''
      this.showDestinationSuggestions = false
      this.destinationSuggestions = []
      this.$emit('clear-route')
    },
    handleSwap() {
      const temp = this.localOrigin
      this.localOrigin = this.localDestination
      this.localDestination = temp
      this.showOriginSuggestions = false
      this.showDestinationSuggestions = false
    },
    handleClickOutside(event) {
      if (!this.$el.contains(event.target)) {
        this.showOriginSuggestions = false
        this.showDestinationSuggestions = false
      }
    },
  },
}
</script>

<style scoped>
.floating-controls {
  position: absolute;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 10;
  width: 90%;
  max-width: 500px;
  animation: fadeInUp 0.6s ease-out;
}

.route-card {
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(15px);
  border-radius: 16px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12) !important;
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
  position: relative;
}

.route-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: inherit;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.4) 0%, rgba(255, 255, 255, 0.1) 100%);
  z-index: -1;
}

.route-input {
  background: transparent !important;
  font-weight: 500 !important;
  color: #1b515e !important;
  padding: 0.375rem 0 0.375rem 0 !important;
  padding-right: 40px !important;
  line-height: 1.3 !important;
}

.route-input::placeholder {
  color: #7a9ba3 !important;
  font-weight: 400 !important;
}

.route-input:focus {
  background: transparent !important;
  box-shadow: none !important;
  color: #1b515e !important;
}

.icon {
  width: 24px;
  height: 24px;
  flex-shrink: 0;
  text-align: center;
}

.icon.origin {
  border: 3px solid #1b515e;
  background: white;
  border-radius: 50%;
}

.icon.destination i {
  color: rgb(216, 26, 26);
  font-size: 20px;
  position: relative;
  top: 2px;
}

.route-line {
  height: 2px;
  background: #1b515e;
  border-radius: 1px;
}

.swap-btn {
  background: rgba(247, 250, 252, 0.9) !important;
  border: 1px solid rgba(27, 81, 94, 0.2) !important;
  color: #1b515e !important;
  font-size: 1.1rem !important;
  border-radius: 8px !important;
  width: 40px;
  height: 40px;
  transition: all 0.3s ease !important;
}

.swap-btn:hover {
  background: rgba(237, 242, 247, 0.95) !important;
  transform: scale(1.1) rotate(180deg) !important;
  border-color: #1b515e !important;
}

.search-btn-circular {
  background: white !important;
  border: none !important;
  color: #1b515e !important;
  font-size: 1.25rem !important;
  width: 64px !important;
  height: 64px !important;
  border-radius: 50% !important;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15) !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  flex-shrink: 0 !important;
}

.search-btn-circular:hover {
  background: #f8f9fa !important;
  color: #1b515e !important;
  transform: translateY(-3px) scale(1.05) !important;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.2) !important;
}

.search-btn-circular:disabled,
.search-btn-circular.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.autocomplete-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  max-height: 300px;
  overflow-y: auto;
  margin-top: 4px;
}

.autocomplete-item {
  padding: 12px 16px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  font-size: 0.9rem;
  color: #333;
  transition: background-color 0.15s ease;
}

.autocomplete-item:last-child {
  border-bottom: none;
}

.autocomplete-item:hover {
  background-color: #f5f5f5;
  color: #1b515e;
}

.btn-clear-destination {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #1b515e;
  font-size: 1.4rem;
  cursor: pointer;
  padding: 6px 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  z-index: 5;
  line-height: 1;
  width: 32px;
  height: 32px;
}

.btn-clear-destination:hover {
  color: #1b515e;
  transform: translateY(-50%) scale(1.3);
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}

@media (max-width: 768px) {
  .floating-controls {
    width: 95%;
    top: 15px;
  }

  .search-btn-circular {
    width: 56px !important;
    height: 56px !important;
  }
}

@media (max-width: 480px) {
  .floating-controls {
    width: 98%;
    top: 10px;
  }

  .search-btn-circular {
    width: 48px !important;
    height: 48px !important;
  }
}

@media (min-width: 1200px) {
  .floating-controls {
    max-width: 600px;
  }

  .search-btn-circular {
    width: 72px !important;
    height: 72px !important;
  }
}
</style>