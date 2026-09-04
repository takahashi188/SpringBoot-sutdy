import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    isLoggedIn: localStorage.getItem("isLoggedIn") === "true",
    userName: localStorage.getItem("userName") || "",
    userId: localStorage.getItem("userId") || null,
  }),

  actions: {
    login(userName, userId) {
      this.isLoggedIn = true;
      this.userName = userName;
      this.userId = userId;
      localStorage.setItem("isLoggedIn", "true");
      localStorage.setItem("userName", userName);
      localStorage.setItem("userId", userId);
    },

    logout() {
      this.isLoggedIn = false;
      this.userName = "";
      this.userId = null;
      localStorage.removeItem("isLoggedIn");
      localStorage.removeItem("userName");
      localStorage.removeItem("userId");
    },
  },
});