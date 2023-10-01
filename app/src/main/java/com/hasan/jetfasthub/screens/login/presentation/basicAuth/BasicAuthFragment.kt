package com.hasan.jetfasthub.screens.login.presentation.basicAuth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hasan.jetfasthub.core.ui.extensions.executeWithLifecycle
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * BasicAuth Fragment
 *
 * @author Anorov Hasan on 30/09/2023
 */

class BasicAuthFragment : Fragment() {

    private val viewModel: BasicAuthViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setDecorFitsSystemWindows(false)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.setDecorFitsSystemWindows(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val localFocusManager = LocalFocusManager.current
                val state by viewModel.uiState.collectAsState()
                JetFastHubTheme {
                    BasicAuthScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                            .background(color = JetHubTheme.colors.background.secondary)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    localFocusManager.clearFocus()
                                })
                            },
                        state = state,
                        intentReducer = { viewModel.handleIntents(it) },
                    )
                }
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

    private fun executeNavigation(navigation: BasicAuthScreenNavigation){
        when(navigation){
            BasicAuthScreenNavigation.GoPreviousScreen -> findNavController().popBackStack()
            is BasicAuthScreenNavigation.BrowserAuth -> {
                requireContext().startActivity(
                    Intent(Intent.ACTION_VIEW, navigation.uri)
                )
            }
        }
    }

}