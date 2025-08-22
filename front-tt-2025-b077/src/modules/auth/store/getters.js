/**
 * Get one product from storage
 * @param state
 */
export function isLogged(state) {
  return !!state.tokenAccess
}

export function getUsername(state) {
  return state.user.username
}