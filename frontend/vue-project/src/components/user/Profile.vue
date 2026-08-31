<script setup>
const form = defineModel();
</script>

<template>
  <div class="space-y-4">
    <div>
      <label class="block mb-1 font-medium">
        ニックネーム
      </label>
      <input
        type="text"
        v-model="form.profile.nickname"
        class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:outline-none"
      >
    </div>

    <div>
      <label class="block mb-1 font-medium">
        誕生日
      </label>
      <input
        type="date"
        v-model="form.profile.birthday"
        class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:outline-none"
      >
    </div>
  </div>
</template>

<style scoped>
</style>