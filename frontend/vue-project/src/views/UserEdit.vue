<script setup>
import { useRoute, useRouter } from "vue-router";
import api from "@/plugins/axios";
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
  password: null,
  profile: {
    nickname: "",
    birthday: null,
  },
  qualifications: [],
});
const step = ref(1);
const errors = ref({});

// 引数と戻り値を追加
const getUser = async (id) => {
  const result = {
    data: null,
    error: "",
  };

  try {
    const response = await api.get(`/api/users/${id}`);
    result.data = response.data;
  } catch (error) {
    console.log(error);

    if (error.response?.data?.message) {
      result.error = error.response.data.message;
    } else {
      result.error = "ユーザー情報の取得に失敗しました。";
    }
  } finally {
    return result;
  }
};

// 引数と戻り値を追加
const getQualifications = async () => {
  const result = {
    data: [],
    error: "",
  };

  try {
    const response = await api.get("/api/qualification-master");

    result.data = response.data;
    return result;
  } catch (error) {
    console.log(error);

    if (error.response?.data?.message) {
      result.error = error.response.data.message;
    } else {
      result.error = "資格マスタの取得に失敗しました。";
    }
  } finally {
    return result;
  }
};

onMounted(async () => {
  errors.value = {}; // エラーをリセット

  const userResult = await getUser(id);
  const qualificationsResult = await getQualifications();

  if (userResult.data) {
    user.value = userResult.data;
  } else {
    errors.value.edit = userResult.error;
  }

  if (qualificationsResult.data) {
    qualifications.value = qualificationsResult.data;
  } else {
    errors.value.edit = qualificationsResult.error;
  }

  if (!user.value) {
    return;
  }
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

    const response = await api.put(
      `/api/users/${id}`,
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

      <div class="flex justify-end mb-6">
        <button
          @click="backUserList"
          class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition"
        >
          一覧に戻る
        </button>
      </div>

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
