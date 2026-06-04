import re

file_path = r'd:\admission-gk\app\src\main\java\com\example\ui\screens\QuizScreens.kt'

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

missing_colors = """
val SnPrimaryColor = Color(0xFF2e3192)
val SnPrimaryFixedColor = Color(0xFFe0e0ff)
val SnOnPrimaryFixedVariantColor = Color(0xFF141775)
val SnOnPrimaryFixedColor = Color(0xFF00003e)
val SnSecondaryColor = Color(0xFF00897b)
val SnTertiaryFixedDimColor = Color(0xFFffb783)
val SnTertiaryFixedColor = Color(0xFFffdbca)
val SnOnTertiaryFixedColor = Color(0xFF331200)
val SnSurfaceColor = Color(0xFFf8f9fa)
val SnSurfaceContainerLowestColor = Color(0xFFffffff)
val SnSurfaceContainerLowColor = Color(0xFFf3f4f9)
val SnSurfaceContainerColor = Color(0xFFedeff5)
val SnSurfaceBlueColor = Color(0xFFf0f4f8)
val SnPrimaryContainerColor = Color(0xFFe2e2fb)
val SnOnPrimaryContainerColor = Color(0xFF0a0d5c)
val SnOnPrimaryColor = Color(0xFFffffff)
val SnOnSurfaceColor = Color(0xFF1a1c1e)
val SnOnSurfaceVariantColor = Color(0xFF43474e)
val SnOutlineColor = Color(0xFF73777f)
val SnOutlineVariantColor = Color(0xFFc3c6cf)
"""

missing_imports = """
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.IntrinsicSize
"""

# Insert imports
import_insert_pos = content.find('import androidx.compose.runtime.*')
if import_insert_pos != -1:
    content = content[:import_insert_pos] + missing_imports + "\n" + content[import_insert_pos:]
else:
    # just prepend
    content = missing_imports + "\n" + content

# Insert colors
color_insert_pos = content.find('@OptIn(ExperimentalMaterial3Api::class)\n@Composable\nfun MCQQuizIntroScreen')
if color_insert_pos != -1:
    content = content[:color_insert_pos] + missing_colors + "\n" + content[color_insert_pos:]
else:
    content += "\n" + missing_colors

# Add some missing OutlinedButton imports
if 'androidx.compose.material3.OutlinedButton' not in content:
    content = "import androidx.compose.material3.OutlinedButton\n" + content

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
