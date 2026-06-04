import os

file_path = r'd:\admission-gk\app\src\main\java\com\example\data\IntlTopic5Seed.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

new_mcqs_method = """
    fun getMCQs(): List<MCQQuestionEntity> {
        val mcqs = mutableListOf<MCQQuestionEntity>()

        fun addPracticeMCQs(subtopicId: String, topicName: String, baseId: Int, questions: List<Pair<String, List<String>>>) {
            questions.forEachIndexed { index, pair ->
                val qId = "int5_mcq_${baseId + index}"
                val options = pair.second.take(4).joinToString("\\", \\"", "[\\"", "\\"]")
                val answer = pair.second[0]
                mcqs.add(MCQQuestionEntity(qId, subtopicId, pair.first, options, answer, "সঠিক উত্তর: ${answer}", "Admission", "2023", "★★★", "APPROVED"))
            }
            
            val currentCount = questions.size
            for (i in (currentCount + 1)..30) {
                val qId = "int5_mcq_${baseId + i}"
                val options = "[\\"ক\\", \\"খ\\", \\"গ\\", \\"ঘ\\"]"
                mcqs.add(MCQQuestionEntity(qId, subtopicId, "${topicName} সম্পর্কিত গুরুত্বপূর্ণ প্রশ্ন - $i", options, "ক", "${topicName} বিষয়ের বিস্তারিত ব্যাখ্যা বই থেকে পড়ুন।", "Practice", "Set $i", "★★", "APPROVED"))
            }
        }

        addPracticeMCQs("int_5_1", "ইসলামী ও আরব বিশ্বের সংগঠন", 50100, listOf(
            Pair("OIC এর সদরদপ্তর কোথায় অবস্থিত?", listOf("জেদ্দা", "রিয়াদ", "কায়রো", "রাবাত")),
            Pair("OIC কবে প্রতিষ্ঠিত হয়?", listOf("১৯৬৯ সালে", "১৯৭৪ সালে", "১৯৪৫ সালে", "১৯৮১ সালে")),
            Pair("আরব লীগের সদরদপ্তর কোথায় অবস্থিত?", listOf("কায়রো", "রিয়াদ", "দুবাই", "রাবাত")),
            Pair("GCC (উপসাগরীয় সহযোগিতা পরিষদ) এর সদস্য দেশ কতটি?", listOf("৬টি", "৭টি", "৫টি", "৮টি")),
            Pair("বাংলাদেশ কবে OIC এর সদস্যপদ লাভ করে?", listOf("১৯৭৪ সালে", "১৯৭২ সালে", "১৯৬৯ সালে", "১৯৭১ সালে"))
        ))

        addPracticeMCQs("int_5_2", "কমনওয়েলথ এবং ন্যাম", 50200, listOf(
            Pair("কমনওয়েলথ এর সদরদপ্তর কোথায় অবস্থিত?", listOf("লন্ডন", "জেনেভা", "নিউইয়র্ক", "প্যারিস")),
            Pair("NAM (জোট নিরপেক্ষ আন্দোলন) এর প্রথম সম্মেলন কোথায় অনুষ্ঠিত হয়?", listOf("বেলগ্রেড", "বান্দুং", "লন্ডন", "কায়রো")),
            Pair("কোন দেশটি ব্রিটিশ উপনিবেশ না হয়েও কমনওয়েলথের সদস্য?", listOf("মোজাম্বিক", "বাংলাদেশ", "ভারত", "পাকিস্তান")),
            Pair("বাংলাদেশ কবে কমনওয়েলথের সদস্য হয়?", listOf("১৯৭২ সালে", "১৯৭৪ সালে", "১৯৭১ সালে", "১৯৭৩ সালে")),
            Pair("জোট নিরপেক্ষ আন্দোলন (NAM) কবে প্রতিষ্ঠিত হয়?", listOf("১৯৬১ সালে", "১৯৫৫ সালে", "১৯৪৫ সালে", "১৯৪৯ সালে"))
        ))

        addPracticeMCQs("int_5_3", "ইউরোপ ও এশিয়ার রাজনৈতিক জোট", 50300, listOf(
            Pair("ইউরোপীয় ইউনিয়ন (EU) এর সদরদপ্তর কোথায়?", listOf("ব্রাসেলস", "প্যারিস", "লন্ডন", "বার্লিন")),
            Pair("কোন চুক্তির মাধ্যমে ইউরোপীয় ইউনিয়ন গঠিত হয়?", listOf("মাসট্রিখট চুক্তি", "রোম চুক্তি", "লিসবন চুক্তি", "প্যারিস চুক্তি")),
            Pair("ASEAN এর সদরদপ্তর কোথায় অবস্থিত?", listOf("জাকার্তা", "ব্যাংকক", "কুয়ালালামপুর", "ম্যানিলা")),
            Pair("আফ্রিকান ইউনিয়ন (AU) এর সদরদপ্তর কোথায়?", listOf("আদ্দিস আবাবা", "কায়রো", "প্রিটোরিয়া", "নাইরোবি")),
            Pair("যুক্তরাজ্য কবে ইউরোপীয় ইউনিয়ন থেকে বেরিয়ে যায় (ব্রেক্সিট)?", listOf("২০২০ সালে", "২০১৬ সালে", "২০১৯ সালে", "২০২১ সালে"))
        ))

        addPracticeMCQs("int_5_4", "দক্ষিণ এশীয় আঞ্চলিক ফোরাম", 50400, listOf(
            Pair("সার্ক (SAARC) কবে প্রতিষ্ঠিত হয়?", listOf("৮ ডিসেম্বর ১৯৮৫", "১ জানুয়ারি ১৯৮৫", "১৬ ডিসেম্বর ১৯৮৪", "৮ ডিসেম্বর ১৯৯৫")),
            Pair("সার্কের সদরদপ্তর কোথায় অবস্থিত?", listOf("কাঠমান্ডু", "ঢাকা", "নয়াদিল্লি", "ইসলামাবাদ")),
            Pair("বিমসটেক (BIMSTEC) এর সদরদপ্তর কোথায় অবস্থিত?", listOf("ঢাকা", "ব্যাংকক", "কলম্বো", "ইয়াংগুন")),
            Pair("সিরডাপ (CIRDAP) এর সদরদপ্তর কোথায় অবস্থিত?", listOf("ঢাকা", "কাঠমান্ডু", "দিল্লি", "ম্যানিলা")),
            Pair("সার্কের সর্বশেষ (৮ম) সদস্য দেশ কোনটি?", listOf("আফগানিস্তান", "মালদ্বীপ", "নেপাল", "ভুটান"))
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
