package com.example.monitoringandevaluationapp.presentation.ui.PdmInfo

import PdmAssetsTab
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.monitoringandevaluationapp.presentation.ViewModel.GroupViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.LocationViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.PDMViewModel
import com.example.monitoringandevaluationapp.presentation.ViewModel.PdmProjectLocalViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PdmScreen(
    navController: NavController,
    pdmViewModel: PDMViewModel,
    groupViewModel: GroupViewModel,
    pdmProjectLocalViewModel: PdmProjectLocalViewModel
) {
//    val tabs = listOf("Assets", "Beneficiary", "Enterprise", "Project","Group")

    val tabs = listOf("Assets", "Beneficiary", "Enterprise", "Project", "Group")

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
            "Project" -> {
                PdmProjectTabViewContent(navController, groupViewModel, pdmProjectLocalViewModel)
            }
            "Group" -> {
                GroupsTabViewContent(navController, groupViewModel)
            }
        }
    }
}
