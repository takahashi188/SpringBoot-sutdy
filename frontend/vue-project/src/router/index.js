import UserCreate from '@/views/UserCreate.vue'
import UserList from '@/views/UserList.vue';
import UserShow from '@/views/UserShow.vue';
import { createRouter, createWebHistory } from 'vue-router'
import { useUserListStore } from "@/store/userListStore";
import { useNavigationStore } from '@/store/navigationStore';

const routes = [
  {
    path: "/",
    name: "create",
    component: UserCreate
  },
  {
    path: "/users",
    name: "list",
    component: UserList,
    meta: {
      Keepalive: true,
    }
  },
  {
    path: "/users/:id",
    name: "show",
    component: UserShow,
    meta: {
      keepUserListState: true,
    }
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
})

router.beforeEach((to, from) => {
  const userListStore = useUserListStore();

  if (
    from.name === "list" &&
    !to.meta.keepUserListState
  ) {
    userListStore.$reset();
  }

  // const navigationStore = useNavigationStore();

  // navigationStore.previousRoute = from.name;
  // navigationStore.currentRoute = to.name;

  // console.log(window.history.state.back, window.history.state.current);
});
export default router
