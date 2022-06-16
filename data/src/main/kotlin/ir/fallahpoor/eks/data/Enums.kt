package ir.fallahpoor.eks.data

import androidx.annotation.StringRes

interface BaseEnum {
    val stringResId: Int
}

enum class NightMode(@StringRes v: Int) : BaseEnum {
    ON(R.string.night_mode_on),
    OFF(R.string.night_mode_off),
    AUTO(R.string.night_mode_auto);

    override val stringResId = v
}

enum class SortOrder(@StringRes v: Int) : BaseEnum {
    A_TO_Z(R.string.sort_order_a_to_z),
    Z_TO_A(R.string.sort_order_z_to_a),
    PINNED_FIRST(R.string.sort_order_pinned_first);

    override val stringResId = v
}
