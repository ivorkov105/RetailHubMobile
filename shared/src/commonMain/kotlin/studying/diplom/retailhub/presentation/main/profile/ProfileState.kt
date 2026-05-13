package studying.diplom.retailhub.presentation.main.profile

import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.models.shop.StoreModel
import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.models.analytics.AnalyticsDashboardModel
import studying.diplom.retailhub.domain.models.analytics.Period
import studying.diplom.retailhub.data.entities.shift.ShiftEntity

data class ProfileState(
    val user: UserModel? = null,
    val store: StoreModel? = null,
    val allDepartments: List<DepartmentModel> = emptyList(),
    val dashboard: AnalyticsDashboardModel? = null,
    val analyticsError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val shifts: List<ShiftEntity> = emptyList(),
    val dateFrom: String? = null,
    val dateTo: String? = null,
    val isShiftsLoading: Boolean = false,
    val selectedPeriod: Period = Period.TODAY
)
