<template>
  <div class="d-flex justify-content-center">
    <div class="calendar-container card shadow">
      <!-- Header con rango seleccionado -->
      <div class="card-header bg-white border-bottom">
        <div class="text-center">
          <small class="text-custom">Rango seleccionado</small>
          <div class="d-flex align-items-center justify-content-center my-2">
            <div class="text-center">
              <small class="text-custom d-block">Inicio</small>
              <span class="fw-bold date-text">{{ formatDateWithYear(startDate) }}</span>
            </div>
            <i class="fas fa-arrow-right mx-3 text-custom-muted"></i>
            <div class="text-center">
              <small class="text-custom d-block">Fin</small>
              <span class="fw-bold date-text">{{ formatDateWithYear(endDate) }}</span>
            </div>
          </div>
          <span v-if="daysDifference > 0" class="badge bg-custom rounded-pill">
            {{ daysDifference }} días
          </span>
        </div>
      </div>

      <!-- Calendario -->
      <div class="card-body">
        <!-- Header del mes -->
        <div class="d-flex justify-content-between align-items-center mb-3">
          <button @click="previousMonth" class="btn btn-outline-custom btn-sm">
            <i class="fas fa-chevron-left"></i>
          </button>
          <h6 class="mb-0 text-capitalize calendar-title">{{ monthYear }}</h6>
          <button @click="nextMonth" class="btn btn-outline-custom btn-sm">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>

        <!-- Días de la semana -->
        <div class="row g-1 mb-2">
          <div v-for="day in weekDays" :key="day" class="col text-center">
            <small class="text-custom fw-bold">{{ day }}</small>
          </div>
        </div>

        <!-- Grid de días -->
        <div class="calendar-grid">
          <div
            v-for="day in calendarDays"
            :key="day.key"
            :class="getDayClass(day)"
            @click="selectDate(day)"
          >
            {{ day.day }}
          </div>
        </div>
      </div>

      <!-- BOTONES DE ACCIÓN -->
      <div class="card-footer bg-transparent border-top px-3 py-3">
        <div class="d-flex gap-2 justify-content-end">
          <button 
            @click="clearSelection" 
            class="btn btn-outline-custom btn-sm"
            :disabled="!startDate && !endDate"
          >
            <i class="fas fa-times me-1"></i>
            Limpiar
          </button>
          <!-- <button 
            @click="applySelection" 
            class="btn btn-custom btn-sm"
            :disabled="!startDate || !endDate"
          >
            <i class="fas fa-check me-1"></i>
            Aplicar
          </button> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DateRangeCalendar',
  props: {
    modelValue: {
      type: Object,
      default: () => ({ start: null, end: null })
    }
  },
  emits: ['update:modelValue', 'rangeSelected', 'rangeApplied', 'rangeCleared'],
  data() {
    return {
      currentDate: new Date(),
      startDate: null,
      endDate: null,
      isSelectingRange: false,
      weekDays: ['L', 'M', 'X', 'J', 'V', 'S', 'D']
    }
  },
  computed: {
    monthYear() {
      const months = [
        'enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio',
        'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'
      ];
      return `${months[this.currentDate.getMonth()]} de ${this.currentDate.getFullYear()}`;
    },
    calendarDays() {
      const year = this.currentDate.getFullYear();
      const month = this.currentDate.getMonth();
      
      const firstDay = new Date(year, month, 1);
      const lastDay = new Date(year, month + 1, 0);
      
      let startWeekDay = firstDay.getDay();
      startWeekDay = startWeekDay === 0 ? 6 : startWeekDay - 1;
      
      const days = [];
      const prevMonth = new Date(year, month, 0);
      for (let i = startWeekDay - 1; i >= 0; i--) {
        const date = new Date(year, month - 1, prevMonth.getDate() - i);
        days.push({
          day: prevMonth.getDate() - i,
          date: date,
          isCurrentMonth: false,
          isPast: this.isPastDate(date),
          isToday: this.isToday(date),
          key: `prev-${prevMonth.getDate() - i}`
        });
      }
      for (let day = 1; day <= lastDay.getDate(); day++) {
        const date = new Date(year, month, day);
        days.push({
          day: day,
          date: date,
          isCurrentMonth: true,
          isPast: this.isPastDate(date),
          isToday: this.isToday(date),
          key: `current-${day}`
        });
      }
      const remainingDays = 42 - days.length;
      for (let day = 1; day <= remainingDays; day++) {
        const date = new Date(year, month + 1, day);
        days.push({
          day: day,
          date: date,
          isCurrentMonth: false,
          isPast: this.isPastDate(date),
          isToday: this.isToday(date),
          key: `next-${day}`
        });
      }
      return days;
    },
    daysDifference() {
      if (!this.startDate || !this.endDate) return 0;
      const diffTime = Math.abs(this.endDate - this.startDate);
      return Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
    }
  },
  watch: {
    modelValue: {
      handler(newValue) {
        if (newValue && newValue.start && newValue.end) {
          this.startDate = new Date(newValue.start);
          this.endDate = new Date(newValue.end);
        }
      },
      immediate: true
    }
  },
  methods: {
    previousMonth() {
      this.currentDate = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() - 1);
    },
    nextMonth() {
      this.currentDate = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() + 1);
    },
    selectDate(dayObj) {
      // VALIDACIÓN: No permitir fechas pasadas
      if (dayObj.isPast) {
        console.log('Fecha pasada bloqueada:', dayObj.date);
        return;
      }
      
      const selectedDate = new Date(dayObj.date);
      console.log('Fecha seleccionada:', selectedDate);
      
      if (!this.startDate || (this.startDate && this.endDate)) {
        this.startDate = selectedDate;
        this.endDate = null;
        this.isSelectingRange = true;
      } else if (this.startDate && !this.endDate) {
        if (selectedDate < this.startDate) {
          this.endDate = this.startDate;
          this.startDate = selectedDate;
        } else {
          this.endDate = selectedDate;
        }
        this.isSelectingRange = false;
        this.emitRangeChange();
      }
    },
    getDayClass(dayObj) {
      let classes = 'calendar-day';
      
      if (!dayObj.isCurrentMonth) {
        classes += ' other-month';
      }
      
      // VALIDACIONES IMPORTANTES
      if (dayObj.isPast) {
        classes += ' past-date';
      }
      
      if (dayObj.isToday) {
        classes += ' today';
      }
      
      if (this.startDate && this.isSameDay(dayObj.date, this.startDate)) {
        classes += ' range-start';
      }
      
      if (this.endDate && this.isSameDay(dayObj.date, this.endDate)) {
        classes += ' range-end';
      }
      
      if (this.startDate && this.endDate && 
          dayObj.date > this.startDate && dayObj.date < this.endDate) {
        classes += ' in-range';
      }
      
      return classes;
    },
    isSameDay(date1, date2) {
      return date1.getDate() === date2.getDate() &&
             date1.getMonth() === date2.getMonth() &&
             date1.getFullYear() === date2.getFullYear();
    },
    isPastDate(date) {
      const today = new Date();
      // Resetear horas para comparación exacta
      today.setHours(0, 0, 0, 0);
      const checkDate = new Date(date);
      checkDate.setHours(0, 0, 0, 0);
      return checkDate < today;
    },
    isToday(date) {
      const today = new Date();
      return date.getDate() === today.getDate() &&
             date.getMonth() === today.getMonth() &&
             date.getFullYear() === today.getFullYear();
    },
    formatDate(date) {
      if (!date) return '';
      const day = date.getDate();
      const months = [
        'enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio',
        'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'
      ];
      return `${day} ${months[date.getMonth()]}`;
    },
    formatDateWithYear(date) {
      if (!date) return '';
      const day = date.getDate();
      const year = date.getFullYear();
      const months = [
        'enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio',
        'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'
      ];
      return `${day} ${months[date.getMonth()]} ${year}`;
    },
    emitRangeChange() {
      const range = {
        start: this.startDate,
        end: this.endDate
      };
      this.$emit('update:modelValue', range);
      this.$emit('rangeSelected', range);
    },
    clearSelection() {
      this.startDate = null;
      this.endDate = null;
      this.isSelectingRange = false;
      
      const emptyRange = { start: null, end: null };
      this.$emit('update:modelValue', emptyRange);
      this.$emit('rangeCleared', emptyRange);
    },
    // applySelection() {
    //   if (this.startDate && this.endDate) {
    //     const range = {
    //       start: this.startDate,
    //       end: this.endDate
    //     };
    //     this.$emit('rangeApplied', range);
    //   }
    // }
  }
}
</script>

