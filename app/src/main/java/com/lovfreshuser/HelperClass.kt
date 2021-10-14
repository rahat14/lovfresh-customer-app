package com.lovfreshuser

import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import es.dmoral.toasty.Toasty
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class HelperClass {
    companion object {

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

//        fun getUserID(): Int {
//            val user: UserModel? = SharedPrefManager.get(Const.USER_PREF)
//            return user?.id ?: 0
//        }


        //        fun isUserLoggedIn(): Boolean {
//            val user: UserModel? = SharedPrefManager.get(Const.USER_PREF)
//
//            return user != null
//
//        }
        fun isForeground(context: Context): Boolean {
            val packageName = "com.example.outdoorbd"
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningTaskInfo = manager.getRunningTasks(1)
            val componentInfo = runningTaskInfo[0].topActivity
            return componentInfo!!.packageName == packageName
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


    }

}