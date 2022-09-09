package com.example.siatlet.hawkstorage

import android.content.Context
import com.example.siatlet.model.LoginResponse
import com.orhanobut.hawk.Hawk

class HawkStorage {
    fun setUser(user: LoginResponse){
        Hawk.put(USER_KEY, user)
    }

    fun getUser(): LoginResponse{
        return Hawk.get(USER_KEY)
    }


    fun isLogin(): Boolean{
        if (Hawk.contains(USER_KEY)){
            return true
        }
        return false
    }

    fun deleteAll(){
        Hawk.deleteAll()
    }

    companion object{
        private const val USER_KEY = "user_key"
        private val hawkStorage = HawkStorage()

        fun instance(context: Context?): HawkStorage{
            Hawk.init(context).build()
            return hawkStorage
        }
    }
}