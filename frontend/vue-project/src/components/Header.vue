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
      <!-- <button @click="goUserList">ユーザー一覧</button> -->
    </div>
  </nav>
</template>

<style scoped></style>
