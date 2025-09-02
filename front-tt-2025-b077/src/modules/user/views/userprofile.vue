<template>
  <div>
    <div class="fixed-header">
      <div class="header-content">
        <hamburgermenu @navigate="handleNavigation" />
        <div class="header-title">
          <p class="mb-1">Mi cuenta</p>
        </div>
        <div class="header-spacer"></div>
      </div>
    </div>

    <div class="main-content">
      <div class="container">
        <div class="card shadow-sm p-4">
          <div class="row">
            <div class="col-12 col-md-4 col-lg-3 mb-3 mb-md-0">
              <div class="user-info-section">
                <div class="user-profile-container">
                  <v-avatar size="180" class="rounded-circle border overflow-hidden">
                    <v-img
                      :src="
                        user?.userProfile?.picture && user.userProfile.picture !== '#'
                          ? user.userProfile.picture
                          : undefined
                      "
                      cover
                    >
                      <template #placeholder>
                        <div
                          class="d-flex align-center justify-center w-100 h-100"
                          style="background: #1b515e"
                        >
                          <v-icon icon="mdi-account" size="96" color="white" />
                        </div>
                      </template>
                      <template #error>
                        <div
                          class="d-flex align-center justify-center w-100 h-100"
                          style="background: #1b515e"
                        >
                          <v-icon icon="mdi-account" size="96" color="white" />
                        </div>
                      </template>
                    </v-img>
                  </v-avatar>

                  <div class="mt-3">
                    <button class="btn btn-outline-primary-custom" @click="triggerFileDialog">
                      <i class="fas fa-image me-2"></i>Cambiar foto
                    </button>
                    <input
                      ref="fileInput"
                      type="file"
                      accept="image/*"
                      class="d-none"
                      @change="onFileSelected"
                    />
                  </div>

                  <div class="user-details">
                    <h4
                      class="text-primary fw-bold d-flex align-items-center justify-content-center justify-content-md-start"
                    >
                      {{ user.fullName }}
                      <i v-if="user.verified" class="fas fa-check-circle ms-2 text-success"></i>
                    </h4>
                    <p class="text-muted text-center text-md-start">
                      <i class="fas fa-at me-2 icon-custom"></i>{{ user.username }}
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-12 col-md-8 col-lg-9">
              <div class="form-content">
                <div v-for="(value, key) in editableFields" :key="key" class="mb-3">
                  <label class="form-label fw-bold text-custom">
                    <i :class="icons[key]" class="me-2 icon-custom"></i>{{ labels[key] }}
                  </label>
                  <div class="input-group">
                    <input
                      v-model="editableFields[key]"
                      :readonly="!editing[key]"
                      class="form-control input-no-border"
                      :class="{ 'border-custom': editing[key] }"
                      :data-field="key"
                      @click="handleInputClick(key)"
                      @focus="handleInputFocus(key)"
                      @blur="handleInputBlur(key)"
                    />
                    <button class="btn btn-icon-custom" @click="toggleEdit(key)">
                      <i v-if="editing[key]" class="fas fa-times text-danger"></i>
                      <i v-else class="fas fa-edit icon-custom"></i>
                    </button>
                  </div>
                </div>

                <div class="mb-0">
                  <label class="form-label fw-bold text-custom">
                    <i class="fas fa-envelope me-2 icon-custom"></i>Correo Electrónico
                  </label>
                  <div class="input-group">
                    <input
                      :value="user.email"
                      readonly
                      class="form-control input-no-border input-readonly"
                    />
                    <div class="input-group-append">
                      <span class="btn btn-icon-readonly">
                        <i class="fas fa-lock icon-custom"></i>
                      </span>
                    </div>
                  </div>
                </div>

                <div class="mt-4">
                  <div class="d-flex justify-content-between align-items-center mb-3">
                    <button
                      ref="moreActionsButton"
                      class="btn btn-more-actions"
                      @click="toggleMoreActions"
                      type="button"
                    >
                      <i class="fas fa-ellipsis-h me-2"></i>Más acciones
                      <i
                        :class="showMoreActions ? 'fas fa-chevron-up' : 'fas fa-chevron-down'"
                        class="ms-2"
                      ></i>
                    </button>

                    <button class="btn btn-modify" :disabled="!isModified" @click="updateUserData">
                      <i class="fas fa-save me-2"></i>Modificar
                    </button>
                  </div>
                  <div
                    ref="moreActionsPanel"
                    class="more-actions-panel"
                    :class="{ show: showMoreActions }"
                  >
                    <div class="more-actions-content">
                      <div class="d-flex flex-column flex-sm-row justify-content-center gap-3">
                        <router-link
                          :to="{ name: 'changepassword' }"
                          class="btn btn-outline-primary-custom"
                        >
                          <i class="fas fa-key me-2"></i>
                          Cambiar contraseña
                        </router-link>

                        <router-link :to="{ name: 'deleteaccount' }" class="btn-link-custom">
                          <i class="fas fa-trash-alt me-2"></i>
                          Eliminar cuenta
                        </router-link>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <BottomNavbar @navigate="handleNavigation" />

    <ConfirmModal
      :show="showConfirmModal"
      @confirm="confirmNavigation"
      @cancel="cancelNavigation"
      @close="closeModal"
    />
  </div>
