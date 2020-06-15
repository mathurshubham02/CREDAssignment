package com.cred.assignment.activities

import android.content.Context
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cred.assignment.R
import com.cred.assignment.adapters.AdapterCardList
import com.cred.assignment.fragments.AddCardFragment
import com.cred.assignment.models.CardModel
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    AddCardFragment.OnFragmentInteraction {

    val FRAGMENT_TAG = "fragment.tag"

    val arrList = ArrayList<CardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardAdapter = AdapterCardList(this, arrList)
        rvCards.layoutManager = LinearLayoutManager(this)
        rvCards.adapter = cardAdapter

        bindData()

        btnAddCard.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = AddCardFragment.newInstance()
            val slideTransition = Slide(
                Gravity.BOTTOM
            )
            slideTransition.duration = 500
            fragment.enterTransition = slideTransition
            fragment.exitTransition = slideTransition
            fragmentTransaction.replace(
                R.id.frgmentContainer,
                fragment, FRAGMENT_TAG)
            fragmentTransaction.commit()
        }
    }

    fun bindData()
    {
        val realm = Realm.getDefaultInstance()
        val list = realm.where(CardModel::class.java).findAll()
        arrList.clear()
        arrList.addAll(list)

        if(arrList.size>0)
        {
            tvNoCards.visibility = View.GONE
        }
        else
        {
            tvNoCards.visibility = View.VISIBLE
        }

        rvCards.adapter?.notifyDataSetChanged()
    }

    override fun closeFragment()
    {
        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if(fragment!=null)
        {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.remove(fragment)
            fragmentTransaction.commit()
        }
        bindData()
        hideKeyBoard(frgmentContainer)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if(fragment!=null)
        {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.remove(fragment)
            fragmentTransaction.commit()
        }
        else {
            super.onBackPressed()
        }
    }

    fun hideKeyBoard(tv: View) {
        val imm = tv.context.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(tv.windowToken, 0)
    }
}
