<template>
  <div class="help-center-wrapper">
    <div class="container">
      <div class="help-center-container">
        <NavTopSupport />

        <div class="accordion mb-4" id="faqAccordion" style="margin-top: 60px">
          <!-- Pregunta 1 -->
          <div class="accordion-item faq-item">
            <h2 class="accordion-header">
              <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq1">
                <v-icon class="question-icon me-3" color="#1B515E">mdi-help-circle-outline</v-icon>
                <span>¿Qué debo hacer si no recuerdo mi usuario y/o contraseña?</span>
              </button>
            </h2>
            <div id="faq1" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
              <div class="accordion-body">
                Puedes recuperar tu usuario y contraseña haciendo clic en 
                <router-link
                  :to="{ name: 'authForgotPassword' }"
                  class="inline-link"
                >
                Olvidé mi contraseña
                </router-link>
                en la pantalla de inicio de sesión. Te enviaremos un correo electrónico con instrucciones para restablecer tu acceso.
              </div>
            </div>
          </div>

          <!-- Pregunta 2 -->
          <div class="accordion-item faq-item">
            <h2 class="accordion-header">
              <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq2">
                <v-icon class="question-icon me-3" color="#1B515E">mdi-help-circle-outline</v-icon>
                <span>¿Cómo genero un itinerario de viaje?</span>
              </button>
            </h2>
            <div id="faq2" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
              <div class="accordion-body">
                Para generar un itinerario, ve a la sección 
                <router-link
                  :to="{ name: 'new-trips' }"
                  class="inline-link"
                >
                  Nuevo Viaje
                </router-link>
                 título del viaje , destino , fechas del viaje y escoge el lugar de hospedaje que más te guste. Posteriormente, nuestra app generará automáticamente un itinerario personalizado basado en
                 <router-link
                  :to="{ name: 'preferences' }"
                  class="inline-link"
                >
                Tus preferencias
                </router-link>
              </div>
            </div>
          </div>

          <!-- Pregunta 3 -->
          <div class="accordion-item faq-item">
            <h2 class="accordion-header">
              <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq3">
                <v-icon class="question-icon me-3" color="#1B515E">mdi-help-circle-outline</v-icon>
                <span>¿Cómo consulto el clima de un destino?</span>
              </button>
            </h2>
            <div id="faq3" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
              <div class="accordion-body">
                En la sección de cada destino encontrarás información actualizada del clima. Cada destino lo puedes buscar desde la pantalla
                <router-link
                  :to="{ name: 'home' }"
                  class="inline-link"
                >
                Inicio
                </router-link>
                donde vas a poder buscar y seleccionar el lugar que deseas visitar, además, podrás visualizar información detallada del lugar.
              </div>
            </div>
          </div>

          <!-- Pregunta 4 -->
          <div class="accordion-item faq-item">
            <h2 class="accordion-header">
              <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq4">
                <v-icon class="question-icon me-3" color="#1B515E">mdi-help-circle-outline</v-icon>
                <span>¿Cómo actualizo la información de mi cuenta?</span>
              </button>
            </h2>
            <div id="faq4" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
              <div class="accordion-body">
                Ve a 
                <router-link
                  :to="{ name: 'myaccount' }"
                  class="inline-link"
                >
                Mi perfil,
                </router-link>
                allí podrás actualizar tus datos como tu foto de perfil, nombre, apellidos, teléfono y modificar tu contraseña, toma en cuenta que el correo electrónico no puede ser modificado.
              </div>
            </div>
          </div>

          <!-- Pregunta 5 -->
          <div class="accordion-item faq-item">
            <h2 class="accordion-header">
              <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq5">
                <v-icon class="question-icon me-3" color="#1B515E">mdi-help-circle-outline</v-icon>
                <span>¿Puedo usar la app sin conexión a internet?</span>
              </button>
            </h2>
            <div id="faq5" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
              <div class="accordion-body">
                Por el momento no  es posible utilizar la app sin conexión a internet. Si deseas tener una buena experiencia utilizando Huellas por México, deberás utilizar internet en todo momento.
              </div>
            </div>
          </div>

          <!-- Pregunta 6 -->
          <div class="accordion-item faq-item">
            <h2 class="accordion-header">
              <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq6">
                <v-icon class="question-icon me-3" color="#1B515E">mdi-help-circle-outline</v-icon>
                <span>¿Cómo reporto un error en la app?</span>
              </button>
            </h2>
            <div id="faq6" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
              <div class="accordion-body">
                Puedes reportar errores enviándonos un correo a <a class="bold">huellaspormexico@gmail.com</a> con una descripción detallada del problema.
              </div>
            </div>
          </div>

          <!-- Pregunta 7 -->
          <div class="accordion-item faq-item">
            <h2 class="accordion-header">
              <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq7">
                <v-icon class="question-icon me-3" color="#1B515E">mdi-help-circle-outline</v-icon>
                <span>¿Qué es el turismo sostenible?</span>
              </button>
            </h2>
            <div id="faq7" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
              <div class="accordion-body">
               El turismo sostenible es un modelo de desarrollo turístico que se compromete a satisfacer las necesidades de los visitantes, la industria, el medio ambiente y las comunidades locales, sin comprometer los recursos para el futuro. Se basa en un equilibrio crucial entre tres pilares: la viabilidad económica a largo plazo con beneficios justos para la comunidad local, la integridad social y cultural a través del respeto y la participación de los residentes, y una rigurosa conservación medioambiental que minimiza la contaminación y el uso de recursos naturales.
              </div>
            </div>
          </div>

          <!-- Pregunta 9 con certificaciones -->
          <div class="accordion-item faq-item">
            <h2 class="accordion-header">
              <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq9">
                <v-icon class="question-icon me-3" color="#1B515E">mdi-help-circle-outline</v-icon>
                <span>¿Qué certificaciones siguen los hoteles que aparecen en la app para ser sostenibles?</span>
              </button>
            </h2>
            <div id="faq9" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
              <div class="accordion-body">
                <p class="mb-3">
                  Los hoteles en nuestra app cuentan con diversas certificaciones reconocidas internacionalmente que validan su compromiso con prácticas responsables en términos ambientales, sociales y económicos:
                </p>

                <!-- Acordeón anidado de certificaciones -->
                <div class="accordion certifications-accordion" id="certAccordion">
                  <!-- Certificación 1 -->
                  <div class="accordion-item cert-item">
                    <h2 class="accordion-header">
                      <button class="accordion-button collapsed cert-button" type="button" data-bs-toggle="collapse" data-bs-target="#cert1">
                        <v-icon class="cert-icon me-2" size="small" color="#ABCD9E">mdi-certificate-outline</v-icon>
                        <span>EarthCheck</span>
                      </button>
                    </h2>
                    <div id="cert1" class="accordion-collapse collapse" data-bs-parent="#certAccordion">
                      <div class="accordion-body cert-body">
                        Organización líder mundial en consultoría y certificación sustentable para la industria de turismo y hospitalidad. Evalúa el desempeño en áreas clave como emisiones de gases de efecto invernadero, eficiencia energética, gestión de agua potable, conservación de ecosistemas, gestión social y cultural, calidad del aire y manejo de residuos sólidos. Está vinculada con la norma ISO 21401:2018 para sistemas de gestión de sostenibilidad en alojamientos turísticos.
                      <a href="https://earthcheck.org/" target="_blank" rel="noopener" class="inline-link">
                        Conoce más sobre la certificación EarthCheck
                      </a>
                      </div>
                    </div>
                  </div>

                  <!-- Certificación 2 -->
                  <div class="accordion-item cert-item">
                    <h2 class="accordion-header">
                      <button class="accordion-button collapsed cert-button" type="button" data-bs-toggle="collapse" data-bs-target="#cert2">
                        <v-icon class="cert-icon me-2" size="small" color="#ABCD9E">mdi-certificate-outline</v-icon>
                        <span>Rainforest Alliance</span>
                      </button>
                    </h2>
                    <div id="cert2" class="accordion-collapse collapse" data-bs-parent="#certAccordion">
                      <div class="accordion-body cert-body">
                        Certificación que garantiza estándares de sostenibilidad ambiental, social y económica, utilizando el estándar del Global Tourism Sustainable Council. Verifica el cuidado del agua mediante sistemas de captación de lluvia, programas de reciclaje y gestión de residuos, cuidado de la biodiversidad, conservación de áreas naturales, buenas condiciones laborales y equidad de género.
                      <a href="https://www.rainforest-alliance.org/es/para-negocios/certificacion/" target="_blank" rel="noopener" class="inline-link">
                        Conoce más sobre la certificación Rainforest Alliance
                      </a>
                      </div>
                    </div>
                  </div>

                  <!-- Certificación 3 -->
                  <div class="accordion-item cert-item">
                    <h2 class="accordion-header">
                      <button class="accordion-button collapsed cert-button" type="button" data-bs-toggle="collapse" data-bs-target="#cert3">
                        <v-icon class="cert-icon me-2" size="small" color="#ABCD9E">mdi-certificate-outline</v-icon>
                        <span>Biosphere</span>
                      </button>
                    </h2>
                    <div id="cert3" class="accordion-collapse collapse" data-bs-parent="#certAccordion">
                      <div class="accordion-body cert-body">
                        Sistema de reconocimiento internacional creado como resultado de 4 Cumbres patrocinadas por UNESCO y la OMT. Contribuye a los 17 Objetivos de Desarrollo Sostenible de la ONU. Evalúa gestión eficiente de recursos (agua y energía renovable), reducción de residuos, protección del medio ambiente, inclusión social con infraestructura 100% accesible, y desarrollo comunitario priorizando proveedores y personal locales.
                      <a href="https://www.biospheretourism.com/es/un-reconocimiento-global-para-tu-compromiso-sostenible/83" target="_blank" rel="noopener" class="inline-link">
                        Conoce más sobre la certificación Biosphere
                      </a>
                      </div>
                    </div>
                  </div>

                  <!-- Certificación 4 -->
                  <div class="accordion-item cert-item">
                    <h2 class="accordion-header">
                      <button class="accordion-button collapsed cert-button" type="button" data-bs-toggle="collapse" data-bs-target="#cert4">
                        <v-icon class="cert-icon me-2" size="small" color="#ABCD9E">mdi-certificate-outline</v-icon>
                        <span>LEED (Leadership in Energy and Environmental Design)</span>
                      </button>
                    </h2>
                    <div id="cert4" class="accordion-collapse collapse" data-bs-parent="#certAccordion">
                      <div class="accordion-body cert-body">
                        Sistema de certificación con reconocimiento internacional para edificios sustentables creado por el U.S. Green Building Council. Evalúa ubicación y acceso a transporte público, eficiencia energética, cuidado del agua mediante recolección de lluvia y aguas grises, gestión de materiales reciclados, programa de separación de residuos en al menos 4 fracciones, y reducción de emisiones de carbono mediante energías renovables.
                      <a href="https://www.usgbc.org/leed" target="_blank" rel="noopener" class="inline-link">
                        Conoce más sobre la certificación LEED
                      </a>
                      </div>
                    </div>
                  </div>

                  <!-- Certificación 5 -->
                  <div class="accordion-item cert-item">
                    <h2 class="accordion-header">
                      <button class="accordion-button collapsed cert-button" type="button" data-bs-toggle="collapse" data-bs-target="#cert5">
                        <v-icon class="cert-icon me-2" size="small" color="#ABCD9E">mdi-certificate-outline</v-icon>
                        <span>Green Key</span>
                      </button>
                    </h2>
                    <div id="cert5" class="accordion-collapse collapse" data-bs-parent="#certAccordion">
                      <div class="accordion-body cert-body">
                        Estándar líder de excelencia en responsabilidad ambiental otorgado por la Foundation for Environmental Education. Evalúa implementación de sistemas de riego inteligentes, reutilización de aguas grises, dispositivos de bajo consumo, disminución del consumo energético por huésped-noche, reciclaje de residuos en al menos 3 fracciones, mantenimiento de áreas verdes, y responsabilidad social con inclusión de empleados y proveedores locales. Cuenta con 248 establecimientos certificados en México.
                      <a href="https://www.greenkey.global/" target="_blank" rel="noopener" class="inline-link">
                        Conoce más sobre la certificación Green Key
                      </a>
                      </div>
                    </div>
                  </div>

                  <!-- Certificación 6 -->
                  <div class="accordion-item cert-item">
                    <h2 class="accordion-header">
                      <button class="accordion-button collapsed cert-button" type="button" data-bs-toggle="collapse" data-bs-target="#cert6">
                        <v-icon class="cert-icon me-2" size="small" color="#ABCD9E">mdi-certificate-outline</v-icon>
                        <span>Distintivo S (México)</span>
                      </button>
                    </h2>
                    <div id="cert6" class="accordion-collapse collapse" data-bs-parent="#certAccordion">
                      <div class="accordion-body cert-body">
                        Reconocimiento otorgado por la Secretaría de Turismo (SECTUR) basado en criterios globales de la OMT y el GSTC. Requiere cumplimiento obligatorio de la norma mexicana NMX-AA-171-SCFI-2014. Evalúa emisión de gases de efecto invernadero, eficiencia energética, gestión de agua dulce y aguas residuales, conservación de ecosistemas, gestión social y cultural, planificación del territorio, calidad del aire, gestión de residuos sólidos, uso eficiente de recursos, fomento de productos locales y educación ambiental.
                      <a href="https://www.gob.mx/sectur/acciones-y-programas/programa-de-calidad-distintivo-s" target="_blank" rel="noopener" class="inline-link">
                        Conoce más sobre la certificación Distintivo S (México)
                      </a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Sección de contacto -->
        <div class="card contact-section shadow-sm">
          <div class="card-body text-center p-4">
            <v-icon class="contact-icon mb-3" size="large" color="#ABCD9E">mdi-email-outline</v-icon>
            <p class="contact-text mb-3">¿Tienes otra duda? Contáctanos aquí.</p>
            <a href="mailto:huellaspormexico@gmail.com" class="btn contact-email">
              huellaspormexico@gmail.com
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavTopSupport from '@/components/NavTopSupport.vue';

