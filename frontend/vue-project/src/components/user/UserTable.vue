<script setup>
defineProps({
  users: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(["goUserShow"]);

const showUser = (id) => {
  emit("goUserShow", id);
}
</script>

<template>
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

          <td @click="showUser(user.id)" class="px-4 py-3">
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
</template>

<style scoped>
</style>