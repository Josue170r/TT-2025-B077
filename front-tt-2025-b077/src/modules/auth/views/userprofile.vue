<template>
  <div>
    <!-- Header fijo -->
    <div class="fixed-header">
      <div class="header-content">
        <hamburgermenu @navigate="handleNavigation" />
        <div class="header-title">
          <p class="mb-1">Mi cuenta</p>
        </div>
        <div class="header-spacer"></div>
      </div>
    </div>

    <!-- Contenido principal con padding-top para compensar el header fijo -->
    <div class="main-content">
      <div class="container">
        <div class="card shadow-sm p-4">
          <div class="row">
            <!-- Columna de información del usuario -->
            <div class="col-12 col-md-4 col-lg-3 mb-0 mb-md-0">
              <div class="user-info-section">
                <div class="user-profile-container">
                  <img
                    v-if="user.userProfile.picture"
                    :src="user.userProfile.picture"
                    alt="Foto de perfil"
                    class="profile-pic rounded-circle border"
                  />
                  <div v-else class="profile-placeholder rounded-circle d-flex align-items-center justify-content-center">
                    <i class="fas fa-user profile-icon text-white"></i>
                  </div>
                  
                  <!-- Nombre y usuario -->
                  <div class="user-details">
                    <h4 class="text-primary fw-bold d-flex align-items-center justify-content-center justify-content-md-start">
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
            
            <!-- Columna de campos editables -->
            <div class="col-12 col-md-8 col-lg-9">
              <div class="form-content">
                <!-- Datos editables -->
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

                <!-- Campo de correo (no editable) -->
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

                <!-- Botones -->
                <div class="mt-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                    <button ref="moreActionsButton" class="btn btn-more-actions" @click="toggleMoreActions" type="button">
                        <i class="fas fa-ellipsis-h me-2"></i>Más acciones
                        <i :class="showMoreActions ? 'fas fa-chevron-up' : 'fas fa-chevron-down'" class="ms-2"></i>
                    </button>
                    
                    <button class="btn btn-modify" :disabled="!isModified" @click="modifyData">
                        <i class="fas fa-save me-2"></i>Modificar
                    </button>
                    </div>
                  <!-- Botones secundarios colapsables -->
                  <div 
                    ref="moreActionsPanel"
                    class="more-actions-panel"
                    :class="{ 'show': showMoreActions }"
                  >
                    <div class="more-actions-content">
                      <div class="d-flex flex-column flex-sm-row justify-content-center gap-3">
                        <router-link 
                          :to="{ path: '/change-password' }" 
                          class="btn btn-outline-primary-custom"
                          @click.native="handleRouterLinkClick('/change-password', $event)"
                        >
                          <i class="fas fa-key me-2"></i>Cambiar contraseña
                        </router-link>
                        
                        <a 
                          href="#" 
                          class="btn-link-custom"
                          @click.prevent="handleNavigation('/delete-account')"
                        >
                          <i class="fas fa-trash-alt me-2"></i>Eliminar cuenta
                        </a>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Mensajes -->
                <div v-if="warningMessage" class="alert mt-3" :class="messageType">
                    <i :class="messageIcon" class="me-2"></i>{{ warningMessage }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Bottom Navbar -->
    <BottomNavbar @navigate="handleNavigation" />
    
    <!-- Modal de confirmación para cambios no guardados -->
    <ConfirmModal
      :show="showConfirmModal"
      @confirm="confirmNavigation"
      @cancel="cancelNavigation"
      @close="closeModal"
    />
  </div>
</template>

<script>
import BottomNavbar from "@/components/bottomnavbar.vue";
import hamburgermenu from "@/components/hamburgermenu.vue";
import ConfirmModal from "@/components/confirmmodal.vue";

export default {
  components: { 
    BottomNavbar, 
    hamburgermenu,
    ConfirmModal 
  },
  data() {
    return {
        messageType: '',
        messageIcon: '',
      user: {
        email: "mruriel982@gmail.com",
        username: "Merle85",
        name: "Jessika",
        lastName: "Collins",
        secondLastName: null,
        phoneNumber: null,
        userProfile: { id: 10, picture: null },
        fullName: "Jessika Collins",
        verified: true,
      },
      editableFields: {
        name: "Jessika",
        lastName: "Collins",
        secondLastName: "",
        phoneNumber: "",
      },
      originalFields: {},
      editing: {
        name: false,
        lastName: false,
        secondLastName: false,
        phoneNumber: false,
      },
      warningMessage: "",
      showConfirmModal: false,
      showMoreActions: false,
      pendingNavigation: null,
      icons: {
        name: "fas fa-user",
        lastName: "fas fa-id-card",
        secondLastName: "fas fa-id-card-alt",
        phoneNumber: "fas fa-phone",
      },
      labels: {
        name: "Nombre",
        lastName: "Apellido Paterno",
        secondLastName: "Apellido Materno",
        phoneNumber: "Teléfono",
      },
    };
  },
  computed: {
    isModified() {
      return JSON.stringify(this.editableFields) !== JSON.stringify(this.originalFields);
    },
  },
  methods: {
    toggleEdit(key) {
      this.editing[key] = !this.editing[key];
    },
    
    // Toggle panel de más acciones con scroll automático
    toggleMoreActions() {
      this.showMoreActions = !this.showMoreActions;
      
      // Si se está expandiendo el panel, hacer scroll después de la animación
      if (this.showMoreActions) {
        this.$nextTick(() => {
          // Esperar a que termine la animación CSS (300ms) + un pequeño buffer
          setTimeout(() => {
            this.scrollToShowPanel();
          }, 350);
        });
      }
    },
    
    // Método para hacer scroll y mostrar el panel expandido
    scrollToShowPanel() {
      if (this.$refs.moreActionsPanel) {
        const panel = this.$refs.moreActionsPanel;
        const button = this.$refs.moreActionsButton;
        
        // Obtener la posición del panel expandido
        const panelRect = panel.getBoundingClientRect();
        const buttonRect = button.getBoundingClientRect();
        
        // Calcular el offset considerando el navbar fijo inferior (aproximadamente 80px)
        const navbarHeight = 80;
        const headerHeight = 80;
        const buffer = 20; // Espacio adicional para mejor visibilidad
        
        // Verificar si el panel está visible completamente en el viewport
        const viewportHeight = window.innerHeight;
        const panelBottom = panelRect.bottom;
        
        // Si el panel se sale del viewport (considerando el navbar inferior)
        if (panelBottom > (viewportHeight - navbarHeight)) {
          // Calcular la posición de scroll necesaria
          const scrollTarget = window.scrollY + (panelBottom - viewportHeight) + navbarHeight + buffer;
          window.scrollTo({
            top: scrollTarget,
            behavior: 'smooth'
          });
        }
      }
    },
    
    handleInputClick(key) {
      if (!this.editing[key]) {
        this.editing[key] = true;
        this.$nextTick(() => {
          const inputs = document.querySelectorAll(`input[data-field="${key}"]`);
          if (inputs.length > 0) {
            const input = inputs[0];
            input.focus();
            input.select();
          }
        });
      }
    },
    handleInputFocus(key) {
      if (!this.editing[key]) {
        this.editing[key] = true;
      }
    },
    handleInputBlur(key) {
      setTimeout(() => {
        this.editing[key] = false;
      }, 100);
    },
    modifyData() {
        this.showWarning("Los datos se guardaron correctamente", 'success');
        this.originalFields = { ...this.editableFields };
        Object.keys(this.editing).forEach(key => {
            this.editing[key] = false;
        });
    },
    showWarning(message, type = 'success') {
    this.warningMessage = message;
    
    if (type === 'success') {
        this.messageType = 'alert-success';
        this.messageIcon = 'fas fa-check-circle';
    } else if (type === 'error') {
        this.messageType = 'alert-danger';
        this.messageIcon = 'fas fa-times-circle';
    }
    
    setTimeout(() => {
        this.warningMessage = "";
        this.messageType = '';
        this.messageIcon = '';
    }, 3000);
    },
    // Manejar clicks en router-links
    handleRouterLinkClick(destination, event) {
      // Si hay cambios sin guardar, prevenir la navegación del router-link
      if (this.isModified) {
        event.preventDefault();
        this.pendingNavigation = destination;
        this.showConfirmModal = true;
        return false;
      }
      // Si no hay cambios, permitir navegación normal del router-link
      return true;
    },
    
    // Método principal para manejar navegación desde cualquier componente
    handleNavigation(destination, event = null) {
      console.log('Intentando navegar a:', destination);
      
      // Si hay cambios sin guardar, prevenir navegación y mostrar modal
      if (this.isModified) {
        // Prevenir navegación por defecto si viene de un evento
        if (event) {
          event.preventDefault();
        }
        
        this.pendingNavigation = destination;
        this.showConfirmModal = true;
        return false; // Detener la navegación
      } 
      
      // Si no hay cambios, permitir navegación normal
      return true;
    },
    
    // Confirmar navegación perdiendo cambios
    confirmNavigation() {
      this.showConfirmModal = false;
      if (this.pendingNavigation) {
        this.executeNavigation(this.pendingNavigation);
        this.pendingNavigation = null;
      }
    },
    
    // Cancelar navegación y mantener cambios
    cancelNavigation() {
      this.showConfirmModal = false;
      this.pendingNavigation = null;
    },
    
    // Cerrar modal haciendo click fuera
    closeModal() {
      this.cancelNavigation();
    },
    
    // Ejecutar la navegación real
    executeNavigation(destination) {
    // Resetear campos modificados al navegar
    this.resetFormState();
    
    // Navegar usando Vue Router
    this.$router.push(destination);
    },
    
    // Resetear estado del formulario
    resetFormState() {
      // Restaurar campos originales
      this.editableFields = { ...this.originalFields };
      
      // Cerrar todos los campos de edición
      Object.keys(this.editing).forEach(key => {
        this.editing[key] = false;
      });
      
      // Limpiar mensajes
      this.warningMessage = "";
    },
    
    // Manejar evento beforeunload del navegador
    handleBeforeUnload(e) {
      if (this.isModified) {
        e.preventDefault();
        e.returnValue = 'Tienes cambios sin guardar. ¿Estás seguro de que quieres salir?';
        return e.returnValue;
      }
    },
  },
  mounted() {
    this.originalFields = { ...this.editableFields };
  },
  // Guard de navegación para Vue Router
  beforeRouteLeave(to, from, next) {
    // Si hay cambios sin guardar, interceptar la navegación
    if (this.isModified) {
      this.pendingNavigation = to.path || to;
      this.showConfirmModal = true;
      next(false); // Bloquear la navegación
    } else {
      next(); // Permitir la navegación
    }
  },
  beforeUnmount() {
    window.removeEventListener('beforeunload', this.handleBeforeUnload);
  },
  created() {
    window.addEventListener('beforeunload', this.handleBeforeUnload);
  },
};
</script>

<style scoped>
/* Header fijo */
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
  color: #1B515E;
}

