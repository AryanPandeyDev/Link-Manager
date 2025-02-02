package com.example.linkmanager

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.linkmanager.data.Link
import com.example.linkmanager.ui.theme.LinkManagerTheme
import com.example.linkmanager.ui.theme.LinkViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinkManagerTheme {
                val linkViewModel = hiltViewModel<LinkViewModel>()
                val links by linkViewModel.allLink.collectAsState(emptyList())
                LinkManager(links)
            }
        }
    }
}


@Composable
fun LinkManager(links : List<Link>) {
    var dialogBoxVisibility by remember { mutableStateOf(false) }
    var updateDialogBoxVisibility by remember { mutableStateOf(false) }
    var linkToBeUpdated by remember { mutableStateOf(Link("","")) }
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                shape = RectangleShape,
            ){
                Text(
                    "Link Manager",
                    modifier = Modifier.align(Alignment.Start)
                        .padding(top = 40.dp, start = 5.dp)
                        .height(50.dp),
                    textAlign = TextAlign.Left,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            GradientDivider()
            LazyColumn(
                Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                items(links) {
                    LinkItem(it,{
                        updateDialogBoxVisibility = true
                        linkToBeUpdated = it
                    })
                }
            }

        }
        Box(modifier = Modifier
            .wrapContentSize()
            .padding(bottom = 26.dp, end = 10.dp)
            .align(Alignment.BottomEnd))
        {
            AddButton{
                dialogBoxVisibility = true
                linkToBeUpdated = Link("","")
            }
        }
        if (dialogBoxVisibility || updateDialogBoxVisibility){
            AddLinkDialog(
                link = linkToBeUpdated,
                isUpdate = updateDialogBoxVisibility
            ) {
                dialogBoxVisibility = false
                updateDialogBoxVisibility = false
            }
        }
    }
}

@Composable
fun LinkItem(link: Link,update: () -> Unit = {}) {
    val context = LocalContext.current
    val linkViewModel = hiltViewModel<LinkViewModel>()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(2.dp,
            color = MaterialTheme.colorScheme.outline)
    ) {
        Row (Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
                    .fillMaxWidth(0.7f)
                    .padding(start = 15.dp)
                    .clickable {
                        val url = when {
                            link.link.startsWith("http://") || link.link.startsWith("https://") -> link.link
                            link.link.matches(Regex(".*\\.[a-z]{2,}(/.*)?")) -> "https://${link.link}"
                            else -> "https://www.google.com/search?q=${URLEncoder.encode(link.link, "UTF-8")}"
                        }

                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
            ) {
                Spacer(Modifier.height(3.dp))
                Text(
                    text = link.linkName,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = link.link,
                    fontSize = 13.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Update",
                modifier = Modifier.size(40.dp)
                    .padding(end = 10.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { update() }
            )
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Add",
                modifier = Modifier.size(40.dp)
                    .padding(end = 10.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { linkViewModel.deleteLink(link) }
            )
        }
    }
}

@Composable
fun AddLinkDialog(
    link: Link = Link("",""),
    isUpdate : Boolean = false,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val linkViewModel = hiltViewModel<LinkViewModel>()
    var linkName by remember { mutableStateOf(link.linkName) }
    var linkUrl by remember { mutableStateOf(link.link) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (!isUpdate)"Add Link" else "Update Link") },
        text = {
            Column {
                TextField(
                    value = linkName,
                    onValueChange = { linkName = it },
                    label = { Text("Link Name") }
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
                TextField(
                    value = linkUrl,
                    onValueChange = { linkUrl = it },
                    label = { Text("Link URL") }
                )
            }
        },
        confirmButton = {
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(onClick = onDismiss ) {
                    Text("Cancel",Modifier.width(48.dp),
                        textAlign = TextAlign.Center)
                }
                Button(onClick = {
                    if (!isUpdate) {
                        if (linkName.isEmpty() || linkUrl.isEmpty()) {
                            Toast.makeText(context,"Please Specify all the remaining fields",Toast.LENGTH_SHORT).show()
                        }else {
                            val addLink = Link(linkName,linkUrl)
                            linkViewModel.addLink(addLink)
                            onDismiss()
                        }
                    }else {
                        if (linkName.isEmpty() || linkUrl.isEmpty()) {
                            Toast.makeText(context,"Please Specify all the remaining fields",Toast.LENGTH_SHORT).show()
                        }else {
                            val updateLink = Link(linkName,linkUrl,link.id)
                            linkViewModel.updateLink(updateLink)
                            onDismiss()
                        }
                    }
                }) {
                    Text(if (!isUpdate) "Add" else "Update", modifier = Modifier.width(48.dp),
                        textAlign = TextAlign.Center)
                }
            }
        },
        dismissButton = {

        }
    )
}

