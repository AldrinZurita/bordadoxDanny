package bo.bordadoxdanny.app.features.orders.data

interface SyncScheduler {
    fun scheduleOrderSync(userId: String)
    fun scheduleTransactionSync(userId: String)
}