</template>

<script>
import { mapState, mapActions, mapMutations } from 'vuex'
import BottomNavbar from '@/components/bottomnavbar.vue'
import hamburgermenu from '@/components/hamburgermenu.vue'
import ConfirmModal from '@/components/confirmmodal.vue'
import { getErrorDetails } from '@/utils/utils'

export default {
  components: {
    BottomNavbar,
    hamburgermenu,
    ConfirmModal,
  },
  data() {
    return {
      messageType: '',
      messageIcon: '',
      editableFields: {
        name: '',
        lastName: '',
        secondLastName: '',
        phoneNumber: '',
      },
      originalFields: {},
      editing: {
        name: false,
        lastName: false,
        secondLastName: false,
        phoneNumber: false,
      },
      warningMessage: '',
      showConfirmModal: false,
      showMoreActions: false,
      pendingNavigation: null,
      icons: {
        name: 'fas fa-user',
        lastName: 'fas fa-id-card',
        secondLastName: 'fas fa-id-card-alt',
        phoneNumber: 'fas fa-phone',
      },
      labels: {
        name: 'Nombre',
        lastName: 'Apellido Paterno',
        secondLastName: 'Apellido Materno',
        phoneNumber: 'Teléfono',
      },
    }
  },
  computed: {
    ...mapState('auth', ['user']),
    isModified() {
      return JSON.stringify(this.editableFields) !== JSON.stringify(this.originalFields)
    },
  },
  methods: {
    ...mapActions('user', {
      updateUser: 'updateUser',
      uploadUserPicture: 'uploadUserPicture',
    }),
    ...mapActions('auth', {
      getProfile: 'getProfile',
    }),
    ...mapMutations('auth', { setUser: 'setUser' }),

    initializeEditableFields() {
      this.editableFields = {
        name: this.user.name || '',
        lastName: this.user.lastName || '',
        secondLastName: this.user.secondLastName || '',
        phoneNumber: this.user.phoneNumber || '',
      }
      this.originalFields = { ...this.editableFields }
    },

    toggleEdit(key) {
      this.editing[key] = !this.editing[key]
    },
    toggleMoreActions() {
      this.showMoreActions = !this.showMoreActions
      if (this.showMoreActions) {
        this.$nextTick(() => {
          setTimeout(() => {
            this.scrollToShowPanel()
          }, 350)
        })
      }
    },

    scrollToShowPanel() {
      if (this.$refs.moreActionsPanel) {
        const panel = this.$refs.moreActionsPanel
        const panelRect = panel.getBoundingClientRect()
        const navbarHeight = 80
        const buffer = 20
        const viewportHeight = window.innerHeight
        const panelBottom = panelRect.bottom
        if (panelBottom > viewportHeight - navbarHeight) {
          const scrollTarget =
            window.scrollY + (panelBottom - viewportHeight) + navbarHeight + buffer
          window.scrollTo({ top: scrollTarget, behavior: 'smooth' })
        }
      }
    },

    handleInputClick(key) {
      if (!this.editing[key]) {
        this.editing[key] = true
        this.$nextTick(() => {
          const inputs = document.querySelectorAll(`input[data-field="${key}"]`)
          if (inputs.length > 0) {
            const input = inputs[0]
            input.focus()
            input.select()
          }
        })
      }
    },
    handleInputFocus(key) {
      if (!this.editing[key]) {
        this.editing[key] = true
      }
    },
    handleInputBlur(key) {
      setTimeout(() => {
        this.editing[key] = false
      }, 100)
    },

    getModifiedFields() {
      const modifiedFields = {}
      Object.keys(this.editableFields).forEach((key) => {
        if (this.editableFields[key] !== this.originalFields[key]) {
          modifiedFields[key] = this.editableFields[key]
        }
      })
      return modifiedFields
    },

    triggerFileDialog() {
      this.$refs.fileInput.click()
    },

    onFileSelected(e) {
      const file = e.target.files && e.target.files[0];
      if (!file) return;

      this.localPreview = URL.createObjectURL(file);
      this.uploadingImage = true;

      this.uploadUserPicture(file)
        .then((response) => {
          this.$alert.success(response.message);
          this.getProfile()
            .then((response) => {
              this.setUser(response.data)
            })
        })
        .catch((error) => {
          this.localPreview = null;
          this.$alert.error(getErrorDetails(error));
        })
        .finally(() => {
          this.uploadingImage = false;
          if (this.$refs.fileInput) this.$refs.fileInput.value = '';
        });
    },

    updateUserData() {
      const modifiedFields = this.getModifiedFields()
      if (Object.keys(modifiedFields).length === 0) {
        return
      }
      this.updateUser(modifiedFields)
        .then((response) => {
          this.setUser(response.data)
          this.$alert.success(response.message)
          this.originalFields = { ...this.editableFields }
          Object.keys(this.editing).forEach((key) => (this.editing[key] = false))
        })
        .catch((error) => {
          this.$alert.error(getErrorDetails(error))
        })
    },

    handleNavigation(destination, event = null) {
      if (this.isModified) {
        if (event) {
          event.preventDefault()
        }
        this.pendingNavigation = destination
        this.showConfirmModal = true
        return false
      }
      return true
    },

    confirmNavigation() {
      this.showConfirmModal = false
      if (this.pendingNavigation) {
        this.executeNavigation(this.pendingNavigation)
        this.pendingNavigation = null
      }
    },

    cancelNavigation() {
      this.showConfirmModal = false
      this.pendingNavigation = null
    },

    closeModal() {
      this.cancelNavigation()
    },

    executeNavigation(destination) {
      this.resetFormState()
      this.$router.push(destination)
    },

    resetFormState() {
      this.editableFields = { ...this.originalFields }
      Object.keys(this.editing).forEach((key) => {
        this.editing[key] = false
      })
      this.warningMessage = ''
    },

    handleBeforeUnload(e) {
      if (this.isModified) {
        e.preventDefault()
        e.returnValue = 'Tienes cambios sin guardar. ¿Estás seguro de que quieres salir?'
        return e.returnValue
      }
    },
  },
  mounted() {
    this.initializeEditableFields()
  },
  watch: {
    user: {
      handler() {
        this.initializeEditableFields()
      },
      deep: true,
    },
  },
  beforeRouteLeave(to, from, next) {
    if (this.isModified) {
      this.pendingNavigation = to.path || to
      this.showConfirmModal = true
      next(false)
    } else {
      next()
    }
  },
  beforeUnmount() {
    window.removeEventListener('beforeunload', this.handleBeforeUnload)
  },
  created() {
    window.addEventListener('beforeunload', this.handleBeforeUnload)
  },
}
</script>

