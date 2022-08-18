package com.ssn.app.data.preference

import java.lang.reflect.Modifier

object SharedPrefKey {
    const val SESSION_IS_LOGIN = "session_is_login"

    // User
    const val SESSION_USER_ID = "session_user_id"
    const val SESSION_USERNAME = "session_username"
    const val SESSION_USER_FULL_NAME = "session_full_name"
    const val SESSION_USER_EMAIL = "session_user_email"
    const val SESSION_USER_PHONE = "session_user_phone"
    const val SESSION_USER_ADDRESS = "session_user_address"
    const val SESSION_USER_PHOTO = "session_user_photo"
    const val SESSION_USER_TOKEN = "session_user_token"

    fun all(): List<String> = SharedPrefKey::class.java.declaredFields
        .filter { field ->
            field.type == String::class.java && isPublicConstantVariable(field.modifiers)
        }
        .map { field -> field.get(String) as String }
        .filterNot(String::isNullOrEmpty)

    private fun isPublicConstantVariable(fieldModifier: Int): Boolean = fieldModifier.run {
        Modifier.isPublic(this) && Modifier.isStatic(this) && Modifier.isFinal(this)
    }
}
