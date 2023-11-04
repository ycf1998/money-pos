import {createApp} from "vue";
import App from './App.vue';
import router from './router';
import store from './store';

import './style/main.css';
import './style/tailwind.css';

import plugins from './plugins'

createApp(App)
    .use(router)
    .use(store)
    .use(plugins)
    .mount("#app")

document.title = import.meta.env.VITE_TITLE