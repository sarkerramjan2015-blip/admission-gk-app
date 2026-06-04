import os

path = r'd:\admission-gk\app\src\main\java\com\example\data\SeedData.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Add BDTopic8Seed to subtopics list
old_subtopics = 'TrueBDTopic1Seed.getSubtopics() + BDTopic1Seed.getSubtopics() + IntlTopic2Seed.getSubtopics() + BDTopic2Seed.getSubtopics() + BDTopic3Seed.getSubtopics() + BDTopic4Seed.getSubtopics() + BDTopic5Seed.getSubtopics() + BDTopic6Seed.getSubtopics() + BDTopic7Seed.getSubtopics() + listOf('
new_subtopics = 'TrueBDTopic1Seed.getSubtopics() + BDTopic1Seed.getSubtopics() + IntlTopic2Seed.getSubtopics() + BDTopic2Seed.getSubtopics() + BDTopic3Seed.getSubtopics() + BDTopic4Seed.getSubtopics() + BDTopic5Seed.getSubtopics() + BDTopic6Seed.getSubtopics() + BDTopic7Seed.getSubtopics() + BDTopic8Seed.getSubtopics() + listOf('
content = content.replace(old_subtopics, new_subtopics)

# Add BDTopic8Seed to MCQs list
old_mcqs = 'bdMcqs.addAll(BDTopic7Seed.getMCQs())'
new_mcqs = 'bdMcqs.addAll(BDTopic7Seed.getMCQs())\n                bdMcqs.addAll(BDTopic8Seed.getMCQs())'
content = content.replace(old_mcqs, new_mcqs)

# Comment out old bd_8 mangled subtopics
for i in range(1, 6):
    content = content.replace(f'GKSubTopicEntity("bd_8_{i}"', f'// GKSubTopicEntity("bd_8_{i}"')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
