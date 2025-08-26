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
            <div class="col-12 col-md-4 col-lg-3 mb-4 mb-md-0">
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
                <div class="mb-3">
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

                <!-- Botón -->
                <div class="d-flex justify-content-end mt-3">
                  <button
                    class="btn btn-modify"
                    :disabled="!isModified"
                    @click="modifyData"
                  >
                    <i class="fas fa-save me-2"></i>Modificar
                  </button>
                </div>

                <!-- Mensajes -->
                <div v-if="warningMessage" class="alert alert-warning mt-3">
                  <i class="fas fa-exclamation-triangle me-2"></i>{{ warningMessage }}
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
    <div v-if="showConfirmModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h5 class="modal-title">
            <i class="fas fa-exclamation-triangle text-warning me-2"></i>
            Cambios sin guardar
          </h5>
        </div>
        <div class="modal-body">
          <p>Has realizado cambios en tus datos que no han sido guardados.</p>
          <p>Si continúas, perderás todos los cambios realizados. Para guardar los cambios, selecciona el botón "Modificar".</p>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="cancelNavigation">
            <i class="fas fa-times me-2"></i>Cancelar
          </button>
          <button class="btn btn-danger" @click="confirmNavigation">
            <i class="fas fa-sign-out-alt me-2"></i>Salir sin guardar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BottomNavbar from "@/components/bottomnavbar.vue";
import hamburgermenu from "@/components/hamburgermenu.vue";

export default {
  components: { BottomNavbar, hamburgermenu },
  data() {
    return {
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
      this.showWarning("✅ Los datos se guardaron correctamente");
      this.originalFields = { ...this.editableFields };
      Object.keys(this.editing).forEach(key => {
        this.editing[key] = false;
      });
    },
    showWarning(message) {
      this.warningMessage = message;
      setTimeout(() => {
        this.warningMessage = "";
      }, 3000);
    },
    // Método principal para manejar navegación desde cualquier componente
    handleNavigation(destination) {
      console.log('Intentando navegar a:', destination);
      
      // Si hay cambios sin guardar, mostrar modal de confirmación
      if (this.isModified) {
        this.pendingNavigation = destination;
        this.showConfirmModal = true;
        return; // Detener la navegación
      } 
      
      // Si no hay cambios, proceder con la navegación
      this.executeNavigation(destination);
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
    
    // Ejecutar la navegación real (aquí implementas tu lógica de ruteo)
    executeNavigation(destination) {
      console.log('Ejecutando navegación a:', destination);
      
      // Resetear campos modificados al navegar
      this.resetFormState();
      
      // Aquí implementas tu lógica de navegación específica
      // Por ejemplo:
      if (this.$router) {
        this.$router.push(destination);
      } else {
        // Si no usas Vue Router, puedes usar window.location o emit eventos
        this.$emit('navigate', destination);
        // O: window.location.href = destination;
      }
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
  beforeRouteLeave(to, from, next) {
    if (this.isModified) {
      this.pendingNavigation = to;
      this.showConfirmModal = true;
      next(false);
    } else {
      next();
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
  margin-top: 80px;
  padding: 1rem 0;
  margin-bottom: 80px;
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

.btn-modify {
  background-color: #1B515E;
  color: white;
}

.btn-modify:disabled {
  opacity: 0.6;
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

/* Estilos del Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  padding: 1rem;
}

.modal-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  max-width: 500px;
  width: 100%;
  animation: modalAppear 0.2s ease-out;
}

@keyframes modalAppear {
  from {
    opacity: 0;
    transform: scale(0.9) translateY(-10px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

.modal-header {
  padding: 1.5rem 1.5rem 1rem 1.5rem;
  border-bottom: 1px solid #e9ecef;
}

.modal-title {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
  color: #1B515E;
  display: flex;
  align-items: center;
}

.modal-body {
  padding: 1.5rem;
}

.modal-body p {
  margin-bottom: 0.75rem;
  color: #495057;
  line-height: 1.5;
}

.modal-body p:last-child {
  margin-bottom: 0;
}

.modal-footer {
  padding: 1rem 1.5rem 1.5rem 1.5rem;
  display: flex;
  gap: 0.75rem;
  justify-content: flex-end;
  border-top: 1px solid #e9ecef;
}

.modal-footer .btn {
  padding: 0.5rem 1.25rem;
  border-radius: 4px;
  font-weight: 500;
  border: 1px solid;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
}

.btn-secondary {
  background-color: #6c757d;
  border-color: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background-color: #5a6268;
  border-color: #545b62;
  color: white;
}

.btn-danger {
  background-color: #dc3545;
  border-color: #dc3545;
  color: white;
}

.btn-danger:hover {
  background-color: #c82333;
  border-color: #bd2130;
  color: white;
}

/* Responsivo para móviles */
@media (max-width: 576px) {
  .modal-overlay {
    padding: 0.5rem;
  }
  
  .modal-footer {
    flex-direction: column-reverse;
  }
  
  .modal-footer .btn {
    width: 100%;
    justify-content: center;
  }
}
</style>