<script setup>
import Header from "./components/Header.vue";
</script>

<template>
  <div class="min-h-screen bg-gray-100 flex flex-col">
    <Header />

    <main class="flex-1 max-w-6xl mx-auto w-full p-6 overflow-hidden">
      <router-view />
    </main>
  </div>
</template>
<!-- <template>
  <div class="min-h-screen bg-gray-100 flex flex-col">
    <Header />

    <main class="flex-1 max-w-6xl mx-auto w-full p-6 overflow-hidden">
  
  <RouterView v-slot="{ Component, route }">
    <KeepAlive>
      <component :is="Component" v-if="route.meta.Keepalive" />
    </KeepAlive>

    <component :is="Component" v-if="!route.meta.Keepalive" />
  </RouterView>
    </main>
  </div>
</template> -->

<style scoped></style>
