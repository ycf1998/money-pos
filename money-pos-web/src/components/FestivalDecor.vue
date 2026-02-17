<template>
    <div class="festival-decor">
        <!-- 雪花效果 -->
        <div v-if="festival && hasDecoration('snow')" class="snow-container">
            <div v-for="i in 50" :key="'snow-' + i" class="snowflake" :style="getSnowStyle(i)" />
        </div>

        <!-- 灯笼 -->
        <template v-if="festival && hasDecoration('lantern')">
            <div class="lantern lantern-left">
                <div class="lantern-body">
                    <div class="lantern-line" />
                    <div class="lantern-tassel" />
                </div>
            </div>
            <div class="lantern lantern-right">
                <div class="lantern-body">
                    <div class="lantern-line" />
                    <div class="lantern-tassel" />
                </div>
            </div>
        </template>

        <!-- 爱心飘落 -->
        <div v-if="festival && hasDecoration('heart')" class="heart-container">
            <div v-for="i in 20" :key="'heart-' + i" class="heart" :style="getHeartStyle(i)">❤</div>
        </div>

        <!-- 月亮与玉兔 -->
        <div v-if="festival && hasDecoration('moon')" class="moon-container">
            <div class="moon"><div class="moon-glow" /></div>
            <div class="rabbit">🐰</div>
        </div>

        <!-- 彩带飘落（替代烟花） -->
        <div v-if="festival && hasDecoration('firework')" class="confetti-container">
            <div v-for="i in 30" :key="'confetti-' + i" class="confetti" :style="getConfettiStyle(i)" />
        </div>

        <!-- 鲜花飘落（劳动节） -->
        <div v-if="festival && hasDecoration('flower')" class="flower-container">
            <div v-for="i in 25" :key="'flower-' + i" class="flower" :style="getFlowerStyle(i)">🌸</div>
        </div>

        <!-- 南瓜 -->
        <div v-if="festival && hasDecoration('pumpkin')" class="pumpkin-container">
            <div class="pumpkin pumpkin-left">🎃</div>
            <div class="pumpkin pumpkin-right">🎃</div>
        </div>

        <!-- 蝙蝠 -->
        <div v-if="festival && hasDecoration('bat')" class="bat-container">
            <div v-for="i in 5" :key="'bat-' + i" class="bat" :style="getBatStyle(i)">🦇</div>
        </div>

        <!-- 圣诞树 -->
        <div v-if="festival && hasDecoration('tree')" class="tree-container">
            <div class="christmas-tree">🎄</div>
        </div>

        <!-- 礼物 -->
        <div v-if="festival && hasDecoration('gift')" class="gift-container">
            <div class="gift gift-left">🎁</div>
            <div class="gift gift-right">🎁</div>
        </div>

        <!-- 鞭炮飘落 -->
        <div v-if="festival && hasDecoration('firecracker')" class="firecracker-rain-container">
            <div v-for="i in 20" :key="'cracker-' + i" class="firecracker-rain" :style="getFirecrackerStyle(i)">🧨</div>
        </div>

        <!-- 粽子飘落 -->
        <div v-if="festival && hasDecoration('zongzi')" class="zongzi-rain-container">
            <div v-for="i in 15" :key="'zongzi-' + i" class="zongzi-rain" :style="getZongziStyle(i)">🍙</div>
        </div>

        <!-- 龙舟竞渡 -->
        <div v-if="festival && hasDecoration('dragonBoat')" class="dragon-boat-container">
            <div class="dragon-boat">
                <span class="boat-body">🚣</span>
                <span class="boat-wave">〰️</span>
            </div>
            <div class="river"></div>
        </div>

        <!-- 国庆气球与和平鸽 -->
        <div v-if="festival && hasDecoration('flag')" class="national-container">
            <div class="balloon balloon-left">🎈</div>
            <div class="balloon balloon-right">🎈</div>
            <div v-for="i in 5" :key="'dove-' + i" class="dove" :style="getDoveStyle(i)">🕊️</div>
        </div>

        <!-- 测试面板 -->
        <!-- <div class="test-panel">
            <div style="margin-bottom: 8px; font-size: 12px; color: #666;">
                当前节日: {{ festival?.name || '无' }}<br>
                装饰: {{ festival?.decorations?.join(', ') || '无' }}
            </div>
            <el-select v-model="testFestival" placeholder="切换节日" size="small" @change="onTestChange">
                <el-option label="无" value="" />
                <el-option label="春节" value="spring" />
                <el-option label="元宵" value="lantern" />
                <el-option label="劳动节" value="labor" />
                <el-option label="端午节" value="dragonBoat" />
                <el-option label="七夕" value="qixi" />
                <el-option label="中秋节" value="midAutumn" />
                <el-option label="国庆节" value="national" />
                <el-option label="元旦" value="newYear" />
            </el-select>
        </div> -->
    </div>

    <!-- 祝福弹窗（放在根级别，使用 teleport） -->
    <el-dialog
        v-model="dialogVisible"
        :title="festival?.name || ''"
        width="300px"
        center
        :show-close="false"
        class="greeting-dialog"
    >
        <div class="greeting-content">
            <div class="greeting-icon">{{ festival ? getFestivalIcon() : '' }}</div>
            <div class="greeting-text">{{ festival?.greeting || '' }}</div>
        </div>
        <template #footer>
            <el-button type="primary" @click="closeDialog">知道了</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({
    festival: {
        type: Object,
        default: null
    },
    showGreeting: {
        type: Boolean,
        default: true
    }
})

