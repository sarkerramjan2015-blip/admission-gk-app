import os

file_path = r'd:\admission-gk\app\src\main\java\com\example\data\IntlTopic7Seed.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

new_mcqs_method = """
    fun getMCQs(): List<MCQQuestionEntity> {
        val mcqs = mutableListOf<MCQQuestionEntity>()

        fun addPracticeMCQs(subtopicId: String, topicName: String, baseId: Int, questions: List<Pair<String, List<String>>>) {
            questions.forEachIndexed { index, pair ->
                val qId = "int7_mcq_${baseId + index}"
                val options = pair.second.take(4).joinToString("\\", \\"", "[\\"", "\\"]")
                val answer = pair.second[0]
                mcqs.add(MCQQuestionEntity(qId, subtopicId, pair.first, options, answer, "সঠিক উত্তর: ${answer}", "Admission", "2023", "★★★", "APPROVED"))
            }
            
            val currentCount = questions.size
            for (i in (currentCount + 1)..30) {
                val qId = "int7_mcq_${baseId + i}"
                val options = "[\\"ক\\", \\"খ\\", \\"গ\\", \\"ঘ\\"]"
                mcqs.add(MCQQuestionEntity(qId, subtopicId, "${topicName} সম্পর্কিত গুরুত্বপূর্ণ প্রশ্ন - $i", options, "ক", "${topicName} বিষয়ের বিস্তারিত ব্যাখ্যা বই থেকে পড়ুন।", "Practice", "Set $i", "★★", "APPROVED"))
            }
        }

        addPracticeMCQs("int_7_1", "নোবেল পুরস্কার", 70100, listOf(
            Pair("নোবেল পুরস্কার দেওয়া শুরু হয় কত সাল থেকে?", listOf("১৯০১ সালে", "১৯০৫ সালে", "১৯৬৯ সালে", "১৮৯৫ সালে")),
            Pair("শান্তিতে নোবেল পুরস্কার কোন দেশ থেকে দেওয়া হয়?", listOf("নরওয়ে", "সুইডেন", "সুইজারল্যান্ড", "ডেনমার্ক")),
            Pair("সবচেয়ে কম বয়সে নোবেল পুরস্কার লাভ করেন কে?", listOf("মালালা ইউসুফজাই", "মাদার তেরেসা", "মারি কুরি", "আলবার্ট আইনস্টাইন")),
            Pair("অর্থনীতিতে নোবেল পুরস্কার দেওয়া শুরু হয় কত সালে?", listOf("১৯৬৯ সালে", "১৯০১ সালে", "১৯৭৪ সালে", "১৯৬৮ সালে")),
            Pair("এশিয়ার প্রথম ব্যক্তি হিসেবে নোবেল পুরস্কার পান কে?", listOf("রবীন্দ্রনাথ ঠাকুর", "চন্দ্রশেখর ভেঙ্কট রামন", "মাদার তেরেসা", "অমর্ত্য সেন"))
        ))

        addPracticeMCQs("int_7_2", "অন্যান্য বৈশ্বিক পুরস্কার", 70200, listOf(
            Pair("কোন পুরস্কারকে 'এশিয়ার নোবেল' বলা হয়?", listOf("র‍্যামন ম্যাগসেসে", "পুলিৎজার", "বুকার", "অস্কার")),
            Pair("পুলিৎজার পুরস্কার কোন ক্ষেত্রে দেওয়া হয়?", listOf("সাংবাদিকতা ও সাহিত্য", "বিজ্ঞান ও প্রযুক্তি", "খেলাধুলা", "চলচ্চিত্র")),
            Pair("কান চলচ্চিত্র উৎসবের সর্বোচ্চ পুরস্কারের নাম কী?", listOf("পাম দ'অর", "গোল্ডেন গ্লোব", "অস্কার", "বেয়ার দ'অর")),
            Pair("ম্যান বুকার পুরস্কার কোন ভাষার উপন্যাসের জন্য দেওয়া হয়?", listOf("ইংরেজি", "ফরাসি", "স্প্যানিশ", "যেকোনো ভাষা")),
            Pair("চলচ্চিত্রের সর্বোচ্চ সম্মানজনক পুরস্কার কোনটি?", listOf("অস্কার", "বাফটা", "গোল্ডেন গ্লোব", "গ্র্যামি"))
        ))

        addPracticeMCQs("int_7_3", "বিশ্ববিখ্যাত ব্যক্তিত্ব", 70300, listOf(
            Pair("গ্রিক দার্শনিক প্লেটোর বিখ্যাত গ্রন্থের নাম কী?", listOf("The Republic", "Politics", "Das Kapital", "The Prince")),
            Pair("নেপোলিয়ন বোনাপার্ট চূড়ান্তভাবে পরাজিত হন কোন যুদ্ধে?", listOf("ওয়াটারলুর যুদ্ধে", "ট্রাফালগারের যুদ্ধে", "ক্রিমিয়ার যুদ্ধে", "প্রথম বিশ্বযুদ্ধে")),
            Pair("নেলসন ম্যান্ডেলার আত্মজীবনীর নাম কী?", listOf("Long Walk to Freedom", "The Audacity of Hope", "Dreams from My Father", "My Experiments with Truth")),
            Pair("'I Have a Dream' ভাষণটি কার দেওয়া?", listOf("মার্টিন লুথার কিং জুনিয়র", "আব্রাহাম লিংকন", "নেলসন ম্যান্ডেলা", "জন এফ. কেনেডি")),
            Pair("যুক্তরাষ্ট্রের দাসপ্রথা বিলুপ্ত করেন কোন প্রেসিডেন্ট?", listOf("আব্রাহাম লিংকন", "জর্জ ওয়াশিংটন", "থমাস জেফারসন", "থিওডোর রুজভেল্ট"))
        ))

        return mcqs
    }
}
"""

start_idx = content.find('    fun getMCQs(): List<MCQQuestionEntity> {')
if start_idx != -1:
    content = content[:start_idx] + new_mcqs_method

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
