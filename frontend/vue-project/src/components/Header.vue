<script setup>
// import { useRouter } from "vue-router";
// import { useNavigationStore } from "@/store/navigationStore";

// const router = useRouter();
// const navigationStore = useNavigationStore();

// const goUserList = () => {
//   if (navigationStore.previousRoute === "list" && navigationStore.currentRoute === "show") {
//   // if (window.history.state.back === "/users/" && window.history.state.current.startsWith("/users/")) {
//     router.back();
//   } else {
//     router.push({ name: "list" });
//   }
//   // router.push({ name: "list" });
// };

import axios from "axios";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/store/authStore";

const router = useRouter();
const authStore = useAuthStore();

const logout = async () => {
  try {
    console.log("logout start");

    await axios.post("http://localhost:8080/logout");

    console.log("logout success");

    // ログアウト後、ストアの状態をリセット
    authStore.logout();

    // ログイン画面にリダイレクト
    router.push({ name: "login" });
  } catch (error) {
    console.error(error);
  }
};
</script>

<template>
  <nav class="bg-white shadow-md">
    <div class="max-w-6xl mx-auto px-6 py-4 flex gap-4">
      <router-link
        :to="{ name: 'create' }"
        class="px-4 py-2 rounded-md transition"
        active-class="bg-blue-600 text-white"
      >
        ユーザー登録
      </router-link>

      <router-link
        :to="{ name: 'list' }"
        class="px-4 py-2 rounded-md transition"
        active-class="bg-blue-600 text-white"
      >
        ユーザー一覧
      </router-link>

      <router-link
        :to="{ name: 'login' }"
        v-if="!authStore.isLoggedIn"
        class="px-4 py-2 rounded-md transition"
        active-class="bg-blue-600 text-white"
      >
        ログイン
      </router-link>
      <!-- <button @click="goUserList">ユーザー一覧</button> -->
      <button
        @click="logout"
        v-if="authStore.isLoggedIn"
        class="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition"
      >
        ログアウト
      </button>

      <p v-if="authStore.isLoggedIn" class="px-4 py-2">
        {{ authStore.userName }} さん
      </p>
    </div>
  </nav>
</template>

<style scoped></style>
