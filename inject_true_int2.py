import os

path = r'd:\admission-gk\app\src\main\java\com\example\data\SeedData.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Replace IntlTopic2Seed.getSubtopics() with TrueIntlTopic2Seed.getSubtopics()
# Note: we might have IntlTopic2Seed.getSubtopics() in the list.
content = content.replace('IntlTopic2Seed.getSubtopics()', 'TrueIntlTopic2Seed.getSubtopics()')

# Also comment out original mangled int_2 subtopics (if not already)
for i in range(1, 8):
    # Match the exact string to comment it out if it isn't commented
    # Only comment if it doesn't already have //
    search_str = f'GKSubTopicEntity("int_2_{i}"'
    replace_str = f'// GKSubTopicEntity("int_2_{i}"'
    
    # We can do this safely: replace `// ` with empty string first, then replace the raw string.
    # Actually just replacing '                    GKSubTopic' with '                    // GKSubTopic'
    
    idx = content.find(search_str)
    if idx != -1:
        # check if it's already commented out by checking a few characters before
        if content[idx-3:idx] != '// ':
            content = content[:idx] + '// ' + content[idx:]

# Inject MCQs
# Look for where we added IntlTopic1Seed.getMCQs()
old_mcqs = 'bdMcqs.addAll(IntlTopic1Seed.getMCQs())'
new_mcqs = 'bdMcqs.addAll(IntlTopic1Seed.getMCQs())\n                bdMcqs.addAll(TrueIntlTopic2Seed.getMCQs())'

if old_mcqs in content and 'TrueIntlTopic2Seed.getMCQs()' not in content:
    content = content.replace(old_mcqs, new_mcqs)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
