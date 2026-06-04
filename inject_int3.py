import os

path = r'd:\admission-gk\app\src\main\java\com\example\data\SeedData.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Add IntlTopic3Seed.getSubtopics() to subtopics
old_subtopics = 'TrueIntlTopic2Seed.getSubtopics()'
new_subtopics = 'TrueIntlTopic2Seed.getSubtopics() + IntlTopic3Seed.getSubtopics()'
content = content.replace(old_subtopics, new_subtopics)

# Comment out original mangled int_3 subtopics (1 to 5)
for i in range(1, 6):
    search_str = f'GKSubTopicEntity("int_3_{i}"'
    idx = content.find(search_str)
    if idx != -1:
        if content[idx-3:idx] != '// ':
            content = content[:idx] + '// ' + content[idx:]

# Inject MCQs
old_mcqs = 'bdMcqs.addAll(TrueIntlTopic2Seed.getMCQs())'
new_mcqs = 'bdMcqs.addAll(TrueIntlTopic2Seed.getMCQs())\n                bdMcqs.addAll(IntlTopic3Seed.getMCQs())'
if old_mcqs in content and 'IntlTopic3Seed.getMCQs()' not in content:
    content = content.replace(old_mcqs, new_mcqs)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
