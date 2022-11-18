package com.example.notesapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.example.notesapp.utils.Constants.PREFS_TOKEN_FILE
import com.example.notesapp.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManger@Inject constructor(@ApplicationContext context : Context) {

    private val pref : SharedPreferences = context.getSharedPreferences(PREFS_TOKEN_FILE,
        MODE_PRIVATE
    )

    fun saveToken(token : String){
        val editor : Editor  = pref.edit()
        editor.putString(USER_TOKEN,null)
        editor.apply()
    }

    fun getToken(): String? {
      return  pref.getString(USER_TOKEN , null)
    }




}