<style scoped>
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

.main-content {
  margin-top: 70px;
  padding: 0.5rem 0;
  margin-bottom: 70px;
}

/* Panel izquierdo: centrado en móvil, columna con divisor en md+ */
.user-info-section {
  text-align: center;
}

.user-profile-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.user-details {
  margin-top: 1rem;
  text-align: center;
}

/* Divisor y alineaciones solo en pantallas md+ */
@media (min-width: 768px) {
  .user-info-section {
    text-align: left;
    padding-right: 2rem;
    border-right: 1px solid #e9ecef;
  }
  .user-profile-container {
    align-items: flex-start;
  }
  .user-details {
    text-align: left;
  }
  .form-content {
    padding-left: 2rem;
  }
}

.text-custom {
  color: #1b515e;
}

.border-custom {
  border: 2px solid #1b515e;
}

.input-readonly {
  background-color: #f8f9fa !important;
  cursor: not-allowed;
}

.icon-custom {
  color: #abcd9e !important;
}

.btn-icon-custom {
  border: none !important;
  background: transparent !important;
  padding: 0.5rem;
}

.btn-icon-custom:hover {
  background-color: rgba(171, 205, 158, 0.1) !important;
  border: none !important;
}

.btn-icon-custom:focus {
  box-shadow: none !important;
  border: none !important;
}

