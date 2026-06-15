package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.RecentGKEntity
import com.example.ui.GKViewModel
import com.example.ui.components.AdmissionTopBar
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(viewModel: GKViewModel, navController: NavController) {
    val pendingItems by viewModel.pendingRecentGKs.collectAsStateWithLifecycle(initialValue = emptyList())
    val approvedItems by viewModel.approvedRecentGKs.collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedTab by remember { mutableIntStateOf(0) }
    var detailItem by remember { mutableStateOf<RecentGKEntity?>(null) }

    Scaffold(
        topBar = {
            AdmissionTopBar(
                title = "Admin Panel",
                subtitle = "কন্টেন্ট ম্যানেজমেন্ট",
                showBack = true,
                onBack = { navController.popBackStack() },
                useGradient = true
            )
        },
        containerColor = AppBackground
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatsCard("পেন্ডিং", pendingItems.size.toString(), ErrorColor, Modifier.weight(1f))
                StatsCard("অ্যাপ্রুভড", approvedItems.size.toString(), SuccessColor, Modifier.weight(1f))
                StatsCard("মোট", (pendingItems.size + approvedItems.size).toString(), BrandPrimary, Modifier.weight(1f))
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = BrandPrimary,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.PendingActions, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("পেন্ডিং (${pendingItems.size})", fontWeight = FontWeight.Bold)
                    }
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CheckCircle, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("অ্যাপ্রুভড (${approvedItems.size})", fontWeight = FontWeight.Bold)
                    }
                }
            }

            val items = if (selectedTab == 0) pendingItems else approvedItems

            if (items.isEmpty()) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(if (selectedTab == 0) Icons.Filled.Inbox else Icons.Filled.DoneAll, null, tint = TextMuted, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(12.dp))
                        Text(
                            if (selectedTab == 0) "কোন পেন্ডিং সাবমিশন নেই" else "কোন অ্যাপ্রুভড কন্টেন্ট নেই",
                            style = MaterialTheme.typography.bodyLarge, color = TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { item ->
                        AdminSubmissionCard(
                            item = item,
                            onApprove = { viewModel.approveRecentGK(item.id) },
                            onReject = { viewModel.rejectRecentGK(item.id) },
                            onDelete = { viewModel.deleteRecentGK(item.id) },
                            onView = { detailItem = item }
                        )
                    }
                }
            }
        }

        // Detail dialog
        detailItem?.let { item ->
            AlertDialog(
                onDismissRequest = { detailItem = null },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (item.category == "BD") BdGkColor.copy(alpha = 0.1f) else IntGkColor.copy(alpha = 0.1f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Article, null, tint = if (item.category == "BD") BdGkColor else IntGkColor, modifier = Modifier.size(24.dp))
                        }
                    }
                        Spacer(Modifier.width(12.dp))
                        Text("সাবমিশন ডিটেইল", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        DetailRow("টপিক", item.topicTitle)
                        DetailRow("ক্যাটাগরি", if (item.category == "BD") "বাংলাদেশ" else "আন্তর্জাতিক")
                        DetailRow("বিশেষ তথ্য", item.specialTopicNote.ifBlank { "N/A" })
                        DetailRow("সোর্স", item.sourceText.ifBlank { "N/A" })
                        DetailRow("কন্ট্রিবিউটর", item.contributorName.ifBlank { "Anonymous" })
                        DetailRow("তারিখ", SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale("bn")).format(Date(item.createdAt)))
                        DetailRow("স্ট্যাটাস", item.status)
                    }
                },
                confirmButton = {
                    if (item.status == "PENDING") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.rejectRecentGK(item.id)
                                    detailItem = null
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                                shape = RoundedCornerShape(12.dp)
                            ) { Text("রিজেক্ট", fontWeight = FontWeight.Bold) }
                            Button(
                                onClick = {
                                    viewModel.approveRecentGK(item.id)
                                    detailItem = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) { Text("অ্যাপ্রুভ করুন", fontWeight = FontWeight.Bold) }
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { detailItem = null }) { Text("বন্ধ করুন", color = TextMuted) }
                }
            )
        }
    }
}

@Composable
private fun StatsCard(title: String, value: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextMuted, maxLines = 1)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, color = color, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun AdminSubmissionCard(
    item: RecentGKEntity,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onDelete: () -> Unit,
    onView: () -> Unit,
) {
    val isBd = item.category == "BD"
    val accent = if (isBd) BdGkColor else IntGkColor

    Card(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(18.dp),
        CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, AppOutlineSoft)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = accent.copy(alpha = 0.08f),
                    border = BorderStroke(0.5.dp, accent.copy(alpha = 0.12f))
                ) {
                    Text(
                        if (isBd) "\uD83C\uDDE7\uD83C\uDDE9 বাংলাদেশ" else "\uD83C\uDF0D আন্তর্জাতিক",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = accent,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = when (item.status) {
                        "PENDING" -> WarningColor.copy(alpha = 0.1f)
                        "APPROVED" -> SuccessColor.copy(alpha = 0.1f)
                        else -> ErrorColor.copy(alpha = 0.1f)
                    }
                ) {
                    Text(
                        item.status,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = when (item.status) { "PENDING" -> WarningColor; "APPROVED" -> SuccessColor; else -> ErrorColor },
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(item.topicTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary, maxLines = 2, overflow = TextOverflow.Ellipsis)

            if (item.specialTopicNote.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(item.specialTopicNote, style = MaterialTheme.typography.bodySmall, color = TextSecondary, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }

            Spacer(Modifier.height(10.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // View detail
                OutlinedButton(
                    onClick = onView,
                    modifier = Modifier.weight(1f).height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted),
                    border = BorderStroke(1.dp, AppOutlineSoft)
                ) {
                    Text("বিস্তারিত", fontSize = 13.sp, maxLines = 1)
                }

                if (item.status == "PENDING") {
                    // Reject
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                        border = BorderStroke(1.dp, ErrorColor.copy(alpha = 0.4f))
                    ) {
                        Text("রিজেক্ট", fontSize = 13.sp, maxLines = 1)
                    }

                    // Approve
                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) {
                        Text("অ্যাপ্রুভ", fontSize = 13.sp, maxLines = 1)
                    }

                    // Delete
                    IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Filled.Delete, "Delete", tint = TextMuted, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            "$label:",
            modifier = Modifier.width(100.dp),
            style = MaterialTheme.typography.labelMedium,
            color = TextMuted,
            fontWeight = FontWeight.Bold
        )
        Text(value, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
    }
}