const emit = defineEmits(['dismiss', 'test'])

const testFestival = ref('')
const dialogVisible = ref(false)

watch(() => props.showGreeting, (val) => {
    if (val && props.festival) {
        dialogVisible.value = true
    }
}, { immediate: true })

watch(() => props.festival, (val) => {
    if (val && props.showGreeting) {
        dialogVisible.value = true
    }
})

function closeDialog() {
    dialogVisible.value = false
    emit('dismiss')
}

function onTestChange(val) {
    emit('test', val)
}

function hasDecoration(type) {
    return props.festival?.decorations?.includes(type)
}

function getSnowStyle(index) {
    const size = Math.random() * 10 + 5
    return {
        left: `${Math.random() * 100}%`,
        width: `${size}px`,
        height: `${size}px`,
        animationDelay: `${Math.random() * 5}s`,
        animationDuration: `${Math.random() * 5 + 5}s`
    }
}

function getHeartStyle(index) {
    return {
        left: `${Math.random() * 100}%`,
        fontSize: `${Math.random() * 15 + 10}px`,
        animationDelay: `${Math.random() * 5}s`,
        animationDuration: `${Math.random() * 3 + 4}s`
    }
}

function getConfettiStyle(index) {
    const colors = ['#ff0000', '#ffd700', '#00ff00', '#ff69b4', '#00bfff', '#ff9800', '#9c27b0']
    const shapes = ['square', 'circle', 'triangle']
    return {
        left: `${Math.random() * 100}%`,
        backgroundColor: colors[index % colors.length],
        width: `${Math.random() * 8 + 6}px`,
        height: `${Math.random() * 8 + 6}px`,
        animationDelay: `${Math.random() * 5}s`,
        animationDuration: `${Math.random() * 3 + 4}s`
    }
}

function getDoveStyle(index) {
    return {
        left: `${-10 + index * 20}%`,
        top: `${Math.random() * 40 + 10}%`,
        animationDelay: `${Math.random() * 3}s`,
        fontSize: `${Math.random() * 10 + 20}px`
    }
}

function getZongziStyle(index) {
    return {
        left: `${Math.random() * 100}%`,
        fontSize: `${Math.random() * 15 + 20}px`,
        animationDelay: `${Math.random() * 5}s`,
        animationDuration: `${Math.random() * 3 + 4}s`
    }
}

function getFirecrackerStyle(index) {
    return {
        left: `${Math.random() * 100}%`,
        fontSize: `${Math.random() * 15 + 20}px`,
        animationDelay: `${Math.random() * 5}s`,
        animationDuration: `${Math.random() * 3 + 4}s`
    }
}

