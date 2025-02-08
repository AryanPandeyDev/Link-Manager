package com.example.linkmanager

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.linkmanager.data.Link
import com.example.linkmanager.data.LinkCategory
import com.example.linkmanager.ui.theme.LinkManagerTheme
import com.example.linkmanager.ui.theme.LinkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Delay
import java.net.URLEncoder


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinkManagerTheme {
                LinkManager()
            }
        }
    }
}


@Composable
fun LinkManager() {
    val linkViewModel = hiltViewModel<LinkViewModel>()
    var currentCategory by remember { mutableStateOf("Default") }
    val links by linkViewModel.allLink(currentCategory).collectAsState(emptyList())
    val allLinks by linkViewModel.allDefaultLinks.collectAsState(emptyList())
    val category by linkViewModel.allCategory.collectAsState(emptyList())
    var categoryDialogBoxVisibility by remember { mutableStateOf(false) }
    var updateCategoryDialogVisibility by remember { mutableStateOf(false) }
    var dialogBoxVisibility by remember { mutableStateOf(false) }
    var updateDialogBoxVisibility by remember { mutableStateOf(false) }
    var linkToBeUpdated by remember { mutableStateOf(Link("","")) }
    var slidingMenuVisibility by rememberSaveable { mutableStateOf(false)  }
    var categoryToUpdated by remember { mutableStateOf(LinkCategory("")) }
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
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item {
                    CategoryChip(
                        text = "All Links",
                        isSelected = currentCategory == "Default",
                        onClick = {
                            currentCategory = "Default"
                        }
                    )
                }
                items(category) {
                    CategoryChip(
                        text = it.category,
                        isSelected = currentCategory == it.category,
                        onClick = {
                            currentCategory = it.category
                            categoryToUpdated = it
                        }
                    )
                }

                item {
                    IconButton(
                        onClick = {
                            categoryDialogBoxVisibility = true
                            categoryToUpdated = LinkCategory("")
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Category",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            Box(Modifier.fillMaxWidth()
                .wrapContentHeight()) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Add Category",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.align(Alignment.CenterEnd)
                        .size(35.dp)
                        .padding(end = 3.dp)
                        .clickable {
                            Log.d("category", currentCategory)
                            if (currentCategory != "Default")  slidingMenuVisibility = true
                        }
                )
            }
            LazyColumn(
                Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                items(
                    if (currentCategory == "Default") allLinks else links
                ) {
                    LinkItem(it) {
                        updateDialogBoxVisibility = true
                        linkToBeUpdated = it
                    }
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
        if (categoryDialogBoxVisibility || updateCategoryDialogVisibility) {
            CategoryDialog(
                links,
                updateCategoryDialogVisibility,
                categoryToUpdated,
                { currentCategory = it }
            ){
                categoryDialogBoxVisibility = false
                updateCategoryDialogVisibility = false
            }
        }
        if (dialogBoxVisibility || updateDialogBoxVisibility){
            AddLinkDialog(
                link = linkToBeUpdated,
                isUpdate = updateDialogBoxVisibility,
                currentCategory
            ) {
                dialogBoxVisibility = false
                updateDialogBoxVisibility = false
            }
        }
        SlidingMenu(
            slidingMenuVisibility,
            modifier = Modifier.align(Alignment.BottomCenter),
            onUpdateCategoryClick = {
                updateCategoryDialogVisibility = true
            },
            onDeleteCategoryClick = {
                linkViewModel.deleteCategory(categoryToUpdated)
                currentCategory = "Default"
            },
            changeVisibility = {slidingMenuVisibility = false}
        )
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
    category : String,
    onDismiss: () -> Unit,
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
                            val addLink = Link(linkName,linkUrl,category)
                            linkViewModel.addLink(addLink)
                            onDismiss()
                        }
                    }else {
                        if (linkName.isEmpty() || linkUrl.isEmpty()) {
                            Toast.makeText(context,"Please Specify all the remaining fields",Toast.LENGTH_SHORT).show()
                        }else {
                            val updateLink = Link(linkName,linkUrl,category, id = link.id)
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
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            modifier = Modifier.size(50.dp)
        )
    }
}




@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.dp,
            if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.outline
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
@Preview(showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
    )
fun DialogBox() {
    LinkManagerTheme {
        var state by rememberSaveable { mutableStateOf(true) }
        Box(
           modifier = Modifier.fillMaxWidth()
        ){
            SlidingMenu(
                visible = state,
                modifier = Modifier.align(Alignment.BottomCenter),
                onUpdateCategoryClick = {},
                onDeleteCategoryClick = {}
            ) {
                state = false
            }
        }
    }
}


@Composable
fun CategoryDialog(
    link: List<Link>,
    isUpdate: Boolean,
    category: LinkCategory = LinkCategory(""),
    changeCategory : (String) -> Unit,
    onDismiss: () -> Unit = {},
) {
    val context = LocalContext.current
    var categoryName by remember { mutableStateOf(category.category) }
    val linkViewModel = hiltViewModel<LinkViewModel>()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (!isUpdate)"Add Category" else "Update Category") },
        text = {
            Column {
                TextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        confirmButton = {
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(onClick = onDismiss) {
                    Text("Cancel",Modifier.width(45.dp),
                        textAlign = TextAlign.Center)
                }
                Button(onClick = {
                    if (!isUpdate) {
                        if (categoryName.isEmpty()) {
                            Toast.makeText(context,"Please specify the name of the category",Toast.LENGTH_SHORT).show()
                        }else {
                            linkViewModel.addCategory(LinkCategory(categoryName))
                            onDismiss()
                        }
                    }else{
                        if (categoryName.isEmpty()) {
                            Toast.makeText(context,"Please specify the name of the category",Toast.LENGTH_SHORT).show()
                        }else {
                            link.forEach { linkViewModel.updateLink(Link(it.linkName,it.link,categoryName,it.id)) }
                            linkViewModel.updateCategory(LinkCategory(categoryName, categoryId = category.categoryId))
                            changeCategory(categoryName)
                            onDismiss()
                        }
                    }
                }) {
                    Text(if (!isUpdate) "Add" else "Update", modifier = Modifier.width(45.dp),
                        textAlign = TextAlign.Center)
                }
            }
        },
        dismissButton = {
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlidingMenu(
    visible : Boolean,
    modifier: Modifier = Modifier,
    onUpdateCategoryClick: () -> Unit,
    onDeleteCategoryClick: () -> Unit,
    changeVisibility: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()
    if (visible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = changeVisibility
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer, // Softer color
                    )
                    .padding(horizontal = 8.dp)
            ) {
                // Update Category Option
                Text(
                    text = "Update Category",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onUpdateCategoryClick()
                            changeVisibility()
                        }
                        .padding(vertical = 15.dp),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Center
                )

                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

                // Delete Category Option
                Text(
                    text = "Delete Category",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDeleteCategoryClick()
                            changeVisibility()
                        }
                        .padding(top = 12.dp, bottom = 20.dp),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

