import os
import re

file_path = r'd:\admission-gk\app\src\main\java\com\example\ui\screens\QuizScreens.kt'

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Delete lines matching val SnXXXColor = Color(...)
content = re.sub(r'^val Sn.*?Color = Color\([^)]+\)\n', '', content, flags=re.MULTILINE)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
