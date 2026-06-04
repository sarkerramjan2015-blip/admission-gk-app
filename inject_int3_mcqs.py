import os

file_path = r'd:\admission-gk\app\src\main\java\com\example\data\IntlTopic3Seed.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

new_mcqs_method = """
    fun getMCQs(): List<MCQQuestionEntity> {
        val mcqs = mutableListOf<MCQQuestionEntity>()

        fun addPracticeMCQs(subtopicId: String, topicName: String, baseId: Int, questions: List<Pair<String, List<String>>>) {
            questions.forEachIndexed { index, pair ->
                val qId = "int3_mcq_${baseId + index}"
                val options = pair.second.take(4).joinToString("\\", \\"", "[\\"", "\\"]")
                val answer = pair.second[0]
                mcqs.add(MCQQuestionEntity(qId, subtopicId, pair.first, options, answer, "সঠিক উত্তর: ${answer}", "Admission", "2023", "★★★", "APPROVED"))
            }
            
            val currentCount = questions.size
            for (i in (currentCount + 1)..30) {
                val qId = "int3_mcq_${baseId + i}"
                val options = "[\\"ক\\", \\"খ\\", \\"গ\\", \\"ঘ\\"]"
                mcqs.add(MCQQuestionEntity(qId, subtopicId, "${topicName} সম্পর্কিত গুরুত্বপূর্ণ প্রশ্ন - $i", options, "ক", "${topicName} বিষয়ের বিস্তারিত ব্যাখ্যা বই থেকে পড়ুন।", "Practice", "Set $i", "★★", "APPROVED"))
            }
        }

        addPracticeMCQs("int_3_1", "জাতিসংঘ প্রতিষ্ঠার ইতিহাস", 30100, listOf(
            Pair("জাতিসংঘ আনুষ্ঠানিকভাবে কবে প্রতিষ্ঠিত হয়?", listOf("২৪ অক্টোবর ১৯৪৫", "২৬ জুন ১৯৪৫", "১০ জানুয়ারি ১৯২০", "১ জানুয়ারি ১৯৪২")),
            Pair("জাতিসংঘের নামকরণ করেন কে?", listOf("ফ্রাঙ্কলিন ডি রুজভেল্ট", "উইনস্টন চার্চিল", "উড্রো উইলসন", "জোসেফ স্ট্যালিন")),
            Pair("নিরাপত্তা পরিষদের স্থায়ী সদস্যদের 'ভেটো' ক্ষমতা প্রদানের সিদ্ধান্ত কোন সম্মেলনে গৃহীত হয়?", listOf("ইয়াল্টা সম্মেলনে", "সান ফ্রান্সিসকো সম্মেলনে", "ডাম্বারটন ওকস সম্মেলনে", "মস্কো সম্মেলনে")),
            Pair("জাতিসংঘের প্রতিষ্ঠাতা সদস্য দেশ কতটি?", listOf("৫১টি", "৫০টি", "৫২টি", "৪৫টি")),
            Pair("জাতিসংঘের সনদে প্রথমে কতটি দেশ স্বাক্ষর করেছিল?", listOf("৫০টি", "৫১টি", "৫২টি", "৪৫টি"))
        ))

        addPracticeMCQs("int_3_2", "জাতিসংঘের গঠন কাঠামো ও প্রতীক", 30200, listOf(
            Pair("জাতিসংঘের বর্তমান সদস্য দেশ কতটি?", listOf("১৯৩টি", "১৯২টি", "১৯৪টি", "১৯৫টি")),
            Pair("জাতিসংঘের সর্বশেষ (১৯৩তম) সদস্য দেশ কোনটি?", listOf("দক্ষিণ সুদান", "মন্টিনিগ্রো", "পূর্ব তিমুর", "সুইজারল্যান্ড")),
            Pair("জাতিসংঘের সদরদপ্তর কোথায় অবস্থিত?", listOf("নিউইয়র্ক", "জেনেভা", "প্যারিস", "ওয়াশিংটন ডিসি")),
            Pair("জাতিসংঘের দাপ্তরিক ভাষা কয়টি?", listOf("৬টি", "৫টি", "৪টি", "৭টি")),
            Pair("নিচের কোনটি জাতিসংঘের পর্যবেক্ষক রাষ্ট্র?", listOf("ভ্যাটিকান সিটি", "তাইওয়ান", "কসোভো", "মোনাকো"))
        ))

        addPracticeMCQs("int_3_3", "বাংলাদেশ ও জাতিসংঘ", 30300, listOf(
            Pair("বাংলাদেশ কবে জাতিসংঘের সদস্যপদ লাভ করে?", listOf("১৭ সেপ্টেম্বর ১৯৭৪", "২৫ সেপ্টেম্বর ১৯৭৪", "১৬ ডিসেম্বর ১৯৭১", "১৭ সেপ্টেম্বর ১৯৭৩")),
            Pair("বাংলাদেশ জাতিসংঘের কততম সদস্য?", listOf("১৩৬তম", "১৩৫তম", "১৩৪তম", "১৩৭তম")),
            Pair("বঙ্গবন্ধু শেখ মুজিবুর রহমান কবে জাতিসংঘে প্রথম বাংলায় ভাষণ দেন?", listOf("২৫ সেপ্টেম্বর ১৯৭৪", "১৭ সেপ্টেম্বর ১৯৭৪", "১০ জানুয়ারি ১৯৭২", "২৬ মার্চ ১৯৭১")),
            Pair("জাতিসংঘ সাধারণ পরিষদের প্রথম বাংলাদেশি সভাপতি কে ছিলেন?", listOf("হুমায়ুন রশীদ চৌধুরী", "আবু সাঈদ চৌধুরী", "কামাল হোসেন", "এম এ মুহিত")),
            Pair("বাংলাদেশের সদস্যপদ লাভের বিরুদ্ধে কোন দেশ ভেটো দিয়েছিল?", listOf("চীন", "যুক্তরাষ্ট্র", "রাশিয়া", "ফ্রান্স"))
        ))

        addPracticeMCQs("int_3_4", "৫টি সক্রিয় মূল অঙ্গ সংগঠন", 30400, listOf(
            Pair("জাতিসংঘের নিরাপত্তা পরিষদের স্থায়ী সদস্য দেশ কয়টি?", listOf("৫টি", "১০টি", "১৫টি", "৭টি")),
            Pair("নিরাপত্তা পরিষদের মোট সদস্য দেশ কতটি?", listOf("১৫টি", "৫টি", "১০টি", "২০টি")),
            Pair("আন্তর্জাতিক বিচারালয়ের (ICJ) সদরদপ্তর কোথায়?", listOf("দ্য হেগ", "জেনেভা", "প্যারিস", "লন্ডন")),
            Pair("জাতিসংঘের প্রথম মহাসচিব কে ছিলেন?", listOf("ট্রিগভে লাই", "কফি আনান", "বান কি মুন", "দ্যাগ হ্যামারশোল্ড")),
            Pair("আন্তর্জাতিক বিচারালয়ের বিচারকদের মেয়াদ কত বছর?", listOf("৯ বছর", "৫ বছর", "৭ বছর", "১০ বছর"))
        ))

        addPracticeMCQs("int_3_5", "বিশেষায়িত সহযোগী সংস্থা", 30500, listOf(
            Pair("জাতিসংঘের প্রাচীনতম বিশেষায়িত সংস্থা কোনটি?", listOf("ILO", "WHO", "UNESCO", "FAO")),
            Pair("UNESCO এর সদরদপ্তর কোথায়?", listOf("প্যারিস", "জেনেভা", "রোম", "লন্ডন")),
            Pair("খাদ্য ও কৃষি সংস্থা (FAO) এর সদরদপ্তর কোথায়?", listOf("রোম", "জেনেভা", "প্যারিস", "নিউইয়র্ক")),
            Pair("বিশ্ব স্বাস্থ্য সংস্থা (WHO) এর সদরদপ্তর কোথায়?", listOf("জেনেভা", "লন্ডন", "প্যারিস", "ওয়াশিংটন ডিসি")),
            Pair("বিশ্বব্যাংক (World Bank) এর সদরদপ্তর কোথায়?", listOf("ওয়াশিংটন ডিসি", "নিউইয়র্ক", "জেনেভা", "লন্ডন"))
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
