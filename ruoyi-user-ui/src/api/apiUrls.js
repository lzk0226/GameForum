// src/api/apiUrls.js

const BASE_URL = 'http://localhost:8080'

export const API_URLS = {
    getGameDetail: (gameId) => `${BASE_URL}/user/game/${gameId}`,
    getAllGameTypes: () => `${BASE_URL}/user/gameType/all`,
    getAllGames: () => `${BASE_URL}/user/game/list`,
    getAllSections: () => `${BASE_URL}/user/section/all`,
    createPost: () => `${BASE_URL}/user/post`,
    uploadPostImage: () => `${BASE_URL}/user/upload/save-post-image`,
    getUserProfile: (userId) => `${BASE_URL}/user/profile/${userId}`,
    getPostDetail: (postId) => `${BASE_URL}/user/post/${postId}`,
    getPostComments: (postId) => `${BASE_URL}/user/comment/post/${postId}`,
    checkPostLikeStatus: (postId) => `${BASE_URL}/user/post/like/check/${postId}`,
    togglePostLike: (postId) => `${BASE_URL}/user/post/like/${postId}`,
    createComment: () => `${BASE_URL}/user/comment`,
    toggleCommentLike: (commentId) => `${BASE_URL}/user/comment/like/${commentId}`,
    deleteComment: (commentId) => `${BASE_URL}/user/comment/${commentId}`,
    updateProfile: () => `${BASE_URL}/user/profile/update`,
    updatePassword: () => `${BASE_URL}/user/profile/updatePassword`,
    deactivateAccount: (userId) => `${BASE_URL}/user/profile/deactivate/${userId}`,
    saveAvatar: () => `${BASE_URL}/user/upload/save-avatar`,
    getSectionById: (sectionId) => `${BASE_URL}/user/section/${sectionId}`,
    getPostsBySection: (sectionId) => `${BASE_URL}/user/post/section/${sectionId}`,
    getHotPosts: () => `${BASE_URL}/user/post/hot`,
    getTopPosts: () => `${BASE_URL}/user/post/top`,
    getAnnouncementsList: () => `${BASE_URL}/user/announcements/list`,
    getSearchPage: (query) => `/search?q=${query}`,
    getLoginRegisterPage: () => '/loginregister',
    getNewPostPage: () => '/newpost',
    getProfileContainerPage: () => '/profileContainer',
    getAllPosts: () => `${BASE_URL}/user/post/list`,
    getMyPosts: () => `${BASE_URL}/user/post/my`,
    searchPosts: (query) => `${BASE_URL}/user/post/search?title=${encodeURIComponent(query)}`,
    getUserPosts: (userId) => `${BASE_URL}/user/post/user/${userId}`,
    getHotSections: (limit) => `${BASE_URL}/user/section/hot?limit=${limit}`,
    getAllSectionList: () => `${BASE_URL}/user/section/list`,
    // 从 Game-page.vue 抽离的接口
    getHotGameList: (limit = 20) => `${BASE_URL}/user/game/hot?limit=${limit}`,
    getGameListByType: (typeId) => `${BASE_URL}/user/game/type/${typeId}`,
    searchGameByName: (name) => `${BASE_URL}/user/game/search?name=${encodeURIComponent(name)}`,
    searchSections: (query) => `${BASE_URL}/user/section/search?name=${encodeURIComponent(query)}`,
    searchGameTypes: (query) => `${BASE_URL}/user/gameType/search?name=${encodeURIComponent(query)}`
}

export default API_URLS