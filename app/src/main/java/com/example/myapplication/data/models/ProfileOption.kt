package com.example.myapplication.data.models

import androidx.annotation.DrawableRes
import com.example.myapplication.R

/**
 * Define la estructura de datos (el "contrato")
 * para una opción del menú de perfil.
 * Es pública para que los componentes la puedan usar.
 */
data class ProfileOption(
    val id: String,
    val title: String,
    @DrawableRes val iconRes: Int
)

/**
 * Datos estáticos (hardcodeados) para la pantalla de Perfil.
 *
 * Es 'internal' (visible solo dentro de este módulo de la app)
 * y es importado directamente por 'ProfileScreen'.
 */
internal val profileScreenOptions = listOf(
    ProfileOption(
        id = "edit_profile",
        title = "Edit Profile",
        iconRes = R.drawable.ic_profile_user
    ),
    ProfileOption(
        id = "security",
        title = "Security",
        iconRes = R.drawable.ic_profile_security
    ),
    ProfileOption(
        id = "setting",
        title = "Setting",
        iconRes = R.drawable.ic_profile_setting
    ),
    ProfileOption(
        id = "help",
        title = "Help",
        iconRes = R.drawable.ic_profile_help
    ),
    ProfileOption(
        id = "logout",
        title = "Logout",
        iconRes = R.drawable.ic_profile_logout
    )
)