@Composable
fun GradientDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.onBackground,
                        MaterialTheme.colorScheme.background)
                )
            )
    )
}

@Composable
fun AddButton(onClick: ()-> Unit = {}) {
    val buttonSize = 80.dp
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .size(buttonSize) // Standard FAB size
            ,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            modifier = Modifier.size(50.dp)
        )
    }
}


@Preview(showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun DarkTaskManagerPreview() {
    val mockLinks = listOf(
        Link(linkName = "Google", link = "https://google.com"),
        Link(linkName = "GitHub", link = "https://github.com"),
        Link(linkName = "Jetpack Compose", link = "https://developer.android.com/jetpack/compose"),
        Link(linkName = "Stack Overflow", link = "https://stackoverflow.com"),
        Link(linkName = "YouTube", link = "https://youtube.com"),
        Link(linkName = "Twitter", link = "https://twitter.com"),
        Link(linkName = "Reddit", link = "https://reddit.com"),
        Link(linkName = "Medium", link = "https://medium.com"),
        Link(linkName = "Kotlin Docs", link = "https://kotlinlang.org/docs/home.html"),
        Link(linkName = "Android Developers", link = "https://developer.android.com"),
        Link(linkName = "Netflix", link = "https://netflix.com"))
    LinkManagerTheme {
        LinkManager(mockLinks)
    }
}

@Preview(showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun TaskManagerPreview() {
    val mockLinks = listOf(
        Link(linkName = "Google", link = "https://google.com"),
        Link(linkName = "GitHub", link = "https://github.com"),
        Link(linkName = "Jetpack Compose", link = "https://developer.android.com/jetpack/compose"),
        Link(linkName = "Stack Overflow", link = "https://stackoverflow.com"),
        Link(linkName = "YouTube", link = "https://youtube.com"),
        Link(linkName = "Twitter", link = "https://twitter.com"),
        Link(linkName = "Reddit", link = "https://reddit.com"),
        Link(linkName = "Medium", link = "https://medium.com"),
        Link(linkName = "Kotlin Docs", link = "https://kotlinlang.org/docs/home.html"),
        Link(linkName = "Android Developers", link = "https://developer.android.com"),
        Link(linkName = "Netflix", link = "https://netflix.com"))
    LinkManagerTheme {
        LinkManager(mockLinks)
    }
}


@Composable
fun AddLinkDialogPreview() {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text("Add Link") },
        text = {
            Column {
                // Link Name TextField
                TextField(
                    value = "", // Empty value for preview
                    onValueChange = { /* Do nothing */ },
                    label = { Text("Link Name") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                // Link URL TextField
                TextField(
                    value = "", // Empty value for preview
                    onValueChange = { /* Do nothing */ },
                    label = { Text("Link URL") }
                )

            }
        },
        confirmButton = {
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(onClick = { /* Do nothing */ }) {
                    Text("Add", modifier = Modifier.width(45.dp),
                        textAlign = TextAlign.Center)
                }
                Button(onClick = { /* Do nothing */ }) {
                    Text("Cancel",Modifier.width(45.dp),
                        textAlign = TextAlign.Center)
                }
            }
        },
        dismissButton = {

        }
    )
}

@Composable
@Preview(showBackground = true)
fun DialogBox() {
    LinkManagerTheme { AddLinkDialogPreview() }
}