import os

file_path = r'd:\admission-gk\app\src\main\java\com\example\data\SeedData.kt'

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Fix the missing lines
replacement = """                // Bangladesh Topics
                repository.insertMainTopics(listOf(
                    GKMainTopicEntity("bd_1", "INTERNATIONAL", "পৃথিবী পরিচিতি ও ভৌগোলিক বৈচিত্র্য", 1, "public"),
                    GKMainTopicEntity("bd_2", "BANGLADESH", "নদ-নদী ও জলাশয়", 2, "water_drop"),
                    GKMainTopicEntity("bd_3", "BANGLADESH", "আবহাওয়া, জলবায়ু ও পরিবেশ", 3, "wb_sunny"),
                    GKMainTopicEntity("bd_4", "BANGLADESH", "বাংলাদেশের সম্পদ (কৃষি, বনজ, খনিজ ও শিল্প)", 4, "agriculture"),
                    GKMainTopicEntity("bd_5", "BANGLADESH", "অর্থনীতি, বাজেট ও বাণিজ্য", 5, "account_balance"),
                    GKMainTopicEntity("bd_6", "BANGLADESH", "যোগাযোগ ব্যবস্থা ও তথ্যপ্রযুক্তি অবকাঠামো", 6, "commute"),
                    GKMainTopicEntity("bd_7","""

content = content.replace('                    GKMainTopicEntity("bd_7",', replacement)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
