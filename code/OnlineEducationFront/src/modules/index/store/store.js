import Vue from 'vue'
import Vuex from 'vuex'
import userModule from './userModule.js'
import courseModule from './courseModule.js'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        user: userModule,
        course: courseModule
    }
})