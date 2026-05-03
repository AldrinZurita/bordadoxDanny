package bo.bordadoxdanny.app.core.daemon.repository

import bo.bordadoxdanny.app.core.daemon.model.HeartbeatModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseHeartbeatRepository : HeartbeatRepository {
    private val database by lazy { FirebaseDatabase.getInstance().getReference("heartbeats") }

    override suspend fun writeHeartbeat(heartbeat: HeartbeatModel) {
        val updates = mapOf(
            "deviceId" to heartbeat.deviceId,
            "timestampMillis" to heartbeat.timestampMillis,
            "status" to heartbeat.status
        )
        database.child(heartbeat.deviceId).setValue(updates).await()
    }

    override fun observeHeartbeat(deviceId: String): Flow<HeartbeatModel?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val heartbeat = if (snapshot.exists()) {
                    HeartbeatModel(
                        deviceId = snapshot.child("deviceId").getValue(String::class.java) ?: "",
                        timestampMillis = snapshot.child("timestampMillis").getValue(Long::class.java) ?: 0L,
                        status = snapshot.child("status").getValue(String::class.java) ?: ""
                    )
                } else null
                trySend(heartbeat)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(null)
            }
        }
        database.child(deviceId).addValueEventListener(listener)
        awaitClose { database.child(deviceId).removeEventListener(listener) }
    }
}
