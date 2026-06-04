import os

path = r'd:\admission-gk\app\src\main\java\com\example\data\SeedData.kt'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Add bd_4 to main topics
old_main = 'GKMainTopicEntity("bd_3", "BANGLADESH", "আবহাওয়া, জলবায়ু ও পরিবেশ", 3, "wb_sunny"),'
new_main = 'GKMainTopicEntity("bd_3", "BANGLADESH", "আবহাওয়া, জলবায়ু ও পরিবেশ", 3, "wb_sunny"),\n                    GKMainTopicEntity("bd_4", "BANGLADESH", "কৃষি, শিল্প, খনিজ ও বনজ সম্পদ", 4, "agriculture"),'
content = content.replace(old_main, new_main)

# Add BDTopic4Seed to subtopics list
old_subtopics = 'TrueBDTopic1Seed.getSubtopics() + BDTopic1Seed.getSubtopics() + IntlTopic2Seed.getSubtopics() + BDTopic2Seed.getSubtopics() + BDTopic3Seed.getSubtopics() + listOf('
new_subtopics = 'TrueBDTopic1Seed.getSubtopics() + BDTopic1Seed.getSubtopics() + IntlTopic2Seed.getSubtopics() + BDTopic2Seed.getSubtopics() + BDTopic3Seed.getSubtopics() + BDTopic4Seed.getSubtopics() + listOf('
content = content.replace(old_subtopics, new_subtopics)

# Add BDTopic4Seed to MCQs list
old_mcqs = 'bdMcqs.addAll(BDTopic3Seed.getMCQs())'
new_mcqs = 'bdMcqs.addAll(BDTopic3Seed.getMCQs())\n                bdMcqs.addAll(BDTopic4Seed.getMCQs())'
content = content.replace(old_mcqs, new_mcqs)

# Comment out old bd_4 mangled subtopics
for i in range(1, 7):
    content = content.replace(f'GKSubTopicEntity("bd_4_{i}"', f'// GKSubTopicEntity("bd_4_{i}"')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
