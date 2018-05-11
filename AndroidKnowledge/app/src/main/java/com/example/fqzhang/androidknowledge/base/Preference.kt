package com.example.fqzhang.androidknowledge.base

import android.content.Context
import android.content.SharedPreferences
import com.example.fqzhang.androidknowledge.constant.Constant
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by fqzhang on 2018/4/6.
 */
class Preference<T>(val name:String,val default:T): ReadWriteProperty<Any?,T> {
    companion object {
        private lateinit var preference:SharedPreferences
        fun setContext(context: Context){
            preference = context.getSharedPreferences(context.packageName + Constant.SHARED_NAME,Context.MODE_PRIVATE)
        }
        fun clear() {
            preference.edit().clear()
        }
    }
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val res: Any = when(default){
            is Long -> preference.getLong(name,default)
            is String -> preference.getString(name,default)
            is Int -> preference.getInt(name,default)
            is Boolean -> preference.getBoolean(name,default)
            is Float -> preference.getFloat(name,default)
            else -> {throw IllegalArgumentException("this type can not be saved into preference") }
        }
        return res as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        preference.edit().run {
            when(value) {
                is Long -> putLong(name, value)
                is String -> putString(name, value)
                is Int -> putInt(name, value)
                is Boolean -> putBoolean(name, value)
                is Float -> putFloat(name, value)
                else -> throw IllegalArgumentException("This type can be saved into Preferences")
            }
        }.apply()
    }


}