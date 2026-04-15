package bo.bordadoxdanny.app

import android.content.Context

object ContextProvider {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    fun getContext(): Context = context ?: throw IllegalStateException("Context not initialized")
}