.btn-icon-readonly {
  border: none !important;
  background: transparent !important;
  cursor: not-allowed;
  padding: 0.5rem;
}

.btn-more-actions {
  background-color: transparent;
  color: #6c757d;
  border: 1px solid #dee2e6;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  font-size: 0.9rem;
}

.btn-more-actions:hover {
  background-color: #f8f9fa;
  border-color: #adb5bd;
  color: #495057;
}

.btn-more-actions:focus {
  box-shadow: 0 0 0 0.2rem rgba(108, 117, 125, 0.25);
  outline: none;
}

.more-actions-panel {
  max-height: 0;
  overflow: hidden;
  transition: all 0.3s ease;
  opacity: 0;
}

.more-actions-panel.show {
  max-height: 200px;
  opacity: 1;
  margin-top: 1rem;
}

.more-actions-content {
  padding-top: 1rem;
  border-top: 1px solid #e9ecef;
}

.btn-more-actions i:last-child {
  transition: transform 0.3s ease;
}

.btn-modify {
  background-color: #1b515e;
  color: white;
  border: none;
  padding: 0.5rem 1.25rem;
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
}

.btn-modify:hover {
  background-color: #144049;
}

.btn-modify:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-outline-primary-custom {
  background-color: transparent;
  color: #1b515e;
  border: 2px solid #1b515e;
  padding: 0.5rem 1.25rem;
  border-radius: 4px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  white-space: nowrap;
}

.btn-outline-primary-custom:hover {
  background-color: #1b515e;
  color: white;
  text-decoration: none;
}

.btn-outline-primary-custom:focus {
  box-shadow: 0 0 0 0.2rem rgba(27, 81, 94, 0.25);
  text-decoration: none;
}

.btn-link-custom {
  background-color: transparent;
  color: #abcd9e;
  border: none;
  padding: 0.5rem 1.25rem;
  border-radius: 4px;
  font-weight: 500;
  text-decoration: underline;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  white-space: nowrap;
}

.btn-link-custom:hover {
  color: #8fb885;
  text-decoration: underline;
}

.btn-link-custom:focus {
  color: #8fb885;
  text-decoration: underline;
  outline: none;
}

.input-no-border {
  border: none !important;
  border-bottom: 1px solid #e9ecef !important;
  border-radius: 0 !important;
  box-shadow: none !important;
  background-color: transparent;
  cursor: pointer;
}

.input-no-border[readonly] {
  cursor: pointer;
}

.input-no-border:not([readonly]) {
  cursor: text;
}

.input-no-border:focus {
  border: none !important;
  border-bottom: 2px solid #1b515e !important;
  box-shadow: none !important;
  outline: none;
}

.input-no-border.border-custom {
  border: none !important;
  border-bottom: 2px solid #1b515e !important;
  box-shadow: none !important;
}
</style>
