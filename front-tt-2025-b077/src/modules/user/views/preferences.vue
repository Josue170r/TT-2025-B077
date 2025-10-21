<template>
  <div class="fixed-header mb-5">
    <div class="header-content">
      <hamburgermenu @navigate="handleNavigation" />
      <div class="header-title">
        <p class="mb-1">Escoge tus preferencias de viaje</p>
      </div>
      <div class="header-spacer"></div>
    </div>
  </div>

  <transition name="fade">
    <div v-if="show" class="container">
      <div class="header">
        <h6 class="sub-text text-center mb-4">
          Se recomendarán lugares de acuerdo a tus preferencias
        </h6>
        <p class="text-center text-muted small">Puedes seleccionar hasta 4 categorías</p>
        <hr />
      </div>

      <div class="categories-wrapper">
        <div class="row g-3">
          <div v-for="category in categories" :key="category.id" class="col-6 col-md-4 col-lg-3">
            <div
              class="category-card d-flex flex-column justify-content-center align-items-center"
              :class="{
                selected: selectedCategoryIds.includes(category.id),
                disabled:
                  !selectedCategoryIds.includes(category.id) && selectedCategoryIds.length >= 4,
              }"
              @click="toggleCategory(category.id)"
              role="button"
              tabindex="0"
              @keydown.enter.prevent="toggleCategory(category.id)"
              @keydown.space.prevent="toggleCategory(category.id)"
              :style="{ backgroundImage: `url(${category.picture})` }"
              loading="lazy"
            >
              <span class="category-text">
                <strong>{{ category.category.split(' ')[0] }}</strong>
                <span v-if="category.category.split(' ').length > 1">
                  <br />
                  {{ category.category.split(' ').slice(1).join(' ') }}
                </span>
              </span>
              <span v-if="selectedCategoryIds.includes(category.id)" class="selected-label">
                Seleccionada ✓
              </span>
            </div>
          </div>
        </div>
      </div>

      <div class="text-center mt-4">
        <hr class="mt-0" />
        <p class="text-muted small mb-3">
          {{ selectedCategoryIds.length }} de 4 categorías seleccionadas
        </p>
        <button
          class="btn-confirm"
          @click="confirmSelection"
          :disabled="selectedCategoryIds.length === 0"
        >
          Confirmar
        </button>
      </div>
    </div>
  </transition>
</template>

<script>
import { mapActions } from 'vuex/dist/vuex.cjs.js'
import { getErrorDetails } from '@/utils/utils'
import hamburgermenu from '@/components/hamburgermenu.vue'

export default {
  name: 'UserPreferences',
  components: {
    hamburgermenu,
  },
  data() {
    return {
      show: false,
      categories: [],
      selectedCategoryIds: [],
      userPreferences: [],
    }
  },
  mounted() {
    this.show = true
    this.initializePreferences()
  },
  methods: {
    ...mapActions('user', {
      fetchCategoryPlaces: 'fetchCategoryPlaces',
      fetchUserPreferences: 'fetchUserPreferences',
      saveUserPreferences: 'saveUserPreferences',
    }),

    async initializePreferences() {
      try {
        await this.fetchCategories()
        await this.loadUserPreferences()
      } catch (error) {
        console.error('Error initializing preferences:', error)
      }
    },

    fetchCategories() {
      return this.fetchCategoryPlaces()
        .then((response) => {
          this.categories = response.data.data || []
        })
        .catch((error) => {
          this.$alert.error(getErrorDetails(error))
        })
    },

    loadUserPreferences() {
      return this.fetchUserPreferences()
        .then((response) => {
          this.userPreferences = response.data.data || []
          this.selectedCategoryIds = this.userPreferences.map((pref) => pref.category.id)
        })
        .catch((error) => {
          this.$alert.error(getErrorDetails(error))
        })
    },

    toggleCategory(categoryId) {
      const index = this.selectedCategoryIds.indexOf(categoryId)

      if (index !== -1) {
        this.selectedCategoryIds.splice(index, 1)
      } else {
        if (this.selectedCategoryIds.length < 4) {
          this.selectedCategoryIds.push(categoryId)
        } else {
          this.$alert.error('Solo puedes seleccionar hasta 4 categorías')
        }
      }
    },

    confirmSelection() {
      if (this.selectedCategoryIds.length === 0) {
        this.$alert.error('Debes seleccionar al menos una categoría')
        return
      }

      const preferencesData = {
        categories: this.selectedCategoryIds.map((id) => ({ id })),
      }

      this.saveUserPreferences(preferencesData)
        .then((response) => {
          this.$alert.success({
            title: 'Preferencias guardadas',
            text: response.data.message,
          })

          this.show = false
          setTimeout(() => {
            this.$router.push({ name: 'home' })
          }, 500)
        })
        .catch((error) => {
          this.$alert.error(getErrorDetails(error))
        })
    },
  },
}
</script>

<style scoped>
.container {
  max-width: 1200px;
  padding: 0 15px;
  padding-top: 80px;
}

.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background-color: white;
  box-shadow: 0 2px 4px rgba(9, 128, 54, 0.1);
  border-bottom: 1px solid #aae3aa;
}

.header-content {
  display: flex;
  align-items: center;
  padding: 1rem;
  max-width: 100%;
}

.header-title {
  flex: 1;
  text-align: center;
}

.header-title p {
  font-weight: bold;
  font-size: 1.2rem;
  color: #1b515e;
}

.header-spacer {
  width: 0px;
}

.categories-wrapper {
  max-height: 63vh;
  overflow-y: auto;
  padding-top: 10px;
}

.categories-wrapper::-webkit-scrollbar {
  display: none;
}

.category-card {
  position: relative;
  border: 2px solid #abcd9e;
  border-radius: 12px;
  height: 100px;
  user-select: none;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
  transition:
    transform 0.15s ease,
    border-color 0.2s ease,
    opacity 0.2s ease;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.category-card.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.category-card.disabled:hover {
  transform: none;
  border-color: #abcd9e;
}

.category-text {
  position: absolute;
  bottom: 8px;
  left: 8px;
  font-size: 16px;
  font-weight: normal;
  color: #1b515e;
  line-height: 1.1;
  text-shadow: 0 1px 2px rgba(255, 255, 255, 0.8);
}

.category-text strong {
  font-weight: bold;
}

.category-card:not(.selected):not(.disabled):hover {
  transform: translateY(-4px);
  border-color: #1b515e;
  cursor: pointer;
}

.category-card.selected {
  border-color: #1b515e;
  border-width: 3px;
}

.category-card.selected:hover {
  border-color: #1b515e;
  transform: translateY(-4px);
}

.selected-label {
  position: absolute;
  top: 8px;
  right: 8px;
  background-color: #abcd9e;
  color: #1b515e;
  padding: 3px 6px;
  border-radius: 6px;
  font-size: 0.7rem;
  font-weight: bold;
}

hr {
  height: 4px;
  background-color: #35aa06;
  border-color: #35aa06;
}

.title {
  color: #1b515e;
  font-size: 1.5rem;
}

.sub-text {
  font-weight: 400;
  font-size: 14px;
  color: #1b515e;
  text-align: center;
}

.btn-confirm {
  background-color: #1b515e;
  color: #ffffff;
  border: none;
  border-radius: 12px;
  padding: 10px 20px;
  font-size: 1rem;
  cursor: pointer;
  transition:
    background-color 0.2s ease,
    transform 0.15s ease;
  width: auto;
  min-width: 120px;
}

.btn-confirm:hover:not(:disabled) {
  background-color: #2a798d;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px #1b515e;
}

.btn-confirm:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
