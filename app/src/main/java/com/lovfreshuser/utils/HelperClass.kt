package com.lovfreshuser.utils

import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.lovfreshuser.Const
import com.lovfreshuser.R
import com.lovfreshuser.database.OfflineDatabase
import com.lovfreshuser.database.models.CartLocalDbModel
import com.lovfreshuser.models.User
import es.dmoral.toasty.Toasty
import org.json.JSONException
import org.json.JSONObject
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class HelperClass {
    companion object {


        fun createAddressJson(
            full_name: String?,
            address: String?,
            mobile: String?,
            flatno: String?,
            landmark: String?,
            street: String?,
            address_type: String?,
            lat: String?,
            lng: String?
        ): JSONObject {
            val `object` = JSONObject()
            try {
                `object`.putOpt("full_name", full_name)
                `object`.putOpt("address", address)
                `object`.putOpt("mobile", mobile)
                `object`.putOpt("flat_number", flatno)
                `object`.putOpt("landmark", landmark)
                `object`.putOpt("streat", street)
                `object`.putOpt("address_type", address_type)
                `object`.putOpt("latitude", lat)
                `object`.putOpt("longitude", lng)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return `object`
        }

        fun getCountOfCarts(db: OfflineDatabase): Int {

            return db.cartDao().getAll().size

        }

        fun convertWorkingDaysToArray(workingDaysString: String): List<String> {

            val conv1 = workingDaysString.lowercase().replace("[", "")
            val conv2 = conv1.replace("]", "")
            val conv3 = conv2.replace("\"", "")
            val days = conv3.split(",")
            return days
        }
        //2021-07-13 20:57:43


        fun covertAppointmentTime(date: String?): String {
            var output: String = ""
            if (date != null) {
                output = try {
                    val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val formatter = SimpleDateFormat("EEEE dd MMMM 'at' hh:mm aa")
                    formatter.format(parser.parse(date))
                } catch (Ex: Exception) {
                    Log.d("TAG", "convertDate: " + Ex.message)
                    getCurrentDate() + ""
                }
            }

            return output
        }

        fun covertTime(date: String?): String {
            var output: String = ""
            if (date != null) {
                output = try {
                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000000Z'")
                    val formatter = SimpleDateFormat("dd/MM/yyyy")
                    formatter.format(parser.parse(date))
                } catch (Ex: Exception) {
                    Log.d("TAG", "convertDate: " + Ex.message)
                    getCurrentDate() + ""
                }
            }

            return output
        }

        fun convertDateFormat(date: String?): String? {
            var spf = SimpleDateFormat("yyyy-MM-dd")
            var newDate: Date? = null
            try {
                newDate = spf.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            spf = SimpleDateFormat("MM/dd/yyyy")
            return spf.format(newDate)
        }

        fun getCurrentDate(): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            return sdf.format(Date())
        }

        fun spiltString(date: String?, start: Int, end: Int): String {
            var newDate: String = ""
            if (date != null) {
                newDate = date.substring(start, end)
            }

            return newDate
        }

        fun hash(text: String): String {
            val bytes = text.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }

        fun showSuccessMsg(msg: String, context: Context) {
            Toasty.success(context, msg, 1).show()
        }

        fun showErrorMsg(msg: String, context: Context) {

            Toasty.error(context, msg, 1).show()
        }


        private fun convertFeetToMeter(feet: Float): Float {
            return (feet * 0.304).toFloat()
        }

        private fun convertInchToMeter(inch: Float): Float {
            return (inch * 0.127).toFloat()
        }

        fun calculateBmi(weight: String?, feet: String?, inch: String?): String {
            var formattedBMI = "0.00"
            try {
                val feetInFloat = feet?.toFloat()?.let { convertFeetToMeter(it) }
                val inchInFloat = inch?.toFloat()?.let { convertInchToMeter(it) }
                val weightInFloat = weight?.toFloat()
                val totalHeightInMeter = feetInFloat?.plus(inchInFloat!!);
                val BMI = weightInFloat?.div((totalHeightInMeter?.times(totalHeightInMeter)!!))
                formattedBMI = "%.2f".format(BMI)
            } catch (Ex: Exception) {
                Log.d("TAG", "calculateBmi: ${Ex.localizedMessage}")
            }
            return formattedBMI
        }


        fun showInfoMsg(msg: String, context: Context) {
            Toasty.info(context, msg, 1).show()
        }

        fun showWarningMsg(msg: String, context: Context) {
            Toasty.warning(context, msg, 1).show()
        }

        fun createProgressDialog(context: Context, msg: String): ProgressDialog {
            val progress: ProgressDialog = ProgressDialog(
                context
            )
            progress.setMessage(msg)
            progress.setCancelable(false)

            return progress

        }

        fun isValidEmail(email: String?): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isValidPassword(password: String): Boolean {
            return !TextUtils.isEmpty(password) && password.length >= 8
        }

        fun isValidPhone(phone: String): Boolean {
            var check = false
            check = if (!Pattern.matches("[a-zA-Z]+", phone)) {
                !(phone.length < 9 || phone.length > 11)
            } else {
                false
            }
            return check
        }

        fun isValidPasswordz(password: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }


//        fun showAnimatedDialogue(activity: Activity, msg: String): Dialog {
//
//            val dialog = Dialog(activity)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//            dialog.setCancelable(false)
//            dialog.setContentView(R.layout.progress_dialogue)
//            val body = dialog.findViewById(R.id.dialogueText) as TextView
//            body.text = msg
//
//            return dialog
//
//        }

//        fun showAnimatedDialogueInFragment(activity: FragmentActivity, msg: String): Dialog {
//
//            val dialog = Dialog(activity)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//            dialog.setCancelable(false)
//            dialog.setContentView(R.layout.progress_dialogue)
//            val body = dialog.findViewById(R.id.dialogueText) as TextView
//            body.text = msg
//
//            return dialog
//
//        }

        fun getUserID(): Int {
            val user: User? = SharedPrefManager.get(Const.USER_PREF)
            return user?.id ?: 0
        }


        fun isUserLoggedIn(): Boolean {
            val user: User? = SharedPrefManager.get(Const.USER_PREF)
            return user != null
        }


        fun formattedAmountText(amount: Double): String {

            val dec = DecimalFormat("#0.0")

            return "${dec.format(amount)}"
        }

//        fun fetchAuthToken(): String? {
//            val token: String? = SharedPrefManager.get(Const.TOKEN)
//            return token
//
//        }

        fun getTotalOfCart(items: List<CartLocalDbModel>): Double {
            var totalValue = 0.0
            for (item in items) {
                totalValue += (item.quantity * item.price)
            }
            return totalValue
        }

        fun convertAmountToString(amt: Double): String {
            return String.format("%.2f", amt)
        }

        fun alertDeleteDialog(
            context: Context
        ): AlertDialog.Builder {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
            builder.setCancelable(true)

            return builder
        }

        fun getSelectedVendorID(): String {

            return "34"

        }

        fun parseDateToddMMyyyy(time: String?): String? {
            val inputPattern = "HH:mm:ss"
            val outputPattern = "h:mm a"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)
            var date: Date? = null
            var str: String? = null
            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }

        fun toDefaultFormattedDateStr(inputDate: String?): String? {
            return formatDateStr(
                "yyyy-MM-dd",
                "dd/MM/yyyy",
                inputDate
            )
        }

        fun formatDateStr(
            inputFormat: String?,
            outputFormat: String?,
            inputDate: String?
        ): String {
            var parsed: Date? = null
            var outputDate = ""
            val df_input = SimpleDateFormat(inputFormat, Locale.getDefault())
            val df_output = SimpleDateFormat(outputFormat, Locale.getDefault())
            try {
                parsed = df_input.parse(inputDate)
                outputDate = df_output.format(parsed)
            } catch (e: ParseException) {
                if (e.message != null) Log.e("BaseUtility", e.message!!)
            }
            return outputDate
        }

    }

}