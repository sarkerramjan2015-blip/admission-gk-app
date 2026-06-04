import os

directory = r'd:\admission-gk\app\src\main\java\com\example\ui\screens'
bad_import = 'import androidx.compose.material.icons.automirrored.filled.*\n'

for filename in os.listdir(directory):
    if filename.endswith('.kt'):
        filepath = os.path.join(directory, filename)
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        if content.startswith(bad_import):
            # Remove from start
            new_content = content[len(bad_import):]
            # Add it after the package statement
            package_str = 'package com.example.ui.screens\n'
            if new_content.startswith(package_str):
                new_content = new_content.replace(package_str, package_str + '\n' + bad_import, 1)
            
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(new_content)
