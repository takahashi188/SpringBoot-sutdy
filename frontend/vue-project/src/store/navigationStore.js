import { defineStore } from "pinia";

export const useNavigationStore = defineStore("navigationStore", {
  state: () => ({
    previousRoute: null,
    currentRoute: null,
  }),

  actions: {
    setPreviousRoute(routeName) {
      this.previousRoute = routeName;
    },

    setCurrentRoute(routeName) {
      this.currentRoute = routeName;
    },

    clearPreviousRoute() {
      this.previousRoute = null;
    },

    clearCurrentRoute() {
      this.currentRoute = null;
    },
  },
});