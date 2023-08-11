package com.hasan.jetfasthub.screens.main.issue

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme

class IssueFragment: Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                JetFastHubTheme {
                    MainContent()
                }
            }
        }
    }

}


@Preview
@Composable
private fun MainContent(){

}
