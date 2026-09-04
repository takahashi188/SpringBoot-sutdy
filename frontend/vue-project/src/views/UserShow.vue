<script setup>
import { useRoute, useRouter } from "vue-router";
import api from "@/plugins/axios";
import { ref, onMounted } from "vue";

const route = useRoute();
const id = route.params.id;
const router = useRouter();

const user = ref(null);
const errorMessage = ref("");

const getUser = async () => {
  try {
    const response = await api.get(`/api/users/${id}`);
    user.value = response.data;
  } catch (error) {
    console.log(error);

    if (error.response?.data?.message) {
      errorMessage.value = error.response.data.message;
    } else {
      errorMessage.value = "ユーザー詳細の取得に失敗しました。";
    }
  }
};

onMounted(getUser);

const formatDate = (date) => {
  if (!date) {
    return "未設定";
  }
  return new Date(date).toLocaleDateString("ja-JP");
};

const backUserList = () => {
  router.push({ name: "list" });
};
</script>

<template>
  <div class="max-w-4xl mx-auto p-6">
    <div class="bg-white rounded-xl shadow-md p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold">ユーザー詳細</h2>

        <button
          @click="backUserList"
          class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition"
        >
          一覧に戻る
        </button>
      </div>

      <p
        v-if="errorMessage"
        class="mb-4 rounded-md bg-red-100 border border-red-300 px-4 py-2 text-red-700"
      >
        {{ errorMessage }}
      </p>

      <div v-if="user">
        <!-- ユーザー情報 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          <div>
            <p class="text-sm text-gray-500">ID</p>
            <p class="font-medium">{{ user.id }}</p>
          </div>

          <div>
            <p class="text-sm text-gray-500">名前</p>
            <p class="font-medium">{{ user.name }}</p>
          </div>

          <div>
            <p class="text-sm text-gray-500">メールアドレス</p>
            <p class="font-medium break-all">
              {{ user.email }}
            </p>
          </div>

          <div>
            <p class="text-sm text-gray-500">ニックネーム</p>
            <p class="font-medium">
              {{ user.profile?.nickname || "未設定" }}
            </p>
          </div>

          <div>
            <p class="text-sm text-gray-500">誕生日</p>
            <p class="font-medium">
              {{ formatDate(user.profile?.birthday) }}
            </p>
          </div>
        </div>

        <!-- 資格一覧 -->
        <div>
          <h3 class="text-lg font-semibold mb-3 border-b pb-2">保有資格</h3>

          <div class="overflow-x-auto">
            <table class="w-full border-collapse">
              <thead>
                <tr class="bg-gray-100">
                  <th class="px-4 py-3 text-left border">資格名</th>
                  <th class="px-4 py-3 text-left border">取得日</th>
                </tr>
              </thead>

              <tbody>
                <tr
                  v-for="qualification in user.qualifications"
                  :key="qualification.qualificationId"
                  class="hover:bg-gray-50"
                >
                  <td class="px-4 py-3 border">
                    {{ qualification.qualificationName }}
                  </td>
                  <td class="px-4 py-3 border">
                    {{ formatDate(qualification.acquisitionDate) }}
                  </td>
                </tr>

                <tr
                  v-if="
                    !user.qualifications || user.qualifications.length === 0
                  "
                >
                  <td
                    colspan="2"
                    class="px-4 py-6 text-center text-gray-500 border"
                  >
                    資格情報はありません
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
