import os
import re

color_kt_path = r'd:\admission-gk\app\src\main\java\com\example\ui\theme\Color.kt'
subtopic_path = r'd:\admission-gk\app\src\main\java\com\example\ui\screens\SubTopicDetailScreen.kt'

colors_to_add = """
val SnPrimaryColor = Color(0xFF3525cd)
val SnPrimaryFixedColor = Color(0xFFe0e0ff)
val SnOnPrimaryFixedVariantColor = Color(0xFF141775)
val SnOnPrimaryFixedColor = Color(0xFF00003e)
val SnSecondaryColor = Color(0xFF712ae2)
val SnTertiaryFixedDimColor = Color(0xFFffb783)
val SnTertiaryFixedColor = Color(0xFFffddb8)
val SnOnTertiaryFixedColor = Color(0xFF331200)
val SnSurfaceColor = Color(0xFFf8f9fa)
val SnSurfaceContainerLowestColor = Color(0xFFffffff)
val SnSurfaceContainerLowColor = Color(0xFFf3f4f9)
val SnSurfaceContainerColor = Color(0xFFedeff5)
val SnSurfaceBlueColor = Color(0xFFf0f4f8)
val SnPrimaryContainerColor = Color(0xFF4f46e5)
val SnOnPrimaryContainerColor = Color(0xFF0a0d5c)
val SnOnPrimaryColor = Color(0xFFffffff)
val SnOnSurfaceColor = Color(0xFF0b1c30)
val SnOnSurfaceVariantColor = Color(0xFF464555)
val SnOutlineColor = Color(0xFF73777f)
val SnOutlineVariantColor = Color(0xFFc7c4d8)
val SnBackgroundColor = Color(0xFFf8f9ff)
val SnTertiaryColor = Color(0xFF684000)
"""

with open(color_kt_path, 'a', encoding='utf-8') as f:
    f.write(colors_to_add)

with open(subtopic_path, 'r', encoding='utf-8') as f:
    content = f.read()

content = re.sub(r'^val Sn.*?Color = Color\([^)]+\)\n', '', content, flags=re.MULTILINE)

with open(subtopic_path, 'w', encoding='utf-8') as f:
    f.write(content)

