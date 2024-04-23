package com.optic.uberclonedriverkotlin.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.optic.uberclonedriverkotlin.activities.MapActivity
import com.optic.uberclonedriverkotlin.activities.MapTripActivity
import com.optic.uberclonedriverkotlin.providers.AuthProvider
import com.optic.uberclonedriverkotlin.providers.BookingProvider
import com.optic.uberclonedriverkotlin.providers.GeoProvider

class CancelReceiver: BroadcastReceiver() {

    private val bookingProvider = BookingProvider()
    private val authProvider = AuthProvider()
    private val geoProvider = GeoProvider()

    override fun onReceive(context: Context, intent: Intent) {
        val idBooking = intent.extras?.getString("idBooking")
        cancelBooking(idBooking!!)
    }

    private fun cancelBooking(idBooking: String){
        bookingProvider.updateStatus(idBooking, "cancel").addOnCompleteListener{
            if(it.isSuccessful) {
                Log.d("RECEIVER", "EL VIAJE FUE CANCELADO")
            }
            else {
                Log.d("RECEIVER", "NO SE PUDO ACTUALIZAR EL ESTADO DEL VIAJE")
            }
        }
    }

    private fun goToMapTrip(context: Context) {
        val i = Intent(context, MapTripActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        i.action = Intent.ACTION_RUN
        context?.startActivity(i)
    }

}