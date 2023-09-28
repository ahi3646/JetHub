package com.hasan.jetfasthub.screens.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.executeWithLifecycle
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.utils.Constants
import com.hasan.jetfasthub.screens.login.ui.LoginChooserNavigation
import com.hasan.jetfasthub.screens.login.ui.LoginChooserScreen
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LoginChooserFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModel()

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
                        state = state.isFetchingUserData,
                        intentReducer = ::handleIntents
                    )
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (view as ComposeView).setContent {
//            val state by viewModel.state.collectAsState()
//            JetFastHubTheme {
//                LoginChooserScreen(
//                    state = state.isFetchingUserData,
//                    intentReducer = ::handleIntents
//                )
//            }
//        }
        viewModel.screenNavigation.executeWithLifecycle(
            lifecycle = viewLifecycleOwner.lifecycle,
            action = ::executeNavigation
        )
    }

    private fun handleIntents(intent: LoginChooserClickIntents){
        when(intent){
            LoginChooserClickIntents.BasicAuthentication -> {
                findNavController().navigate(R.id.action_loginChooserFragment_to_basicAuthFragment)
            }
            LoginChooserClickIntents.OAuthCLick -> {
                requireContext().startActivity(
                    Intent(Intent.ACTION_VIEW, getAuthorizationUrl())
                )
            }
        }
    }

    private fun getAuthorizationUrl(): Uri {
        return Uri.Builder()
            .scheme("https")
            .authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", Constants.CLIENT_ID)
            .appendQueryParameter("redirect_uri", Constants.REDIRECT_URL)
            .appendQueryParameter("scope", Constants.SCOPE)
            .appendQueryParameter("state", Constants.STATE)
            .build()
    }

    private fun executeNavigation(navigation: LoginChooserNavigation) {
        when (navigation) {
            LoginChooserNavigation.BasicAuth -> {
                findNavController().navigate(R.id.action_loginChooserFragment_to_basicAuthFragment)
            }
        }
    }

}