.header-spacer {
  width: 0px;
}

/* Contenido principal con espacio para header fijo */
.main-content {
  margin-top: 70px;
  padding:  0.5rem 0;
  margin-bottom: 70px;
}

/* Sección de información del usuario - Responsiva */
.user-info-section {
  text-align: center;
  text-align: center;
}

@media (min-width: 768px) {
  .user-info-section {
    text-align: left;
    padding-right: 2rem;
    border-right: 1px solid #e9ecef;
    position: sticky;
    top: 100px;
    min-height: 500px;
  }
}

.user-profile-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

@media (max-width: 767px) {
  .user-profile-container {
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    gap: 1rem;
  }
}

/* Foto de perfil responsiva */
.profile-pic {
  width: 150px;
  height: 150px;
  object-fit: cover;
}

.profile-placeholder {
  width: 150px;
  height: 150px;
  background-color: #1B515E;
}

.profile-icon {
  font-size: 4rem;
}

@media (max-width: 767px) {
  .profile-pic,
  .profile-placeholder {
    width: 80px;
    height: 80px;
  }
  
  .profile-icon {
    font-size: 2rem;
  }
}

/* Detalles del usuario */
.user-details {
  margin-top: 1rem;
  flex: 1;
}

@media (max-width: 767px) {
  .user-details {
    margin-top: 0;
    text-align: left;
  }
  
  .user-details h4 {
    justify-content: flex-start !important;
  }
  
  .user-details p {
    text-align: left !important;
  }
}

