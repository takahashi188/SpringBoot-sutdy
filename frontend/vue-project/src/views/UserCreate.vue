<script setup>
import BaseInfo from "@/components/user/BaseInfo.vue";
import Profile from "@/components/user/Profile.vue";
import Qualification from "@/components/user/Qualification.vue";
import { ref, reactive, onMounted } from "vue";
import api from "@/plugins/axios";

const step = ref(1);
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
const qualifications = ref([]);
const errors = ref({});

const getQualifications = async () => {
  try {
    const response = await api.get("/api/qualification-master");

    qualifications.value = response.data;
  } catch (error) {
    console.log(error);
  }
};

onMounted(getQualifications);

// 引数で共通化
const changeTab = (stepNumber) => {
    step.value = stepNumber;
}

const validateForm = () => {
  errors.value = {};

  if (!form.name) {
    errors.value.name = "名前は必須です";
  }

  if (!form.email) {
    errors.value.email = "メールアドレスは必須です";
  }

  if (!form.password) {
    errors.value.password = "パスワードは必須です";
  } else if (form.password.length < 8) {
    errors.value.password = "パスワードは８文字以上入力してください";
  }

  return Object.keys(errors.value).length === 0;
};

const sendCreateRequestApi = async () => {
  if (!validateForm()) return;

  const request = {
    ...form,
    // 条件の簡略化
    profile:
      Object.values(form.profile).every(value => !value) ? null : form.profile
  };

  try {
    const response = await api.post("/api/users", request);

    console.log(response.data);

    form.name = "";
    form.email = "";
    form.password = "";
    form.profile.nickname = "";
    form.profile.birthday = null;
    form.qualifications = [];

    step.value = 1;
  } catch (error) {
    if(error.response?.data?.errors) {
        errors.value = error.response.data.errors;
    }
  }
};
</script>

<template>
  <div class="max-w-3xl mx-auto p-6">
    <div class="bg-white rounded-lg shadow-md p-6">

      <h2 class="text-2xl font-bold text-center mb-6">
        ユーザー登録
      </h2>

      <div class="flex gap-2 mb-6">
        <button
          type="button"
          @click="changeTab(1)"
          :class="[
            'px-4 py-2 rounded-md transition',
            step === 1
              ? 'bg-blue-600 text-white'
              : 'bg-gray-200 hover:bg-gray-300'
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
              : 'bg-gray-200 hover:bg-gray-300'
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
              : 'bg-gray-200 hover:bg-gray-300'
          ]"
        >
          資格情報
        </button>
      </div>

      <form
        @submit.prevent="sendCreateRequestApi"
        class="space-y-6"
      >
        <BaseInfo
          v-if="step === 1"
          v-model="form"
          :errors="errors"
        />

        <Profile
          v-if="step === 2"
          v-model="form"
        />

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
            登録
          </button>
        </div>
      </form>
    </div>
  </div>
</template>


<style scoped></style>
