import Vue from 'vue'
import { getDetail as getDict } from '@/api/system/dict'

export default class Dict {
  constructor(dict) {
    this.dict = dict
  }

  async init (names, completeCallback) {
    if (names === undefined) {
      throw new Error('need Dict names')
    }
    const ps = []
    names.forEach(name => {
      Vue.set(this.dict.dict, name, {})
      Vue.set(this.dict.label, name, {})
      Vue.set(this.dict, name, [])
      ps.push(getDict(name).then(response => {
        const { data } = response
        this.dict[name] = data
        data.forEach(d => {
          Vue.set(this.dict.dict[name], d.value, d)
          Vue.set(this.dict.label[name], d.value, d.label)
        })
      }))
    })
    await Promise.all(ps)
    completeCallback()
  }
}
