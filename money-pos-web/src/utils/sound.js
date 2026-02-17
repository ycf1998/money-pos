import { ref } from 'vue'

const audioContext = typeof window !== 'undefined' 
    ? new (window.AudioContext || window.webkitAudioContext)() 
    : null

function playTone(frequency, duration, type = 'sine', volume = 0.3) {
    if (!audioContext) return
    
    if (audioContext.state === 'suspended') {
        audioContext.resume()
    }
    
    const oscillator = audioContext.createOscillator()
    const gainNode = audioContext.createGain()
    
    oscillator.connect(gainNode)
    gainNode.connect(audioContext.destination)
    
    oscillator.frequency.value = frequency
    oscillator.type = type
    
    gainNode.gain.setValueAtTime(volume, audioContext.currentTime)
    gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + duration / 1000)
    
    oscillator.start(audioContext.currentTime)
    oscillator.stop(audioContext.currentTime + duration / 1000)
}

function playSlideTone(startFreq, endFreq, duration, type = 'sine', volume = 0.3) {
    if (!audioContext) return
    
    if (audioContext.state === 'suspended') {
        audioContext.resume()
    }
    
    const oscillator = audioContext.createOscillator()
    const gainNode = audioContext.createGain()
    
    oscillator.connect(gainNode)
    gainNode.connect(audioContext.destination)
    
    oscillator.frequency.setValueAtTime(startFreq, audioContext.currentTime)
    oscillator.frequency.exponentialRampToValueAtTime(endFreq, audioContext.currentTime + duration / 1000)
    oscillator.type = type
    
    gainNode.gain.setValueAtTime(volume, audioContext.currentTime)
    gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + duration / 1000)
    
    oscillator.start(audioContext.currentTime)
    oscillator.stop(audioContext.currentTime + duration / 1000)
}

export const SoundEffects = {
    scan() {
        playTone(1200, 80, 'sine', 0.2)
    },
    
    add() {
        playTone(1000, 60, 'sine', 0.15)
    },
    
    remove() {
        playSlideTone(600, 400, 100, 'sine', 0.2)
    },
    
    success() {
        playTone(800, 100, 'sine', 0.25)
        setTimeout(() => playTone(1000, 150, 'sine', 0.25), 100)
    },
    
    error() {
        playTone(300, 300, 'square', 0.15)
    },
    
    member() {
        playSlideTone(600, 900, 150, 'sine', 0.2)
        setTimeout(() => playTone(900, 100, 'sine', 0.2), 150)
    },
    
    clear() {
        playSlideTone(600, 300, 200, 'sine', 0.2)
    },
    
    click() {
        playTone(1500, 30, 'sine', 0.1)
    }
}

export function useSound(enabled = true) {
    const isEnabled = ref(enabled)
    
    const play = (soundName) => {
        if (isEnabled.value && SoundEffects[soundName]) {
            SoundEffects[soundName]()
        }
    }
    
    const toggle = () => {
        isEnabled.value = !isEnabled.value
    }
    
    return {
        isEnabled,
        play,
        toggle,
        sounds: SoundEffects
    }
}
