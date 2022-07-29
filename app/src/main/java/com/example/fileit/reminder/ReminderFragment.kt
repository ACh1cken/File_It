package com.example.fileit.reminder

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fileit.R
import java.util.*


class ReminderFragment : Fragment() {

    private lateinit var dateView : TextView
    private lateinit var dateButton : Button
    private val myCalendar = Calendar.getInstance()
    private lateinit var pref : SharedPreferences
    private lateinit var alarmManager : AlarmManager
    private lateinit var summonNotification : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reminder, container, false)
        dateView = view.findViewById(R.id.date_display)
        dateButton = view.findViewById(R.id.date_picker_button)
        val setNotifButton = view.findViewById<Button>(R.id.set_reminder_button)
        val notificationStatus = view.findViewById<TextView>(R.id.notification_status)
        val notificationWarn = view.findViewById<TextView>(R.id.notification_warn)
        pref = requireContext().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
        val reminderCancelButton = view.findViewById<Button>(R.id.reminder_cancel_button)
        val cancelReminderText = view.findViewById<TextView>(R.id.textView5)
        val reminder_help_button = view.findViewById<ImageButton>(R.id.reminder_help_button)


        reminder_help_button.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setTitle("Help center")
                .setMessage("Set automatic reminder that appears each month before tax deadline. \n" +
                        "Disable reminder in settings. ")
                .setPositiveButton("Okay"){_,_ ->}
                .show()
        }

        summonNotification = view.findViewById<Button>(R.id.summon_notification)

//        var  calendar = Calendar.getInstance();
//        var year = calendar.get(Calendar.YEAR);
//
//        var month = calendar.get(Calendar.MONTH);
//        var day = calendar.get(Calendar.DAY_OF_MONTH);

        var alarmUp : Boolean = PendingIntent
            .getBroadcast(
                requireContext(), 1,
                Intent(requireActivity(),BootReceiver::class.java),
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            ) != null

        if (pref.getBoolean("NoReminder",true)){

            notificationStatus.text = "Notification status : Disabled"
            notificationWarn.visibility = View.VISIBLE
            dateView.visibility = View.GONE
            dateButton.visibility = View.GONE
            setNotifButton.visibility = View.GONE

            myCalendar.set(Calendar.YEAR, pref.getLong("ReminderYear", myCalendar[Calendar.YEAR].toLong()).toInt())
            myCalendar.set(Calendar.MONTH, pref.getLong("ReminderMonth", myCalendar[Calendar.MONTH].toLong()).toInt())
            myCalendar.set(Calendar.DAY_OF_MONTH, pref.getLong("ReminderDay", myCalendar[Calendar.DAY_OF_MONTH].toLong()).toInt())





            if (alarmUp) {

                val pendingIntent = PendingIntent
                    .getBroadcast(
                        requireContext(), 1,
                        Intent(requireActivity(),BootReceiver::class.java),
                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                alarmManager.cancel(pendingIntent)
            }

        }else{
            notificationStatus.text = "Notification status : Enabled"
            dateView.visibility = View.VISIBLE
            dateButton.visibility = View.VISIBLE
            setNotifButton.visibility = View.VISIBLE
            notificationWarn.visibility = View.GONE

            createNotificationChannel()

            if (alarmUp) {
                reminderCancelButton.visibility = View.GONE
                cancelReminderText.visibility = View.GONE
                reminderCancelButton.setOnClickListener{
//                    Log.e("Alarm", "Alarm is already active")
                    val pendingIntent = PendingIntent
                        .getBroadcast(
                            requireContext(), 1,
                            Intent(requireActivity(),BootReceiver::class.java),
                            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

//                    Log.e("Alarm", "Alarm is cancelled due to reminder.")
                    AlertDialog.Builder(requireContext())
                        .setTitle("Reminder cancelled")
                        .setMessage(
                            "Remove current reminder?")
                        .setPositiveButton("Confirm"){_,_ ->
                            alarmManager.cancel(pendingIntent)
                        }
                        .setNegativeButton("Cancel"){
                            dialog,_ ->

                            dialog.cancel()
                        }
                        .show()
                }

            }

        }



        val date =
            OnDateSetListener { _, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }


        dateButton.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        setNotifButton.setOnClickListener{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                scheduleNotification()
            }else {
                Toast.makeText(requireContext(),"Failed to set reminder, SDK version ${Build.VERSION_CODES.M} needed",
                    Toast.LENGTH_SHORT)
            }

        }



        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Fire notification instantly for testing
        summonNotification.setOnClickListener{
            val intent = Intent(requireContext(), BootReceiver::class.java)
            val title = "Tax reminder"
            val message = "Tax deadline is set at ${pref.getLong("ReminderDay", 1)}/" +
                    "${pref.getLong("ReminderMonth", 1)}/" +
                    "${pref.getLong("ReminderYear", 1990)}"
            intent.putExtra(titleExtra, title)
            intent.putExtra(messageExtra, message)

            requireContext().sendBroadcast(intent)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateLabel() {

        dateView.text = "Date Selected: "+
        android.text.format.DateFormat.getLongDateFormat(requireContext()).format(Date(myCalendar.timeInMillis))
        val editPref = pref.edit()
        editPref.putLong("ReminderYear", myCalendar[Calendar.YEAR].toLong())
        editPref.putLong("ReminderMonth", myCalendar[Calendar.MONTH].toLong())
        editPref.putLong("ReminderDay", myCalendar[Calendar.DAY_OF_MONTH].toLong())
        editPref.putBoolean("NoReminder",false)
        editPref.apply()

    }

    private fun scheduleNotification(){
        val intent = Intent(requireContext(), BootReceiver::class.java)
        val title = "Tax reminder"
        val message = "Next monthly reminder is set "
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

         alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        if (time <=myCalendar.timeInMillis ) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                getTime(),
                pendingIntent
            )

            showAlert(time, title, message)
        }else{
            Toast.makeText(requireContext(),"Deadline is closer than a month. Failed to set reminder.",Toast.LENGTH_SHORT).show()
        }

    }

    private fun showAlert(time: Long, title: String, message: String) {

        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(requireContext())

        AlertDialog.Builder(requireContext())
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }

    private fun getTime(): Long {


      // return myCalendar.timeInMillis
        return getDuration()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dateFormat = android.text.format.DateFormat.getLongDateFormat(requireContext())
            val name = "Tax filing reminder"
            val desc = "Reminder to file tax before ${dateFormat.format(Date(myCalendar.timeInMillis))}"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(channelID, name, importance)


            channel.description = desc
            val notificationManager = requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }else{
            //does not meet minimum android
        }
    }

    private fun getDuration(): Long {
        // get todays date
        val cal = Calendar.getInstance()
        // get current month
        var currentMonth = cal[Calendar.MONTH]

        // move month ahead
        currentMonth++
        // check if has not exceeded threshold of december
        if (currentMonth > Calendar.DECEMBER) {
            // alright, reset month to jan and forward year by 1 e.g fro 2013 to 2014
            currentMonth = Calendar.JANUARY
            // Move year ahead as well
            cal[Calendar.YEAR] = cal[Calendar.YEAR] + 1
        }

        // reset calendar to next month
        cal[Calendar.MONTH] = currentMonth
        // get the maximum possible days in this month
        val maximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        // set the calendar to maximum day (e.g in case of fEB 28th, or leap 29th)
        cal[Calendar.DAY_OF_MONTH] = maximumDay
        return cal.timeInMillis // this is what you set as trigger point time i.e one month after
    }



}