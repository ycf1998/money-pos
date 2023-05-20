<template>
  <el-input :value="value" @input="input" @keydown.native.enter="compute">
    <svg-icon slot="prefix" icon-class="calculator" />
  </el-input>
</template>

<script>
import calculator from '@/utils/calculator'
export default {
  name: 'ComputeInput',
  // eslint-disable-next-line vue/require-prop-types
  props: ['value'],
  methods: {
    input(val) {
      this.$emit('input', val)
    },
    compute() {
      const reg = /(.*)([\\+\\-\\*/])(.*)/
      const group = this.value.match(reg)
      if (group) {
        const x = group[1]
        const operate = group[2]
        const y = group[3]
        switch (operate) {
          case '+':
            this.input(calculator.Add(x, y))
            break
          case '-':
            this.input(calculator.Sub(x, y))
            break
          case '*':
            this.input(calculator.Mul(x, y))
            break
          case '/':
            this.input(calculator.Div(x, y))
            break
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
