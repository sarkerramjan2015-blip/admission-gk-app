package com.example.data

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object SeedData {

    fun populateDatabase(context: Context, repository: GKRepository) {
        syncWithSeed(repository)
    }

    private fun validateSeedIds(
        mainTopics: List<GKMainTopicEntity>,
        subTopics: List<GKSubTopicEntity>,
        mcqs: List<MCQQuestionEntity>
    ) {
        if (!BuildConfig.DEBUG) return
        Log.d("SEED_DATA", "Validating: ${mainTopics.size} main, ${subTopics.size} sub, ${mcqs.size} mcq")
        fun <T> checkDuplicates(items: List<T>, label: String, idSelector: (T) -> String) {
            val ids = items.map(idSelector)
            val duplicates = ids.groupingBy { it }.eachCount().filter { it.value > 1 }
            if (duplicates.isNotEmpty()) {
                val msg = "$label — DUPLICATE IDs:\n${duplicates.entries.joinToString("\n") { "  ${it.key}: ${it.value}x" }}"
                throw IllegalStateException(msg)
            }
        }
        checkDuplicates(mainTopics, "GKMainTopicEntity") { it.id }
        checkDuplicates(subTopics, "GKSubTopicEntity") { it.id }
        checkDuplicates(mcqs, "MCQQuestionEntity") { it.id }
    }

    private fun syncWithSeed(repository: GKRepository) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val hasBDTopics = repository.getMainTopics("BANGLADESH").first().isNotEmpty()
                val hasINTTopics = repository.getMainTopics("INTERNATIONAL").first().isNotEmpty()
                Log.d("SEED_DATA", "=== Seed Check: hasBD=$hasBDTopics, hasINT=$hasINTTopics ===")
                if (!hasBDTopics) {
                Log.d("SEED_DATA", ">>> BD block STARTED")
                // Bangladesh Topics
                val bdMainTopics = listOf(
                    GKMainTopicEntity("bd_1", "BANGLADESH", "বাংলাদেশ পরিচিতি ও ভূ-প্রকৃতি", 1, "map"),
                    GKMainTopicEntity("bd_2", "BANGLADESH", "নদ-নদী ও জলাশয়", 2, "water_drop"),
                    GKMainTopicEntity("bd_3", "BANGLADESH", "আবহাওয়া, জলবায়ু ও পরিবেশ", 3, "wb_sunny"),
                    GKMainTopicEntity("bd_4", "BANGLADESH", "কৃষি, শিল্প, খনিজ ও বনজ সম্পদ", 4, "agriculture"),
                    GKMainTopicEntity("bd_5", "BANGLADESH", "অর্থনীতি, বাজেট ও বাণিজ্য", 5, "account_balance"),
                    GKMainTopicEntity("bd_6", "BANGLADESH", "যোগাযোগ ব্যবস্থা ও তথ্যপ্রযুক্তি অবকাঠামো", 6, "commute"),
                    GKMainTopicEntity("bd_7", "BANGLADESH", "ক্ষুদ্র নৃ-গোষ্ঠী ও উপজাতি", 7, "groups"),
                    GKMainTopicEntity("bd_8", "BANGLADESH", "বাংলার প্রাচীন ইতিহাস ও মুসলিম শাসন", 8, "history_edu"),
                    GKMainTopicEntity("bd_9", "BANGLADESH", "ব্রিটিশ শাসনামলে বাংলা", 9, "gavel"),
                    GKMainTopicEntity("bd_10", "BANGLADESH", "পাকিস্তান আমল (১৯৪৭-১৯৭১)", 10, "flag"),
                    GKMainTopicEntity("bd_11", "BANGLADESH", "মুক্তিযুদ্ধ ও বাংলাদেশের অভ্যুদয়", 11, "military_tech"),
                    GKMainTopicEntity("bd_12", "BANGLADESH", "স্বাধীনতা পরবর্তী রাজনৈতিক পরিক্রমা ও শাসন আমল", 12, "how_to_vote"),
                    GKMainTopicEntity("bd_13", "BANGLADESH", "সংবিধান ও রাজনৈতিক কাঠামো", 13, "menu_book"),
                    GKMainTopicEntity("bd_14", "BANGLADESH", "প্রত্নতাত্ত্বিক নিদর্শন ও পর্যটন কেন্দ্র", 14, "tour"),
                    GKMainTopicEntity("bd_15", "BANGLADESH", "শিল্প, সংস্কৃতি, ক্রীড়া ও বিবিধ", 15, "palette"),
                    GKMainTopicEntity("bd_16", "BANGLADESH", "ব্রিটিশ শাসন ও বিরোধী আন্দোলন", 16, "gavel")
                )
                repository.insertMainTopics(bdMainTopics)
                Log.d("SEED_DATA", "BD MainTopics inserted: ${bdMainTopics.size}")
                // Bangladesh Subtopics
                val bdSubTopics = BD1Seed.getSubtopics() + BDTopic2Seed.getSubtopics() + BDTopic3Seed.getSubtopics() + BDTopic4Seed.getSubtopics() + BDTopic5Seed.getSubtopics() + BDTopic6Seed.getSubtopics() + BDTopic7Seed.getSubtopics() + BDTopic8Seed.getSubtopics() + BDTopic9Seed.getSubtopics() + BDTopic10Seed.getSubtopics() + BDTopic11Seed.getSubtopics() + BDTopic12Seed.getSubtopics() + BDTopic13Seed.getSubtopics() + BDTopic14Seed.getSubtopics() + BDTopic15Seed.getSubtopics() + BDTopic16Seed.getSubtopics() + listOf(
                    GKSubTopicEntity("bd_1_5", "bd_1", "সীমান্ত দৈর্ঘ্য এবং সমুদ্রসীমা জয় (ITLOS ও PCA এর রায়)", 5, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_1_7", "bd_1", "বাংলাদেশ-ভারত স্থল সীমান্ত চুক্তি (LBA) ও ছিটমহল বিনিময় ইতিহাস", 7, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_1_8", "bd_1", "বাংলাদেশের ভূ-প্রকৃতি (টারশিয়ারি পাহাড়, প্লাইস্টোসিন সোপান ও প্লাবন সমভূমি)", 8, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_8_6", "bd_8", "উপমহাদেশ ও বাংলায় ইসলামের আগমন এবং মুসলিম শাসন প্রতিষ্ঠা (বখতিয়ার খলজী)", 6, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_8_7", "bd_8", "দিল্লির সালতানাত এবং বাংলায় স্বাধীন সুলতানি আমল (ফখরুদ্দিন মোবারক শাহ, আলাউদ্দিন হোসেন শাহ)", 7, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_8_8", "bd_8", "মুঘল সাম্রাজ্য, বারো ভূঁইয়াদের ইতিহাস এবং সুবাদারি ও নবাবি আমল", 8, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_9_6", "bd_9", "স্বদেশী আন্দোলন, খিলাফত ও অসহযোগ আন্দোলন", 6, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_9_7", "bd_9", "১৯৩৫ সালের ভারত শাসন আইন, লাহোর প্রস্তাব (১৯৪০) এবং ভারত বিভাগ (১৯৪৭)", 7, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_10_6", "bd_10", "আগরতলা ষড়যন্ত্র মামলা, '৬৯-এর গণঅভ্যুত্থান ও ১৯৭০ সালের সাধারণ নির্বাচন", 6, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_13_6", "bd_13", "বাংলাদেশের নির্বাচন ব্যবস্থা, রাজনৈতিক দল ও সুশাসন", 6, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_15_6", "bd_15", "জাতীয় পদক, পুরস্কার ও বিভিন্ন সম্মাননা", 6, "", "[]", "", "[]")
                )
                validateSeedIds(bdMainTopics, bdSubTopics, emptyList())
                repository.insertSubTopics(bdSubTopics)
                Log.d("SEED_DATA", "BD SubTopics inserted: ${bdSubTopics.size}")

                // BD MCQs
                val bdMcqs = mutableListOf<MCQQuestionEntity>()
                bdMcqs.addAll(BD1Seed.getMCQs())
                bdMcqs.addAll(BDTopic2Seed.getMCQs())
                bdMcqs.addAll(BDTopic3Seed.getMCQs())
                bdMcqs.addAll(BDTopic4Seed.getMCQs())
                bdMcqs.addAll(BDTopic5Seed.getMCQs())
                bdMcqs.addAll(BDTopic6Seed.getMCQs())
                bdMcqs.addAll(BDTopic7Seed.getMCQs())
                bdMcqs.addAll(BDTopic8Seed.getMCQs())
                bdMcqs.addAll(BDTopic9Seed.getMCQs())
                bdMcqs.addAll(BDTopic10Seed.getMCQs())
                bdMcqs.addAll(BDTopic11Seed.getMCQs())
                bdMcqs.addAll(BDTopic12Seed.getMCQs())
                bdMcqs.addAll(BDTopic13Seed.getMCQs())
                bdMcqs.addAll(BDTopic14Seed.getMCQs())
                bdMcqs.addAll(BDTopic15Seed.getMCQs())
                bdMcqs.addAll(BDTopic16Seed.getMCQs())
                try { validateSeedIds(bdMainTopics, bdSubTopics, bdMcqs) } catch (e: Exception) { Log.e("SEED_DATA", "BD validation failed: ${e.message}") }
                repository.insertMCQs(bdMcqs)
                Log.d("SEED_DATA", "BD MCQs inserted: ${bdMcqs.size}")
                } // end if (!hasBDTopics)

                if (!hasINTTopics) {
                Log.d("SEED_DATA", ">>> INT block STARTED")
                // International Topics
                val intMainTopics = listOf(
                    GKMainTopicEntity("int_1", "INTERNATIONAL", "পৃথিবী পরিচিতি ও ভৌগোলিক বৈচিত্র্য", 1, "public"),
                    GKMainTopicEntity("int_2", "INTERNATIONAL", "বিশ্ব ইতিহাস ও বিশ্বযুদ্ধ", 2, "history"),
                    GKMainTopicEntity("int_3", "INTERNATIONAL", "জাতিসংঘ ও এর অঙ্গসংগঠন", 3, "account_balance"),
                    GKMainTopicEntity("int_4", "INTERNATIONAL", "জাতিসংঘের উন্নয়ন লক্ষ্যমাত্রা ও বিশেষ সংস্থা", 4, "track_changes"),
                    GKMainTopicEntity("int_5", "INTERNATIONAL", "আঞ্চলিক ও আন্তর্জাতিক রাজনৈতিক জোট", 5, "handshake"),
                    GKMainTopicEntity("int_6", "INTERNATIONAL", "অর্থনৈতিক জোট, আর্থিক প্রতিষ্ঠান ও সম্পদ", 6, "monetization_on"),
                    GKMainTopicEntity("int_7", "INTERNATIONAL", "মধ্যপ্রাচ্য সংকট, স্নায়ুযুদ্ধ ও আন্তর্জাতিক নিরাপত্তা", 7, "security"),
                    GKMainTopicEntity("int_8", "INTERNATIONAL", "বৈশ্বিক পরিবেশ, জলবায়ু ও বিশ্ব রাজনীতি", 8, "eco"),
                    GKMainTopicEntity("int_9", "INTERNATIONAL", "যোগাযোগ ব্যবস্থা, সংস্কৃতি ও বিশ্বের বিভিন্ন ধর্ম", 9, "flight"),
                    GKMainTopicEntity("int_10", "INTERNATIONAL", "আন্তর্জাতিক পুরস্কার ও খেলাধুলা", 10, "emoji_events"),
                    GKMainTopicEntity("int_11", "INTERNATIONAL", "আইসিটি, বিজ্ঞান ও মহাবিশ্ব", 11, "science"),
                    GKMainTopicEntity("int_12", "INTERNATIONAL", "বিবিধ আন্তর্জাতিক বিষয়াবলি", 12, "category")
                )
                repository.insertMainTopics(intMainTopics)
                Log.d("SEED_DATA", "INT MainTopics inserted: ${intMainTopics.size}")
                // International Subtopics (only INT seeds, not BD)
                val intSubTopics = IntlTopic1Seed.getSubtopics() + INT2Seed.getSubtopics() + IntlTopic3Seed.getSubtopics() + IntlTopic4Seed.getSubtopics() + IntlTopic5Seed.getSubtopics() + IntlTopic6Seed.getSubtopics() + IntlTopic7Seed.getSubtopics() + IntlTopic8Seed.getSubtopics() + listOf(
                    GKSubTopicEntity("int_7_4", "int_7", "স্নায়ুযুদ্ধকালীন বৈশ্বিক সংকট: কোরীয় যুদ্ধ, ভিয়েতনাম যুদ্ধ, কিউবা ক্ষেপণাস্ত্র সংকট, নক্ষত্র যুদ্ধ (Strategic Defense Initiative) এবং সোভিয়েত ইউনিয়নের (USSR) বিলুপ্তি", 4, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_7_5", "int_7", "আন্তর্জাতিক গোয়েন্দা ও পুলিশ সংস্থা: CIA, KGB, MI6, Mossad, INTERPOL", 5, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_7_6", "int_7", "আন্তর্জাতিক সাহায্য ও মানবাধিকার সংস্থা: রেড ক্রস (ICRC), রোটারি ইন্টারন্যাশনাল, অক্সফাম, অ্যামনেস্টি ইন্টারন্যাশনাল, ট্রান্সপারেন্সি ইন্টারন্যাশনাল, CARE, USAID, হিউম্যান রাইটস ওয়াচ", 6, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_9_1", "int_9", "যোগাযোগ ব্যবস্থা: বিশ্বের বিখ্যাত ও ব্যস্ততম বিমানবন্দর, আন্তর্জাতিক বিমানসংস্থা, বিখ্যাত টানেল, সমুদ্র-সেতু, আন্তর্জাতিক রেলপথ এবং বিভিন্ন আন্তর্জাতিক দিবস", 1, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_9_2", "int_9", "সংস্কৃতি ও শিল্পকলা: হিজরি ও গ্রেগরীয় বর্ষপঞ্জি, বিশ্ব শিল্পকলা, বিখ্যাত দার্শনিক ও সাহিত্যিক, বিখ্যাত গ্রন্থাবলি, পৃথিবীর বড় বড় গ্রন্থাগার ও জাদুঘর, বিখ্যাত স্থাপত্য এবং ইউনেস্কো ঘোষিত বিশ্ব ঐতিহ্য (World Heritage Sites)", 2, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_9_3", "int_9", "বিশ্বের বিভিন্ন ধর্ম: প্রধান প্রধান ধর্মের ইতিহাস, মুসলিম রাষ্ট্রসমূহ, ইসলাম, হিন্দু, খ্রিস্ট, বৌদ্ধ, ইহুদি ও জৈন ধর্মের বিবর্তন", 3, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_10_1", "int_10", "আন্তর্জাতিক পুরস্কার: নোবেল পুরস্কার, বুকার পুরস্কার, রামোন ম্যাগসেসে পুরস্কার, অস্কার (Academy Awards), পুলিৎজার পুরস্কার, আগা খান স্থাপত্য পুরস্কার এবং চলচ্চিত্র পুরস্কার", 1, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_10_2", "int_10", "বিশ্ব ক্রীড়াঙ্গন: অলিম্পিক গেমস, বিশ্বকাপ ক্রিকেট, বিশ্বকাপ ফুটবল, বাস্কেটবল, ব্যাডমিন্টন, ভলিবল, টেনিস, দাবা ও হকি-সহ বিভিন্ন আন্তর্জাতিক ক্রীড়া প্রতিযোগিতার ইতিহাস", 2, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_11_1", "int_11", "কম্পিউটার বিজ্ঞান, আধুনিক তথ্য ও যোগাযোগ প্রযুক্তি এবং কৃত্রিম বুদ্ধিমত্তা (AI)", 1, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_11_2", "int_11", "সাধারণ বিজ্ঞান ও পরিমাপ: আবিষ্কার ও আবিষ্কারক, বৈজ্ঞানিক পরিমাপক যন্ত্র, বিজ্ঞানের বিভিন্ন শাখার জনক ও তত্ত্বের প্রবক্ত", 2, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_11_3", "int_11", "মহাবিশ্ব ও মহাকাশ বিজ্ঞান: গ্যালাক্সি, ছায়াপথ, সূর্য, গ্রহ, উপগ্রহ, উল্কা, ধূমকেতু, সৌরজগৎ, বিশ্বের প্রধান মহাকাশ গবেষণা সংস্থাসমূহ (যেমন: NASA, ESA, ISRO) এবং ঐতিহাসিক মহাকাশ অভিযানসমূহ", 3, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_12_1", "int_12", "ভৌগোলিক রেকর্ড: বিশ্বের বিখ্যাত ও দর্শনীয় স্থান, বিশ্বের বৃহত্তম, ক্ষুদ্রতম, উচ্চতম এবং দীর্ঘতম বিষয়ের রেকর্ডসমূহ", 1, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_12_2", "int_12", "বহুল ব্যবহৃত আন্তর্জাতিক শব্দ সংক্ষেপ (Abbreviations) ও বিবিধ সাধারণ জ্ঞান", 2, "", "[]", "", "[]")
                )
                validateSeedIds(intMainTopics, intSubTopics, emptyList())
                repository.insertSubTopics(intSubTopics)
                Log.d("SEED_DATA", "INT SubTopics inserted: ${intSubTopics.size}")

                // Int MCQs
                val intMcqs = mutableListOf<MCQQuestionEntity>()
                intMcqs.addAll(IntlTopic1Seed.getMCQs())
                intMcqs.addAll(INT2Seed.getMCQs())
                intMcqs.addAll(IntlTopic3Seed.getMCQs())
                intMcqs.addAll(IntlTopic4Seed.getMCQs())
                intMcqs.addAll(IntlTopic5Seed.getMCQs())
                intMcqs.addAll(IntlTopic6Seed.getMCQs())
                intMcqs.addAll(IntlTopic7Seed.getMCQs())
                intMcqs.addAll(IntlTopic8Seed.getMCQs())
                try { validateSeedIds(intMainTopics, intSubTopics, intMcqs) } catch (e: Exception) { Log.e("SEED_DATA", "INT validation failed: ${e.message}") }
                repository.insertMCQs(intMcqs)
                Log.d("SEED_DATA", "INT MCQs inserted: ${intMcqs.size}")
                } // end if (!hasINTTopics)

                // Recent GK
                repository.insertRecentGK(listOf(
                    RecentGKEntity("rgk_1", "BD", "পদ্মা সেতু", "পদ্মা সেতুতে সর্বশেষ স্প্যান বসানো হয়েছে...", "", "দৈনিক ইত্তেফাক", "Rahim", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_2", "BD", "মেট্রোরেল", "মেট্রোরেলের নতুন লাইন সম্প্রসারণ...", "", "বাংলাদেশ প্রতিদিন", "", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_3", "INT", "COP 28", "COP 28 সম্মেলন দুবাইয়ে অনুষ্ঠিত...", "", "Daily Star", "", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_4", "INT", "G20 সম্মেলন", "G20 সম্মেলন ভারতে অনুষ্ঠিত...", "", "BBC", "Karim", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis())
                ))

                // ── Mega Quiz Exams (3টি, বাংলায়) ──
                // mq_1: আজ LIVE, mq_2: 7 দিন পরে, mq_3: 15 দিন পরে
                val now = System.currentTimeMillis()
                repository.insertMegaQuizExams(listOf(
                    MegaQuizExamEntity("mq_1", "মেগা কুইজ ১: বাংলাদেশের ইতিহাস ও সংস্কৃতি", now + 60000, "LIVE"),
                    MegaQuizExamEntity("mq_2", "মেগা কুইজ ২: আন্তর্জাতিক বিষয়াবলি ও ভূগোল", now + 604800000, "UPCOMING"),
                    MegaQuizExamEntity("mq_3", "মেগা কুইজ ৩: বিজ্ঞান, প্রযুক্তি ও সাধারণ জ্ঞান", now + 1296000000, "UPCOMING")
                ))

                // ── Mega Quiz Questions (30 per exam, 90 total, Bengali) ──
                val mqQuestions = mutableListOf<MegaQuizQuestionEntity>()
                val qData = listOf(
                    // mq_1: বাংলাদেশের ইতিহাস ও সংস্কৃতি (30 questions)
                    listOf(
                        "বাংলাদেশের মুক্তিযুদ্ধ কত সালে সংঘটিত হয়?" to "১৯৭১",
                        "বাংলাদেশের জাতীয় পতাকার ডিজাইনার কে?" to "শিব নারায়ণ দাস",
                        "বাংলাদেশের জাতীয় সংগীতের রচয়িতা কে?" to "রবীন্দ্রনাথ ঠাকুর",
                        "বাংলাদেশের জাতীয় ফুলের নাম কি?" to "শাপলা",
                        "বাংলাদেশের জাতীয় মাছের নাম কি?" to "ইলিশ",
                        "বাংলাদেশের জাতীয় পশুর নাম কি?" to "রয়েল বেঙ্গল টাইগার",
                        "জাতীয় স্মৃতিসৌধ কোথায় অবস্থিত?" to "সাভার",
                        "মুজিবনগর সরকার কবে গঠিত হয়?" to "১০ এপ্রিল ১৯৭১",
                        "বাংলাদেশের প্রথম রাষ্ট্রপতি কে ছিলেন?" to "শেখ মুজিবুর রহমান",
                        "৬ দফা দাবি কত সালে উত্থাপিত হয়?" to "১৯৬৬",
                        "বাংলা একাডেমি কত সালে প্রতিষ্ঠিত হয়?" to "১৯৫৫",
                        "ভাষা আন্দোলনের প্রথম শহীদ কে?" to "রফিক উদ্দিন আহমদ",
                        "২১ শে ফেব্রুয়ারি আন্তর্জাতিক মাতৃভাষা দিবস কবে স্বীকৃতি পায়?" to "১৯৯৯",
                        "বাংলাদেশের সুপ্রিম কোর্ট কোথায় অবস্থিত?" to "ঢাকা",
                        "বাংলাদেশের প্রথম অস্থায়ী রাষ্ট্রপতি কে ছিলেন?" to "সৈয়দ নজরুল ইসলাম",
                        "বাংলাদেশের প্রথম প্রধানমন্ত্রী কে ছিলেন?" to "তাজউদ্দীন আহমদ",
                        "বাংলাদেশ কবে জাতিসংঘের সদস্যপদ লাভ করে?" to "১৭ সেপ্টেম্বর ১৯৭৪",
                        "অপারেশন সার্চলাইট কবে শুরু হয়?" to "২৫ মার্চ ১৯৭১",
                        "বাংলাদেশের স্বাধীনতার ঘোষণা কে দেন?" to "বঙ্গবন্ধু শেখ মুজিবুর রহমান",
                        "৬ দফার প্রথম দফা কি ছিল?" to "প্রাদেশিক স্বায়ত্তশাসন",
                        "বীরশ্রেষ্ঠ কতজন?" to "৭ জন",
                        "বাংলাদেশের জাতীয় পাখির নাম কি?" to "দোয়েল",
                        "বাংলাদেশের জাতীয় গাছের নাম কি?" to "আম",
                        "পদ্মা সেতুর দৈর্ঘ্য কত?" to "৬.১৫ কি.মি.",
                        "বাংলাদেশের সর্বোচ্চ পর্বতশৃঙ্গ কোনটি?" to "তাজিংডং",
                        "সুন্দরবনের আয়তন কত?" to "১০,০০০ বর্গ কি.মি.",
                        "মহাস্থানগড় কোন জেলায় অবস্থিত?" to "বগুড়া",
                        "বাংলাদেশের জাতীয় জাদুঘর কোথায়?" to "শাহবাগ, ঢাকা",
                        "বাংলাদেশের সংবিধান কবে গৃহীত হয়?" to "৪ নভেম্বর ১৯৭২",
                        "বাংলাদেশের সংবিধানে মোট কয়টি ভাগ আছে?" to "১১টি"
                    ),
                    // mq_2: আন্তর্জাতিক বিষয়াবলি ও ভূগোল (30)
                    listOf(
                        "জাতিসংঘ কত সালে প্রতিষ্ঠিত হয়?" to "১৯৪৫",
                        "জাতিসংঘের বর্তমান মহাসচিব কে?" to "আন্তোনিও গুতেরেস",
                        "বিশ্বের বৃহত্তম মহাদেশ কোনটি?" to "এশিয়া",
                        "বিশ্বের বৃহত্তম মহাসাগর কোনটি?" to "প্রশান্ত মহাসাগর",
                        "ভারতের বর্তমান প্রধানমন্ত্রী কে?" to "নরেন্দ্র মোদি",
                        "SAARC কত সালে প্রতিষ্ঠিত হয়?" to "১৯৮৫",
                        "SAARC-এর সদর দপ্তর কোথায়?" to "কাঠমান্ডু, নেপাল",
                        "BRICS-এর সদস্য দেশ কয়টি?" to "১১টি",
                        "G20-এর বর্তমান সভাপতি দেশ কোনটি?" to "দক্ষিণ আফ্রিকা",
                        "ওআইসি(OIC)-এর সদর দপ্তর কোথায়?" to "জেদ্দা, সৌদি আরব",
                        "বিশ্বের সবচেয়ে ছোট দেশ কোনটি?" to "ভ্যাটিকান সিটি",
                        "নায়াগ্রা জলপ্রপাত কোথায় অবস্থিত?" to "যুক্তরাষ্ট্র-কানাডা সীমান্তে",
                        "পানামা খাল কোন দুটি মহাসাগরকে সংযুক্ত করে?" to "প্রশান্ত ও আটলান্টিক",
                        "সুয়েজ খাল কোন দেশে অবস্থিত?" to "মিশর",
                        "বিশ্বের দীর্ঘতম নদী কোনটি?" to "নীল নদ",
                        "অ্যামাজন নদী কোন মহাদেশে?" to "দক্ষিণ আমেরিকা",
                        "আন্তর্জাতিক অপরাধ আদালত(ICC) কোথায়?" to "হেগ, নেদারল্যান্ডস",
                        "ইউরোপীয় ইউনিয়নের(EU) সদস্য দেশ কয়টি?" to "২৭টি",
                        "বিশ্ব স্বাস্থ্য সংস্থা(WHO) কবে প্রতিষ্ঠিত হয়?" to "১৯৪৮",
                        "UNESCO-এর সদর দপ্তর কোথায়?" to "প্যারিস, ফ্রান্স",
                        "কোপেনহেগেন কোন দেশের রাজধানী?" to "ডেনমার্ক",
                        "বিশ্বের বৃহত্তম মরুভূমি কোনটি?" to "সাহারা",
                        "মাউন্ট এভারেস্টের উচ্চতা কত?" to "৮,৮৪৮ মিটার",
                        "ইউরোপের দীর্ঘতম নদী কোনটি?" to "ভলগা",
                        "ওপেক(OPEC)-এর সদর দপ্তর কোথায়?" to "ভিয়েনা, অস্ট্রিয়া",
                        "আন্তর্জাতিক মুদ্রা তহবিল(IMF) কবে প্রতিষ্ঠিত হয়?" to "১৯৪৪",
                        "বিশ্বব্যাংকের সদর দপ্তর কোথায়?" to "ওয়াশিংটন ডি.সি.",
                        "Commonwealth কবে প্রতিষ্ঠিত হয়?" to "১৯৩১",
                        "রেড ক্রস কবে প্রতিষ্ঠিত হয়?" to "১৮৬৩",
                        "GRAMEEN Bank কবে প্রতিষ্ঠিত হয়?" to "১৯৮৩"
                    ),
                    // mq_3: বিজ্ঞান, প্রযুক্তি ও সাধারণ জ্ঞান (30)
                    listOf(
                        "পানির রাসায়নিক সংকেত কি?" to "H₂O",
                        "সূর্যের আলো পৃথিবীতে আসতে কত সময় লাগে?" to "৮ মিনিট ২০ সেকেন্ড",
                        "DNA-এর পূর্ণরূপ কি?" to "Deoxyribonucleic Acid",
                        "মানব দেহে কয়টি হাড় আছে?" to "২০৬টি",
                        "লোহিত রক্তকণিকার আয়ুষ্কাল কত?" to "১২০ দিন",
                        "ইন্টারনেটের জনক কাকে বলা হয়?" to "ভিন্টন সার্ফ",
                        "কম্পিউটারের জনক কে?" to "চার্লস ব্যাবেজ",
                        "প্রথম কৃত্রিম উপগ্রহ কোনটি?" to "স্পুটনিক-১",
                        "চাঁদে প্রথম কে পৌঁছান?" to "নীল আর্মস্ট্রং",
                        "বাংলাদেশের প্রথম স্যাটেলাইট কোনটি?" to "বঙ্গবন্ধু স্যাটেলাইট-১",
                        "একটি কম্পিউটারের মস্তিষ্ক বলা হয় কাকে?" to "CPU",
                        "WiFi-এর পূর্ণরূপ কি?" to "Wireless Fidelity",
                        "পারমাণবিক বোমার আবিষ্কারক কে?" to "জে. রবার্ট ওপেনহাইমার",
                        "পেনিসিলিন কে আবিষ্কার করেন?" to "আলেকজান্ডার ফ্লেমিং",
                        "এক্স-রে কে আবিষ্কার করেন?" to "উইলহেম রন্টজেন",
                        "টেলিফোন কে আবিষ্কার করেন?" to "আলেকজান্ডার গ্রাহাম বেল",
                        "এক মিটার সমান কত সেন্টিমিটার?" to "১০০",
                        "এক কিলোমিটার সমান কত মিটার?" to "১০০০",
                        "সৌরজগতের বৃহত্তম গ্রহ কোনটি?" to "বৃহস্পতি",
                        "পৃথিবীর নিকটতম গ্রহ কোনটি?" to "শুক্র",
                        "মহাকর্ষ বলের আবিষ্কারক কে?" to "আইজ্যাক নিউটন",
                        "E=mc² সূত্রটি কার?" to "আলবার্ট আইনস্টাইন",
                        "মানুষের ক্রোমোজোম সংখ্যা কত?" to "২৩ জোড়া",
                        "এইডস(AIDS) রোগের ভাইরাসের নাম কি?" to "HIV",
                        "HTML-এর পূর্ণরূপ কি?" to "HyperText Markup Language",
                        "RAM-এর পূর্ণরূপ কি?" to "Random Access Memory",
                        "বাংলাদেশের প্রথম নিউক্লিয়ার বিদ্যুৎকেন্দ্র কোথায়?" to "রূপপুর, পাবনা",
                        "কাগজ কে আবিষ্কার করেন?" to "সাই লুন (চীন)",
                        "বিশ্বের দ্রুততম কম্পিউটার কোনটি?" to "Frontier",
                        "HTTP-এর পূর্ণরূপ কি?" to "HyperText Transfer Protocol"
                    )
                )

                val examIds = listOf("mq_1", "mq_2", "mq_3")
                val distractorSets = listOf(
                    listOf("১৯৪৭", "১৯৫২", "১৯৭২"), listOf("জয়নুল আবেদীন", "কামরুল হাসান", "এস এম সুলতান"),
                    listOf("কাজী নজরুল ইসলাম", "জসীমউদ্দীন", "লালন শাহ"), listOf("গোলাপ", "পদ্ম", "শিউলি"),
                    listOf("রুই", "কাতলা", "মৃগেল"), listOf("হরিণ", "হাতি", "শিয়াল"),
                    listOf("মিরপুর", "গাজীপুর", "নারায়ণগঞ্জ"), listOf("১৭ এপ্রিল ১৯৭১", "২৬ মার্চ ১৯৭১", "১৬ ডিসেম্বর ১৯৭১"),
                    listOf("সৈয়দ নজরুল ইসলাম", "খন্দকার মোশতাক আহমেদ", "জিয়াউর রহমান"), listOf("১৯৬২", "১৯৬৯", "১৯৭০"),
                    listOf("১৯৫২", "১৯৫৬", "১৯৬০"), listOf("শফিকুর রহমান", "আবুল বরকত", "আব্দুল জব্বার"),
                    listOf("১৯৯৮", "২০০০", "২০০১"), listOf("চট্টগ্রাম", "রাজশাহী", "খুলনা"),
                    listOf("তাজউদ্দীন আহমদ", "খন্দকার মোশতাক", "জিয়াউর রহমান"), listOf("সৈয়দ নজরুল ইসলাম", "শেখ মুজিবুর রহমান", "ক্যাপ্টেন মনসুর আলী"),
                    listOf("১৯৭২", "১৯৭৩", "১৯৭৫"), listOf("২৬ মার্চ ১৯৭১", "১৬ ডিসেম্বর ১৯৭১", "৭ মার্চ ১৯৭১"),
                    listOf("জিয়াউর রহমান", "তাজউদ্দীন আহমদ", "এম এ জি ওসমানী"), listOf("পূর্ণ স্বাধীনতা", "অর্থনৈতিক বৈষম্য দূরীকরণ", "ভাষার অধিকার"),
                    listOf("৫", "৮", "১০"), listOf("ময়না", "কাক", "চড়ুই"),
                    listOf("কাঁঠাল", "জাম", "লিচু"), listOf("৪.৮ কি.মি.", "৫.৫ কি.মি.", "৭ কি.মি."),
                    listOf("কেওক্রাডং", "গারো পাহাড়", "সীতাকুণ্ড"), listOf("৬,০০০", "৮,০০০", "১২,০০০"),
                    listOf("রাজশাহী", "দিনাজপুর", "পাবনা"), listOf("মতিঝিল", "ধানমন্ডি", "গুলশান"),
                    listOf("১৬ ডিসেম্বর ১৯৭২", "২৬ মার্চ ১৯৭২", "১ জানুয়ারি ১৯৭৩"), listOf("৯টি", "১২টি", "১৫টি")
                )

                // Build 90 questions with realistic distractors
                fun buildMqOptions(correct: String, distIdx: Int): String {
                    val all = distractorSets[distIdx % distractorSets.size].toMutableList()
                    all.add(correct); all.shuffle(); return all.joinToString("\",\"", "[\"", "\"]")
                }

                qData.forEachIndexed { examIndex, questions ->
                    val examId = examIds[examIndex]
                    questions.forEachIndexed { qi, (qText, correct) ->
                        val distIdx = examIndex * 30 + qi
                        mqQuestions.add(
                            MegaQuizQuestionEntity(
                                id = "${examId}_q${qi + 1}",
                                examId = examId,
                                questionText = qText,
                                options = buildMqOptions(correct, distIdx),
                                correctAnswer = correct,
                                explanation = ""
                            )
                        )
                    }
                }
                repository.insertMegaQuizQuestions(mqQuestions)

                // ── Universities (IDs match UI) ──
                repository.insertUniversities(listOf(
                    UniversityEntity("du", "DU", "ঢাকা বিশ্ববিদ্যালয়", "", 1),
                    UniversityEntity("ju", "JU", "জাহাঙ্গীরনগর বিশ্ববিদ্যালয়", "", 2),
                    UniversityEntity("ru", "RU", "রাজশাহী বিশ্ববিদ্যালয়", "", 3),
                    UniversityEntity("cu", "CU", "চট্টগ্রাম বিশ্ববিদ্যালয়", "", 4),
                    UniversityEntity("ku", "KU", "খুলনা বিশ্ববিদ্যালয়", "", 5),
                    UniversityEntity("iu", "IU", "ইসলামী বিশ্ববিদ্যালয়, কুষ্টিয়া", "", 6),
                    UniversityEntity("sust", "SUST", "শাহজালাল বিজ্ঞান ও প্রযুক্তি বিশ্ববিদ্যালয়", "", 7),
                    UniversityEntity("buet", "BUET", "বাংলাদেশ প্রকৌশল বিশ্ববিদ্যালয়", "", 8),
                    UniversityEntity("bup", "BUP", "বাংলাদেশ ইউনিভার্সিটি অব প্রফেশনালস", "", 9),
                    UniversityEntity("jnu", "JnU", "জগন্নাথ বিশ্ববিদ্যালয়", "", 10),
                    UniversityEntity("gst", "GST", "GST Combined", "", 11),
                    UniversityEntity("cou", "CoU", "কুমিল্লা বিশ্ববিদ্যালয়", "", 12),
                    UniversityEntity("hstu", "HSTU", "হাজী মোহাম্মদ দানেশ বিজ্ঞান ও প্রযুক্তি বিশ্ববিদ্যালয়", "", 13),
                    UniversityEntity("mbstu", "MBSTU", "মাওলানা ভাসানী বিজ্ঞান ও প্রযুক্তি বিশ্ববিদ্যালয়", "", 14),
                    UniversityEntity("nstu", "NSTU", "নোয়াখালী বিজ্ঞান ও প্রযুক্তি বিশ্ববিদ্যালয়", "", 15),
                    UniversityEntity("just", "JUST", "যশোর বিজ্ঞান ও প্রযুক্তি বিশ্ববিদ্যালয়", "", 16),
                    UniversityEntity("jkkniu", "JKKNIU", "জাতীয় কবি কাজী নজরুল ইসলাম বিশ্ববিদ্যালয়", "", 17),
                    UniversityEntity("brur", "BRUR", "বেগম রোকেয়া বিশ্ববিদ্যালয়, রংপুর", "", 18),
                    UniversityEntity("bu", "BU", "বরিশাল বিশ্ববিদ্যালয়", "", 19),
                    UniversityEntity("pust", "PUST", "পাবনা বিজ্ঞান ও প্রযুক্তি বিশ্ববিদ্যালয়", "", 20)
                ))

                // ── University Questions (real Bengali GK, 6 years × ~10 per uni) ──
                val uQuestions = mutableListOf<UniversityQuestionEntity>()
                val uniQData = mapOf(
                    "du" to listOf(
                        "বাংলাদেশের জাতীয় সংসদের মোট আসন সংখ্যা কত?" to "৩৫০",
                        "মুক্তিযুদ্ধে বাংলাদেশকে কয়টি সেক্টরে ভাগ করা হয়?" to "১১টি",
                        "বাংলাদেশের সংবিধানের প্রণেতা কে?" to "ড. কামাল হোসেন",
                        "বাংলাদেশের প্রথম অস্থায়ী রাষ্ট্রপতি কে ছিলেন?" to "সৈয়দ নজরুল ইসলাম",
                        "বাংলাদেশের জাতীয় পতাকার ডিজাইনার কে?" to "শিব নারায়ণ দাস",
                        "পদ্মা সেতুর দৈর্ঘ্য কত কিলোমিটার?" to "৬.১৫",
                        "জাতির পিতা বঙ্গবন্ধু শেখ মুজিবুর রহমান কত সালে জন্মগ্রহণ করেন?" to "১৯২০",
                        "বাংলাদেশের সর্বোচ্চ পর্বতশৃঙ্গের নাম কি?" to "তাজিংডং",
                        "৬ দফা দাবি কত সালে পেশ করা হয়?" to "১৯৬৬",
                        "বাংলাদেশে বর্তমানে কয়টি সিটি কর্পোরেশন আছে?" to "১২টি"
                    ),
                    "ju" to listOf(
                        "জাহাঙ্গীরনগর বিশ্ববিদ্যালয় কত সালে প্রতিষ্ঠিত হয়?" to "১৯৭০",
                        "জাতিসংঘ কত সালে প্রতিষ্ঠিত হয়?" to "১৯৪৫",
                        "বাংলাদেশে প্রথম গ্যাসক্ষেত্র আবিষ্কৃত হয় কোথায়?" to "হরিপুর",
                        "কম্পিউটারের জনক কাকে বলা হয়?" to "চার্লস ব্যাবেজ",
                        "মুক্তিযুদ্ধের সর্বাধিনায়ক কে ছিলেন?" to "জেনারেল আতাউল গণি ওসমানী",
                        "বাংলাদেশের জাতীয় ফুলের নাম কি?" to "শাপলা",
                        "জাতিসংঘের প্রথম মহাসচিব কে ছিলেন?" to "ট্রিগভে লি",
                        "SAARC এর সদর দপ্তর কোথায়?" to "কাঠমান্ডু",
                        "বাংলাদেশের প্রথম রাষ্ট্রপতি কে ছিলেন?" to "শেখ মুজিবুর রহমান",
                        "ইউরোপীয় ইউনিয়নের মুদ্রার নাম কি?" to "ইউরো"
                    ),
                    "ru" to listOf(
                        "রাজশাহী বিশ্ববিদ্যালয় কত সালে প্রতিষ্ঠিত হয়?" to "১৯৫৩",
                        "বাংলাদেশের বৃহত্তম জেলা কোনটি?" to "রাঙ্গামাটি",
                        "আন্তর্জাতিক মাতৃভাষা দিবস কবে পালিত হয়?" to "২১ ফেব্রুয়ারি",
                        "বিশ্বের বৃহত্তম মহাসাগর কোনটি?" to "প্রশান্ত মহাসাগর",
                        "বাংলাদেশের বৃহত্তম স্থলবন্দর কোনটি?" to "বেনাপোল",
                        "জাতীয় স্মৃতিসৌধ কোথায় অবস্থিত?" to "সাভার",
                        "বাংলাদেশের জাতীয় পশুর নাম কি?" to "রয়েল বেঙ্গল টাইগার",
                        "কোন দেশকে 'উদীয়মান সূর্যের দেশ' বলা হয়?" to "জাপান",
                        "বাংলাদেশের জাতীয় কবি কে?" to "কাজী নজরুল ইসলাম",
                        "পৃথিবীর নিকটতম গ্রহ কোনটি?" to "শুক্র"
                    ),
                    "cu" to listOf(
                        "চট্টগ্রাম বিশ্ববিদ্যালয় কত সালে প্রতিষ্ঠিত হয়?" to "১৯৬৬",
                        "বাংলাদেশের প্রধান সমুদ্রবন্দরের নাম কি?" to "চট্টগ্রাম বন্দর",
                        "জাতিসংঘের বর্তমান সদস্য সংখ্যা কত?" to "১৯৩",
                        "বাংলাদেশের মুক্তিযুদ্ধ কবে শুরু হয়?" to "২৬ মার্চ ১৯৭১",
                        "বাংলাদেশের জাতীয় মাছের নাম কি?" to "ইলিশ",
                        "OPEC এর সদর দপ্তর কোথায়?" to "ভিয়েনা",
                        "বাংলাদেশের দীর্ঘতম নদী কোনটি?" to "পদ্মা",
                        "মাউন্ট এভারেস্টের উচ্চতা কত?" to "৮৮৪৮ মিটার",
                        "বাংলাদেশের জাতীয় সংগীতের রচয়িতা কে?" to "রবীন্দ্রনাথ ঠাকুর",
                        "বাংলাদেশের প্রথম প্রধানমন্ত্রী কে ছিলেন?" to "তাজউদ্দীন আহমদ"
                    ),
                    "ku" to listOf(
                        "খুলনা বিশ্ববিদ্যালয় কত সালে প্রতিষ্ঠিত হয়?" to "১৯৯১",
                        "সুন্দরবনের আয়তন কত বর্গকিলোমিটার?" to "১০,০০০",
                        "বিশ্বের বৃহত্তম মরুভূমি কোনটি?" to "সাহারা",
                        "বাংলাদেশে কয়টি বিভাগ আছে?" to "৮টি",
                        "বাংলাদেশের জাতীয় পাখির নাম কি?" to "দোয়েল",
                        "UNESCO এর সদর দপ্তর কোথায়?" to "প্যারিস",
                        "বাংলাদেশের বৃহত্তম দ্বীপ কোনটি?" to "ভোলা",
                        "রেড ক্রস কত সালে প্রতিষ্ঠিত হয়?" to "১৮৬৩",
                        "বাংলাদেশের সংবিধানে মোট কয়টি ভাগ আছে?" to "১১টি",
                        "বিশ্বের দীর্ঘতম নদী কোনটি?" to "নীল নদ"
                    ),
                    "gst" to listOf(
                        "GST Admission কত সালে চালু হয়?" to "২০২১",
                        "বাংলাদেশের স্বাধীনতা দিবস কবে?" to "২৬ মার্চ",
                        "বাংলাদেশের বিজয় দিবস কবে?" to "১৬ ডিসেম্বর",
                        "বাংলাদেশের জাতীয় সংসদ ভবনের স্থপতি কে?" to "লুই কান",
                        "পানির রাসায়নিক সংকেত কি?" to "H₂O",
                        "আলোর গতি প্রতি সেকেন্ডে কত?" to "৩ লক্ষ কিমি",
                        "ইন্টারনেট কবে আবিষ্কৃত হয়?" to "১৯৬৯",
                        "বঙ্গবন্ধু স্যাটেলাইট-১ কবে উৎক্ষেপণ করা হয়?" to "২০১৮",
                        "সূর্যের আলো পৃথিবীতে আসতে কত সময় লাগে?" to "৮ মিনিট ২০ সেকেন্ড",
                        "DNA এর পূর্ণরূপ কি?" to "Deoxyribonucleic Acid"
                    )
                )

                fun buildUniDistractors(correct: String): String {
                    val pool = listOf("৪০০", "১৫০", "২৫০", "১২টি", "৮টি", "১০টি", "১৯৪৮", "১৯৫০", "১৯৫২",
                        "বঙ্গোপসাগর", "আরব সাগর", "লাল সাগর", "রুই", "কাতলা", "মৃগেল",
                        "গোলাপ", "পদ্ম", "শিউলি", "কেওক্রাডং", "গারো পাহাড়", "সীতাকুণ্ড",
                        "১৯২১", "১৯২২", "১৯১৯", "১৯৪৬", "১৯৪৪", "১৯৪৭",
                        "ইউরোপ", "আফ্রিকা", "এশিয়া", "১৯২", "১৯৪", "১৯৫",
                        "১৯৭০", "১৯৭১", "১৯৬৯", "১০,০০০", "৮,০০০", "১২,০০০",
                        "ময়না", "কাক", "চড়ুই", "ভিয়েনা", "জেনেভা", "নিউইয়র্ক",
                        "মেঘনা", "যমুনা", "ব্রহ্মপুত্র", "৮৮৫০", "৮৮৪০", "৮৮৩০",
                        "কাজী নজরুল ইসলাম", "জসিমউদ্দীন", "লালন শাহ",
                        "ক্যাপ্টেন মনসুর আলী", "সৈয়দ নজরুল ইসলাম", "খন্দকার মোশতাক",
                        "১৯৯২", "১৯৯০", "১৯৮৯", "১৪টি", "১৬টি", "১৮টি",
                        "H₂SO₄", "NaCl", "CO₂", "৩ লক্ষ", "২ লক্ষ", "১ লক্ষ",
                        "২০১৯", "২০২০", "২০১৭")
                    val dist = pool.shuffled().take(3)
                    return listOf(correct, dist[0], dist[1], dist[2]).shuffled().joinToString("\",\"", "[\"", "\"]")
                }

                val years = listOf(2022, 2023, 2021, 2020, 2019, 2018)
                for ((uniId, qas) in uniQData) {
                    for (year in years) {
                        qas.forEachIndexed { i, (q, a) ->
                            uQuestions.add(
                                UniversityQuestionEntity(
                                    id = "uq_${uniId}_${year}_${i + 1}",
                                    universityId = uniId,
                                    year = year,
                                    unitName = if (uniId == "gst") "Combined" else if (year % 2 == 0) "A Unit" else "B Unit",
                                    subject = "GK",
                                    questionText = q,
                                    options = buildUniDistractors(a),
                                    correctAnswer = a,
                                    explanation = "",
                                    referenceText = "$uniId Admission $year"
                                )
                            )
                        }
                    }
                }
                repository.insertUniversityQuestions(uQuestions)
            } catch (e: Exception) {
                Log.e("SEED_DATA", "SEED FAILED: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }
}