/* Contenido del formulario */
.form-content {
  padding-left: 0;
}

@media (min-width: 768px) {
  .form-content {
    padding-left: 2rem;
  }
}

/* Estilos base */
.text-custom {
  color: #1B515E;
}

.border-custom {
  border: 2px solid #1B515E;
}

.input-readonly {
  background-color: #f8f9fa !important;
  cursor: not-allowed;
}

.icon-custom {
  color: #ABCD9E !important;
}

/* Botones de edición sin bordes */
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

/* Botón "Más acciones" */
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

/* Panel colapsable de más acciones */
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

/* Animación suave para los chevrons */
.btn-more-actions i:last-child {
  transition: transform 0.3s ease;
}

.btn-modify {
  background-color: #1B515E;
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

/* Botón de cambiar contraseña */
.btn-outline-primary-custom {
  background-color: transparent;
  color: #1B515E;
  border: 2px solid #1B515E;
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
  background-color: #1B515E;
  color: white;
  text-decoration: none;
}

.btn-outline-primary-custom:focus {
  box-shadow: 0 0 0 0.2rem rgba(27, 81, 94, 0.25);
  text-decoration: none;
}

/* Botón de eliminar cuenta*/
.btn-link-custom {
  background-color: transparent;
  color: #ABCD9E;
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

/* Estilos para inputs editables */
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
  border-bottom: 2px solid #1B515E !important;
  box-shadow: none !important;
  outline: none;
}

.input-no-border.border-custom {
  border: none !important;
  border-bottom: 2px solid #1B515E !important;
  box-shadow: none !important;
}

</style>