import os

filepath = r'd:\admission-gk\app\src\main\java\com\example\ui\screens\HomeScreen.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('import androidx.compose.foundation.lazy.items', 'import androidx.compose.foundation.lazy.items\nimport androidx.compose.animation.core.animateFloatAsState\nimport androidx.compose.foundation.interaction.MutableInteractionSource\nimport androidx.compose.foundation.interaction.collectIsPressedAsState\nimport androidx.compose.ui.draw.scale\nimport androidx.compose.foundation.shape.CircleShape\nimport androidx.compose.foundation.LocalIndication')

# Update Top padding and structure
old_lazy = '''        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {'''
new_lazy = '''        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {'''
content = content.replace(old_lazy, new_lazy)

# Header & Search Bar
old_header = '''            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(40.dp).padding(end = 12.dp),
                            shape = RoundedCornerShape(999.dp),
                            color = primary.copy(alpha = 0.1f)
                        ) {
                            Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", tint = primary)
                        }
                        Column {
                            Text("Admission GK", style = MaterialTheme.typography.titleLarge, color = primary, fontWeight = FontWeight.Bold)
                            Text("Ready for today's GK practice?", style = MaterialTheme.typography.labelSmall, color = onSurfaceVariant)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = primaryContainer,
                    ) {
                        Text("🔥 7 Days", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium, color = onPrimaryContainer, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Search Bar
            item {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search topics, MCQ or questions", color = TextMuted) },
                        trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = primary) },
                        shape = RoundedCornerShape(999.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }
            }'''
new_header = '''            // Header & Search Bar Full Bleed
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(Brush.linearGradient(listOf(BrandGradientStart, BrandGradientEnd)))
                        .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 32.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    modifier = Modifier.size(48.dp),
                                    shape = CircleShape,
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", tint = Color.White, modifier = Modifier.padding(8.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Admission GK", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Ready for today's practice?", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha=0.8f))
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = Color.White.copy(alpha = 0.2f),
                            ) {
                                Text("🔥 7 Days", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color.White,
                            shadowElevation = 8.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Search topics, MCQ or questions", color = TextMuted) },
                                trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = BrandGradientStart) },
                                shape = RoundedCornerShape(999.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                singleLine = true
                            )
                        }
                    }
                }
            }'''
content = content.replace(old_header, new_header)

# Add padding to other items
content = content.replace('item {\n                Row(modifier = Modifier.fillMaxWidth(),', 'item {\n                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),')

# Wrap MegaQuizCard
content = content.replace('''            // Mega Quiz Card
            item {
                MegaQuizCard(
                    onClick = { navController.navigate(MegaQuizRoutineRoute) },
                    primaryContainer = primaryContainer,
                    errorColor = errorColor
                )
            }''', '''            // Mega Quiz Card
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    MegaQuizCard(
                        onClick = { navController.navigate(MegaQuizRoutineRoute) },
                        primaryContainer = BrandGradientStart,
                        errorColor = WrongColor
                    )
                }
            }''')

# Wrap RecentGkSections
content = content.replace('item {\n                RecentGkSection(title = "Recent GK (BD)"', 'item {\n                Box(modifier = Modifier.padding(horizontal = 16.dp)) { RecentGkSection(title = "Recent GK (BD)"')
content = content.replace('onClickAll = { navController.navigate(RecentGKRoute) })\n            }', 'onClickAll = { navController.navigate(RecentGKRoute) }) }\n            }')

content = content.replace('item {\n                RecentGkSection(title = "Recent GK (International)"', 'item {\n                Box(modifier = Modifier.padding(horizontal = 16.dp)) { RecentGkSection(title = "Recent GK (International)"')

# Wrap QuestionBankCard
content = content.replace('item {\n                QuestionBankCard', 'item {\n                Box(modifier = Modifier.padding(horizontal = 16.dp)) { QuestionBankCard')
content = content.replace('navController.navigate(QuestionBankRoute(null, null)) }\n            }', 'navController.navigate(QuestionBankRoute(null, null)) } }\n            }')

# Wrap DashboardSummaryMenu
content = content.replace('item {\n                DashboardSummaryMenu()\n            }', 'item {\n                Box(modifier = Modifier.padding(horizontal = 16.dp)) { DashboardSummaryMenu() }\n            }')

# Fix CategoryCard Component
old_cat = '''@Composable
fun CategoryCard(modifier: Modifier = Modifier, title: String, subtitle: String, buttonText: String, topicsCount: String, gradient: Brush, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {'''
new_cat = '''@Composable
fun CategoryCard(modifier: Modifier = Modifier, title: String, subtitle: String, buttonText: String, topicsCount: String, gradient: Brush, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = LocalIndication.current) { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {'''
content = content.replace(old_cat, new_cat)

# Use new gradients in GK Category Cards
content = content.replace('gradient = Brush.linearGradient(listOf(Color(0xFF262626), Color(0xFF5A4D29)))', 'gradient = Brush.linearGradient(listOf(BdGkStart, BdGkEnd))')
content = content.replace('gradient = Brush.linearGradient(listOf(Color(0xFF10182B), Color(0xFF1D3261)))', 'gradient = Brush.linearGradient(listOf(IntGkStart, IntGkEnd))')

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
