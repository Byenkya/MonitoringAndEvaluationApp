package com.example.monitoringandevaluationapp.presentation.ui.PdmInfo

import PdmAssetsTab
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.presentation.ViewModel.GroupViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.LocationViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.PDMViewModel

@Composable
fun PdmScreen(navController: NavController, pdmViewModel: PDMViewModel, groupViewModel: GroupViewModel) {
    val tabs = listOf("Assets", "Beneficiary", "Enterprise", "Group")

    PdmInfoTabView(tabs = tabs, navController = navController) { selectedTab ->
        when (selectedTab) {
            "Assets" -> {
                PdmAssetsTab(pdmViewModel, navController, groupViewModel)
            }
            "Beneficiary" -> {
                BeneficiaryContentTab(navController, groupViewModel)
            }
            "Enterprise" -> {
                EnterpriseTabViewContent(navController, groupViewModel)
            }
            "Group" -> {
                GroupsTabViewContent(navController, groupViewModel)
            }
        }
    }
}