<style scoped>
/* Clases de utilidad para colores personalizados */
.text-custom {
  color: #1b515e !important;
}

.text-custom-muted {
  color: rgba(27, 81, 94, 0.6) !important;
}

.date-text {
  color: #1b515e;
  font-size: 0.95rem;
}

.calendar-title {
  color: #1b515e;
  font-weight: 600;
}

/* Badge personalizado */
.badge.bg-custom {
  background-color: #abcd9e !important;
  color: #1b515e !important;
  font-size: 12px;
  padding: 6px 12px;
  font-weight: 600;
}

/* Botones personalizados */
.btn-outline-custom {
  color: #1b515e;
  border-color: #abcd9e;
  background-color: transparent;
  font-weight: 500;
  font-size: 0.875rem;
}

.btn-outline-custom:hover:not(:disabled) {
  color: #1b515e;
  background-color: #abcd9e;
  border-color: #abcd9e;
}

.btn-outline-custom:focus {
  color: #1b515e;
  background-color: #abcd9e;
  border-color: #abcd9e;
  box-shadow: 0 0 0 0.2rem rgba(171, 205, 158, 0.25);
}

.btn-outline-custom:disabled {
  color: rgba(27, 81, 94, 0.4);
  border-color: rgba(171, 205, 158, 0.4);
  background-color: transparent;
  cursor: not-allowed;
}

/* Botón sólido personalizado */
.btn-custom {
  background-color: #abcd9e;
  border-color: #abcd9e;
  color: #1b515e;
  font-weight: 600;
  font-size: 0.875rem;
}

