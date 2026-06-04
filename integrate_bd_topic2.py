import os
import re

file_path = r'd:\admission-gk\app\src\main\java\com\example\data\SeedData.kt'

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Remove old bd_2 subtopics
lines = content.split('\n')
new_lines = []
for line in lines:
    if re.search(r'"bd_2_[1-7]"', line):
        continue
    new_lines.append(line)

content = '\n'.join(new_lines)

# Replace the subtopic insertion to include BDTopic2Seed
old_subtopic_line = 'repository.insertSubTopics(BDTopic1Seed.getSubtopics() + IntlTopic2Seed.getSubtopics() + listOf('
new_subtopic_line = 'repository.insertSubTopics(BDTopic1Seed.getSubtopics() + IntlTopic2Seed.getSubtopics() + BDTopic2Seed.getSubtopics() + listOf('
content = content.replace(old_subtopic_line, new_subtopic_line)

# Add bdMcqs for Topic 2
old_bd_mcqs_line = 'bdMcqs.addAll(BDTopic1Seed.getMCQs())'
new_bd_mcqs_line = 'bdMcqs.addAll(BDTopic1Seed.getMCQs())\n                bdMcqs.addAll(BDTopic2Seed.getMCQs())'
content = content.replace(old_bd_mcqs_line, new_bd_mcqs_line)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
