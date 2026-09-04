<script setup>
import { reactive, ref } from "vue";
import api from "@/plugins/axios";
import { useAuthStore } from "@/store/authStore";
import { useRouter, useRoute } from "vue-router";

const authStore = useAuthStore();
const router = useRouter();
const errorMessage = ref("");
const route = useRoute();
const message = route.query.message || "";

const form = reactive({
  email: "",
  password: "",
});

const login = async () => {
  try {
    // HTML形式で送信するためにURLSearchParamsを使用してフォームデータを作成
    const params = new URLSearchParams();

    params.append("email", form.email);
    params.append("password", form.password);

    // ログインリクエストを送信
    const response = await api.post("/login", params);

    // ログイン成功後、ユーザー情報を取得
    const meResponse = await api.get("/api/auth/me");

    // ユーザー名をストアに保存してログイン状態を更新
    authStore.login(meResponse.data.name, meResponse.data.id);
    
    console.log("ログイン成功:", meResponse);
    router.push({ name: "list" }); // ユーザー一覧ページにリダイレクト
  } catch (error) {
    // ログイン失敗時の処理
    console.error("ログイン失敗:", error.response?.data);

    errorMessage.value =
      error.response?.data?.message || "ログインに失敗しました。";
  }
};
</script>

<template>
  <div class="min-h-[calc(100vh-120px)] flex justify-center pt-20 bg-gray-100">
    <div class="w-full max-w-md bg-white shadow-lg rounded-xl p-8 self-start">
      <h2 class="text-3xl font-bold text-center text-gray-800 mb-8">
        ログイン
      </h2>

      <p v-if="message" class="mb-4 rounded-md bg-blue-100 border border-blue-300 px-3 py-2 text-blue-700 text-sm">
        {{ message }}
      </p>

      <p
        v-if="errorMessage"
        class="mb-4 rounded-md bg-red-100 border border-red-300 px-3 py-2 text-red-700 text-sm"
      >
        {{ errorMessage }}
      </p>

      <form @submit.prevent="login" class="space-y-6">
        <div>
          <label
            for="email"
            class="block text-sm font-medium text-gray-700 mb-2"
          >
            メールアドレス
          </label>
          <input
            id="email"
            type="email"
            v-model="form.email"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="example@example.com"
          />
        </div>

        <div>
          <label
            for="password"
            class="block text-sm font-medium text-gray-700 mb-2"
          >
            パスワード
          </label>
          <input
            id="password"
            type="password"
            v-model="form.password"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="パスワードを入力"
          />
        </div>

        <button
          type="submit"
          class="w-full bg-blue-600 text-white py-2.5 rounded-lg font-semibold hover:bg-blue-700 transition duration-200"
        >
          ログイン
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped></style>