export default {
  name: "HelpCenter",
  components: {NavTopSupport},
  methods: {
    goToNewTrip() {
      this.$router.push({ name: 'Nuevo viaje' });
    }
  }
};
</script>

<style scoped>
.help-center-wrapper {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8f0f2 100%);
  padding: 2rem 1rem;
}

.help-center-container {
  max-width: 800px;
  margin: 0 auto;
}

.help-title {
  color: #1B515E;
  font-size: 1.5rem;
  font-weight: 600;
}

/* Acordeón principal */
.faq-item {
  border: none !important;
  border-radius: 12px !important;
  margin-bottom: 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  overflow: hidden;
}

.faq-item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.faq-item .accordion-button {
  background-color: white;
  color: #1B515E;
  font-weight: 500;
  padding: 1.25rem 1.5rem;
  border: none;
}

.faq-item .accordion-button:not(.collapsed) {
  background-color: rgba(171, 205, 158, 0.15);
  color: #1B515E;
  font-weight: 600;
  box-shadow: none;
}

.faq-item .accordion-button:hover {
  background-color: rgba(171, 205, 158, 0.08);
}

.faq-item .accordion-button:focus {
  box-shadow: none;
  border: none;
}

.faq-item .accordion-button::after {
  color: #1B515E;
  transition: transform 0.3s ease;
}

