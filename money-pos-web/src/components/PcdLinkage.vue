<!-- 省市区联动 -->
<script setup>
import provincesApi from "@/api/ums/provinces.js";

const {province, city, district} = defineProps(['province', 'city', 'district'])
const emits = defineEmits(['change'])

const pcd = [province, city, district]

const props = {
    lazy: true,
    lazyLoad(node, resolve) {
        const {level, data} = node
        switch (level) {
            case 0:
                provincesApi.loadProvinces().then(res => resolve(res.data));
                break;
            case 1:
                provincesApi.loadCities(data.value).then(res => resolve(res.data));
                break;
            case 2:
                provincesApi.loadDistricts(data.value).then(res => resolve(res.data.map(e => {
                    return {...e, leaf: true}
                })));
                break;
        }
    }
}

function pcdChange(value) {
    emits('change', value[0], value[1], value[2])
}
</script>
<template>
    <el-cascader v-model="pcd" :props="props" @change="pcdChange" placeholder="请选择地址" />
</template>