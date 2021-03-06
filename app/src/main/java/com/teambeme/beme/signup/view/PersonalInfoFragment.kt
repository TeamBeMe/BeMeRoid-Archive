package com.teambeme.beme.signup.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.teambeme.beme.R
import com.teambeme.beme.base.BindingFragment
import com.teambeme.beme.databinding.FragmentPersonalInfoBinding
import com.teambeme.beme.signup.viewmodel.SignUpViewModel
import com.teambeme.beme.util.color
import com.teambeme.beme.util.recordClickEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInfoFragment :
    BindingFragment<FragmentPersonalInfoBinding>(R.layout.fragment_personal_info) {
    private val signUpViewModel: SignUpViewModel by activityViewModels()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "SignUpActivity")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "INF_SIGN")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.signUpViewModel = signUpViewModel
        setUiClickListener()
        subscribeData()
        return binding.root
    }

    private fun setUiClickListener() {
        binding.btnPersonalBack.setOnClickListener { view ->
            recordClickEvent("BACK_PRESS", "OUT_INF_SIGN")
            view.findNavController().popBackStack()
        }
        binding.btnPersonalDone.setOnClickListener { view ->
            view.findNavController()
                .navigate(R.id.action_personalInfoFragment_to_imageChooseFragment)
            signUpViewModel.setUserInfo()
        }
    }

    private fun subscribeData() {
        signUpViewModel.userEmail.observe(viewLifecycleOwner) { email ->
            if (email.isNullOrBlank()) {
                binding.txtPersonalEmailCheck.apply {
                    text = "bean@example.com ???????????? ????????? ?????????"
                    setTextColor(color(R.color.signup_personal_check))
                }
                binding.imgPersonalEmailCheck.setImageResource(R.drawable.ic_personal_check_gray)
            }
        }
        signUpViewModel.isEmailValid.observe(viewLifecycleOwner) {
            if (requireNotNull(signUpViewModel.userEmail.value).isNotEmpty()) {
                when (it) {
                    true -> {
                        binding.txtPersonalEmailCheck.apply {
                            text = "????????? ?????? ??????????????????"
                            setTextColor(color(R.color.signup_term_blue))
                        }
                        binding.imgPersonalEmailCheck.setImageResource(R.drawable.ic_personal_check_blue)
                    }
                    else -> {
                        binding.txtPersonalEmailCheck.apply {
                            text = "????????? ?????? ?????? ??????????????????"
                            setTextColor(color(R.color.signup_red))
                        }
                        binding.imgPersonalEmailCheck.setImageResource(R.drawable.ic_personal_check_red)
                    }
                }
            }
        }

        signUpViewModel.isNicknameValid.observe(viewLifecycleOwner) {
            if (requireNotNull(signUpViewModel.userNickName.value).isNotEmpty()) {
                binding.btnPersonalNicknameDoubleCheck.isEnabled = it
                if (!it) {
                    binding.imgPersonalNicknameCheck.setImageResource(R.drawable.ic_personal_check_red)
                    binding.txtPersonalNicknameCheck.apply {
                        text = "?????? ???????????? ??????????????????"
                        setTextColor(color(R.color.signup_red))
                    }
                    signUpViewModel.nickDoubleCheckInvalidated()
                } else if (signUpViewModel.isDoubleCheckedId(requireNotNull(signUpViewModel.userNickName.value))) {
                    binding.imgPersonalNicknameCheck.setImageResource(R.drawable.ic_personal_check_blue)
                    binding.txtPersonalNicknameCheck.apply {
                        text = "?????? ????????? ??????????????????"
                        setTextColor(color(R.color.signup_term_blue))
                    }
                    signUpViewModel.nickDoubleCheckValidated()
                } else {
                    binding.imgPersonalNicknameCheck.setImageResource(R.drawable.ic_personal_check_blue)
                    binding.txtPersonalNicknameCheck.apply {
                        text = "?????? ????????? ??????????????????, ??????????????? ????????????"
                        setTextColor(color(R.color.signup_term_blue))
                    }
                    signUpViewModel.nickDoubleCheckInvalidated()
                }
            }
        }
        signUpViewModel.userNickName.observe(viewLifecycleOwner) { nickName ->
            if (nickName.isEmpty()) {
                binding.imgPersonalNicknameCheck.setImageResource(R.drawable.ic_personal_check_gray)
                binding.txtPersonalNicknameCheck.apply {
                    text = "??????, ????????? 5??? ?????? 20??? ????????? ????????? ?????????."
                    setTextColor(color(R.color.signup_personal_check))
                }
            } else {
                signUpViewModel.nickDoubleCheckInvalidated()
            }

            if (signUpViewModel.isDoubleCheckedId(nickName)) {
                binding.imgPersonalNicknameCheck.setImageResource(R.drawable.ic_personal_check_blue)
                binding.txtPersonalNicknameCheck.apply {
                    text = "?????? ????????? ??????????????????"
                    setTextColor(color(R.color.signup_term_blue))
                }
                signUpViewModel.nickDoubleCheckValidated()
            }
        }

        signUpViewModel.nickDoubleCheck.observe(viewLifecycleOwner) { checkInfo ->
            if (!checkInfo.data.nicknameExist) {
                Toast.makeText(
                    requireContext(),
                    "???????????? ${signUpViewModel.userNickName.value!!}??? ?????????????????????.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.txtPersonalNicknameCheck.apply {
                    text = "?????? ????????? ??????????????????"
                    setTextColor(color(R.color.signup_term_blue))
                }
                signUpViewModel.registerId(signUpViewModel.userNickName.value!!)
                signUpViewModel.nickDoubleCheckValidated()
            } else {
                binding.txtPersonalNicknameCheck.apply {
                    text = "?????? ???????????? ??????????????????"
                    setTextColor(color(R.color.signup_red))
                }
                binding.imgPersonalNicknameCheck.setImageResource(R.drawable.ic_personal_check_red)
            }
        }
        signUpViewModel.isNickNameDoubleChecked.observe(viewLifecycleOwner) {
            if (it) {
                binding.imgPersonalNicknameCheck.setImageResource(R.drawable.ic_personal_check_blue)
                binding.txtPersonalNicknameCheck.apply {
                    text = "?????? ????????? ??????????????????"
                    setTextColor(color(R.color.signup_term_blue))
                }
            }
        }

        signUpViewModel.userPassWord.observe(viewLifecycleOwner) { passWord ->
            if (passWord.isEmpty()) {
                binding.imgPersonalPassword.setImageResource(R.drawable.ic_personal_check_gray)
                binding.txtPersonalPassword.apply {
                    text = "??????????????? ?????? ????????? 8??? ?????? ????????? ?????????"
                    setTextColor(color(R.color.signup_personal_check))
                }
            }
        }
        signUpViewModel.isPasswordValid.observe(viewLifecycleOwner) {
            if (requireNotNull(signUpViewModel.userPassWord.value).isNotEmpty()) {
                if (it) {
                    binding.imgPersonalPassword.setImageResource(R.drawable.ic_personal_check_blue)
                    binding.txtPersonalPassword.apply {
                        text = "?????? ????????? ?????????????????????"
                        setTextColor(color(R.color.signup_term_blue))
                    }
                } else {
                    binding.imgPersonalPassword.setImageResource(R.drawable.ic_personal_check_red)
                    binding.txtPersonalPassword.apply {
                        text = "8??? ?????? 20??? ????????? ????????? ????????? ??????????????? ?????????."
                        setTextColor(color(R.color.signup_red))
                    }
                }
            }
        }

        signUpViewModel.userPassWordCheck.observe(viewLifecycleOwner) { passWordCheck ->
            binding.linearPersonalPasswordCheck.isVisible = passWordCheck.isNotEmpty()
        }
        signUpViewModel.isPasswordDoubleChecked.observe(viewLifecycleOwner) {
            if (it) {
                binding.imgPersonalPasswordCheck.setImageResource(R.drawable.ic_personal_check_blue)
                binding.txtPersonalPasswordCheck.apply {
                    text = "??????????????? ???????????????"
                    setTextColor(color(R.color.signup_term_blue))
                }
            } else {
                binding.imgPersonalPasswordCheck.setImageResource(R.drawable.ic_personal_check_red)
                binding.txtPersonalPasswordCheck.apply {
                    text = "??????????????? ???????????? ????????????"
                    setTextColor(color(R.color.signup_red))
                }
            }
        }

        signUpViewModel.isDoneButtonEnabled.observe(viewLifecycleOwner) {
            binding.btnPersonalDone.isEnabled = it
        }
    }
}
