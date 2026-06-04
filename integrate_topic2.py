import os

file_path = r'd:\admission-gk\app\src\main\java\com\example\data\SeedData.kt'

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Replace the subtopic insertion
old_subtopic_line = 'repository.insertSubTopics(BDTopic1Seed.getSubtopics() + listOf('
new_subtopic_line = 'repository.insertSubTopics(BDTopic1Seed.getSubtopics() + IntlTopic2Seed.getSubtopics() + listOf('
content = content.replace(old_subtopic_line, new_subtopic_line)

# Replace the intMcqs insertion
old_int_mcq_line = 'repository.insertMCQs(intMcqs)'
new_int_mcq_line = 'intMcqs.addAll(IntlTopic2Seed.getMCQs())\n                repository.insertMCQs(intMcqs)'
content = content.replace(old_int_mcq_line, new_int_mcq_line)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
