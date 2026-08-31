<script setup>
const form = defineModel();

const props = defineProps(["errors"]);
</script>

<template>
  <div class="space-y-4">
    <div>
      <label class="block mb-1 font-medium">ユーザー名</label>
      <input
        type="text"
        v-model="form.name"
        class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:outline-none"
      >
      <p
        v-if="props.errors.name"
        class="mt-1 text-sm text-red-500"
      >
        {{ props.errors.name }}
      </p>
    </div>

    <div>
      <label class="block mb-1 font-medium">メールアドレス</label>
      <input
        type="email"
        v-model="form.email"
        class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:outline-none"
      >
      <p
        v-if="props.errors.email"
        class="mt-1 text-sm text-red-500"
      >
        {{ props.errors.email }}
      </p>
    </div>

    <div>
      <label class="block mb-1 font-medium">パスワード</label>
      <input
        type="password"
        v-model="form.password"
        class="w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:outline-none"
      >
      <p
        v-if="props.errors.password"
        class="mt-1 text-sm text-red-500"
      >
        {{ props.errors.password }}
      </p>
    </div>
  </div>
</template>


<style scoped>
</style>