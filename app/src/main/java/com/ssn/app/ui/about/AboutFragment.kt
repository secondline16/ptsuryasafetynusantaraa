package com.ssn.app.ui.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ssn.app.R
import com.ssn.app.databinding.FragmentAboutBinding
import com.ssn.app.helper.viewBinding

class AboutFragment : Fragment(R.layout.fragment_about) {

    private val binding by viewBinding(FragmentAboutBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}