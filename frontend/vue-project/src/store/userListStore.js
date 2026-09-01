import { defineStore } from "pinia";

export const useUserListStore = defineStore('userListStore', {
    state: () => ({
        currentPage: 0,
        totalPage: 0,
        sort: "id,asc",
        searchKeyword: "",
        doSearch: false
    }),
});