package com.optic.uberclonedriverkotlin.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.optic.uberclonedriverkotlin.R
import com.optic.uberclonedriverkotlin.activities.MapActivity
import com.optic.uberclonedriverkotlin.activities.MapTripActivity
import com.optic.uberclonedriverkotlin.models.Booking
import com.optic.uberclonedriverkotlin.providers.AuthProvider
import com.optic.uberclonedriverkotlin.providers.BookingProvider
import com.optic.uberclonedriverkotlin.providers.GeoProvider

class ModalBottomSheetBooking: BottomSheetDialogFragment() {

    private lateinit var textViewOrigin: TextView
    private lateinit var textViewDestination: TextView
    private lateinit var textViewOTimeAndDistance: TextView
    private lateinit var btnAccept: TextView
    private lateinit var btnCancel: TextView
    private val bookingProvider = BookingProvider()
    private val geoProvider = GeoProvider()
    private val authProvider = AuthProvider()
    private lateinit var mapActivity: MapActivity
    private lateinit var booking: Booking

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_bottom_sheet_booking, container, false)

        textViewOrigin = view.findViewById(R.id.textViewOrigin)
        textViewDestination = view.findViewById(R.id.textViewDestination)
        textViewOTimeAndDistance = view.findViewById(R.id.textViewTimeAndDistance)
        btnAccept = view.findViewById(R.id.btnAccept)
        btnCancel = view.findViewById(R.id.btnCancel)

        val data = arguments?.getString("booking")
        booking = Booking.fromJson(data!!)!!
        Log.d("ARGUMENTS", "Booking: ${booking?.toJson()}")

        textViewOrigin.text = booking?.origin
        textViewDestination.text = booking?.destination
        textViewOTimeAndDistance.text = "${booking?.time} Min - ${booking?.km} km"

        btnAccept.setOnClickListener{  acceptBooking(booking?.idClient!!) }
        btnCancel.setOnClickListener{  cancelBooking(booking?.idClient!!) }

        return view
    }

    private fun cancelBooking(idClient: String){
        bookingProvider.updateStatus(idClient, "cancel").addOnCompleteListener{
            (activity as? MapActivity)?.timer?.cancel()
            if(it.isSuccessful) {
//                Toast.makeText(activity, "Viaje cancelado", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            else {
//                Toast.makeText(activity, "no se pudo cancelar el viaje", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun acceptBooking(idClient: String){
        bookingProvider.updateStatus(idClient, "accept").addOnCompleteListener{
            (activity as? MapActivity)?.timer?.cancel()
            if(it.isSuccessful) {
                (activity as? MapActivity)?.easywayLocation?.endUpdates()
                geoProvider.removeLocation(authProvider.getId())
                goToMapTrip()
            }
            else {
//                if (context != null) {
//                    Toast.makeText(activity, "no se pudo aceptar el viaje", Toast.LENGTH_LONG).show()
//                }
            }
        }
    }

    private fun goToMapTrip() {
        val i = Intent(context, MapTripActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context?.startActivity(i)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? MapActivity)?.timer?.cancel()
//        if (booking.idClient != null){
//            cancelBooking(booking.idClient!!)
//        }
    }
}