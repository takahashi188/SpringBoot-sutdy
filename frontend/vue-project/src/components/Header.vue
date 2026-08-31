<script setup>
</script>

<template>
  <nav class="bg-white shadow-md">
    <div class="max-w-6xl mx-auto px-6 py-4 flex gap-4">
      <router-link
        :to="{ name: 'create' }"
        class="px-4 py-2 rounded-md transition"
        active-class="bg-blue-600 text-white"
      >
        ユーザー登録
      </router-link>

      <router-link
        :to="{ name: 'list' }"
        class="px-4 py-2 rounded-md transition"
        active-class="bg-blue-600 text-white"
      >
        ユーザー一覧
      </router-link>
    </div>
  </nav>
</template>

<style scoped>
</style>