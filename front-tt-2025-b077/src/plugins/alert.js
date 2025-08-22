import { createApp } from 'vue'
import vuetify from '@/plugins/vuetify'
import router from '@/router'
import Alert from '@/components/alert.vue'

const AlertPlugin = {
  install(app) {
    const alert = {
      success(options) {
        return this.show(options, 'success')
      },
      error(options) {
        return this.show(options, 'error')
      },
      show(options, type = 'success') {
        if (typeof options === 'string') options = { message: options }

        const container = document.createElement('div')
        document.body.appendChild(container)

        const alertApp = createApp(Alert)
        alertApp.use(vuetify)
        alertApp.use(router)

        const instance = alertApp.mount(container)

        instance.message = options.message || options.text || ''
        instance.type = type
        instance.show = true
        if (options.title) instance.customTitle = options.title
        if (options.nextRoute) instance.nextRoute = options.nextRoute

        instance.$watch('show', (val) => {
          if (!val) {
            if (instance.nextRoute) {
              router.push({ name: instance.nextRoute }).catch(() => {})
            }
            alertApp.unmount()
            if (container.parentNode) container.parentNode.removeChild(container)
          }
        })

        return instance
      }
    }

    app.config.globalProperties.$alert = alert
  }
}

export default AlertPlugin
