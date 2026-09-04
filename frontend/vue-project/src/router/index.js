import UserCreate from '@/views/UserCreate.vue'
import UserList from '@/views/UserList.vue';
import UserShow from '@/views/UserShow.vue';
import UserEdit from '@/views/UserEdit.vue';
import { createRouter, createWebHistory } from 'vue-router'
import { useUserListStore } from "@/store/userListStore";
import { useNavigationStore } from '@/store/navigationStore';
import Login from '@/views/Login.vue';
import { useAuthStore } from '@/store/authStore';

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
      requireAuth: true,
    }
  },
  {
    path: "/users/:id/show",
    name: "show",
    component: UserShow,
    meta: {
      keepUserListState: true,
      requireAuth: true,
      authUserOnly: true, // 認証ユーザーのみアクセス可能
    }
  },
  {
    path: "/users/:id/edit",
    name: "edit",
    component: UserEdit,
    meta: {
      keepUserListState: true,
      requireAuth: true,
      authUserOnly: true, // 認証ユーザーのみアクセス可能
    }
  },
  {
    path: "/login",
    name: "login",
    component: Login
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

  // 認証が必要なルートにアクセスする場合、ログイン状態を確認
  if (to.meta.requireAuth && !useAuthStore().isLoggedIn) {
    return { name: "login" };
  }

  // 認証ユーザーのみアクセス可能なルートにアクセスする場合、現在のユーザーと一致するか確認
  if (to.meta.authUserOnly && Number(to.params.id) !== Number(useAuthStore().userId)) {
    return { name: "list", query: { error: "他のユーザーの詳細ページや編集ページにはアクセスできません。" } };
  }
  // const navigationStore = useNavigationStore();

  // navigationStore.previousRoute = from.name;
  // navigationStore.currentRoute = to.name;

  // console.log(window.history.state.back, window.history.state.current);
});
export default router
