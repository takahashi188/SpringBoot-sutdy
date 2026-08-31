import UserCreate from '@/views/UserCreate.vue'
import UserList from '@/views/UserList.vue';
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: "/",
    name: "create",
    component: UserCreate
  },
  {
    path: "/users",
    name: "list",
    component: UserList
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
})

export default router
