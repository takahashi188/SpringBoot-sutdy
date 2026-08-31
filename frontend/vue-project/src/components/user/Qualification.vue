<script setup>
import { ref, reactive } from "vue";

const props = defineProps(["qualifications"]);
const form = defineModel("form");
const errors = defineModel("errors");

const qualificationForm = reactive({
  qualificationId: null,
  acquisitionDate: null,
});

const addQualification = () => {
  delete errors.value.qualificationId;
  delete errors.value.acquisitionDate;

  if (!qualificationForm.qualificationId) {
    errors.value.qualificationId = "資格名を選択してください";
  }

  if (!qualificationForm.acquisitionDate) {
    errors.value.acquisitionDate = "取得日を入力してください";
  }

  if (errors.value.qualificationId || errors.value.acquisitionDate) {
    return;
  }

  const exists = form.value.qualifications.some(
    (qualification) =>
      qualification.qualificationId === qualificationForm.qualificationId,
  );

  if (exists) {
    errors.value.qualificationId = "同じ資格は追加できません";
    return;
  }

  form.value.qualifications.push({
    qualificationId: qualificationForm.qualificationId,
    acquisitionDate: qualificationForm.acquisitionDate,
  });

  form.qualificationId = null;
  form.acquisitionDate = null;
};

// IDで消す
const removeQualification = (quqlificationId) => {
  form.value.qualifications = form.value.qualifications.filter(
    (qualification) => qualification.qualificationId != quqlificationId,
  );
};
</script>

<template>
  <div class="space-y-4">

    <div>
      <label class="block mb-1 font-medium">
        資格名
      </label>

      <select
        v-model="qualificationForm.qualificationId"
        class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:outline-none"
      >
        <option :value="null">
          選択してください
        </option>

        <option
          v-for="qualification in props.qualifications"
          :key="qualification.qualificationId"
          :value="qualification.qualificationId"
        >
          {{ qualification.qualificationName }}
        </option>
      </select>

      <p
        v-if="errors.qualificationId"
        class="mt-1 text-sm text-red-500"
      >
        {{ errors.qualificationId }}
      </p>
    </div>

    <div>
      <label class="block mb-1 font-medium">
        取得日
      </label>

      <input
        type="date"
        v-model="qualificationForm.acquisitionDate"
        class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:outline-none"
      >

      <p
        v-if="errors.acquisitionDate"
        class="mt-1 text-sm text-red-500"
      >
        {{ errors.acquisitionDate }}
      </p>
    </div>

    <button
      type="button"
      @click="addQualification"
      class="rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
    >
      資格追加
    </button>

    <div class="space-y-2">
      <div
        v-for="qualification in form.qualifications"
        :key="qualification.qualificationId"
        class="flex items-center justify-between rounded-lg border border-gray-200 bg-gray-50 px-4 py-3"
      >
        <div>
          <p class="font-medium">
            {{
              props.qualifications.find(
                q => q.qualificationId === qualification.qualificationId
              )?.qualificationName
            }}
          </p>

          <p class="text-sm text-gray-500">
            {{ qualification.acquisitionDate }}
          </p>
        </div>

        <button
          type="button"
          @click="removeQualification(qualification.qualificationId)"
          class="rounded bg-red-500 px-3 py-1 text-white hover:bg-red-600"
        >
          ×
        </button>
      </div>
    </div>

  </div>
</template>

<style scoped></style>
