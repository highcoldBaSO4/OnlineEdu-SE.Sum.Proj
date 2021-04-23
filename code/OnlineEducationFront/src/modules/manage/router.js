import Vue from 'vue'
import VueRouter from 'vue-router'
import ManageUser from "./views/ManageUser.vue"
import ManageCourse from "./views/ManageCourse"
import ManageCoursePrototype from "./views/ManageCoursePrototype"

Vue.use(VueRouter)

const routes = [
    {
        path: '/ManageUser',
        name: 'ManageUser',
        component: ManageUser
    },
    {
        path: '/ManageCourse',
        name: 'ManageCourse',
        component: ManageCourse
    },
    {
        path: '/ManageCoursePrototype',
        name: 'ManageCoursePrototype',
        component: ManageCoursePrototype
    },
]

export default new VueRouter({
    routes: routes
})
