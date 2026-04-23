package bo.bordadoxdanny.app.workers

interface WorkerScheduler {
    fun testWorkImmediately()
    fun scheduleLogUpload()
    fun syncConfigNow()
}
