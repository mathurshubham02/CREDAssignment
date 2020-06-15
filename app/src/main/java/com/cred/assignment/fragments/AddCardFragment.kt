package com.cred.assignment.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.cred.assignment.R
import com.cred.assignment.activities.MainActivity
import com.cred.assignment.models.CardModel
import com.cred.assignment.views.CustomTextInputEditText
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_add_card.*
import java.text.SimpleDateFormat
import java.util.*


class AddCardFragment : Fragment(), TextWatcher {

    var listener: OnFragmentInteraction? = null
    var brand = ""
    var isCardValidated = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteraction) {
            listener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteraction"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var isDiner = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tiEtCardNo.setParentLayout(parentView, mListener, 20)
        tiEtMonth.addTextChangedListener(this)
        tiEtName.addTextChangedListener(this)
        tiEtYear.addTextChangedListener(this)
        tiEtCvv.addTextChangedListener(this)

        cancelBtn.setOnClickListener {
            listener?.closeFragment()
        }

        btnAdd.setOnClickListener {
            addToRealm()
        }
    }

    fun addToRealm() {
        var name = tiEtName.text.toString()
        val cardNumber = tiEtCardNo.cardNumber

        val security = tiEtCvv.text.toString()

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val cardObject = realm.createObject(CardModel::class.java)
        cardObject.name = name
        cardObject.cardNumber = cardNumber
        cardObject.security = security
        cardObject.brand = brand
        realm.commitTransaction()
        listener?.closeFragment()
    }


    var mListener = object :
        CustomTextInputEditText.TextInputListener {
        override fun setCardType(isDiner: Boolean, brand: String) {
            this@AddCardFragment.isDiner = isDiner
            this@AddCardFragment.brand = brand
            when (brand) {
                "visa" -> ivCardImg.setImageResource(R.drawable.visa)
                "master" -> ivCardImg.setImageResource(R.drawable.master)
                "diners" -> ivCardImg.setImageResource(R.drawable.diners)
                "amex" -> ivCardImg.setImageResource(R.drawable.amex)
            }
        }

        override fun checkDetails(cardNumber: String, isComplete: Boolean) {
            if (isComplete) {
                if (!checkLuhn(cardNumber.replace(" ", ""))) {
                    tilCardNo.error = "This card number is not valid"
                    isCardValidated = false
                } else {
                    val realm = Realm.getDefaultInstance()
                    if (realm.where(CardModel::class.java).equalTo("cardNumber", cardNumber)
                            .findAll().size > 0
                    ) {
                        tilCardNo.error = "This card already exists"
                        isCardValidated = false
                    } else {
                        tilCardNo.error = ""
                        isCardValidated = true
                    }
                }
            } else {
                tilCardNo.error = ""
                isCardValidated = false
            }

        }
    }


    fun checkLuhn(cardNo: String): Boolean {
        val nDigits = cardNo.length
        var nSum = 0
        var isSecond = false
        for (i in nDigits - 1 downTo 0) {
            var d = cardNo[i] - '0'
            if (isSecond == true) d = d * 2
            nSum += d / 10
            nSum += d % 10
            isSecond = !isSecond
        }
        return nSum % 10 == 0 && nSum > 0
    }

    fun checkDetails(): Boolean {
        var result = true
        if (tiEtName.text?.length == 0) {
            result = false

            return result
        }

        if (tiEtMonth.text!!.length < 2L || tiEtYear.text!!.length < 2L) {
            result = false

            return result
        } else {
            val month = tiEtMonth.text?.toString()
            val year = tiEtYear.text?.toString()
            if (month!!.toInt() < 13) {
                val dateFormat = SimpleDateFormat("MMyy")
                val date = dateFormat.parse("$month$year")
                val currcalendar = Calendar.getInstance()
                val tempcalendar = Calendar.getInstance()
                tempcalendar.time = date
                val checkcalendar = Calendar.getInstance()
                checkcalendar.set(Calendar.MONTH, tempcalendar.get(Calendar.MONTH))
                checkcalendar.set(Calendar.YEAR, tempcalendar.get(Calendar.YEAR))
                if (checkcalendar.time.before(currcalendar.time)) {
                    result = false

                    return result
                }
            } else {
                result = false

                return result
            }
        }

        if (tiEtCvv.text!!.length < 3) {
            result = false

            return result
        }

        return result
    }

    override fun afterTextChanged(s: Editable?) {
        if (checkDetails() && isCardValidated) {
            btnAdd.requestFocus()
            btnAdd.visibility = View.VISIBLE
        } else {
            btnAdd.visibility = View.GONE
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    companion object {
        @JvmStatic
        fun newInstance(): AddCardFragment {
            val fragment = AddCardFragment()

            return fragment
        }

    }

    interface OnFragmentInteraction {
        fun closeFragment()

    }
}