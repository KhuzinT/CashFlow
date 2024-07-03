package ru.khuzintka.cashflow.ui.settings

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.khuzintka.cashflow.R
import ru.khuzintka.cashflow.databinding.SettingsFragmentBinding
import ru.khuzintka.cashflow.ui.settings.viewmodel.SettingsEvent
import ru.khuzintka.cashflow.ui.settings.viewmodel.SettingsViewModel
import ru.khuzintka.cashflow.ui.settings.viewmodel.unknownRate
import ru.khuzintka.cashflow.util.Notification
import ru.khuzintka.cashflow.util.channelID
import ru.khuzintka.cashflow.util.notificationID
import ru.khuzintka.cashflow.util.titleExtra

const val dollarRateKey = "dollarRate"
const val euroRateKey = "euroRate"

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val viewModel by viewModels<SettingsViewModel>()

    private lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = SettingsFragmentBinding.inflate(layoutInflater, container, false)
        if (savedInstanceState != null) {
            binding.apply {
                dollarRate.text =
                    savedInstanceState.getString(dollarRateKey, unknownRate.toString())
                euroRate.text = savedInstanceState.getString(euroRateKey, unknownRate.toString())
            }
        }
        createNotificationChannel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { state ->
                    binding.apply {
                        locale.text = when (state.locale) {
                            "en" -> getString(R.string.english)
                            "ru" -> getString(R.string.russian)
                            else -> getString(R.string.unknown)
                        }
                        darkModeSwitcher.isChecked = state.isDarkMode
                        notifySwitcher.isChecked = state.isNotify

                        dollarRate.text =
                            if (state.dollarRate != unknownRate) state.dollarRate.toString()
                            else getString(R.string.unknown)
                        euroRate.text =
                            if (state.euroRate != unknownRate) state.euroRate.toString()
                            else getString(R.string.unknown)
                    }

                    viewModel.event(SettingsEvent.LoadRates)
                }
            }
        }

        binding.apply {
            locale.setOnClickListener {
                showLocaleDialog()
            }
            darkModeSwitcher.setOnCheckedChangeListener { _, isChecked ->
                viewModel.event(SettingsEvent.SetDarkMode(isChecked))
            }
            notifySwitcher.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (checkNotificationPermissions(requireActivity())) {
                        scheduleNotification(requireActivity().applicationContext)
                        viewModel.event(SettingsEvent.SetNotify(true))
                    } else {
                        notifySwitcher.isChecked = false
                        viewModel.event(SettingsEvent.SetNotify(false))
                    }
                } else {
                    cancelNotification(requireActivity().applicationContext)
                    viewModel.event(SettingsEvent.SetNotify(false))
                }
            }
            happy.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_woof)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.state.value.apply {
            outState.putString(dollarRateKey, dollarRate.toString())
            outState.putString(euroRateKey, euroRate.toString())
        }
    }

    private fun showLocaleDialog() {
        val languages = arrayOf("English", "Русский")

        val builder = AlertDialog.Builder(context)
        val checkedItem = when (viewModel.state.value.locale) {
            "en" -> 0
            "ru" -> 1
            else -> 42
        }

        builder
            .setTitle(getString(R.string.choose_lang))
            .setSingleChoiceItems(languages, checkedItem) { dialog, which ->
                when (which) {
                    0 -> viewModel.event(SettingsEvent.SetLocale("en"))
                    1 -> viewModel.event(SettingsEvent.SetLocale("ru"))
                }
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    // Notifications

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelID, "name", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "desc"
            val notificationManager =
                requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val areEnabled = notificationManager.areNotificationsEnabled()
            if (!areEnabled) {
                openSettings(context)
                return false
            }
        } else {
            val areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
            if (!areEnabled) {
                openSettings(context)
                return false
            }
        }
        return true
    }

    private fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        context.startActivity(intent)
    }

    private fun scheduleNotification(applicationContext: Context) {
        val intent = Intent(applicationContext, Notification::class.java)
        intent.putExtra(titleExtra, getString(R.string.notification))

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // ToDo: не нашел как правильно заводить будильник на каждый день в одно и то же время
        // Поэтому сделал уведомление, которое появляется тут же
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
    }

    private fun cancelNotification(applicationContext: Context) {
        val intent = Intent(applicationContext, Notification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}