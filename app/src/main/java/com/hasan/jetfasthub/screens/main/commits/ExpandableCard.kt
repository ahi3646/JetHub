package com.hasan.jetfasthub.screens.main.commits

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.screens.main.commits.models.commit_model.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableCard(file: File, onAction: (String, String?) -> Unit) {
    var expandableState by remember {
        mutableStateOf(false)
    }
    val rotationState by animateFloatAsState(targetValue = if (expandableState) 180f else 0f)
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = ShapeDefaults.Small,
        elevation = 12.dp,
        onClick = {
            expandableState = !expandableState
        }
    ) {
        Column {

            Row(
                modifier = Modifier.padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                Text(text = file.filename)

                Spacer(Modifier.weight(1F))

                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                    onClick = { expandableState = !expandableState }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dropdown_icon),
                        contentDescription = "dropdown icon"
                    )
                }

                Box {
                    IconButton(
                        onClick = {
                            showMenu = !showMenu
                        },
                    ) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "more option")
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        DropdownMenuItem(text = { Text(text = "Open") }, onClick = {
                            onAction("browser", file.blob_url)
                            showMenu = false
                        })
                        DropdownMenuItem(text = { Text(text = "Download") }, onClick = {
                            onAction("download", file.blob_url)
                            showMenu = false
                        })
                        DropdownMenuItem(text = { Text(text = "Share") }, onClick = {
                            onAction("share", file.blob_url)
                            showMenu = false
                        })

                        DropdownMenuItem(text = { Text(text = "Copy URL") },
                            onClick = {
                                onAction("copy", file.blob_url)
                                showMenu = false
                            })

                    }
                }

            }

            if (expandableState) {
                Column(
                    modifier = Modifier
                        .height(72.dp)
                        .fillMaxWidth()
                ) {

                }
            }

            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .height(0.5.dp)
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Changes")
                    Text(text = file.changes.toString())
                }
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Addition")
                    Text(text = file.additions.toString())
                }
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Deletion")
                    Text(text = file.deletions.toString())
                }
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Status")
                    Text(text = file.status, fontWeight = FontWeight.Bold)
                }
            }

        }
    }


}