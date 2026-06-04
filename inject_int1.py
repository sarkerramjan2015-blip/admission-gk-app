import os

path = r'd:\admission-gk\app\src\main\java\com\example\data\SeedData.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Add IntlTopic1Seed to subtopics list. Note: IntlTopic2Seed is already there. Let's place IntlTopic1Seed right before IntlTopic2Seed.
old_subtopics = 'TrueBDTopic1Seed.getSubtopics() + BDTopic1Seed.getSubtopics() + IntlTopic2Seed.getSubtopics()'
new_subtopics = 'TrueBDTopic1Seed.getSubtopics() + BDTopic1Seed.getSubtopics() + IntlTopic1Seed.getSubtopics() + IntlTopic2Seed.getSubtopics()'
content = content.replace(old_subtopics, new_subtopics)

# Also remove the mangled int_1 subtopics
for i in range(1, 8):
    content = content.replace(f'GKSubTopicEntity("int_1_{i}"', f'// GKSubTopicEntity("int_1_{i}"')

# Add IntlTopic1Seed to MCQs list
# Look for a place to add it. Let's add it at the beginning of the intlMcqs or simply at the end of bdMcqs.
# Currently, intlMcqs is not populated yet, wait, we might need an intlMcqs list, or we just put it in the same list?
# Let's check how SeedData handles MCQs. 
# It has bdMcqs which is used for repository.insertMCQQuestions(bdMcqs). We can just put it there.
old_mcqs = 'bdMcqs.addAll(BDTopic15Seed.getMCQs())'
new_mcqs = 'bdMcqs.addAll(BDTopic15Seed.getMCQs())\n                bdMcqs.addAll(IntlTopic1Seed.getMCQs())'
content = content.replace(old_mcqs, new_mcqs)

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
