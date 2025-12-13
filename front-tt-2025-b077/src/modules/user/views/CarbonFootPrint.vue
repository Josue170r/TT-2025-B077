<template>
  <NavTopFootPrint />
  <div class="carbon-wrapper" style="margin-top: 20px">
    <div class="row g-4 g-md-5">
      <div class="col-12 col-md-6">
        <div class="card shadow-sm carbon-card">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <button
                @click="previousMonth"
                class="btn btn-link p-2 rounded-circle nav-btn"
                :disabled="currentMonthIndex === 0"
              >
                <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2.5"
                    d="M15 19l-7-7 7-7"
                  />
                </svg>
              </button>
              <h5 class="mb-0 fw-semibold month-title">{{ monthLabel }}</h5>
              <button
                @click="nextMonth"
                class="btn btn-link p-2 rounded-circle nav-btn"
                :disabled="currentMonthIndex === months.length - 1"
              >
                <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2.5"
                    d="M9 5l7 7-7 7"
                  />
                </svg>
              </button>
            </div>
            <div class="chart-container mb-4">
              <apexchart type="bar" height="200" :options="chartOptions" :series="series" />
            </div>
            <div class="border-top pt-3">
              <div
                class="d-flex justify-content-between align-items-center py-2 px-2 rounded stat-row"
              >
                <span class="fw-medium stat-label">Hoy</span>
                <span class="fw-semibold stat-value"
                  >{{ todayValue }} gr de CO<sub class="subscript">2</sub></span
                >
              </div>
              <div
                class="d-flex justify-content-between align-items-center py-2 px-2 rounded stat-row"
              >
                <span class="fw-medium stat-label">Más bajo del mes</span>
                <span class="fw-semibold stat-value"
                  >{{ lowestValue }} gr de CO<sub class="subscript">2</sub></span
                >
              </div>
              <div
                class="d-flex justify-content-between align-items-center py-3 px-3 rounded stat-row-highlight border-start border-4 mt-2"
              >
                <span class="fw-medium stat-label">Total del mes</span>
                <span class="fw-bold stat-value-highlight fs-6"
                  >{{ totalValue }} gr de CO<sub class="subscript">2</sub></span
                >
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-12 col-md-6">
        <div class="content-wrapper">
          <div class="mb-3 d-flex justify-content-center gap-2 flex-wrap">
            <button
              v-for="n in 4"
              :key="n"
              @click="summaryVersion = n"
              class="btn btn-sm version-btn"
              :class="{ active: summaryVersion === n }"
            >
              Impacto {{ n }}
            </button>
          </div>
          <div v-if="summaryVersion === 1" class="impact-summary p-3 rounded mb-3">
            <p class="impact-text mb-2">
              Has generado
              <strong>{{ totalValue }} gramos de CO<sub class="subscript">2</sub></strong> este mes
              usando la aplicación.
            </p>
            <ul class="impact-list mb-0">
              <li>Se registraron {{ activities.length }} actividades durante el mes.</li>
            </ul>
          </div>
          <div v-if="summaryVersion === 2" class="impact-summary p-3 rounded mb-3">
            <p class="impact-text mb-2">
              Este mes has generado
              <strong>{{ totalValue }} gramos de CO<sub class="subscript">2</sub></strong> al usar
              la aplicación.
            </p>
            <ul class="impact-list mb-0">
              <li>
                Tu día con menor huella fue con {{ lowestValue }} gr de CO<sub class="subscript"
                  >2</sub
                >.
              </li>
              <li>
                Mantener este ritmo significa {{ (totalValue * 12).toFixed(1) }} gr anuales
                aproximadamente.
              </li>
            </ul>
          </div>
          <div v-if="summaryVersion === 3" class="impact-summary p-3 rounded mb-3">
            <p class="impact-text mb-2">
              Has generado
              <strong>{{ totalValue }} gramos de CO<sub class="subscript">2</sub></strong> este mes
              al usar la aplicación.
            </p>
            <ul class="impact-list mb-0">
              <li>
                El promedio diario de este mes es {{ (totalValue / 30).toFixed(1) }} gr de CO<sub
                  class="subscript"
                  >2</sub
                >.
              </li>
              <li>Cada transferencia de datos consume energía que genera emisiones.</li>
            </ul>
          </div>
          <div v-if="summaryVersion === 4" class="impact-summary p-3 rounded mb-3">
            <p class="impact-text mb-2">
              Este mes has generado
              <strong>{{ totalValue }} gramos de CO<sub class="subscript">2</sub></strong> usando la
              aplicación.
            </p>
            <ul class="impact-list mb-0">
              <li>Cada acción digital tiene un impacto ambiental medible.</li>
              <li>Pequeños cambios en tus hábitos pueden reducir estas emisiones.</li>
            </ul>
          </div>
          <div class="tips-section">
            <h6 class="tips-title mb-3">
              Consejos para reducir tu huella de carbono al usar la aplicación
            </h6>
            <ul class="tips-list">
              <li>Cierre las aplicaciones que no se ejecutan en segundo plano.</li>
              <li>Desactive el GPS cuando no esté utilizando la aplicación.</li>
              <li>Reduzca el brillo de su pantalla.</li>
              <li>Mantenga activado el modo de ahorro de batería.</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VueApexCharts from 'vue3-apexcharts'
