package com.flxrs.medplan.common.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.flxrs.medplan.common.main.MedPlanMain
import com.flxrs.medplan.common.main.MedPlanMain.Model
import com.flxrs.medplan.common.main.MedPlanMainItem
import com.flxrs.medplan.common.main.Usage
import com.flxrs.medplan.common.main.UsageTime

@Preview
@Composable
fun MedPlanMainContentPreview() {
    MedPlanMainContent(MedPlanMainPreview())
}

class MedPlanMainPreview : MedPlanMain {
    override val models: Value<Model> =
        MutableValue(
            Model(
                profile = "Opa",
                items = listOf(
                    MedPlanMainItem(
                        id = 1L,
                        profileId = 0L,
                        name = "Arznei 1",
                        usages = listOf(
                            Usage(
                                time = UsageTime.LUNCH,
                                amount = "1",
                            )
                        )
                    )
                )
            )
        )

    override fun onItemNameChanged(id: Long, name: String) {}

    override fun onItemUsageChanged(id: Long, usage: Usage) {}

    override fun onItemDeleteClicked(id: Long) {}

    override fun onAddItemClicked(name: String) {}

    override fun onBackClicked() {}

    override fun onPrintClicked() {}
}