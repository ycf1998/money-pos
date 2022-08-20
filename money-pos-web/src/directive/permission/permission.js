import store from '@/store'

// 【qk-money】 权限检查
function checkPermission(el, binding) {
  const { value } = binding
  const roles = store.getters && store.getters.roles
  const permissions = store.getters && store.getters.permissions
  // 角色和权限标识
  const roleWithPermission = [...roles, ...permissions]

  if (value && value instanceof Array) {
    if (value.length > 0) {
      const permissionRoles = value

      let hasPermission = false
      // 【qk-money】超级管理员放行所有权限
      if (roleWithPermission.includes('SUPER_ADMIN')) {
        hasPermission = true
      } else {
        hasPermission = roleWithPermission.some(p => {
          return permissionRoles.includes(p)
        })
      }

      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  } else {
    // 绑定到标签上，根据是否有该角色或者权限显示或隐藏
    throw new Error(`need roles! Like v-permission="['ADMIN','user:add']"`)
  }
}

export default {
  inserted(el, binding) {
    checkPermission(el, binding)
  },
  update(el, binding) {
    checkPermission(el, binding)
  }
}
