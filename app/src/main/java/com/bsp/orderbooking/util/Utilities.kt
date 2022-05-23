package com.bsp.orderbooking.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.net.http.HttpResponseCache
import android.os.BatteryManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.Nullable
import androidx.core.content.pm.PackageInfoCompat
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException
import java.net.InetAddress
import java.net.MalformedURLException
import java.net.URL
import java.net.UnknownHostException
import java.text.DecimalFormat
import java.text.MessageFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object Utilities {
    const val SYNC_DAYS = 15
    private const val TYPE_WIFI = 1
    private const val TYPE_MOBILE = 2
    var TYPE_NOT_CONNECTED = 0
    private var mDeviceUdId: String? = null
    var ALLOW_SCREEN_CAPTURE = false
    var ALLOW_SCREEN_CAPTURE_CHECKED = false

    //    private var USER_INFO: LoginResponse.DataBean? = null
    var taskType = 0
    var LOCATION_ON_CALL_LOG_START = "CALL_LOG_START"
    var LOCATION_ON_CALL_LOG_END = "CALL_LOG_END"
    var LOCATION_ON_CALL_LOG_SAVE = "CALL_LOG_SAVED"

    fun isEmailValid(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isPhoneNumberValid(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches()
    }

    fun getDateTime(milliSeconds: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun getExtension(filename: String?): String? {
        if (filename == null) {
            return null
        }
        val index = indexOfExtension(filename)
        return if (index == -1) {
            ""
        } else {
            filename.substring(index + 1)
        }
    }

    private fun indexOfExtension(filename: String?): Int {
        if (filename == null) {
            return -1
        }
        val extensionPos = filename.lastIndexOf(".")
        val lastSeparator = indexOfLastSeparator(filename)
        return if (lastSeparator > extensionPos) -1 else extensionPos
    }

    private fun indexOfLastSeparator(filename: String?): Int {
        if (filename == null) {
            return -1
        }
        val lastUnixPos = filename.lastIndexOf("/")
        val lastWindowsPos = filename.lastIndexOf("\\")
        return Math.max(lastUnixPos, lastWindowsPos)
    }

    fun getDeviceUdId(context: Context): String? {
        if (TextUtils.isEmpty(mDeviceUdId)) {
                mDeviceUdId =  Settings.Secure.getString(
                    App.context.getContentResolver(),
                    Settings.Secure.ANDROID_ID)

        }
        return mDeviceUdId
    }






    fun setPostSpan(textView: TextView, linkText: String?, clickableSpanListener: ClickableSpan?) {
        if (linkText == null || linkText.length == 0) {
            return
        }
        val text = textView.text.toString()
        val startIndex = text.indexOf(linkText)
        val endIndex = startIndex + linkText.length
        if (startIndex < 0 || endIndex < 0) {
            return
        }
        textView.movementMethod = LinkMovementMethod.getInstance()
        val spannable = textView.text as Spannable
        spannable.setSpan(
            clickableSpanListener,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    fun setUnderLineSpannable(@Nullable text: CharSequence?): SpannableString? {
        if (text == null) return null
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        return content
    }

 fun setStrikeLineSpannable(@Nullable text: CharSequence?): SpannableString? {
        if (text == null) return null
        val content = SpannableString(text)
        content.setSpan(StrikethroughSpan(), 0, content.length, 0)
        return content
    }

    fun changeForeground(targetText: CharSequence, color: Int): Spannable {
        val spannable: Spannable = SpannableString(targetText)
        spannable.setSpan(
            ForegroundColorSpan(color),
            0,
            targetText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    fun changeForeground(textView: TextView, targetText: String, color: Int) {
        val text = textView.text.toString()
        val startIndex = text.indexOf(targetText)
        val endIndex = startIndex + targetText.length
        if (startIndex < 0 || endIndex < 0) {
            return
        }
        val spannable: Spannable = SpannableString(text)
        spannable.setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    @JvmOverloads
    fun changeButtonBackground(button: Button, color: Int, supportOldStyle: Boolean = false) {
        if (Build.VERSION.SDK_INT > 19 || !supportOldStyle) {
            button.background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        } else {
            button.setBackgroundColor(color)
        }
    }


//    private fun createDrawable(context: Context, color: Int, disabled: Boolean): Drawable {
//        val oShape = OvalShape()
//        val background = ShapeDrawable(oShape)
//        background.paint.color = color
//        val shader = ShapeDrawable(oShape)
//        shader.shaderFactory = object : ShaderFactory() {
//            override fun resize(width: Int, height: Int): Shader {
//                return LinearGradient(
//                    0f, 0f, 0f, height.toFloat(), intArrayOf(
//                        Color.WHITE,
//                        Color.GRAY,
//                        Color.DKGRAY,
//                        Color.BLACK
//                    ), null, Shader.TileMode.REPEAT
//                )
//            }
//        }
//        val icon = ContextCompat.getDrawable(context, R.drawable.bottom_bar)!!.mutate()
//
//        icon.setColorFilter(
//            PorterDuffColorFilter(
//                ActivityCompat.getColor(
//                    context, R.color.colorPrimaryDark
//                ),
//                PorterDuff.Mode.SRC_IN
//            )
//        )
//        val layer: Drawable = LayerDrawable(arrayOf(shader, background, icon))
//        layer.alpha = if (disabled) 128 else 255
//
//        // Note that on KitKat, setting a ColorFilter on a Drawable contained in a StateListDrawable
//        //  apparently doesn't work, although it does on later versions, so we have to render the colored
//        //  bitmap into a BitmapDrawable and then put that into the StateListDrawable
//        val bitmap =
//            Bitmap.createBitmap(icon.intrinsicWidth, icon.intrinsicHeight, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        layer.setBounds(0, 0, layer.intrinsicWidth, layer.intrinsicHeight)
//        layer.draw(canvas)
//        return BitmapDrawable(context.resources, bitmap)
//    }

/*    fun isInternetConnected(context: Context): Boolean {
        return getConnectivityStatus(context) != TYPE_NOT_CONNECTED
    }

    fun isInternetConnectedWithToast(context: Context): Boolean {
        val value = getConnectivityStatus(context) != TYPE_NOT_CONNECTED
//        if (!value) showToast(context, context.getString(R.string.error_msg_internet))
        return value
    }

    fun isInternetConnected(context: Context, view: View?, notify: Boolean): Boolean {
        val isDisconnected = getConnectivityStatus(context) == TYPE_NOT_CONNECTED
        if (notify && isDisconnected) {
//            showSnackbar(
//                view,
//                context.getString(R.string.error_msg_internet),
//                context.getString(R.string.retry)
//            ) { v: View? -> isInternetConnected(context, view, true) }
        }
        return !isDisconnected
    }*/


    //    private static String[] SUFFIX = new String[]{"", "K", "M", "B", "T"};
    //    private static char[] SUFFIX_CHAR = new char[]{'K', 'M', 'B', 'T'};
    //    private static int MAX_LENGTH = 4;
    //    public static String formatToK(double number)
    //    {
    //        String r = new DecimalFormat("##0E0").format(number);
    //        r = r.replaceAll("E[0-9]", SUFFIX[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
    //        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]"))
    //        {
    //            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
    //        }
    //        return r;
    //    }
    //    /**
    //     * Recursive implementation, invokes itself for each factor of a thousand, increasing the class on each invokation.
    //     *
    //     * @param n         the number to format
    //     * @param iteration in fact this is the class from the array c
    //     * @return a String representing the number n formatted in a cool looking way.
    //     */
    //    public static String coolFormat(double n, int iteration)
    //    {
    //        double d = (n / 100) / 10.0;
    //        boolean isRound = (d * 10) % 10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
    //        return (d < 1000 ? //this determines the class, i.e. 'k', 'm' etc
    //                ((d > 99.9 || isRound || (d > 9.99) ? //this decides whether to trim the decimals
    //                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
    //                ) + "" + SUFFIX_CHAR[iteration])
    //                : coolFormat(d, iteration + 1));
    //    }
    @JvmOverloads
    fun withSuffix(count: Long, oneDigit: Boolean = true): String {
        if (count < 1000) return "" + count
        val charc = "KMBT"
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        val value = count / Math.pow(1000.0, exp.toDouble())
        return if (oneDigit) String.format(
            Locale.ENGLISH, "%.1f%c",
            value,
            charc[exp - 1]
        ) else value.toLong().toString() + "" + charc[exp - 1]
    }

//    val userInfo: LoginResponse.DataBean?
//        get() {
//            if (USER_INFO == null || USER_INFO.getEmployeeId() === 0) {
//                USER_INFO = AppDb.get().workPlanBase().getUserInformation()
//            }
//            return USER_INFO
//        }
//
//    fun setUser(user: LoginResponse.DataBean?) {
//        USER_INFO = user
//    }

    val weekDay: String
        get() {
            val calendar = Calendar.getInstance()
            val day = calendar[Calendar.DAY_OF_WEEK]
            val weekDay: String
            weekDay = when (day) {
                Calendar.SUNDAY -> "Sunday"
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                else -> "Unknown"
            }
            return weekDay
        }

    fun roundValue(value: Double): Double {
        val decimalFormat = DecimalFormat("#.##")
        return java.lang.Double.valueOf(decimalFormat.format(value))
    }

    val dateTime: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val date = Date()
            return dateFormat.format(date)
        }

    fun formatDate(date: Date?): String {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
        return dateFormat.format(date)
    }

    fun formatDate(dateString: String?): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        try {
            return formatDate(dateFormat.parse(dateString))
        } catch (e: ParseException) {
            LogException(e)
        }
        return "ERROR"
    }

    fun formatDate(date: Date?, format: String?): String {
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return dateFormat.format(date)
    }

    fun getDateTime(dateString: String?): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        try {
            return dateFormat.parse(dateString)
        } catch (e: ParseException) {
            LogException(e)
        }
        return null
    }

    fun parseDate(dateString: String?, format: String?): Date? {
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        try {
            return dateFormat.parse(dateString)
        } catch (e: ParseException) {
            LogException(e)
        }
        return null
    }

    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun getDirectory(directoryName: String): File {
        val direct = File(Environment.getExternalStorageDirectory().path + directoryName)
        val AppDirectory = File(Environment.getExternalStorageDirectory().path, directoryName)
        if (!direct.exists()) {
            AppDirectory.mkdirs()
        }
        return AppDirectory
    }

    val dateTimePrefix: String
        get() {
            val dateFormat = SimpleDateFormat("yyMMddhhmmss", Locale.ENGLISH)
            return dateFormat.format(Date())
        }

    @SuppressLint("MissingPermission")
    fun getConnectivityStatus(context: Context): Int {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: Network? = null
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                activeNetwork = cm.activeNetwork
            } else {
                cm.activeNetworkInfo?.isConnected?.let {
                    if (it) return TYPE_WIFI
                }
            }
            if (activeNetwork != null) {
                return TYPE_WIFI
            }
            TYPE_NOT_CONNECTED
        } catch (ex: Exception) {
            LogException(ex)
            TYPE_NOT_CONNECTED
        }
    }


    @Throws(IOException::class)
    fun getConnectivityStatusString(context: Context): String? {
        val conn = getConnectivityStatus(context)
        var status: String? = null
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled"
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled"
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet"
        }
        return status
    }

    val androidOS: String
        get() {
            try {
                val builder = StringBuilder()
                builder.append("android - ").append(Build.VERSION.RELEASE)
                val fields = VERSION_CODES::class.java.fields
                for (field in fields) {
                    val fieldName = field.name
                    var fieldValue = -1
                    try {
                        fieldValue = field.getInt(Any())
                    } catch (e: IllegalArgumentException) {
                        LogException(e)
                    } catch (e: IllegalAccessException) {
                        LogException(e)
                    } catch (e: NullPointerException) {
                        LogException(e)
                    }
                    if (fieldValue == Build.VERSION.SDK_INT) {
                        builder.append(" - ").append(fieldName).append(" - ")
                        builder.append("sdk=").append(fieldValue)
                    }
                }
                return builder.toString()
            } catch (ex: Exception) {
                LogException(ex)
            }
            return "Android"
        }
    val cPUArtech: String
        get() {
            val arc = System.getProperty("os.arch")
            return if (TextUtils.isEmpty(arc)) "" else " - $arc"
        }

    fun getSharedPreferences(context: Context): SharedPreferences {
        return UserData.getSharedPreferences(context)
    }

//    fun join(delimiter: CharSequence, tokens: Iterable<SingleItem>): String {
//        val it: Iterator<SingleItem> = tokens.iterator()
//        if (!it.hasNext()) {
//            return ""
//        }
//        val sb = StringBuilder()
//        sb.append(it.next().title)
//        while (it.hasNext()) {
//            sb.append(delimiter)
//            sb.append(it.next().title)
//        }
//        return sb.toString()
//    }

    fun getEditor(context: Context): Editor {
        return getSharedPreferences(context).edit()
    }

    val deviceName: String
        get() {
            try {
                val manufacturer = Build.MANUFACTURER
                val model = Build.MODEL
                return if (model.startsWith(manufacturer)) capitalize(model) else capitalize(
                    manufacturer
                ) + " " + model
            } catch (ex: Exception) {
                LogException(ex)
            }
            return "Android Device"
        }

    fun capitalize(s: String?): String {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) s else Character.toUpperCase(first)
            .toString() + s.substring(1)
    }

    fun showSnackbar(
        v: View?,
        message: String?,
        actionText: String?,
        onClickListener: View.OnClickListener?,
    ) {
        Snackbar.make(v!!, message!!, Snackbar.LENGTH_LONG).setAction(actionText, onClickListener)
            .show()
    }

    fun showSnackbar(
        v: View?,
        message: String?,
        actionText: String?,
        messageColor: Int,
        onClickListener: View.OnClickListener? = null,
    ) {
        val snackbar = Snackbar
            .make(v!!, message!!, Snackbar.LENGTH_LONG)
            .setAction(actionText, onClickListener)
        val sbView = snackbar.view
      /*  val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(messageColor)*/
        snackbar.show()
    }

    fun turnGPSOn(context: Context) {
        try {
            val intent = Intent("android.location.GPS_ENABLED_CHANGE")
            intent.putExtra("enabled", true)
            context.sendBroadcast(intent)
            val provider = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            if (!provider.contains("gps")) //if gps is disabled
            {
                val poke = Intent()
                poke.setClassName(
                    "com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider"
                )
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
                poke.data = Uri.parse("3")
                context.sendBroadcast(poke)
            }
        } catch (ex: Exception) {
            LogException(ex)
        }
        //WifiManager wifiManager = (WifiManager)this.context.getSystemService(Context.WIFI_SERVICE);
        //wifiManager.setWifiEnabled(status);
    }

    fun turnGPSOff(context: Context) {
        val provider = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.LOCATION_PROVIDERS_ALLOWED
        )
        if (provider.contains("gps")) //if gps is enabled
        {
            val poke = Intent()
            poke.setClassName(
                "com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider"
            )
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
            poke.data = Uri.parse("3")
            context.sendBroadcast(poke)
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
        return if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            try {
                locationMode =
                    Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: SettingNotFoundException) {
                LogException(e)
            }
            locationMode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            locationProviders = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            !TextUtils.isEmpty(locationProviders)
        }
    }

    fun isGpsEnabled(context: Context): Boolean {
        val locationMode: Int
        locationMode = try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: SettingNotFoundException) {
            LogException(e)
            return false
        }
        return when (locationMode) {
            Settings.Secure.LOCATION_MODE_HIGH_ACCURACY, Settings.Secure.LOCATION_MODE_SENSORS_ONLY -> true
            Settings.Secure.LOCATION_MODE_BATTERY_SAVING, Settings.Secure.LOCATION_MODE_OFF -> false
            else -> false
        }
    }

    fun getForegroundScreen(context: Context): String? {
        var foregroundScreen: String? = null
        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            @SuppressLint("WrongConstant") val mUsageStatsManager =
                context.getSystemService("usagestats") as UsageStatsManager
            val time = System.currentTimeMillis()
            // We get usage stats for the last 10 seconds
            val stats = mUsageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - 1000 * 10,
                time
            )
            // Sort the stats by the last time used
            if (stats != null) {
                val mySortedMap: SortedMap<Long, UsageStats> = TreeMap()
                for (usageStats in stats) {
                    mySortedMap[usageStats.lastTimeUsed] = usageStats
                }
                if (!mySortedMap.isEmpty()) {
                    foregroundScreen = mySortedMap[mySortedMap.lastKey()]!!.packageName
                }
            }
        } else {
            val mActivityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val RunningTask = mActivityManager.getRunningTasks(1)
            val ar = RunningTask[0]
            foregroundScreen = ar.topActivity!!.className
        }
        return foregroundScreen
    }

    fun isUsageAccessEnabled(context: Context): Boolean {
        try {
            if (Build.VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
                return true
            }
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )
            return mode == AppOpsManager.MODE_ALLOWED
        } catch (ex: Exception) {
            LogException(ex)
        }
        return false
    }

    fun generateString(length: Int): String {
        val characters = "abcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random()
        val text = CharArray(length)
        for (i in 0 until length) {
            text[i] = characters[random.nextInt(characters.length)]
        }
        return String(text)
    }

    fun getBatteryLevel(context: Context): Float {
        try {
            val batteryIntent =
                context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            // Error checking that probably isn't needed but I added just in case.
            return if (level == -1 || scale == -1) {
                50.0f
            } else level.toFloat() / scale.toFloat() * 100.0f
        } catch (x: Exception) {
            LogException(x)
        }
        return 0f
    }

    fun isMobileDataEnabled(context: Context): Boolean {
        var mobileDataEnabled = false // Assume disabled
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            val cmClass = Class.forName(cm.javaClass.name)
            val method = cmClass.getDeclaredMethod("getMobileDataEnabled")
            method.isAccessible = true
            mobileDataEnabled = method.invoke(cm) as Boolean
        } catch (e: Exception) {
            LogException(e)
        }
        return mobileDataEnabled
    }

    fun getIpAddressFromUrl(url: String?): String {
        try {
            return InetAddress.getByName(URL(url).host).hostAddress
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getRoundValueTo0Decimal(value: Double): String {
        val decimalFormat = DecimalFormat("#,###,###,###,###")
        val rValue = decimalFormat.format(value)
        return if (TextUtils.equals(rValue, "NaN")) "0" else rValue
    }

    fun getRoundValueTo1Decimal(value: Double): String {
        val decimalFormat = DecimalFormat("#,###,###,###,###.0")
        val rValue = decimalFormat.format(value)
        return if (TextUtils.equals(rValue, "NaN")) "0" else rValue
    }

    fun getRoundValueTo2Decimal(value: Double): String {
        val decimalFormat = DecimalFormat("#,###,###,###,###.00")
        val rValue = decimalFormat.format(value)
        return if (TextUtils.equals(rValue, "NaN")) "0.00" else rValue
    }

    fun getHostFromUrl(url: String?): String {
        try {
            return URL(url).host
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getNumberWithOrdinal(number: Int): String {
        if (number <= 0) {
            return number.toString()
        }
        when (number % 100) {
            11, 12, 13 -> return number.toString() + "th"
        }
        return when (number % 10) {
            1 -> number.toString() + "st"
            2 -> number.toString() + "nd"
            3 -> number.toString() + "rd"
            else -> number.toString() + "th"
        }
    }

    fun LogException(e: Exception) {
        Log.wtf("MREP", "EXCEPTION: " + e.message, e)
    }

    fun installAPK(fileName: String) {
        val file = File(fileName)
        if (file.exists()) {
            try {
                val command: String
                command = "adb install -r $fileName"
                val proc = Runtime.getRuntime().exec(arrayOf("-c", command))
                proc.waitFor()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCurrentAppVersion(context: Context): String {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName.replace("build ", "")
        } catch (e: Exception) {
            LogException(e)
            "0.0.0"
        }
    }

    fun deleteAllFiles(directory: File) {
        if (directory.isDirectory) {
            for (child in directory.listFiles()) {
                deleteAllFiles(child)
            }
        }
        directory.delete()
    }

    fun getScreenSize(context: Activity): DisplayMetrics {
        val displaymetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displaymetrics)
        return displaymetrics
    }

    fun getTextViewContentsWidth(textView: TextView): Int {
        val bounds = Rect()
        val textPaint: Paint = textView.paint
        textPaint.getTextBounds(textView.text.toString(), 0, textView.text.length, bounds)
        return bounds.width()
    }

    fun setTextAnimation(context: Activity, textView: TextView) {
        val width = getScreenSize(context).widthPixels
        val animation: Animation = TranslateAnimation(
            width.toFloat(),
            (-1 * getTextViewContentsWidth(textView)).toFloat(),
            0f,
            0f
        )
        animation.duration = 12000
        animation.setInterpolator(context, android.R.anim.linear_interpolator)
        animation.repeatMode = Animation.INFINITE
        animation.repeatCount = Animation.INFINITE
        textView.startAnimation(animation)
    }

    fun changeTextSize(text: CharSequence, size: Int): SpannableString {
        val span2 = SpannableString(text)
        span2.setSpan(AbsoluteSizeSpan(size), 0, text.length, SPAN_INCLUSIVE_INCLUSIVE)
        return span2;
    }

    fun setLayoutAnimation(context: Activity, layout: LinearLayout) {
        val width = getScreenSize(context).widthPixels
        val animation = TranslateAnimation(
            width.toFloat(),
            -1 * convertDpToPixel(layout.childCount * 140.toFloat(), context),
            0f,
            0f
        )
        animation.duration = 1700 * layout.childCount.toLong()
        animation.setInterpolator(context, android.R.anim.linear_interpolator)
        animation.repeatMode = Animation.INFINITE
        animation.repeatCount = Animation.INFINITE
        layout.startAnimation(animation)
    }

    fun setTextAnimationHalfStart(context: Activity, textView: TextView) {
        val width: Int =
            getScreenSize(context).widthPixels
        val animation: Animation = TranslateAnimation(
            (width / 2).toFloat(),
            -1f * getTextViewContentsWidth(textView),
            0f,
            0f
        )
        animation.duration = 5000
        animation.setInterpolator(context, android.R.anim.linear_interpolator)
        animation.repeatMode = Animation.INFINITE
        animation.repeatCount = Animation.INFINITE
        textView.startAnimation(animation)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


    fun dp2px(resources: Resources, dp: Float): Float {
        val scale = resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    fun sp2px(resources: Resources, sp: Float): Float {
        val scale = resources.displayMetrics.scaledDensity
        return sp * scale
    }

    private var VERSION_NAME: String? = null
    private var VERSION_CODE: Long = 0
    fun getCurrentVersionName(context: Context): String? {
        return try {
            if (TextUtils.isEmpty(VERSION_NAME)) {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                VERSION_NAME = pInfo.versionName
            }
            VERSION_NAME
        } catch (e: PackageManager.NameNotFoundException) {
            LogException(e)
            "0.0.0"
        }
    }

    fun removeAccount() {
//        ServiceUrls.setCloudServerUrl(null)
        //        try
//        {
//            File data = Environment.getDataDirectory();
//            String currentDBPath = "//data//" + App.getContext().getPackageName() + "//databases//" + AppDb.DATABASE_NAME;
//            File currentDB = new File(data, currentDBPath);
//            App.getContext().deleteDatabase(AppDb.DATABASE_NAME);
//            currentDB.delete();
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        AppDb.get().clearAllTables()
//        AppDb.setNull()
        getSharedPreferences(App.context).edit()
            .clear().apply()
//        Utilities.setUser(null)
//        UserData.setDataPassword(null)
//        TokenDataTransmit.clearCache(App.getContext())
        if (HttpResponseCache.getInstalled() != null) {
            HttpResponseCache.getInstalled().flush()
        }
    }

    fun getCurrentVersionCode(context: Context): Long {
        return try {
            if (VERSION_CODE == 0L) {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                VERSION_CODE = PackageInfoCompat.getLongVersionCode(pInfo)
            }
            VERSION_CODE
        } catch (e: PackageManager.NameNotFoundException) {
            LogException(e)
            0
        }
    }

/*    fun getTimeDifference(serverDateTime: String?): Int {
        try {
            val takenTime: DateTime = DateTime.parse(
                serverDateTime,
                DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ")
            )
            val currentTime: DateTime = DateTime.now()
            val difference = Duration(takenTime, currentTime)
            val seconds = difference.getStandardSeconds() as Int
            Utilities.printDebug(" difference $seconds")
            return seconds
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }*/

    private fun lcs(text1: String, text2: String): String {
        val lengths = Array(text1.length + 1) { IntArray(text2.length + 1) }
        for (i in 0 until text1.length) for (j in 0 until text2.length) if (text1[i] == text2[j]) lengths[i + 1][j + 1] =
            lengths[i][j] + 1 else lengths[i + 1][j + 1] = Math.max(
            lengths[i + 1][j], lengths[i][j + 1]
        )
        val sb = StringBuffer()
        var x = text1.length
        var y = text2.length
        while (x != 0 && y != 0) {
            if (lengths[x][y] == lengths[x - 1][y]) x-- else if (lengths[x][y] == lengths[x][y - 1]) y-- else {
                assert(text1[x - 1] == text2[y - 1])
                sb.append(text1[x - 1])
                x--
                y--
            }
        }
        return sb.reverse().toString()
    }

    fun blinkTextView(textView: TextView) {
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 50 //You can manage the blinking time with this parameter
        anim.startOffset = 20
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        textView.startAnimation(anim)
    }

    fun logEntry(tag: String?, vararg message: String?) {
        print(MessageFormat.format("{0}: {1}\n", tag, TextUtils.join("\n", message)))
    }

    fun getStaticMapUrl(zoom: Int, lat: Double, lon: Double): String {
        return "https://maps.googleapis.com/maps/api/staticmap?zoom=$zoom&maptype=roadmap&visual_refresh=true&size=540x305&maptype=roadmap&markers=icon:https://www.iconfinder.com/data/icons/small-n-flat/24/678111-map-marker-32.png|label:C|$lat,$lon&sensor=false&key=AIzaSyBpY2QA83t4QtDPqu2QQovsbRRDAZBLwJs"
    }

    fun getStaticMapUrl(zoom: Int, latC: Double, lonC: Double, latD: Double, lonD: Double): String {
        return "https://maps.googleapis.com/maps/api/staticmap?center=" + latC + "," + lonC + "zoom=" + zoom + "&maptype=roadmap&visual_refresh=true&size=540x305&maptype=roadmap&markers=color:blue|label:C|" + latC + "," + lonC + "&markers=color:red|label:D|" + latD + "," + lonD + "&sensor=false&key=AIzaSyBpY2QA83t4QtDPqu2QQovsbRRDAZBLwJs"
    }

    const val KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates"

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The [Context].
     */
    fun requestingLocationUpdates(context: Context?): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            KEY_REQUESTING_LOCATION_UPDATES, false
        )
    }

    /**
     * Stores the location updates state in SharedPreferences.
     *
     * @param requestingLocationUpdates The location updates state.
     */
    fun setRequestingLocationUpdates(context: Context?, requestingLocationUpdates: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
            .apply()
    }

    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double, unit: Char): Double {
        val theta = lon1 - lon2
        var dist =
            Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(
                deg2rad(lat2)
            ) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        if (unit == 'm') {
            dist = dist * 1.609344 * 1000
        } else if (unit == 'K') {
            dist = dist * 1.609344
        } else if (unit == 'N') {
            dist = dist * 0.8684
        }
        return dist
    }

    fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    fun isMockSettingsTurnedOn(context: Context): Boolean {
        return !TextUtils.equals(
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ALLOW_MOCK_LOCATION
            ), "0"
        )
    }

    fun areThereMockPermissionApps(context: Context): Boolean {
        var count = 0
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        for (applicationInfo in packages) {
            try {
                val packageInfo =
                    pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS)
                val requestedPermissions = packageInfo.requestedPermissions
                if (requestedPermissions != null) {
                    for (requestedPermission in requestedPermissions) {
                        if (requestedPermission == "android.permission.ACCESS_MOCK_LOCATION" && applicationInfo.packageName != context.packageName) {
                            count++
                        }
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e("Got exception ", e.message!!)
            }
        }
        return count > 0
    }

    fun getMonthPrefix(month: Int): String {
        val prefix: String
        prefix = when (month) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> month.toString()
        }
        return prefix
    }

/*
    // only print in debug build
    fun printDebug(test: Any?) {
        if (BuildConfig.DEBUG) println(test)
    }
*/

    fun hideKeyboard(view: View?) {
        if (view != null) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

//    fun formatAsHtml(context: Context, monthYear: String): Spanned {
//        return HtmlCompat.fromHtml(
//            context.getString(R.string.month) + ": <b><u>" + monthYear + "</u></b>",
//            HtmlCompat.FROM_HTML_MODE_LEGACY
//        )
//    }

/*    val uriAuthority: String
        get() = BuildConfig.APPLICATION_ID + ".fileprovider"*/

    fun careGrowth(committed: Double, current: Double): String {
        return if (current == 0.0) "00.00" else ((committed - current) / current * 100).toString()
    }

    fun checkLenght( item: String): String{
        if(item.length > 2)
            return "99+"
        return item
    }

    fun dayName(day : String) : String{
        if(day.lowercase().contains("mo",true)){
            return "Monday"
        }
        if(day.lowercase().contains("tu",true)){
            return "Tuesday"
        }
        if(day.lowercase().contains("we",true)){
            return "Wednesday"
        }
        if(day.lowercase().contains("th",true)){
            return "Thursday"
        }
        if(day.lowercase().contains("fr",true)){
            return "Friday"
        }
        if(day.lowercase().contains("sa",true)){
            return "Saturday"
        }
        if(day.lowercase().contains("su",true)){
            return "Sunday"
        }
        return day
    }
}