function getFlowerStyle(index) {
    const flowers = ['🌸', '🌺', '🌻', '🌷', '🌹']
    return {
        left: `${Math.random() * 100}%`,
        fontSize: `${Math.random() * 15 + 20}px`,
        animationDelay: `${Math.random() * 5}s`,
        animationDuration: `${Math.random() * 3 + 4}s`
    }
}

function getBatStyle(index) {
    return {
        left: `${Math.random() * 100}%`,
        top: `${Math.random() * 50}%`,
        animationDelay: `${Math.random() * 3}s`,
        fontSize: `${Math.random() * 10 + 20}px`
    }
}

function getFestivalIcon() {
    const icons = {
        spring: '🧧',
        lantern: '🏮',
        labor: '🌻',
        dragonBoat: '🐉',
        qixi: '💫',
        midAutumn: '🌕',
        national: '🎉',
        newYear: '🎊'
    }
    return icons[props.festival?.key] || '🎉'
}
</script>

<style lang="less" scoped>
.festival-decor {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;
    z-index: 9999;
    overflow: hidden;
}

.snow-container {
    position: absolute;
    width: 100%;
    height: 100%;
}

.snowflake {
    position: absolute;
    top: -20px;
    background: white;
    border-radius: 50%;
    opacity: 0.8;
    animation: snowfall linear infinite;
    box-shadow: 0 0 5px rgba(255, 255, 255, 0.5);
}

@keyframes snowfall {
    0% { transform: translateY(-20px) rotate(0deg); opacity: 0; }
    10% { opacity: 0.8; }
    90% { opacity: 0.8; }
    100% { transform: translateY(100vh) rotate(360deg); opacity: 0; }
}

.lantern {
    position: absolute;
    top: 0;
    animation: lanternSwing 3s ease-in-out infinite;
    transform-origin: top center;

    &-left { left: 220px; }
    &-right { right: 180px; animation-delay: 0.5s; }
}

