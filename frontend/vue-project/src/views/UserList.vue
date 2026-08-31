<script setup>
import axios from "axios";
import { ref, watch } from "vue";

const users = ref([]);
const currentPage = ref(0);
const totalPage = ref(0);
const searchKeyword = ref("");
const sort = ref("id,asc");

const getUsers = async () => {
  try {
    const url = searchKeyword.value
      ? "http://localhost:8080/api/users/search"
      : "http://localhost:8080/api/users";

    const response = await axios.get(url, {
      params: {
        page: currentPage.value,
        size: 10,
        sort: sort.value,
        // 検索条件があるならクエリに追加する
        ...(searchKeyword.value && {
          name: searchKeyword.value,
        }),
      },
    });

    users.value = response.data.content;
    currentPage.value = response.data.number;
    totalPage.value = response.data.totalPages;
    console.log(response);
  } catch (error) {
    console.log(error);
  }
};

const searchUsers = () => {
  currentPage.value = 0;
  getUsers();
};

const backPage = () => {
  currentPage.value--;
};

const nextPage = () => {
  currentPage.value++;
};

const resetSearchKeyword = () => {
  searchKeyword.value = "";
  currentPage.value = 0;
  getUsers();
};

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

      <h2 class="text-2xl font-bold mb-6">
        ユーザー一覧
      </h2>

      <!-- 検索エリア -->
      <div class="flex flex-wrap gap-3 mb-6">

        <input
          type="text"
          v-model="searchKeyword"
          placeholder="ユーザー名で検索"
          class="flex-1 min-w-64 px-3 py-2 border border-gray-300 rounded-md
                 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >

        <button
          type="button"
          @click="searchUsers"
          class="px-4 py-2 bg-blue-600 text-white rounded-md
                 hover:bg-blue-700 transition"
        >
          検索
        </button>

        <button
          type="button"
          @click="resetSearchKeyword"
          class="px-4 py-2 bg-red-500 text-white rounded-md
                 hover:bg-red-600 transition"
        >
          ×
        </button>

        <select
          v-model="sort"
          class="px-3 py-2 border border-gray-300 rounded-md
                 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="id,asc">ID昇順</option>
          <option value="id,desc">ID降順</option>
          <option value="name,asc">名前昇順</option>
          <option value="name,desc">名前降順</option>
        </select>

      </div>

      <!-- 一覧 -->
      <div class="overflow-x-auto">
        <table class="w-full border-collapse">

          <thead>
            <tr class="bg-blue-600 text-white">
              <th class="px-4 py-3 text-left">ID</th>
              <th class="px-4 py-3 text-left">ユーザー名</th>
              <th class="px-4 py-3 text-left">メールアドレス</th>
              <th class="px-4 py-3 text-left">ニックネーム</th>
            </tr>
          </thead>

          <tbody>

            <tr
              v-for="user in users"
              :key="user.id"
              class="border-b hover:bg-gray-50"
            >
              <td class="px-4 py-3">
                {{ user.id }}
              </td>

              <td class="px-4 py-3">
                {{ user.name }}
              </td>

              <td class="px-4 py-3">
                {{ user.email }}
              </td>

              <td class="px-4 py-3">
                {{
                  user.profile?.nickname
                    ? user.profile.nickname
                    : "未設定"
                }}
              </td>
            </tr>

            <tr v-if="users.length === 0">
              <td
                colspan="4"
                class="py-6 text-center text-gray-500"
              >
                ユーザーが存在しません
              </td>
            </tr>

          </tbody>

        </table>
      </div>

      <!-- ページング -->
      <div class="flex items-center justify-center gap-4 mt-6">

        <button
          type="button"
          v-if="currentPage > 0"
          @click="backPage"
          class="px-4 py-2 bg-blue-600 text-white rounded-md
                 hover:bg-blue-700 transition"
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
          class="px-4 py-2 bg-blue-600 text-white rounded-md
                 hover:bg-blue-700 transition"
        >
          次へ
        </button>

      </div>

    </div>
  </div>
</template>

<style scoped></style>
