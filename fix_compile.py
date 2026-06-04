import re
import os

# 1. Fix PracticeScreen.kt
practice_path = r'd:\admission-gk\app\src\main\java\com\example\ui\screens\PracticeScreen.kt'
with open(practice_path, 'r', encoding='utf-8') as f:
    practice_content = f.read()

practice_content = practice_content.replace('parsedQuestion.entity.reference.isNotBlank()', 'parsedQuestion.entity.sourceExam.isNotBlank()')
practice_content = practice_content.replace('parsedQuestion.entity.importance.isNotBlank()', 'parsedQuestion.entity.referenceText.isNotBlank()')
practice_content = practice_content.replace('text = parsedQuestion.entity.importance', 'text = parsedQuestion.entity.referenceText')
practice_content = practice_content.replace('text = parsedQuestion.entity.reference', 'text = "${parsedQuestion.entity.sourceExam} ${parsedQuestion.entity.year}".trim()')

with open(practice_path, 'w', encoding='utf-8') as f:
    f.write(practice_content)

# 2. Fix BDTopic1Seed.kt
seed_path = r'd:\admission-gk\app\src\main\java\com\example\data\BDTopic1Seed.kt'
with open(seed_path, 'r', encoding='utf-8') as f:
    seed_content = f.read()

# I accidentally replaced from 'return listOf(' to 'val mcqs = mutableListOf...'
# So the end of getSubtopics and the start of getMCQs was lost.
# Currently the file has:
#         )
# 
#         val mcqs = mutableListOf<MCQQuestionEntity>()
# Let's fix this by adding the missing function boundary.
# The previous script replaced:
# start_idx = content.find('        return listOf(')
# end_idx = content.find('        val mcqs = mutableListOf<MCQQuestionEntity>()')
# And inserted:
# content[:start_idx] + new_subtopics + "\n\n" + content[end_idx:]
# This resulted in:
#         )
# 
#         val mcqs = mutableListOf<MCQQuestionEntity>()
# Which means we are missing `    }\n\n    fun getMCQs(): List<MCQQuestionEntity> {\n`

# Let's add it back.
if 'fun getMCQs(): List<MCQQuestionEntity> {' not in seed_content:
    seed_content = seed_content.replace('val mcqs = mutableListOf<MCQQuestionEntity>()', '''    }

    fun getMCQs(): List<MCQQuestionEntity> {
        val mcqs = mutableListOf<MCQQuestionEntity>()''')

with open(seed_path, 'w', encoding='utf-8') as f:
    f.write(seed_content)
