import store from '@/store'

function checkPermission (el, binding) {
  const { value } = binding
  // 角色名 + 权限标识
  const roles = store.getters && store.getters.roles
  const permissions = store.getters && store.getters.permissions
  const roleWithPermission = [...roles, ...permissions]

  if (value && value instanceof Array) {
    if (value.length < 1) return
    let hasPermission = false
    // 超级管理员放行所有权限
    if (roleWithPermission.includes('SUPER_ADMIN')) {
      hasPermission = true
    } else {
      hasPermission = roleWithPermission.some(p => {
        return value.includes(p)
      })
    }

    if (!hasPermission) {
      el.parentNode && el.parentNode.removeChild(el)
    }

  } else {
    throw new Error(`need roles! Like v-permission="['ADMIN','user:add']"`)
  }
}

export default {
  inserted (el, binding) {
    checkPermission(el, binding)
  },
  update (el, binding) {
    checkPermission(el, binding)
  }
}
