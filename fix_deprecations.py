import os

directory = r'd:\admission-gk\app\src\main\java\com\example\ui\screens'

replacements = {
    'Icons.Filled.TrendingUp': 'Icons.AutoMirrored.Filled.TrendingUp',
    'Icons.Filled.TrendingDown': 'Icons.AutoMirrored.Filled.TrendingDown',
    'Icons.Filled.TrendingFlat': 'Icons.AutoMirrored.Filled.TrendingFlat',
    'Icons.Filled.ArrowBack': 'Icons.AutoMirrored.Filled.ArrowBack',
    'Icons.Filled.ArrowForward': 'Icons.AutoMirrored.Filled.ArrowForward',
    'Icons.Filled.EventNote': 'Icons.AutoMirrored.Filled.EventNote',
    'Icons.Filled.MenuBook': 'Icons.AutoMirrored.Filled.MenuBook',
    'Icons.Filled.Article': 'Icons.AutoMirrored.Filled.Article',
    'Icons.Filled.List': 'Icons.AutoMirrored.Filled.List',
    'Icons.Filled.Assignment': 'Icons.AutoMirrored.Filled.Assignment',
    'Icons.Filled.Send': 'Icons.AutoMirrored.Filled.Send',
    'Divider(': 'HorizontalDivider('
}

for filename in os.listdir(directory):
    if filename.endswith('.kt'):
        filepath = os.path.join(directory, filename)
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        new_content = content
        for old, new in replacements.items():
            new_content = new_content.replace(old, new)
        
        if 'Icons.AutoMirrored.Filled' in new_content and 'import androidx.compose.material.icons.automirrored.filled.*' not in new_content:
            # find where imports end or just put it after 'import androidx.compose.material.icons.filled.*'
            if 'import androidx.compose.material.icons.filled.*' in new_content:
                new_content = new_content.replace('import androidx.compose.material.icons.filled.*', 'import androidx.compose.material.icons.filled.*\nimport androidx.compose.material.icons.automirrored.filled.*')
            else:
                new_content = 'import androidx.compose.material.icons.automirrored.filled.*\n' + new_content

        if new_content != content:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(new_content)