import NavTopFootPrint from '@/components/NavTopFootPrint.vue'
import { mapState, mapActions } from 'vuex'

export default {
  name: 'CarbonFootPrint',
  components: { apexchart: VueApexCharts, NavTopFootPrint },
  data() {
    return {
      summaryVersion: 1,
      currentMonthIndex: 0,
      months: [],
    }
  },
  computed: {
    ...mapState('user', ['activities', 'totalCo2']),
    monthLabel() {
      const date = new Date()
      return date.toLocaleString('es-MX', { month: 'long', year: 'numeric' })
    },
    dailyData() {
      const grouped = {}
      this.activities.forEach((a) => {
        const d = new Date(a.registeredAt).toISOString().split('T')[0]
        if (!grouped[d]) grouped[d] = 0
        grouped[d] += a.co2Generated * 1000
      })
      const sorted = Object.entries(grouped).sort((a, b) => new Date(a[0]) - new Date(b[0]))
      return {
        categories: sorted.map(([d]) => d.slice(8, 10)),
        data: sorted.map(([, v]) => parseFloat(v.toFixed(4))),
      }
    },
    series() {
      return [{ name: 'Emisiones de CO₂', data: this.dailyData.data }]
    },
    chartOptions() {
      const lastIndex = this.dailyData.data.length - 1
      return {
        chart: { type: 'bar', toolbar: { show: false }, animations: { enabled: true, speed: 500 } },
        plotOptions: {
          bar: { columnWidth: '32%', borderRadius: 5, borderRadiusApplication: 'end' },
        },
        colors: this.dailyData.data.map((_, i) => (i === lastIndex ? '#7FA87D' : '#ABCD9E')),
        xaxis: {
          categories: this.dailyData.categories,
          labels: { style: { colors: '#9CA3AF', fontSize: '12px', fontWeight: 500 } },
          axisBorder: { show: false },
          axisTicks: { show: false },
          title: {
            text: 'Días',
            offsetY: -5,
            style: { color: '#9CA3AF', fontSize: '11px', fontWeight: 400 },
          },
        },
        yaxis: {
          show: true,
          labels: {
            style: { colors: '#E5E7EB', fontSize: '10px' },
            formatter: (val) => val.toFixed(1),
          },
          axisBorder: { show: false },
        },
        dataLabels: { enabled: false },
        grid: {
          borderColor: '#F3F4F6',
          yaxis: { lines: { show: true } },
          xaxis: { lines: { show: false } },
        },
        tooltip: {
          enabled: true,
          theme: 'light',
          y: { formatter: (val) => `${val} gr de CO₂`, title: { formatter: () => '' } },
          marker: { show: false },
        },
        states: {
          hover: { filter: { type: 'darken', value: 0.15 } },
          active: { filter: { type: 'darken', value: 0.2 } },
        },
      }
    },
    todayValue() {
      if (this.dailyData.data.length === 0) return 0
      return this.dailyData.data[this.dailyData.data.length - 1].toFixed(4)
    },
    lowestValue() {
      if (this.dailyData.data.length === 0) return 0
      return Math.min(...this.dailyData.data).toFixed(4)
    },
    totalValue() {
      return (this.totalCo2 * 1000).toFixed(4)
    },
  },
  methods: {
    ...mapActions('user', {
      fetchCarbonActivities: 'fetchCarbonActivities',
      fetchTotalCo2: 'fetchTotalCo2',
    }),
    previousMonth() {
      if (this.currentMonthIndex > 0) this.currentMonthIndex--
    },
    nextMonth() {
      if (this.currentMonthIndex < this.months.length - 1) this.currentMonthIndex++
    },
  },
  async mounted() {
    await this.fetchCarbonActivities()
    await this.fetchTotalCo2()
  },
}
</script>

