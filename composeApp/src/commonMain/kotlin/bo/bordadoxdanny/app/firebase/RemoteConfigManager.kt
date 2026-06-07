package bo.bordadoxdanny.app.firebase

interface RemoteConfigManager {
    fun getShowAccountsReceivable(): Boolean
    fun getMaxAccountsReceivable(): Long
}
