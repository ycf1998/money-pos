import variables from '@/styles/element-variables.module.scss'
import MoneyConfig from '@/money.config'

const state = {
  theme: variables.theme,
  showSettings: MoneyConfig.settings.showSettings,
  tagsView: MoneyConfig.settings.tagsView,
  fixedHeader: MoneyConfig.settings.fixedHeader,
  sidebarLogo: MoneyConfig.settings.sidebarLogo
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    // eslint-disable-next-line no-prototype-builtins
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  changeSetting ({ commit }, data) {
    commit('CHANGE_SETTING', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