.btn-custom:hover:not(:disabled) {
  background-color: #9bbd8e;
  border-color: #9bbd8e;
  color: #1b515e;
}

.btn-custom:focus {
  background-color: #9bbd8e;
  border-color: #9bbd8e;
  color: #1b515e;
  box-shadow: 0 0 0 0.2rem rgba(171, 205, 158, 0.25);
}

.btn-custom:disabled {
  background-color: rgba(171, 205, 158, 0.4);
  border-color: rgba(171, 205, 158, 0.4);
  color: rgba(27, 81, 94, 0.5);
  cursor: not-allowed;
}

.calendar-container {
  max-width: 350px;
  border-radius: 12px;
  overflow: hidden;
  border: 0.5px solid #ABCD9E;
  box-shadow: 
    0 1rem 3rem rgba(252, 255, 235, 0.2),
    0 0.5rem 2rem rgba(204, 243, 207, 0.15) !important;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 3px;
  padding: 8px;
}

.calendar-day {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  margin: 0 auto;
  border-radius: 8px;
  color: #1b515e;
}

.calendar-day:hover {
  background-color: rgba(171, 205, 158, 0.2);
  color: #1b515e;
}

.calendar-day.other-month {
  color: rgba(27, 81, 94, 0.5);
  font-weight: 400;
}

.calendar-day.other-month:hover {
  background-color: rgba(171, 205, 158, 0.1);
  color: rgba(27, 81, 94, 0.7);
}

/* ESTILOS PARA VALIDACIÓN DE FECHAS */

/* Día de hoy - DESTACADO CON BORDE */
.calendar-day.today {
  background-color: rgba(171, 205, 158, 0.2);
  border: 2px solid #abcd9e;
  font-weight: 700;
  box-shadow: 0 0 0 1px rgba(171, 205, 158, 0.3);
}

.calendar-day.today:hover {
  background-color: rgba(171, 205, 158, 0.3);
}

/* Fechas pasadas - BLOQUEADAS */
.calendar-day.past-date {
  color: rgba(27, 81, 94, 0.25) !important;
  background-color: rgba(200, 200, 200, 0.1) !important;
  cursor: not-allowed !important;
  text-decoration: line-through;
  opacity: 0.5;
}

.calendar-day.past-date:hover {
  background-color: rgba(200, 200, 200, 0.1) !important;
  color: rgba(27, 81, 94, 0.25) !important;
  cursor: not-allowed !important;
}

/* Días del rango intermedio */
.calendar-day.in-range {
  background-color: rgba(171, 205, 158, 0.3);
  color: #1b515e;
  font-weight: 600;
}

.calendar-day.in-range.other-month {
  background-color: rgba(171, 205, 158, 0.25);
  color: #1b515e;
}

/* Día de inicio */
.calendar-day.range-start {
  background-color: #abcd9e;
  color: #1b515e;
  font-weight: 700;
  box-shadow: 0 2px 8px rgba(171, 205, 158, 0.4);
}

.calendar-day.range-start:hover {
  background-color: #9bbd8e;
}

/* Día de fin */
.calendar-day.range-end {
  background-color: #abcd9e;
  color: #1b515e;
  font-weight: 700;
  box-shadow: 0 2px 8px rgba(171, 205, 158, 0.4);
}

.calendar-day.range-end:hover {
  background-color: #9bbd8e;
}

/* Cuando inicio y fin son el mismo día */
.calendar-day.range-start.range-end {
  background-color: #abcd9e;
  box-shadow: 0 2px 8px rgba(171, 205, 158, 0.5);
}

.calendar-day.range-start.range-end:hover {
  background-color: #9bbd8e;
}

/* Pequeños indicadores visuales */
.calendar-day.range-start::after {
  content: '';
  position: absolute;
  top: 2px;
  left: 2px;
  width: 6px;
  height: 6px;
  background-color: rgba(27, 81, 94, 0.6);
  border-radius: 50%;
}

.calendar-day.range-end::after {
  content: '';
  position: absolute;
  top: 2px;
  right: 2px;
  width: 6px;
  height: 6px;
  background-color: rgba(27, 81, 94, 0.6);
  border-radius: 50%;
}

.calendar-day.range-start.range-end::after {
  top: 2px;
  left: 50%;
  transform: translateX(-50%);
}

/* Header mejorado */
.card-header {
  background: linear-gradient(135deg, rgba(171, 205, 158, 0.1) 0%, rgba(171, 205, 158, 0.15) 100%);
  border-radius: 12px 12px 0 0;
  color: #1b515e;
}

/* Footer para los botones */
.card-footer {
  background: rgba(171, 205, 158, 0.05);
  border-top: 1px solid rgba(171, 205, 158, 0.2);
}

/* Responsive */
@media (max-width: 400px) {
  .calendar-day {
    width: 36px;
    height: 36px;
    font-size: 13px;
  }
  
  .calendar-grid {
    gap: 2px;
    padding: 4px;
  }
}
</style>