<script setup>
import UserTable from "@/components/user/UserTable.vue";
import axios from "axios";
import { ref, watch } from "vue";

const users = ref([]);
const currentPage = ref(0);
const totalPage = ref(0);
const searchKeyword = ref("");
const sort = ref("id,asc");
const errorMessage = ref("");
const doSearch = ref(false);
const count = ref(1);

const getUsers = async () => {
  try {
    const url = doSearch.value
      ? "http://localhost:8080/api/users/search"
      : "http://localhost:8080/api/users";

    const response = await axios.get(url, {
      params: {
        page: currentPage.value,
        size: 10,
        sort: sort.value,
        // 検索条件があるならクエリに追加する
        ...(doSearch.value && {
          name: searchKeyword.value,
        }),
      },
    });

    users.value = response.data.content;
    currentPage.value = response.data.number;
    totalPage.value = response.data.totalPages;
    console.log(count.value++);
  } catch (error) {
    console.log(error);

    if (error.response?.data?.message) {
      errorMessage.value = error.response.data.message;
    } else {
      errorMessage.value = "ユーザー一覧の取得に失敗しました。";
    }
  }
};

const searchUsers = () => {
  doSearch.value = true;
  reloadUsers();
};

const backPage = () => {
  currentPage.value--;
};

const nextPage = () => {
  currentPage.value++;
};

const resetSearchKeyword = () => {
  searchKeyword.value = "";
  doSearch.value = false;
  reloadUsers();
};

const reloadUsers = () => {
  if (currentPage.value === 0) {
    getUsers();
  } else {
    currentPage.value = 0;
  }
}

watch(
  [currentPage, sort],
  () => {
    getUsers();
  },
  { immediate: true },
);
</script>

<template>
  <div class="max-w-6xl mx-auto p-6">
    <div class="bg-white rounded-xl shadow-md p-6 h-full flex flex-col">
      <h2 class="text-2xl font-bold mb-6">ユーザー一覧</h2>

      <!-- 検索エリア -->
      <div class="flex flex-wrap gap-3 mb-6">
        <input
          type="text"
          v-model="searchKeyword"
          placeholder="ユーザー名で検索"
          class="flex-1 min-w-64 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />

        <button
          type="button"
          @click="searchUsers"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
        >
          検索
        </button>

        <button
          type="button"
          @click="resetSearchKeyword"
          class="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition"
        >
          ×
        </button>

        <select
          v-model="sort"
          class="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="id,asc">ID昇順</option>
          <option value="id,desc">ID降順</option>
          <option value="name,asc">名前昇順</option>
          <option value="name,desc">名前降順</option>
        </select>
      </div>

      <p
        v-if="errorMessage"
        class="mb-4 rounded-md bg-red-100 border border-red-300 px-4 py-2 text-red-700"
      >
        {{ errorMessage }}
      </p>

      <!-- 一覧 -->
      <UserTable :users="users" />

      <!-- ページング -->
      <div class="flex items-center justify-center gap-4 mt-6">
        <button
          type="button"
          v-if="currentPage > 0"
          @click="backPage"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
        >
          前へ
        </button>

        <span class="font-medium">
          {{ currentPage + 1 }} / {{ totalPage }}
        </span>

        <button
          type="button"
          v-if="currentPage + 1 < totalPage"
          @click="nextPage"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
        >
          次へ
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
