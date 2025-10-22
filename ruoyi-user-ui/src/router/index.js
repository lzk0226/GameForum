import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import LoginRegister from '@/views/LoginRegister.vue'
import ProfileContainer from "@/components/user/details/Profile-container.vue"
import PostDetail from "@/components/user/details/Post-detail.vue"
import newPost from "@/components/user/details/Newpost.vue"
import Search from "@/components/user/pagination/Search.vue"
import GamePage from "@/components/user/pagination/Game-page.vue"
import Game from "@/components/user/details/Game.vue"
import SectionPage from "@/components/user/pagination/Section-page.vue"
import Section from "@/components/user/details/Section.vue"
import Profile from "@/components/user/details/Others.vue";

const routes = [
    //主页
    {
        path: '/',
        name: 'Home',
        component: MainLayout
    },
    //登录注册页
    {
        path: '/loginregister',
        name: 'LoginRegister',
        component: LoginRegister
    },
    //用户详情页
    {
        path: '/profileContainer',
        name: 'profileContainer',
        component: ProfileContainer
    },
    //帖子详情页
    {
        path: '/postDetail/:id',
        name: 'postDetail',
        component: PostDetail
    },
    //发帖子页
    {
        path: "/newpost",
        name: 'newPost',
        component: newPost
    },
    //搜索页面
    {
        path: "/search",
        name: 'Search',
        component: Search
    },
    {
        path: "/gamepage",
        name: 'GamePage',
        component: GamePage
    },
    {
        path: "/game",
        name: 'Game',
        component: Game
    },
    {
        path: "/sectionpage",
        name: 'SectionPage',
        component: SectionPage
    },
    {
        path: "/section",
        name: 'Section',
        component: Section
    },
    {
        path: '/profile/:userId',
        name: 'Profile',
        component: Profile
    }
    /*
    {
        path: '/shopping',
        name: 'Shopping',
        component: () => import('../views/Shopping.vue')
    }
    */
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router