<style scoped>
.carbon-wrapper {
  max-width: 320px;
  margin: 0 auto;
  padding: 2rem 1rem;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.content-wrapper {
  animation: fadeInUp 0.5s ease-out 0.2s backwards;
}
.carbon-card {
  border-radius: 24px !important;
  transition:
    box-shadow 0.3s ease,
    transform 0.3s ease;
  animation: fadeInUp 0.5s ease-out;
  height: 100%;
}
.carbon-card:hover {
  box-shadow:
    0 10px 15px -3px rgba(0, 0, 0, 0.08),
    0 4px 6px -2px rgba(0, 0, 0, 0.04) !important;
  transform: translateY(-2px);
}
.version-btn {
  background: #f3f4f6;
  color: #1b515e;
  border: 2px solid transparent;
  border-radius: 8px;
  padding: 0.4rem 0.8rem;
  font-size: 11px;
  font-weight: 500;
  transition: all 0.2s ease;
}
.version-btn:hover {
  background: #e5e7eb;
  transform: translateY(-1px);
}
.version-btn.active {
  background: #abcd9e;
  color: #1b515e;
  border-color: #7fa87d;
  font-weight: 600;
}
.nav-btn {
  width: 36px;
  height: 36px;
  text-decoration: none;
  color: #1b515e;
  transition: all 0.2s ease;
}
.nav-btn:hover:not(:disabled) {
  background: #f3f4f6 !important;
  transform: scale(1.1);
  color: #1b515e;
}
.nav-btn:active:not(:disabled) {
  background: #e5e7eb !important;
  transform: scale(0.95);
}
.nav-btn:disabled {
  opacity: 0.25;
}
.nav-btn .icon {
  width: 20px;
  height: 20px;
}
.month-title {
  letter-spacing: -0.01em;
  font-size: 16px;
  color: #1b515e;
}
.chart-container {
  margin: 0 -0.5rem;
}
.stat-row {
  transition: background 0.2s ease;
}
.stat-row:hover {
  background: #f9fafb !important;
}
.stat-row-highlight {
  background: linear-gradient(135deg, #f0f8f0 0%, #e8f5e8 100%);
  border-color: #abcd9e !important;
}
.stat-row-highlight:hover {
  background: linear-gradient(135deg, #e8f5e8 0%, #d8edd8 100%) !important;
}
.stat-label {
  color: #1b515e;
  opacity: 0.7;
}
.stat-value {
  color: #1b515e;
}
.stat-value-highlight {
  color: #1b515e;
}
.subscript {
  font-size: 0.75em;
  vertical-align: sub;
  font-weight: 500;
}
.impact-summary {
  background: linear-gradient(135deg, #f0f8f0 0%, #e8f5e8 100%);
  border-left: 3px solid #abcd9e;
  animation: fadeIn 0.3s ease-out;
}
.impact-text {
  font-size: 13px;
  color: #1b515e;
  line-height: 1.5;
}
.impact-list {
  list-style: none;
  padding-left: 0;
  margin-bottom: 0;
}
.impact-list li {
  font-size: 12px;
  color: #1b515e;
  opacity: 0.8;
  padding-left: 1.2rem;
  position: relative;
  line-height: 1.6;
  margin-bottom: 0.25rem;
}
.impact-list li:before {
  content: '•';
  color: #abcd9e;
  font-weight: bold;
  position: absolute;
  left: 0.5rem;
}
.tips-section {
  margin-top: 1rem;
}
.tips-title {
  font-size: 13px;
  color: #1b515e;
  font-weight: 600;
  margin-bottom: 0.75rem;
}
.tips-list {
  list-style: none;
  padding-left: 0;
  margin-bottom: 0;
}
.tips-list li {
  font-size: 12px;
  color: #1b515e;
  opacity: 0.8;
  padding-left: 1.2rem;
  position: relative;
  line-height: 1.6;
  margin-bottom: 0.5rem;
}
.tips-list li:before {
  content: '•';
  color: #abcd9e;
  font-weight: bold;
  position: absolute;
  left: 0.5rem;
}
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
.nav-btn:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(171, 205, 158, 0.2) !important;
}
@media (max-width: 767px) {
  .carbon-wrapper {
    max-width: 100%;
    padding: 2rem 0.75rem;
  }
  .carbon-card {
    border-radius: 16px !important;
    margin-top: 12px;
  }
  .month-title {
    font-size: 15px;
  }
  .impact-text,
  .tips-title {
    font-size: 12px;
  }
  .impact-list li,
  .tips-list li {
    font-size: 11px;
  }
  .version-btn {
    font-size: 10px;
    padding: 0.35rem 0.6rem;
  }
}
@media (min-width: 768px) {
  .carbon-wrapper {
    max-width: 1000px;
    padding: 2rem;
  }
  .impact-text,
  .tips-title {
    font-size: 13px;
  }
  .impact-list li,
  .tips-list li {
    font-size: 12px;
  }
  .row {
    align-items: stretch;
  }
  .row > [class*='col-'] {
    padding-left: 1.5rem;
    padding-right: 1.5rem;
  }
}
@media (min-width: 992px) {
  .carbon-wrapper {
    max-width: 1200px;
  }
  .impact-text,
  .tips-title {
    font-size: 14px;
  }
  .impact-list li,
  .tips-list li {
    font-size: 13px;
  }
  .row > [class*='col-'] {
    padding-left: 4rem;
    padding-right: 4rem;
  }
}
</style>
