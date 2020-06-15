package com.cred.assignment.views

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.cred.assignment.R
import com.google.android.material.textfield.TextInputEditText
import java.lang.StringBuilder

class CustomTextInputEditText(context: Context, attributeSet: AttributeSet) :
    TextInputEditText(context, attributeSet), TextWatcher {

    var prevView: View? = null
    var nextView: View? = null
    var parentView: View? = null
    var listener: TextInputListener? = null
    var maxLength = 0
    var enableFontString = "<font color=#ffffff>"
    var disableFontString = "<font color=#a1a1a1>"
    var closeFont = "</font>"
    var cardNumber = ""

    public fun setParentLayout(
        parentLayout: View,
        mListener: TextInputListener,
        maxLength: Int
    ) {
        this.parentView = parentLayout
        this.listener = mListener
        this.maxLength = maxLength
        setText(HtmlCompat.fromHtml(disableFontString+"XXXX XXXX XXXX XXXX"+closeFont, HtmlCompat.FROM_HTML_MODE_COMPACT))
    }

    override fun afterTextChanged(text: Editable) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }


    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        prevView = parentView?.findViewById<View>(nextFocusUpId)
        nextView = parentView?.findViewById<View>(nextFocusDownId)
        if (this.id == R.id.tiEtCardNo) {
            if (text.length > 0) {
                if(text.startsWith("4"))
                {
                    listener?.setCardType(false, "visa")
                }
                else if(text.startsWith("5"))
                {
                    listener?.setCardType(false, "master")
                }
                else if(text.startsWith("36"))
                {
                    listener?.setCardType(true, "diners")
                }
                else if(text.startsWith("379"))
                {
                    listener?.setCardType(false, "amex")
                }
                else
                {
                    listener?.setCardType(false, "")
                }
            }
        }


        if (text.length == maxLength && maxLength!=0) {
            var str = text.subSequence(0,maxLength-1).toString()
            val count = str.count { ch->ch.toString().equals("X") }
            if(count>0)
            {
                str =  str.replaceFirst("X", text[maxLength-1].toString())
            }
            val charCount = str.count{ ch->(!ch.toString().equals("X") && !TextUtils.isEmpty(ch.toString().trim())) }
            if(charCount==1)
            {
                str = str.substring(0,1) + "XXX XXXX XXXX XXXX"
                maxLength = 20
            }
            else if(charCount == 2)
            {
                if(str.startsWith("36"))
                {
                    str = str.substring(0,2) + "XX XXXXXX XXXX"
                    maxLength = 17
                }
            }
            else if(charCount == 3)
            {
                if(str.startsWith("379"))
                {
                    str = str.substring(0,3) + "X XXXXXX XXXXX"
                    maxLength = 18
                }

            }

//            str = getColorString(str, true)
//            str = str.replaceFirst("X", disableFontString+"X")
//            str = str + closeFont

            val ccount = str.indexOfLast{ ch->(!ch.toString().equals("X") && !TextUtils.isEmpty(ch.toString().trim())) }


            if(ccount>=0)
            {
                cardNumber = str.subSequence(0,ccount+1).toString()
                str = enableFontString + str.subSequence(0,ccount+1) + closeFont + disableFontString+str.subSequence(ccount+1, str.lastIndex+1) + closeFont
            }
            else
            {
                cardNumber = ""

                str = disableFontString+str.subSequence(ccount+1, str.lastIndex+1) + closeFont

            }

            setText(HtmlCompat.fromHtml(str, HtmlCompat.FROM_HTML_MODE_COMPACT))
            setSelection(HtmlCompat.fromHtml(str, HtmlCompat.FROM_HTML_MODE_COMPACT).toString().length)
        }
        if(lengthBefore==1 && lengthAfter==0 && text.length==maxLength-2)
        {
            var str = StringBuilder(text.toString())
            val count = str.indexOfLast{ ch->(!ch.toString().equals("X") && !TextUtils.isEmpty(ch.toString().trim())) }
            if(count>-1 && cardNumber.length!=maxLength-1) {
                str.setCharAt(count, "X"[0])
            }
            str.append("X")
            var tempStr = str.toString()
            val charCount = str.count{ ch->(!ch.toString().equals("X") && !TextUtils.isEmpty(ch.toString().trim())) }
            if(charCount==1)
            {
                tempStr = tempStr.substring(0,1) + "XXX XXXX XXXX XXXX"
                maxLength = 20
            }
            else if(charCount == 2)
            {
                if(tempStr.startsWith("36"))
                {
                    tempStr = tempStr.substring(0,2) + "XX XXXXXX XXXX"
                    maxLength = 17
                }
            }
            else if(charCount == 3)
            {
                if(tempStr.startsWith("379"))
                {
                    tempStr = tempStr.substring(0,3) + "X XXXXXX XXXXX"
                    maxLength = 18
                }
            }

            val ccount = tempStr.indexOfLast{ ch->(!ch.toString().equals("X") && !TextUtils.isEmpty(ch.toString().trim())) }

            if(ccount>=0)
            {
                cardNumber = tempStr.subSequence(0,ccount+1).toString()
                tempStr = enableFontString + tempStr.subSequence(0,ccount+1) + closeFont + disableFontString+tempStr.subSequence(ccount+1, tempStr.lastIndex+1) + closeFont
            }
            else
            {
                cardNumber = ""
                tempStr = disableFontString+tempStr.subSequence(ccount+1, tempStr.lastIndex+1) + closeFont
            }


            setText(HtmlCompat.fromHtml(tempStr, HtmlCompat.FROM_HTML_MODE_COMPACT))
            setSelection(HtmlCompat.fromHtml(tempStr, HtmlCompat.FROM_HTML_MODE_COMPACT).toString().length)
        }


        if(cardNumber!=null && cardNumber.length == maxLength-1)
        {
            listener?.checkDetails(cardNumber, true)
        }
        else
        {
            listener?.checkDetails("",false)
        }


        if (text.length == 0 && start == 0 && lengthBefore == 1 && lengthAfter == 0) {
            prevView?.requestFocus()
        }
    }

    fun getColorString(string:String, enable:Boolean):String{
        return if(enable)
        {
            enableFontString +string +closeFont
        }
        else
        {
            disableFontString + string+ closeFont
        }
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        setSelection(text!!.length)
    }

    interface TextInputListener {
        fun setCardType(isDiner: Boolean, brand:String)
        fun checkDetails(cardNumber:String, isComplete:Boolean)
    }
}