import axios from "axios";
import router from "@/router";
import { useAuthStore } from "@/store/authStore";

const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true
});

api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {

    if (error.response?.status === 401) {
      const authStore = useAuthStore();

      authStore.logout();

      router.push({
        name: "login",
        query: { message: "セッションが切れました。再度ログインしてください。" },
      });
    }

    return Promise.reject(error);
  }
);

export default api;