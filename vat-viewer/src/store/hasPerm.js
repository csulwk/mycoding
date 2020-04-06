import store from '../store'

export function hasPerm(permission) {
  const perms = store.getters.perms
  console.log('MyPerms -->' + perms)
  return perms.indexOf(permission) > -1
}