.lantern-body {
    width: 40px;
    height: 50px;
    background: linear-gradient(135deg, #ff0000, #cc0000);
    border-radius: 50% 50% 45% 45%;
    position: relative;
    box-shadow: 0 0 20px rgba(255, 0, 0, 0.5);

    &::before {
        content: '福';
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        color: #ffd700;
        font-size: 18px;
        font-weight: bold;
    }
}

.lantern-line {
    width: 2px;
    height: 30px;
    background: #ffd700;
    position: absolute;
    top: -30px;
    left: 50%;
    transform: translateX(-50%);
}

.lantern-tassel {
    width: 4px;
    height: 25px;
    background: linear-gradient(to bottom, #ffd700, #ff6600);
    position: absolute;
    bottom: -20px;
    left: 50%;
    transform: translateX(-50%);
    border-radius: 0 0 2px 2px;
    animation: tasselSwing 1s ease-in-out infinite;
}

@keyframes lanternSwing {
    0%, 100% { transform: rotate(-5deg); }
    50% { transform: rotate(5deg); }
}

@keyframes tasselSwing {
    0%, 100% { transform: translateX(-50%) rotate(-10deg); }
    50% { transform: translateX(-50%) rotate(10deg); }
}

.heart-container {
    position: absolute;
    width: 100%;
    height: 100%;
}

.heart {
    position: absolute;
    top: -30px;
    color: #ff69b4;
    animation: heartFall linear infinite;
    opacity: 0.8;
}

@keyframes heartFall {
    0% { transform: translateY(-30px) rotate(0deg) scale(1); opacity: 0; }
    10% { opacity: 0.8; }
    100% { transform: translateY(100vh) rotate(360deg) scale(0.5); opacity: 0; }
}

.moon-container {
    position: absolute;
    top: 20px;
    right: 180px;
}

.moon {
    width: 80px;
    height: 80px;
    background: linear-gradient(135deg, #fff9c4, #ffd54f);
    border-radius: 50%;
    position: relative;
    box-shadow: 0 0 50px rgba(255, 215, 0, 0.6);
    animation: moonGlow 3s ease-in-out infinite;
}

.moon-glow {
    position: absolute;
    top: -20px;
    left: -20px;
    width: 120px;
    height: 120px;
    background: radial-gradient(circle, rgba(255, 215, 0, 0.3) 0%, transparent 70%);
    border-radius: 50%;
}

.rabbit {
    position: absolute;
    bottom: -30px;
    right: -20px;
    font-size: 30px;
    animation: rabbitJump 2s ease-in-out infinite;
}

@keyframes moonGlow {
    0%, 100% { box-shadow: 0 0 50px rgba(255, 215, 0, 0.6); }
    50% { box-shadow: 0 0 80px rgba(255, 215, 0, 0.8); }
}

@keyframes rabbitJump {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-10px); }
}

// ==================== 鲜花飘落 ====================
.flower-container {
    position: absolute;
    width: 100%;
    height: 100%;
}

.flower {
    position: absolute;
    top: -30px;
    animation: flowerFall linear infinite;
    opacity: 0.9;
}

@keyframes flowerFall {
    0% { transform: translateY(-30px) rotate(0deg); opacity: 0; }
    10% { opacity: 0.9; }
    100% { transform: translateY(100vh) rotate(360deg); opacity: 0; }
}

// ==================== 彩带飘落 ====================
.confetti-container {
    position: absolute;
    width: 100%;
    height: 100%;
}

.confetti {
    position: absolute;
    top: -20px;
    animation: confettiFall linear infinite;
    opacity: 0.9;
}

@keyframes confettiFall {
    0% { transform: translateY(-20px) rotate(0deg); opacity: 0; }
    10% { opacity: 0.9; }
    100% { transform: translateY(100vh) rotate(720deg); opacity: 0; }
}

// ==================== 国庆气球与和平鸽 ====================
.national-container {
    position: absolute;
    width: 100%;
    height: 100%;
}

.balloon {
    position: absolute;
    font-size: 50px;
    animation: balloonFloat 4s ease-in-out infinite;

    &-left { left: 220px; top: 30px; }
    &-right { right: 180px; top: 30px; animation-delay: 1s; }
}

@keyframes balloonFloat {
    0%, 100% { transform: translateY(0) rotate(-5deg); }
    50% { transform: translateY(-20px) rotate(5deg); }
}

.dove {
    position: absolute;
    animation: doveFly 8s linear infinite;
    opacity: 0.8;
}

@keyframes doveFly {
    0% { transform: translateX(0) translateY(0); }
    25% { transform: translateX(25vw) translateY(-30px); }
    50% { transform: translateX(50vw) translateY(0); }
    75% { transform: translateX(75vw) translateY(-20px); }
    100% { transform: translateX(100vw) translateY(0); }
}

.pumpkin-container { position: absolute; top: 50px; }

.pumpkin {
    position: absolute;
    font-size: 50px;
    animation: pumpkinFloat 3s ease-in-out infinite;
    &-left { left: 220px; }
    &-right { right: 180px; animation-delay: 1s; }
}

@keyframes pumpkinFloat {
    0%, 100% { transform: translateY(0) rotate(-5deg); }
    50% { transform: translateY(-10px) rotate(5deg); }
}

.bat-container { position: absolute; width: 100%; height: 50%; }

.bat {
    position: absolute;
    animation: batFly 5s ease-in-out infinite;
    opacity: 0.8;
}

@keyframes batFly {
    0%, 100% { transform: translateX(0) translateY(0); }
    25% { transform: translateX(50px) translateY(-20px); }
    50% { transform: translateX(100px) translateY(0); }
    75% { transform: translateX(50px) translateY(20px); }
}

.tree-container { position: absolute; bottom: 20px; right: 20px; }

.christmas-tree { font-size: 60px; animation: treeGlow 2s ease-in-out infinite; }

@keyframes treeGlow {
    0%, 100% { filter: drop-shadow(0 0 10px rgba(0, 255, 0, 0.5)); }
    50% { filter: drop-shadow(0 0 20px rgba(0, 255, 0, 0.8)); }
}

.gift-container { position: absolute; bottom: 20px; }

.gift {
    position: absolute;
    font-size: 40px;
    animation: giftBounce 2s ease-in-out infinite;
    &-left { left: 30px; }
    &-right { right: 30px; animation-delay: 0.5s; }
}

@keyframes giftBounce {
    0%, 100% { transform: translateY(0) scale(1); }
    50% { transform: translateY(-10px) scale(1.1); }
}

// ==================== 鞭炮飘落 ====================
.firecracker-rain-container {
    position: absolute;
    width: 100%;
    height: 100%;
}

.firecracker-rain {
    position: absolute;
    top: -30px;
    animation: firecrackerFall linear infinite;
    opacity: 0.9;
}

@keyframes firecrackerFall {
    0% { transform: translateY(-30px) rotate(0deg); opacity: 0; }
    10% { opacity: 0.9; }
    100% { transform: translateY(100vh) rotate(720deg); opacity: 0; }
}

// ==================== 粽子飘落 ====================
.zongzi-rain-container {
    position: absolute;
    width: 100%;
    height: 100%;
}

.zongzi-rain {
    position: absolute;
    top: -30px;
    animation: zongziFall linear infinite;
    opacity: 0.9;
}

@keyframes zongziFall {
    0% { transform: translateY(-30px) rotate(0deg); opacity: 0; }
    10% { opacity: 0.9; }
    100% { transform: translateY(100vh) rotate(360deg); opacity: 0; }
}

// ==================== 龙舟竞渡 ====================
.dragon-boat-container {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 80px;
    overflow: hidden;
    pointer-events: none;
}

.river {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 40px;
    background: linear-gradient(to bottom, rgba(76, 175, 80, 0.3), rgba(76, 175, 80, 0.5));
    animation: riverFlow 3s ease-in-out infinite;
}

@keyframes riverFlow {
    0%, 100% { transform: translateX(-5px); }
    50% { transform: translateX(5px); }
}

.dragon-boat {
    position: absolute;
    bottom: 25px;
    left: -100px;
    animation: boatRace 8s linear infinite;
    display: flex;
    align-items: center;
}

.boat-body {
    font-size: 40px;
    animation: boatRock 0.5s ease-in-out infinite;
}

.boat-wave {
    font-size: 30px;
    margin-left: -10px;
    animation: waveMove 1s ease-in-out infinite;
}

@keyframes boatRace {
    0% { left: -100px; }
    100% { left: calc(100% + 100px); }
}

@keyframes boatRock {
    0%, 100% { transform: rotate(-5deg); }
    50% { transform: rotate(5deg); }
}

@keyframes waveMove {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-5px); }
}

