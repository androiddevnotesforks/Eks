package ir.fallahpoor.eks.data

import androidx.annotation.StringRes

interface BaseEnum {
    val stringResId: Int
}

enum class SortOrder(@StringRes v: Int) : BaseEnum {
    A_TO_Z(R.string.sort_order_a_to_z),
    Z_TO_A(R.string.sort_order_z_to_a),
    PINNED_FIRST(R.string.sort_order_pinned_first);

    override val stringResId = v
}
