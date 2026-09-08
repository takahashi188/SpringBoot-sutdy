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
        query: { message: "認証期限が切れました。再度ログインしてください。" },
      });
    }

    return Promise.reject(error);
  }
);

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      // トークンが存在する場合、AuthorizationヘッダーにBearerトークンを設定
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  }
);

export default api;