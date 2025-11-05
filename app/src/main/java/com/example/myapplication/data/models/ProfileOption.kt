package com.example.myapplication.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes // <-- 1. IMPORTA ESTO
import com.example.myapplication.R

/**
 * Define la estructura de datos (el "contrato")
 * para una opción del menú de perfil.
 */
data class ProfileOption(
    val id: String,
    @StringRes val titleRes: Int, // <-- 2. CAMBIADO DE 'title: String'
    @DrawableRes val iconRes: Int
)

/**
 * Datos estáticos (hardcodeados) para la pantalla de Perfil.
 */
internal val profileScreenOptions = listOf(
    // --- 3. CAMBIADO A REFERENCIAS DE STRING ---
    ProfileOption(
        id = "edit_profile",
        titleRes = R.string.profile_menu_edit,
        iconRes = R.drawable.ic_profile_user
    ),
    ProfileOption(
        id = "security",
        titleRes = R.string.profile_menu_security,
        iconRes = R.drawable.ic_profile_security
    ),
    ProfileOption(
        id = "setting",
        titleRes = R.string.profile_menu_setting,
        iconRes = R.drawable.ic_profile_setting
    ),
    ProfileOption(
        id = "help",
        titleRes = R.string.profile_menu_help,
        iconRes = R.drawable.ic_profile_help
    ),
    ProfileOption(
        id = "logout",
        titleRes = R.string.profile_menu_logout,
        iconRes = R.drawable.ic_profile_logout
    )
)