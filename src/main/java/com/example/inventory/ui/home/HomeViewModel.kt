package com.example.inventory.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel lấy toàn bộ mặt hàng từ Room Database.
 * Nhận [ItemsRepository] qua constructor để truy cập dữ liệu.
 */
class HomeViewModel(itemsRepository: ItemsRepository) : ViewModel() {

    /**
     * Lắng nghe Flow từ Room — tự động cập nhật mỗi khi dữ liệu thay đổi.
     * stateIn() chuyển Cold Flow → StateFlow để UI collect được.
     */
    val homeUiState: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream()
            .map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val itemList: List<Item> = listOf())