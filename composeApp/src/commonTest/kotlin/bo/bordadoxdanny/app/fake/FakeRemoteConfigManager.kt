package bo.bordadoxdanny.app.fake

import bo.bordadoxdanny.app.firebase.RemoteConfigManager

class FakeRemoteConfigManager : RemoteConfigManager {
    private var showAR = true
    private var maxAR = 10L

    override fun getShowAccountsReceivable(): Boolean = showAR
    override fun getMaxAccountsReceivable(): Long = maxAR

    fun setShowAR(value: Boolean) {
        showAR = value
    }

    fun setMaxAR(value: Long) {
        maxAR = value
    }
}