.faq-item .accordion-button:not(.collapsed)::after {
  color: #ABCD9E;
}

.faq-item .accordion-body {
  background: linear-gradient(to bottom, #f8faf9, #ffffff);
  color: #1B515E;
  font-size: 0.9rem;
  line-height: 1.7;
  padding: 1.5rem;
}

.question-icon {
  flex-shrink: 0;
}

.inline-link {
  color: #ABCD9E;
  font-weight: 600;
  text-decoration: none;
  border-bottom: 2px solid transparent;
  padding: 0 0.25rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.inline-link:hover {
  color: #1B515E;
  border-bottom-color: #ABCD9E;
  background-color: rgba(171, 205, 158, 0.1);
}

/* Acordeón de certificaciones */
.certifications-accordion {
  background: rgba(171, 205, 158, 0.05);
  border-radius: 8px;
  padding: 0.5rem;
}

.cert-item {
  border: none !important;
  border-radius: 8px !important;
  margin-bottom: 0.5rem;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.cert-item:last-child {
  margin-bottom: 0;
}

.cert-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-1px);
}

.cert-button {
  background-color: white !important;
  color: #1B515E !important;
  font-weight: 500;
  font-size: 0.875rem;
  padding: 1rem 1.25rem;
  border: none;
}

.cert-button:not(.collapsed) {
  background-color: rgba(171, 205, 158, 0.12) !important;
}

.cert-button:focus {
  box-shadow: none;
}

.cert-body {
  font-size: 0.85rem;
  color: #1B515E;
  line-height: 1.6;
  padding: 1rem 1.25rem;
}

.cert-icon {
  flex-shrink: 0;
}

/* Sección de contacto */
.contact-section {
  border: none;
  border-radius: 16px;
  transition: all 0.3s ease;
}

.contact-section:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12) !important;
  transform: translateY(-2px);
}

.contact-icon {
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.contact-text {
  color: #1B515E;
  font-size: 1rem;
  font-weight: 500;
}

.contact-email {
  color: #1B515E !important;
  font-size: 1.1rem;
  font-weight: 600;
  text-decoration: none;
  padding: 0.5rem 1.5rem;
  border-radius: 8px;
  background: linear-gradient(135deg, rgba(171, 205, 158, 0.1), rgba(171, 205, 158, 0.2));
  border: none;
  transition: all 0.3s ease;
}

.contact-email:hover {
  color: white !important;
  background: linear-gradient(135deg, #ABCD9E, #8db97f);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(171, 205, 158, 0.3);
}

/* Responsive */
@media (min-width: 768px) {
  .help-title {
    font-size: 1.75rem;
  }
}

@media (min-width: 992px) {
  .help-center-container {
    max-width: 900px;
  }
  
  .help-title {
    font-size: 2rem;
  }
}
</style>