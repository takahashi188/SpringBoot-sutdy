<script setup>
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import { ref, reactive, onMounted } from "vue";
import BaseInfo from "@/components/user/BaseInfo.vue";
import Profile from "@/components/user/Profile.vue";
import Qualification from "@/components/user/Qualification.vue";

const route = useRoute();
const id = route.params.id;
const router = useRouter();

const user = ref(null);
const qualifications = ref([]);
const form = reactive({
  name: "",
  email: "",
  password: "",
  profile: {
    nickname: "",
    birthday: null,
  },
  qualifications: [],
});
const step = ref(1);
const errors = ref({});

const getUser = async () => {
  try {
    errors.value = {}; // エラーをリセット
    
    const response = await axios.get(`http://localhost:8080/api/users/${id}`);
    user.value = response.data;
  } catch (error) {
    console.log(error);

    if (error.response?.data?.message) {
      errors.value.edit = error.response.data.message;
    } else {
      errors.value.edit = "ユーザー情報の取得に失敗しました。";
    }
  }
};

const getQualifications = async () => {
  try {
    errors.value = {}; // エラーをリセット

    const response = await axios.get(
      "http://localhost:8080/api/qualification-master",
    );

    qualifications.value = response.data;
  } catch (error) {
    console.log(error);

    if (error.response?.data?.message) {
      errors.value.edit = error.response.data.message;
    } else {
      errors.value.edit = "資格マスタの取得に失敗しました。";
    }
  }
};

onMounted(async () => {
  await getUser();
  await getQualifications();
  form.name = user.value.name;
  form.email = user.value.email;
  form.profile.nickname = user.value.profile?.nickname || "";
  form.profile.birthday = user.value.profile?.birthday || null;
  form.qualifications = user.value.qualifications;
});

const editUser = async () => {
  try {
    if (!validateForm()) {
      return;
    }

    if (!form.password) {
      form.password = null; // パスワードが空の場合はnullに設定して送信
    }

    const response = await axios.put(
      `http://localhost:8080/api/users/${id}`,
      form,
    );

    console.log("ユーザー情報を更新しました:", response.data);
    router.push({ name: "list" }); // 更新後にユーザー一覧ページに遷移
  } catch (error) {
    console.log(error);
    if (error.response?.data?.message) {
      errors.value.edit = error.response.data.message;
    } else {
      errors.value.edit = "ユーザー情報の更新に失敗しました。";
    }
    console.log("エラー内容:", errors.value);
  }
};

const changeTab = (stepNumber) => {
  step.value = stepNumber;
};

const validateForm = () => {
  errors.value = {};
  if (!form.name) {
    errors.value.name = "ユーザー名は必須です";
  }
  if (!form.email) {
    errors.value.email = "メールアドレスは必須です";
  }

  if (form.password && form.password.length < 8) {
    errors.value.password = "パスワードは８文字以上入力してください";
  }
  return Object.keys(errors.value).length === 0;
};

const backUserList = () => {
  router.push({ name: "list" });
};
</script>

<template>
  <div class="max-w-3xl mx-auto p-6">
    <div class="bg-white rounded-lg shadow-md p-6">
      <h2 class="text-2xl font-bold text-center mb-6">ユーザー編集</h2>

      <button @click="backUserList">一覧に戻る</button>

      <div class="flex gap-2 mb-6">
        <button
          type="button"
          @click="changeTab(1)"
          :class="[
            'px-4 py-2 rounded-md transition',
            step === 1
              ? 'bg-blue-600 text-white'
              : 'bg-gray-200 hover:bg-gray-300',
          ]"
        >
          基本情報
        </button>

        <button
          type="button"
          @click="changeTab(2)"
          :class="[
            'px-4 py-2 rounded-md transition',
            step === 2
              ? 'bg-blue-600 text-white'
              : 'bg-gray-200 hover:bg-gray-300',
          ]"
        >
          プロフィール
        </button>

        <button
          type="button"
          @click="changeTab(3)"
          :class="[
            'px-4 py-2 rounded-md transition',
            step === 3
              ? 'bg-blue-600 text-white'
              : 'bg-gray-200 hover:bg-gray-300',
          ]"
        >
          資格情報
        </button>
      </div>

      <p v-if="errors.edit" class="text-red-500">{{ errors.edit }}</p>

      <form @submit.prevent="editUser" class="space-y-6">
        <BaseInfo v-if="step === 1" v-model="form" :errors="errors" />

        <Profile v-if="step === 2" v-model="form" />

        <Qualification
          v-if="step === 3"
          v-model:form="form"
          v-model:errors="errors"
          :qualifications="qualifications"
        />

        <div class="flex justify-end">
          <button
            type="submit"
            class="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition"
          >
            更新
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped></style>
