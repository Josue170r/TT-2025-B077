import { createApp } from 'vue'
// import { createPinia } from 'pinia'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// Solid
import { fas } from '@fortawesome/free-solid-svg-icons'
//Regular
import { far } from '@fortawesome/free-regular-svg-icons'

import App from './App.vue'
import router from './router'

const app = createApp(App)

library.add(
  fas, far
)

export const byPrefixAndName = {
  fas: fas,
  far: far
}
app.component('font-awesome-icon', FontAwesomeIcon)

// app.use(createPinia())
app.use(router)

app.mount('#app')
