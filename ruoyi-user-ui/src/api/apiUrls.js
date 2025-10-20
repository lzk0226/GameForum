// src/api/apiUrls.js
const BASE_URL = import.meta.env.VITE_BASE_URL//生产环境
//const BASE_URL = "http://110.41.1.63:8080"//部署环境

export const API_URLS = {
    getBASEURL: () => `${BASE_URL}`,
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
    getSectionById: (sectionId) => `${BASE_URL}/user/section/${sectionId}`,
    getPostsBySection: (sectionId) => `${BASE_URL}/user/post/section/${sectionId}`,
    getHotPosts: () => `${BASE_URL}/user/post/hot`,
    getTopPosts: () => `${BASE_URL}/user/post/top`,
    getAnnouncementsList: () => `${BASE_URL}/user/announcements/list`,
    getLoginRegisterPage: () => '/loginregister',
    getNewPostPage: () => '/newpost',
    getProfileContainerPage: () => '/profileContainer',
    getHotSections: (limit) => `${BASE_URL}/user/section/hot?limit=${limit}`,
    getAllSectionList: () => `${BASE_URL}/user/section/list`,
    getHotGameList: (limit = 20) => `${BASE_URL}/user/game/hot?limit=${limit}`,
    getGameListByType: (typeId) => `${BASE_URL}/user/game/type/${typeId}`,
    searchGameByName: (name) => `${BASE_URL}/user/game/search?name=${encodeURIComponent(name)}`,
    getGameIdBySectionId: (sectionId) => `${BASE_URL}/user/section/gameId/${sectionId}`,
    getPostList: () => `${BASE_URL}/user/post/list`,
    getMyPosts: () => `${BASE_URL}/user/post/my`,
    getPostsByUser: (userId) => `${BASE_URL}/user/post/user/${userId}`,
    searchPosts: () => `${BASE_URL}/user/post/search`,
    incrementViewCount: (postId) => `${BASE_URL}/user/post/${postId}`,
    deletePostLike: (postId) => `${BASE_URL}/user/post/like/${postId}`,
    createPostLike: (postId) => `${BASE_URL}/user/post/like/${postId}`,
    searchGamesByName: (name) => `${BASE_URL}/user/game/search?name=${encodeURIComponent(name)}`,
    searchPostsByTitle: (title) => `${BASE_URL}/user/post/search?title=${encodeURIComponent(title)}`,
    searchSectionsByName: (name) => `${BASE_URL}/user/section/search?name=${encodeURIComponent(name)}`,
    searchGameTypesByName: (name) => `${BASE_URL}/user/gameType/search?name=${encodeURIComponent(name)}`,
    getGamePage: (gameId) => `/game?gameId=${gameId}`,
    getPostDetailPage: (postId) => `/postDetail/${postId}`,
    getSectionPage: (sectionId) => `/section?sectionId=${sectionId}`,
    getGameTypePage: (typeId) => `/gamepage?typeId=${typeId}`,
    uploadAvatar: () => `${BASE_URL}/user/upload/save-avatar`,
    updateProfile: () => `${BASE_URL}/user/profile/update`,
    updatePassword: () => `${BASE_URL}/user/profile/updatePassword`,
    deactivateAccount: (userId) => `${BASE_URL}/user/profile/deactivate/${userId}`,
    getPhotos: () => `${BASE_URL}/user/public/`,
    getPostPhotos: () => `${BASE_URL}/user/public/`,
    getGameIcon: () => `${BASE_URL}/user/public/`,
    getSectionIcon: () => `${BASE_URL}/user/public/`,
    getGamePhoto: () => `${BASE_URL}/user/public/`,
    // 获取帖子收藏数量
    getPostFavoriteCount: (postId) => `${BASE_URL}/user/post/favorite/count/post/${postId}`,
    // APK下载相关
    getApkDownloadUrl() {
        return `${BASE_URL}/user/download/apk`;
    },
    // 帖子收藏相关
    addPostFavorite: (postId) => `${BASE_URL}/user/post/favorite/${postId}`,
    removePostFavorite: (postId) => `${BASE_URL}/user/post/favorite/${postId}`,
    checkPostFavoriteStatus: (postId) => `${BASE_URL}/user/post/favorite/check/${postId}`,
    getMyFavorites: () => `${BASE_URL}/user/post/favorite/my`,

    // 用户关注相关
    followUser: (userId) => `${BASE_URL}/user/follow/${userId}`,
    unfollowUser: (userId) => `${BASE_URL}/user/follow/${userId}`,
    checkFollowStatus: (userId) => `${BASE_URL}/user/follow/check/${userId}`,
    getMyFollowing: () => `${BASE_URL}/user/follow/following/my`,
    getMyFollowers: () => `${BASE_URL}/user/follow/follower/my`,
}

export default API_URLS