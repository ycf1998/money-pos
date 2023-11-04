import { createPinia } from 'pinia';
import { markRaw } from 'vue'
import router from '../router';

const store = createPinia();
store.use(({ store }) => {
    store.$router = markRaw(router)
});
export default store;

export * from './modules/app.js'
export * from './modules/user.js'