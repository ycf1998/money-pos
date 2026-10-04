import { ref, watch } from 'vue'

const FESTIVALS = {
    spring: {
        name: '春节',
        key: 'spring',
        theme: {
            primary: '#e53935',
            secondary: '#ffd700',
            accent: '#ff5722'
        },
        decorations: ['lantern', 'firecracker'],
        greeting: '新春快乐，万事如意！',
        getDate: (year) => getLunarNewYear(year)
    },
    lantern: {
        name: '元宵',
        key: 'lantern',
        theme: {
            primary: '#e53935',
            secondary: '#ff9800',
            accent: '#ffc107'
        },
        decorations: ['lantern', 'firework'],
        greeting: '元宵节快乐，团团圆圆！',
        darkMode: true,
        getDate: (year) => {
            const spring = getLunarNewYear(year)
            return addDays(spring, 15)
        }
    },
    labor: {
        name: '劳动节',
        key: 'labor',
        theme: {
            primary: '#ff9800',
            secondary: '#ffc107',
            accent: '#ff5722'
        },
        decorations: ['flower'],
        greeting: '劳动节快乐！',
        getDate: (year) => new Date(year, 4, 1)
    },
    dragonBoat: {
        name: '端午节',
        key: 'dragonBoat',
        theme: {
            primary: '#4caf50',
            secondary: '#8bc34a',
            accent: '#cddc39'
        },
        decorations: ['zongzi', 'dragonBoat'],
        greeting: '端午安康！',
        getDate: (year) => getDragonBoatDate(year)
    },
    qixi: {
        name: '七夕',
        key: 'qixi',
        theme: {
            primary: '#9c27b0',
            secondary: '#e91e63',
            accent: '#7c4dff'
        },
        decorations: ['heart', 'star'],
        greeting: '七夕快乐！',
        getDate: (year) => getQixiDate(year)
    },
    midAutumn: {
        name: '中秋节',
        key: 'midAutumn',
        theme: {
            primary: '#ffd700',
            secondary: '#ff9800',
            accent: '#fff59d'
        },
        decorations: ['moon', 'rabbit'],
        greeting: '中秋快乐，阖家团圆！',
        darkMode: true,
        getDate: (year) => getMidAutumnDate(year)
    },
    national: {
        name: '国庆节',
        key: 'national',
        theme: {
            primary: '#e53935',
            secondary: '#ffd700',
            accent: '#ff5722'
        },
        decorations: ['flag', 'firework'],
        greeting: '国庆快乐！',
        getDate: (year) => new Date(year, 9, 1)
    },
    newYear: {
        name: '元旦',
        key: 'newYear',
        theme: {
            primary: '#1976d2',
            secondary: '#ffd700',
            accent: '#ff5722'
        },
        decorations: ['firework', 'confetti'],
        greeting: '新年快乐！',
        getDate: (year) => new Date(year, 0, 1)
    }
}

const LUNAR_NEW_YEAR_DATES = {
    2026: new Date(2026, 1, 17),
    2027: new Date(2027, 1, 6),
    2028: new Date(2028, 0, 26),
    2029: new Date(2029, 1, 13),
    2030: new Date(2030, 1, 3)
}

function getLunarNewYear(year) {
    return LUNAR_NEW_YEAR_DATES[year] || new Date(year, 1, 1)
}

function getDragonBoatDate(year) {
    const dates = {
        2026: new Date(2026, 5, 19),
        2027: new Date(2027, 5, 9),
        2028: new Date(2028, 4, 28),
        2029: new Date(2029, 5, 16),
        2030: new Date(2030, 5, 5)
    }
    return dates[year] || new Date(year, 5, 1)
}

function getQixiDate(year) {
    const dates = {
        2026: new Date(2026, 7, 18),
        2027: new Date(2027, 7, 8),
        2028: new Date(2028, 6, 27),
        2029: new Date(2029, 7, 15),
        2030: new Date(2030, 7, 4)
    }
    return dates[year] || new Date(year, 7, 1)
}

function getMidAutumnDate(year) {
    const dates = {
        2026: new Date(2026, 8, 25),
        2027: new Date(2027, 8, 15),
        2028: new Date(2028, 9, 3),
        2029: new Date(2029, 8, 22),
        2030: new Date(2030, 8, 12)
    }
    return dates[year] || new Date(year, 8, 15)
}

function addDays(date, days) {
    const result = new Date(date)
    result.setDate(result.getDate() + days)
    return result
}

function isInRange(date, start, days) {
    const end = addDays(start, days)
    return date >= start && date <= end
}

function getCurrentFestival() {
    const now = new Date()
    const year = now.getFullYear()
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())

    for (const [key, festival] of Object.entries(FESTIVALS)) {
        const festivalDate = festival.getDate(year)
        const rangeDays = getFestivalRangeDays(key)
        
        const startDate = addDays(festivalDate, -Math.floor(rangeDays / 2))
        
        if (isInRange(today, startDate, rangeDays)) {
            return festival
        }
    }
    
    return null
}

function getFestivalRangeDays(key) {
    const ranges = {
        spring: 9,
        national: 7,
        midAutumn: 3,
        dragonBoat: 3,
        labor: 3,
        lantern: 1,
        qixi: 1,
        newYear: 1
    }
    return ranges[key] || 1
}

export function useFestival() {
    const currentFestival = ref(getCurrentFestival())
    const enabled = ref(true)
    const showGreeting = ref(true)

    function toggle() {
        enabled.value = !enabled.value
    }

    function dismissGreeting() {
        showGreeting.value = false
    }

    function setTestFestival(key) {
        if (key && FESTIVALS[key]) {
            currentFestival.value = FESTIVALS[key]
            showGreeting.value = true
        } else {
            currentFestival.value = null
        }
    }

    function applyTheme() {
        if (!enabled.value || !currentFestival.value) {
            document.documentElement.removeAttribute('data-festival')
            document.documentElement.classList.remove('dark')
            return
        }

        const { theme, key, darkMode } = currentFestival.value
        document.documentElement.setAttribute('data-festival', key)
        document.documentElement.style.setProperty('--festival-primary', theme.primary)
        document.documentElement.style.setProperty('--festival-secondary', theme.secondary)
        document.documentElement.style.setProperty('--festival-accent', theme.accent)
        
        if (darkMode) {
            document.documentElement.classList.add('dark')
        } else {
            document.documentElement.classList.remove('dark')
        }
    }

    watch([enabled, currentFestival], () => {
        applyTheme()
    }, { immediate: true })

    return {
        currentFestival,
        enabled,
        showGreeting,
        toggle,
        dismissGreeting,
        setTestFestival,
        festivals: FESTIVALS
    }
}

export { FESTIVALS }
