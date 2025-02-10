package com.example.linkmanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.linkmanager.data.Link
import com.example.linkmanager.data.LinkCategory
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
    var linkToBeUpdated by remember { mutableStateOf(Link("", "")) }
    var slidingMenuVisibility by rememberSaveable { mutableStateOf(false) }
    var categoryToUpdated by remember { mutableStateOf(LinkCategory("")) }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Enhanced Header
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp)
            ) {
                Text(
                    "Link Manager",
                    modifier = Modifier
                        .padding(top = 38.dp, start = 9.dp, end = 9.dp)
                        .height(55.dp),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

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
                            .clip(CircleShape)
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
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

            Box(
                Modifier.fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Add Category",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.align(Alignment.CenterEnd)
                        .size(35.dp)
                        .padding(end = 3.dp)
                        .clickable {
                            Log.d("category", currentCategory)
                            if (currentCategory != "Default") slidingMenuVisibility = true
                        }
                )
            }

            LazyColumn(
                Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                items(
                    if (currentCategory == "Default") allLinks else links
                ) {
                    LinkItem(it) {
                        updateDialogBoxVisibility = true
                        linkToBeUpdated = it
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 26.dp, end = 10.dp)
                .align(Alignment.BottomEnd)
        ) {
            AddButton {
                dialogBoxVisibility = true
                linkToBeUpdated = Link("", "")
            }
        }

        if (categoryDialogBoxVisibility || updateCategoryDialogVisibility) {
            CategoryDialog(
                links,
                updateCategoryDialogVisibility,
                categoryToUpdated,
                { currentCategory = it }
            ) {
                categoryDialogBoxVisibility = false
                updateCategoryDialogVisibility = false
            }
        }

        if (dialogBoxVisibility || updateDialogBoxVisibility) {
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
            onUpdateCategoryClick = {
                updateCategoryDialogVisibility = true
            },
            onDeleteCategoryClick = {
                linkViewModel.deleteCategory(categoryToUpdated)
                currentCategory = "Default"
            },
            changeVisibility = { slidingMenuVisibility = false }
        )
    }
}

@Composable
fun LinkItem(link: Link, update: () -> Unit = {}) {
    val context = LocalContext.current
    val linkViewModel = hiltViewModel<LinkViewModel>()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(2.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
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
                Text(
                    text = link.linkName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = link.link,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { update() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.background,
                        )
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Update",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                IconButton(
                    onClick = { linkViewModel.deleteLink(link) },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.errorContainer,
                        )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
fun AddButton(onClick: () -> Unit = {}) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = Modifier
            .padding(16.dp)
            .size(64.dp)
            .shadow(8.dp, shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            modifier = Modifier.size(32.dp)
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
            .shadow(2.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun AddLinkDialog(
    link: Link = Link("", ""),
    isUpdate: Boolean = false,
    category: String,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val linkViewModel = hiltViewModel<LinkViewModel>()
    var linkName by remember { mutableStateOf(link.linkName) }
    var linkUrl by remember { mutableStateOf(link.link) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (!isUpdate) "Add Link" else "Update Link") },
        text = {
            Column {
                OutlinedTextField(
                    value = linkName,
                    onValueChange = { linkName = it },
                    label = { Text("Link Name") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
                OutlinedTextField(
                    value = linkUrl,
                    onValueChange = { linkUrl = it },
                    label = { Text("Link URL") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Cancel")
                }

                TextButton(
                    onClick = {
                        if (!isUpdate) {
                            if (linkName.isEmpty() || linkUrl.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Please Specify all the remaining fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val addLink = Link(linkName, linkUrl, category)
                                linkViewModel.addLink(addLink)
                                onDismiss()
                            }
                        } else {
                            if (linkName.isEmpty() || linkUrl.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Please Specify all the remaining fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val updateLink = Link(linkName, linkUrl, category, id = link.id)
                                linkViewModel.updateLink(updateLink)
                                onDismiss()
                            }
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (!isUpdate) "Add" else "Update")
                }
            }
        },
        dismissButton = {}
    )
}

@Composable
fun CategoryDialog(
    link: List<Link>,
    isUpdate: Boolean,
    category: LinkCategory = LinkCategory(""),
    changeCategory: (String) -> Unit,
    onDismiss: () -> Unit = {},
) {
    val context = LocalContext.current
    var categoryName by remember { mutableStateOf(category.category) }
    val linkViewModel = hiltViewModel<LinkViewModel>()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (!isUpdate) "Add Category" else "Update Category") },
        text = {
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") },
                    shape = RoundedCornerShape(12.dp),
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
                    Text(if (!isUpdate) "Add" else "Update", modifier = Modifier.width(48.dp),
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
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Category Options",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Update menu items with icons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onUpdateCategoryClick()
                            changeVisibility()
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.width(16.dp))
                    Text("Update Category", style = MaterialTheme.typography.bodyLarge)
                }

                // Delete Category Option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDeleteCategoryClick()
                            changeVisibility()
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.width(16.dp))
                    Text("Delete Category", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }

}

@Composable
@Preview(showSystemUi = true)
fun LinkManagerUI() {
    LinkManagerTheme {
        LinkManager()
    }
}