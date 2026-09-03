import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    isLoggedIn: localStorage.getItem("isLoggedIn") === "true",
    userName: localStorage.getItem("userName") || "",
  }),

  actions: {
    login(userName) {
      this.isLoggedIn = true;
      this.userName = userName;
      localStorage.setItem("isLoggedIn", "true");
      localStorage.setItem("userName", userName);
    },

    logout() {
      this.isLoggedIn = false;
      this.userName = "";
      localStorage.removeItem("isLoggedIn");
      localStorage.removeItem("userName");
    },
  },
});