package com.hasan.jetfasthub.screens.login.presentation.loginPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.executeWithLifecycle
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.login.presentation.LoginViewModel
import com.hasan.jetfasthub.screens.main.AppActivity
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * LoginChooser Fragment
 *
 * @author Anorov Hasan on 30/09/2023
 */

class LoginChooserFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        // Dispose of the Composition when the view's LifecycleOwner
        // is destroyed
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            val state by viewModel.state.collectAsState()
            JetFastHubTheme {
                LoginChooserScreen(
                    state = state.loadingPageStatus,
                    intentReducer = {
                        viewModel.handleIntents(it)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = JetHubTheme.colors.background.secondary)
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.screenNavigation.executeWithLifecycle(
            lifecycle = viewLifecycleOwner.lifecycle,
            action = ::executeNavigation
        )
    }

    private fun executeNavigation(navigation: LoginChooserNavigation) {
        when (navigation) {
            LoginChooserNavigation.BasicAuth -> {
                findNavController().navigate(R.id.action_loginChooserFragment_to_basicAuthFragment)
            }

            is LoginChooserNavigation.OAuth -> {
                requireContext().startActivity(
                    Intent(Intent.ACTION_VIEW, navigation.uri)
                )
            }

            LoginChooserNavigation.NavigateToMain -> {
                Intent(requireActivity(), AppActivity::class.java).also { intent ->
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                activity?.finish()
            }
        }
    }
}

