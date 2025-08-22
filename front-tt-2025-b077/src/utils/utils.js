export function getUrlVars() {
  let vars = {}
  window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (
    m,
    key,
    value
  ) {
    vars[key] = value
  })
  return vars
}

export function getErrorDetails(error) {
  const data = error?.response?.data ?? error
  const first = Array.isArray(data?.fieldErrors) && data.fieldErrors.length ? data.fieldErrors[0] : null
  if (first) {
    const field = String(first.property ?? first.path ?? '').trim()
    const msg = String(first.message ?? '').trim()
    if (field && msg) return `${field}: ${msg}`
    if (msg) return msg
  }
  const dataMsg = typeof data?.message === 'string' ? data.message.trim() : ''
  if (dataMsg) return dataMsg
  const errMsg = typeof error?.message === 'string' ? error.message.trim() : ''
  if (errMsg) return errMsg
  return 'Ocurrió un error inesperado'
}