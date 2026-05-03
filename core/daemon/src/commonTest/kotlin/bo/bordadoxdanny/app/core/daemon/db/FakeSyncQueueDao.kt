package bo.bordadoxdanny.app.core.daemon.db

class FakeSyncQueueDao : SyncQueueDao {
    var shouldThrow = false
    val items = mutableListOf<SyncQueueEntity>()

    override suspend fun insertAll(items: List<SyncQueueEntity>) {
        this.items.addAll(items)
    }

    override suspend fun getByStatus(status: String): List<SyncQueueEntity> {
        return items.filter { it.status == status }
    }

    override suspend fun updateStatus(id: Int, status: String) {
        items.find { it.id == id }?.let {
            // Since SyncQueueEntity is likely a data class, we'd replace it or use a mutable property if possible.
            // For a fake, let's assume we can replace it in the list.
            val index = items.indexOf(it)
            items[index] = it.copy(status = status)
        }
    }

    override suspend fun deleteCompleted() {
        items.removeAll { it.status == "COMPLETED" }
    }

    override suspend fun resetFailedToRetry() {
        if (shouldThrow) throw Exception("DB Error")
        items.forEachIndexed { index, entity ->
            if (entity.status == "FAILED") {
                items[index] = entity.copy(status = "PENDING", retryCount = 0)
            }
        }
    }
}
