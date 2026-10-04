import {useDark, useToggle} from '@vueuse/core'
import {reactive} from 'vue'

export const isDark = useDark()
export const toggleDarkMode = useToggle(isDark)

export const sidebarState = reactive({
    isOpen: window.innerWidth > 1024,
    isHovered: false,
    handleHover(value) {
        if (window.innerWidth < 1024) {
            return
        }
        sidebarState.isHovered = value
    },
    handleWindowResize() {
        sidebarState.isOpen = window.innerWidth > 1024;
    },
})

let lastScrollTop = 0
export const scrolling = reactive({
    down: false,
    up: false,
})
export const handleScroll = () => {
    let st = window.scrollY || document.documentElement.scrollTop
    if (st > lastScrollTop) {
        scrolling.down = true
        scrolling.up = false
    } else {
        scrolling.down = false
        scrolling.up = true
        if (st === 0) {
            //  reset
            scrolling.down = false
            scrolling.up = false
        }
    }
    lastScrollTop = st <= 0 ? 0 : st
}