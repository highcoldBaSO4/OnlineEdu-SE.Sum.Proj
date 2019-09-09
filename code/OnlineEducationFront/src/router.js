import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/home',
      name: 'home',
      component: Home
    },
    {
      path: '/viewcourses',
      name: 'viewCourses',
    },
    {
      path: '/user',
      name: 'user',
      children: [
        {
          path: 'course',
          name: 'userCourse',

        },
        {
          path: 'info',
          name: 'userInfo'
        },
      ]
    },
    {
      path: '/course',
      name: 'course',
      children: [
        {
          path: 'student',
          name: 'courseStudent',
          children: [
            {
              path: 'welcome',
              name: 'courseStudentWelcome',
            },
            {
              path: 'home',
              name: 'courseStudentHome',
            },
            {
              path: 'chapters',
              name: 'courseStudentChapters',
            },
            {
              path: 'homework',
              name: 'courseStudentHomework',
            },
            {
              path: 'resources',
              name: 'courseStudentResources',
            },
            {
              path: 'grade',
              name: 'courseStudentGrade',
            }
          ]
        },
        {
          path: 'manager',
          name: 'courseManager',
          children: [

          ]
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('./views/Login.vue')
    },
    {},
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
    }
  ]
})