.flag-container { position: absolute; top: 50px; left: 50%; transform: translateX(-50%); }

.flag { font-size: 50px; animation: flagWave 2s ease-in-out infinite; }

@keyframes flagWave {
    0%, 100% { transform: rotate(-5deg); }
    50% { transform: rotate(5deg); }
}

.test-panel {
    position: fixed;
    bottom: 80px;
    right: 20px;
    z-index: 10000;
    background: rgba(255, 255, 255, 0.95);
    padding: 12px;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
    pointer-events: auto;
}

.greeting-content { text-align: center; }

.greeting-icon {
    font-size: 60px;
    margin-bottom: 15px;
    animation: iconBounce 1s ease-in-out infinite;
}

.greeting-text {
    font-size: 20px;
    font-weight: bold;
    color: var(--festival-primary, #409eff);
}

@keyframes iconBounce {
    0%, 100% { transform: scale(1); }
    50% { transform: scale(1.1); }
}
</style>

<style lang="less">
.greeting-dialog {
    .el-dialog {
        border-radius: 16px;
        overflow: hidden;
    }

    .el-dialog__header {
        background: linear-gradient(135deg, var(--festival-primary, #409eff), var(--festival-secondary, #67c23a));
        color: white;
        padding: 15px;
        margin: 0;
    }

    .el-dialog__body {
        padding: 30px;
    }
}
</style>
