<template>
  <div v-if="show" class="modal-overlay" @click="handleOverlayClick">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <div class="warning-icon-container">
          <i class="fas fa-exclamation-triangle warning-icon"></i>
        </div>
        <h5 class="modal-title">{{ title }}</h5>
      </div>
      <div class="modal-body">
        <p>{{ message }}</p>
        <p v-if="submessage">{{ submessage }}</p>
      </div>
      <div class="modal-footer">
        <button class="btn btn-cancel" @click="handleCancel">
          <i class="fas fa-arrow-left me-2"></i>{{ cancelText }}
        </button>
        <button class="btn btn-danger" @click="handleConfirm">
          <i class="fas fa-sign-out-alt me-2"></i>{{ confirmText }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConfirmModal',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: 'Cambios sin guardar'
    },
    message: {
      type: String,
      default: 'Has realizado cambios en tus datos que no han sido guardados.'
    },
    submessage: {
      type: String,
      default: 'Si continúas, perderás todos los cambios realizados. Para guardar los cambios, selecciona el botón "Modificar".'
    },
    cancelText: {
      type: String,
      default: 'Quedarme aquí'
    },
    confirmText: {
      type: String,
      default: 'Salir sin guardar'
    },
    closeOnOverlay: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    handleConfirm() {
      this.$emit('confirm');
    },
    handleCancel() {
      this.$emit('cancel');
    },
    handleOverlayClick() {
      if (this.closeOnOverlay) {
        this.$emit('close');
      }
    }
  }
};
</script>

<style scoped>
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
  backdrop-filter: blur(2px);
}

.modal-content {
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  max-width: 480px;
  width: 100%;
  animation: modalAppear 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  overflow: hidden;
}

@keyframes modalAppear {
  from {
    opacity: 0;
    transform: scale(0.8) translateY(-20px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

.modal-header {
  padding: 2rem 2rem 1rem 2rem;
  text-align: center;
  background: linear-gradient(135deg, #fff 0%, #f8f9fa 100%);
}

.warning-icon-container {
  margin-bottom: 1rem;
}

.warning-icon {
  font-size: 3rem;
  color: #f39c12;
  background: #fff3cd;
  border-radius: 50%;
  padding: 1rem;
  border: 3px solid #f1c40f;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.modal-title {
  margin: 0;
  font-size: 1.4rem;
  font-weight: 600;
  color: #1B515E;
}

.modal-body {
  padding: 1rem 2rem 1.5rem 2rem;
  text-align: center;
}

.modal-body p {
  margin-bottom: 0.75rem;
  color: #6c757d;
  line-height: 1.6;
  font-size: 0.95rem;
}

.modal-body p:last-child {
  margin-bottom: 0;
}

.modal-footer {
  padding: 1rem 2rem 2rem 2rem;
  display: flex;
  gap: 1rem;
  justify-content: center;
  background: #f8f9fa;
}

.modal-footer .btn {
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-weight: 500;
  border: none;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  min-width: 140px;
  justify-content: center;
}

.btn-cancel {
  background-color: #ABCD9E;
  color: white;
  box-shadow: 0 2px 8px rgba(171, 205, 158, 0.3);
}

.btn-cancel:hover {
  background-color: #9bb88e;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(171, 205, 158, 0.4);
}

.btn-danger {
  background-color: #e74c3c;
  color: white;
  box-shadow: 0 2px 8px rgba(231, 76, 60, 0.3);
}

.btn-danger:hover {
  background-color: #c0392b;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(231, 76, 60, 0.4);
}

/* Responsivo para móviles */
@media (max-width: 576px) {
  .modal-overlay {
    padding: 1rem;
  }
  
  .modal-header {
    padding: 1.5rem 1.5rem 1rem 1.5rem;
  }
  
  .modal-body {
    padding: 1rem 1.5rem;
  }
  
  .modal-footer {
    flex-direction: column-reverse;
    padding: 1rem 1.5rem 1.5rem 1.5rem;
  }
  
  .modal-footer .btn {
    width: 100%;
    min-width: auto;
  }
  
  .warning-icon {
    font-size: 2.5rem;
    padding: 0.8rem;
  }
}
</style>