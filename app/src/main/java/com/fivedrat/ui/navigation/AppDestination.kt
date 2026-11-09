package com.fivedrat.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.fivedrat.R

/**
 * Destinos raiz da navegação — todos com telas reais (Início, Missões,
 * Laboratório, Perfil).
 */
enum class AppDestination(
    val route: String,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    Home("home", R.string.nav_home, Icons.Default.Home),
    Missions("missions", R.string.nav_missions, Icons.Default.Search),
    Lab("lab", R.string.nav_lab, Icons.Default.Build),
    Profile("profile", R.string.nav_profile, Icons.Default.Person),
}
