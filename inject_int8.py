import os

path = r'd:\admission-gk\app\src\main\java\com\example\data\SeedData.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Add IntlTopic8Seed.getSubtopics() to subtopics
old_subtopics = 'IntlTopic7Seed.getSubtopics()'
new_subtopics = 'IntlTopic7Seed.getSubtopics() + IntlTopic8Seed.getSubtopics()'
if old_subtopics in content and 'IntlTopic8Seed.getSubtopics()' not in content:
    content = content.replace(old_subtopics, new_subtopics)

# Comment out original mangled int_8 subtopics (1 to 4)
for i in range(1, 5):
    search_str = f'GKSubTopicEntity("int_8_{i}"'
    idx = content.find(search_str)
    if idx != -1:
        if content[idx-3:idx] != '// ':
            content = content[:idx] + '// ' + content[idx:]

# Inject MCQs
old_mcqs = 'bdMcqs.addAll(IntlTopic7Seed.getMCQs())'
new_mcqs = 'bdMcqs.addAll(IntlTopic7Seed.getMCQs())\n                bdMcqs.addAll(IntlTopic8Seed.getMCQs())'
if old_mcqs in content and 'IntlTopic8Seed.getMCQs()' not in content:
    content = content.replace(old_mcqs, new_mcqs)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
