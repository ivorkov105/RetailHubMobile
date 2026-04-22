package studying.diplom.retailhub.presentation.main.qr.create_qr

import studying.diplom.retailhub.domain.models.shop.DepartmentModel

data class CreateQrState(
    val departments: List<DepartmentModel> = emptyList(),
    val selectedDepartmentId: String = "",
    val label: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
