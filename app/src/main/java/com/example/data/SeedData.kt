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
                val hasMegaQuizExams = repository.getMegaQuizExams().first().isNotEmpty()
                val hasMegaQuizQuestions = repository.getMegaQuizQuestions("mq_1").first().isNotEmpty()
                Log.d("SEED_DATA", "=== Seed Check: hasBD=$hasBDTopics, hasINT=$hasINTTopics, hasMQ=$hasMegaQuizExams, hasMQQuestions=$hasMegaQuizQuestions ===")
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
                    RecentGKEntity("rgk_1", "BD", "জুলাই গণ-অভ্যুত্থান ও অন্তর্বর্তী সরকার", "২০২৪-২৫ সালে ছাত্র-জনতার গণ-অভ্যুত্থানের মাধ্যমে রাজনৈতিক পটপরিবর্তন হয় এবং ড. মুহাম্মদ ইউনূসের নেতৃত্বে নতুন অন্তর্বর্তীকালীন সরকার গঠিত হয়।", "পরীক্ষার জন্য এই গণ-অভ্যুত্থানের তারিখ ও শহীদদের নাম অত্যন্ত গুরুত্বপূর্ণ।", "সংবাদপত্র", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_2", "BD", "রাষ্ট্র সংস্কারের ১১টি কমিশন", "গণঅভ্যুত্থানের পর অন্তর্বর্তীকালীন সরকার রাষ্ট্র সংস্কারের জন্য ১১টি সংস্কার কমিশন গঠন করে।", "", "সরকারি প্রজ্ঞাপন", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_3", "BD", "জুলাই জাতীয় সনদ-২০২৫", "১৭ অক্টোবর ২০২৫ তারিখে প্রধান উপদেষ্টা ও বিভিন্ন রাজনৈতিক দলের অংশগ্রহণে 'জুলাই জাতীয় সনদ-২০২৫' স্বাক্ষরিত হয়।", "", "সংবাদপত্র", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_4", "BD", "ত্রয়োদশ জাতীয় সংসদ নির্বাচন", "১২ ফেব্রুয়ারি ২০২৬ তারিখে বাংলাদেশের ত্রয়োদশ জাতীয় সংসদ নির্বাচন অনুষ্ঠিত হয়।", "", "নির্বাচন কমিশন", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_5", "BD", "সংবিধান সংস্কার ও গণভোট", "১২ ফেব্রুয়ারি ২০২৬ তারিখে 'জুলাই জাতীয় সনদ' ও সংবিধান সংস্কারের প্রস্তাবনার ওপর একটি ঐতিহাসিক গণভোট অনুষ্ঠিত হয়।", "", "সংবাদপত্র", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_6", "BD", "প্রবাসীদের ডাকযোগে ভোট", "২০২৬ সালের নির্বাচনে প্রথমবারের মতো বিদেশে অবস্থানরত প্রবাসীদের ডাকযোগে (Postal Ballot) ভোট দেওয়ার সুযোগ দেওয়া হয়।", "", "নির্বাচন কমিশন", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_7", "BD", "আওয়ামী লীগের কার্যক্রম নিষিদ্ধ", "১২ মে ২০২৫ তারিখে এক সরকারি প্রজ্ঞাপনের মাধ্যমে আওয়ামী লীগ ও এর সহযোগী সংগঠনের সব ধরনের রাজনৈতিক কার্যক্রম নিষিদ্ধ ও নিবন্ধন স্থগিত করা হয়।", "", "প্রজ্ঞাপন", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_8", "BD", "জাতীয় নাগরিক পার্টি (এনসিপি)", "বৈষম্যবিরোধী ছাত্র আন্দোলনের নেতাদের নেতৃত্বে ২৮ ফেব্রুয়ারি ২০২৫ 'জাতীয় নাগরিক পার্টি' (এনসিপি) আত্মপ্রকাশ করে।", "", "সংবাদপত্র", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_9", "BD", "শেখ হাসিনার মৃত্যুদণ্ড", "১৭ নভেম্বর ২০২৫ আন্তর্জাতিক অপরাধ ট্রাইব্যুনাল সাবেক প্রধানমন্ত্রী শেখ হাসিনা ও সাবেক স্বরাষ্ট্রমন্ত্রীকে অনুপস্থিতিতে মৃত্যুদণ্ড প্রদান করে।", "", "ট্রাইব্যুনাল", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_10", "BD", "বেগম খালেদা জিয়ার ইন্তেকাল", "৩০ ডিসেম্বর ২০২৫ তারিখে বাংলাদেশের সাবেক প্রধানমন্ত্রী ও বিএনপির চেয়ারপারসন বেগম খালেদা জিয়া মৃত্যুবরণ করেন।", "", "জাতীয় পত্রিকা", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_11", "BD", "ধানমন্ডি ৩২ নম্বর ভাঙচুর", "৫ ফেব্রুয়ারি ২০২৫ তারিখে শেখ মুজিবুর রহমানের ধানমন্ডি-৩২ এর ঐতিহাসিক বাসভবন ভাঙচুর ও ধ্বংস করা হয়।", "", "সংবাদপত্র", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_12", "BD", "যমুনা রেলসেতু উদ্বোধন", "১২ ফেব্রুয়ারি ২০২৫ যমুনা নদীর উপর বঙ্গবন্ধু শেখ মুজিব রেলওয়ে সেতুতে (যমুনা রেলসেতু) যাত্রীবাহী ট্রেন চলাচল শুরু হয়। এটি দেশের দীর্ঘতম রেলসেতু।", "দৈর্ঘ্য ৪.৮ কিলোমিটার।", "বাংলাদেশ রেলওয়ে", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_13", "BD", "শিক্ষাক্রম সংস্কার", "২১ জুন ২০২৬ তারিখে প্রাথমিক শিক্ষাক্রম সংস্কারের আনুষ্ঠানিক যাত্রা শুরু হয়। এছাড়া এসএসসি ও এইচএসসি পরীক্ষায় অভিন্ন প্রশ্নপত্র চালুর সিদ্ধান্ত গৃহীত হয়।", "", "শিক্ষা মন্ত্রণালয়", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_14", "BD", "মাতারবাড়ী গভীর সমুদ্রবন্দর", "কক্সবাজারের মহেশখালীতে নির্মিত দেশের প্রথম গভীর সমুদ্রবন্দর মাতারবাড়ী পোর্টের কার্যক্রম পুরোদমে শুরু হয়েছে।", "দেশের প্রথম গভীর সমুদ্রবন্দর।", "বন্দর কর্তৃপক্ষ", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_15", "BD", "রূপপুর পারমাণবিক বিদ্যুৎকেন্দ্র", "পাবনার ঈশ্বরদীতে অবস্থিত দেশের প্রথম পারমাণবিক বিদ্যুৎকেন্দ্র থেকে জাতীয় গ্রিডে বিদ্যুৎ সরবরাহ শুরু।", "এটি দেশের প্রথম পারমাণবিক বিদ্যুৎকেন্দ্র।", "বিদ্যুৎ মন্ত্রণালয়", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_16", "BD", "বঙ্গবন্ধু শেখ মুজিবুর রহমান টানেল", "কর্ণফুলী নদীর তলদেশে নির্মিত দেশের প্রথম টানেল, যা দক্ষিণ এশিয়ার প্রথম আন্ডারওয়াটার টানেল।", "দৈর্ঘ্য ৩.৩২ কিলোমিটার।", "সংবাদপত্র", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_17", "BD", "পদ্মা সেতুতে ট্রেন চলাচল", "ঢাকা থেকে পদ্মা সেতু হয়ে দক্ষিণ-পশ্চিমাঞ্চলে নিয়মিত ট্রেন চলাচল শুরু, যা যোগাযোগ ব্যবস্থায় যুগান্তকারী পরিবর্তন এনেছে।", "", "বাংলাদেশ রেলওয়ে", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_18", "BD", "ঢাকা এলিভেটেড এক্সপ্রেসওয়ে", "ঢাকার যানজট নিরসনে নির্মিত দেশের প্রথম এলিভেটেড এক্সপ্রেসওয়ের (উড়াল সড়ক) বিভিন্ন অংশ যান চলাচলের জন্য উন্মুক্ত করা হয়েছে।", "দেশের প্রথম এলিভেটেড এক্সপ্রেসওয়ে।", "যোগাযোগ মন্ত্রণালয়", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_19", "BD", "মেট্রোরেল (এমআরটি লাইন ৬)", "উত্তরা থেকে মতিঝিল হয়ে কমলাপুর পর্যন্ত মেট্রোরেলের (এমআরটি লাইন ৬) সম্প্রসারণ কাজ সম্পন্ন।", "দেশের প্রথম মেট্রোরেল", "DMTCL", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_20", "BD", "দেশের প্রথম পাতাল রেল", "এমআরটি লাইন-১ এর অধীনে বিমানবন্দর থেকে কমলাপুর পর্যন্ত দেশের প্রথম পাতাল রেলের (Underground Metro) নির্মাণ কাজ চলমান।", "", "DMTCL", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_21", "BD", "সেন্টমার্টিনে পর্যটক সীমিতকরণ", "পরিবেশ রক্ষায় প্রবাল দ্বীপ সেন্টমার্টিনে পর্যটকদের যাতায়াত ও রাত্রিযাপনের ওপর কড়াকড়ি আরোপ করা হয়েছে।", "", "পরিবেশ মন্ত্রণালয়", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_22", "BD", "সর্বজনীন পেনশন স্কিম", "সব বয়সের মানুষের সামাজিক নিরাপত্তা নিশ্চিত করতে চালু হওয়া সর্বজনীন পেনশন স্কিমের আওতা সম্প্রসারণ।", "স্কিমসমূহ: প্রবাস, প্রগতি, সুরক্ষা, সমতা।", "অর্থ মন্ত্রণালয়", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_23", "BD", "বাংলাদেশের তৃতীয় সাবমেরিন ক্যাবল", "ইন্টারনেট সেবার মান বাড়াতে বাংলাদেশ 'সি-মি-উই ৬' নামের তৃতীয় সাবমেরিন ক্যাবলে যুক্ত হচ্ছে।", "", "টেলিযোগাযোগ মন্ত্রণালয়", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_24", "BD", "স্মার্ট বাংলাদেশ রূপকল্প", "সরকারের 'ভিশন ২০৪১' বা স্মার্ট বাংলাদেশ গড়ার রূপকল্পের ৪টি মূলভিত্তি: স্মার্ট সিটিজেন, স্মার্ট অর্থনৈতিক, স্মার্ট সরকার ও স্মার্ট সমাজ ব্যবস্থা।", "", "তথ্য ও প্রযুক্তি মন্ত্রণালয়", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_25", "BD", "সাফ নারী চ্যাম্পিয়নশিপ", "বাংলাদেশ নারী ফুটবল দল সাফ চ্যাম্পিয়নশিপে অসাধারণ নৈপুণ্য প্রদর্শন করে পুনরায় শিরোপা জয় করেছে।", "", "বাফুফে", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_26", "BD", "ঘূর্ণিঝড় রিমাল", "বাংলাদেশের উপকূলীয় অঞ্চলে আঘাত হানা প্রবল ঘূর্ণিঝড় রিমাল, যার নামকরণ করেছিল ওমান।", "রিমাল শব্দের অর্থ বালি।", "আবহাওয়া অধিদপ্তর", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_27", "BD", "জনশুমারি ও গৃহগণনা ২০২২", "দেশের সর্বশেষ ষষ্ঠ জনশুমারি ও গৃহগণনা অনুযায়ী বর্তমান জনসংখ্যা এবং সাক্ষরতার হারের হালনাগাদ তথ্য প্রকাশ।", "ষষ্ঠ জনশুমারি।", "বিবিএস", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_28", "BD", "বঙ্গবন্ধু স্যাটেলাইট-২", "বাংলাদেশের দ্বিতীয় স্যাটেলাইট উৎক্ষেপণের জন্য কারিগরি ও প্রস্তুতিমূলক কার্যক্রম চলমান রয়েছে।", "এটি হবে আর্থ অবজারভেশন স্যাটেলাইট।", "বিটিআরসি", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_29", "BD", "বাংলাদেশের মাথাপিছু আয়", "সর্বশেষ অর্থনৈতিক সমীক্ষা অনুযায়ী বাংলাদেশের বর্তমান মাথাপিছু আয় বৃদ্ধি পেয়েছে এবং অর্থনীতি ঘুরে দাঁড়াচ্ছে।", "", "অর্থনৈতিক সমীক্ষা", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_30", "BD", "জাতীয় বাজেট ২০২৫-২৬", "বাংলাদেশের জাতীয় সংসদে ২০২৫-২৬ অর্থবছরের নতুন বাজেট পেশ করা হয়, যেখানে শিক্ষা ও স্বাস্থ্য খাতে সর্বোচ্চ গুরুত্ব দেওয়া হয়েছে।", "", "অর্থ মন্ত্রণালয়", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),

                    RecentGKEntity("rgk_31", "INT", "ইসরায়েল-হামাস যুদ্ধবিরতি", "অক্টোবর ২০২৫-এ যুক্তরাষ্ট্রের প্রেসিডেন্ট ডোনাল্ড ট্রাম্পের মধ্যস্থতায় ইসরায়েল ও হামাসের মধ্যে একটি ঐতিহাসিক যুদ্ধবিরতি চুক্তি স্বাক্ষরিত হয়।", "মধ্যস্থতাকারীর নাম পরীক্ষায় আসতে পারে।", "আন্তর্জাতিক সংবাদ", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_32", "INT", "২৩তম ফুটবল বিশ্বকাপ ২০২৬", "২০২৬ সালের ফুটবল বিশ্বকাপ যুক্তরাষ্ট্র, কানাডা ও মেক্সিকোতে যৌথভাবে অনুষ্ঠিত হচ্ছে। এতে ৪৮টি দেশ অংশগ্রহণ করছে।", "অংশগ্রহণকারী দেশের সংখ্যা ৩২ থেকে বাড়িয়ে ৪৮ করা হয়েছে।", "FIFA", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_33", "INT", "টি-টোয়েন্টি বিশ্বকাপ ২০২৬", "২০২৬ সালে আইসিসি পুরুষ টি-টোয়েন্টি বিশ্বকাপ ভারত ও শ্রীলঙ্কায় যৌথভাবে অনুষ্ঠিত হবে।", "", "ICC", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_34", "INT", "শীতকালীন অলিম্পিক ২০২৬", "২০২৬ সালের শীতকালীন অলিম্পিক গেমস ইতালির মিলান ও কর্টিনাতে অনুষ্ঠিত হবে।", "", "Olympics", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_35", "INT", "জাতিসংঘ জলবায়ু সম্মেলন (COP)", "বৈশ্বিক জলবায়ু পরিবর্তন মোকাবেলায় জাতিসংঘ আয়োজিত সর্বশেষ জলবায়ু সম্মেলন (COP) অত্যন্ত গুরুত্বপূর্ণ সিদ্ধান্ত গ্রহণ করে।", "", "UNFCCC", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_36", "INT", "যুক্তরাষ্ট্রের ৪৭তম প্রেসিডেন্ট", "ডোনাল্ড ট্রাম্প ২০২৫ সালের ২০ জানুয়ারি যুক্তরাষ্ট্রের ৪৭তম প্রেসিডেন্ট হিসেবে আনুষ্ঠানিকভাবে শপথ গ্রহণ করেন।", "", "White House", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_37", "INT", "জাতিসংঘ ঘোষিত বর্ষ ২০২৫", "জাতিসংঘ ২০২৫ সালকে 'আন্তর্জাতিক কোঅপারেটিভ বর্ষ', 'হিমবাহ সংরক্ষণ বর্ষ' এবং 'শান্তি ও আস্থা বর্ষ' হিসেবে ঘোষণা করেছে।", "", "UN", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_38", "INT", "জাতিসংঘ ঘোষিত বর্ষ ২০২৬", "জাতিসংঘ ২০২৬ সালকে 'আন্তর্জাতিক নারী কৃষক বর্ষ', 'টেকসই উন্নয়নের জন্য স্বেচ্ছাসেবক বর্ষ' এবং 'রেঞ্জল্যান্ডস ও পশুপালক বর্ষ' হিসেবে ঘোষণা করেছে।", "", "UN", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_39", "INT", "ব্রিকস (BRICS) সম্মেলন", "ব্রিকসের সর্বশেষ শীর্ষ সম্মেলন অনুষ্ঠিত হয়, যেখানে নতুন সদস্য রাষ্ট্রগুলোর অংশগ্রহণ জোটের অর্থনৈতিক শক্তি আরও বৃদ্ধি করেছে।", "", "BRICS", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_40", "INT", "জি-২০ (G20) সম্মেলন", "বিশ্বের প্রধান অর্থনীতির দেশগুলোর জোট জি-২০ এর সর্বশেষ শীর্ষ সম্মেলন অনুষ্ঠিত হয়েছে, যেখানে জলবায়ু অর্থায়ন ও টেকসই উন্নয়ন গুরুত্ব পেয়েছে।", "", "G20", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_41", "INT", "যুক্তরাজ্যের নতুন প্রধানমন্ত্রী", "২০২৪ সালের নির্বাচনে জয়লাভ করে লেবার পার্টির নেতা কিয়ার স্টারমার যুক্তরাজ্যের নতুন প্রধানমন্ত্রী হিসেবে দায়িত্ব গ্রহণ করেন।", "", "BBC", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_42", "INT", "ন্যাটোর নতুন সদস্য", "সুইডেন আনুষ্ঠানিকভাবে ন্যাটোর (NATO) ৩২তম সদস্য হিসেবে যোগদান করেছে, যা ইউরোপের নিরাপত্তায় বড় পরিবর্তন।", "", "NATO", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_43", "INT", "কৃত্রিম বুদ্ধিমত্তা (AI) নিরাপত্তা সম্মেলন", "কৃত্রিম বুদ্ধিমত্তার ঝুঁকি মোকাবেলা ও নিরাপদ ব্যবহারের লক্ষ্যে বিশ্বব্যাপী 'গ্লোবাল এআই সেফটি সামিট' অনুষ্ঠিত হয়েছে।", "", "Tech News", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_44", "INT", "টাইম পারসন অফ দ্য ইয়ার", "টাইম ম্যাগাজিন বিশ্বজুড়ে প্রভাব বিস্তারকারী ব্যক্তি বা গোষ্ঠীকে সর্বশেষ 'পারসন অফ দ্য ইয়ার' হিসেবে নির্বাচিত করেছে।", "", "Time Magazine", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_45", "INT", "নোবেল পুরস্কার", "চিকিৎসা, পদার্থবিজ্ঞান, রসায়ন, সাহিত্য, শান্তি ও অর্থনীতিতে এ বছরের নোবেল বিজয়ীদের নাম ঘোষণা করা হয়েছে।", "শান্তি ও সাহিত্যের নোবেল পরীক্ষার জন্য বেশি গুরুত্বপূর্ণ।", "Nobel Prize", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_46", "INT", "ইউরো ২০২৪", "জার্মানিতে অনুষ্ঠিত উয়েফা ইউরো ২০২৪ ফুটবল টুর্নামেন্টে ইংল্যান্ডকে হারিয়ে স্পেন শিরোপা জয় করেছে।", "", "UEFA", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_47", "INT", "কোপা আমেরিকা ২০২৪", "যুক্তরাষ্ট্রে অনুষ্ঠিত কোপা আমেরিকা টুর্নামেন্টে কলম্বিয়াকে হারিয়ে শিরোপা জেশ করেছে আর্জেন্টিনা।", "", "CONMEBOL", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_48", "INT", "প্যারিস অলিম্পিক ২০২৪", "ফ্রান্সের প্যারিসে ৩৩তম গ্রীষ্মকালীন অলিম্পিক গেমস অনুষ্ঠিত হয়, যেখানে যুক্তরাষ্ট্র সর্বোচ্চ পদক জয় করে।", "", "Olympics", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_49", "INT", "আন্তর্জাতিক অপরাধ আদালত (ICC)", "যুদ্ধাপরাধের দায়ে আন্তর্জাতিক অপরাধ আদালত (ICC) গুরুত্বপূর্ণ রাজনৈতিক নেতাদের বিরুদ্ধে গ্রেপ্তারি পরোয়ানা জারি করেছে।", "", "ICC", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_50", "INT", "ভারতের মহাকাশ অভিযান (গগনযান)", "ভারতের প্রথম মানববাহী মহাকাশ অভিযান 'গগনযান' (Gaganyaan) এর প্রস্তুতি চূড়ান্ত পর্যায়ে পৌঁছেছে।", "", "ISRO", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_51", "INT", "চাঁদে বাণিজ্যিক অবতরণ", "ইতিহাসে প্রথমবারের মতো কোনো বেসরকারি বাণিজ্যিক মহাকাশযান সফলভাবে চাঁদের বুকে অবতরণ করেছে।", "", "SpaceX/NASA", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_52", "INT", "আইসিসি চ্যাম্পিয়ন্স ট্রফি ২০২৫", "২০২৫ সালের আইসিসি চ্যাম্পিয়ন্স ট্রফি ক্রিকেট টুর্নামেন্ট পাকিস্তানে অনুষ্ঠিত হবে।", "", "ICC", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_53", "INT", "মাঙ্কিপক্স (Mpox) স্বাস্থ্য জরুরি অবস্থা", "বিশ্ব স্বাস্থ্য সংস্থা (WHO) মাঙ্কিপক্স (Mpox) ভাইরাসের প্রাদুর্ভাবকে বৈশ্বিক জনস্বাস্থ্য জরুরি অবস্থা হিসেবে ঘোষণা করেছে।", "", "WHO", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_54", "INT", "অস্কার পুরস্কার", "লস অ্যাঞ্জেলেসে অনুষ্ঠিত সর্বশেষ একাডেমি অ্যাওয়ার্ডসে (অস্কার) শ্রেষ্ঠ চলচ্চিত্র এবং কলাকুশলীদের পুরস্কৃত করা হয়েছে।", "", "Academy", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_55", "INT", "আন্তর্জাতিক বুকার পুরস্কার", "বিশ্ব সাহিত্যের অন্যতম মর্যাদাপূর্ণ পুরস্কার 'আন্তর্জাতিক বুকার পুরস্কার' এ বছর শ্রেষ্ঠ অনুবাদ সাহিত্যের জন্য প্রদান করা হয়েছে।", "", "Booker", "Admin", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis())
                ))

                // ── Mega Quiz Exams & Questions — independent seeding block ──
                if (!hasMegaQuizExams || !hasMegaQuizQuestions) {
                Log.d("SEED_DATA", ">>> MegaQuiz block STARTED")
                val now = System.currentTimeMillis()
                val allMainTopics = repository.getAllMainTopicsSync()
                val allSubTopics = repository.getAllSubTopicsSync()
                val allMCQs = repository.getAllMCQsSync()
                
                val mcqsBySubtopic = allMCQs.groupBy { it.subTopicId }
                val subTopicsByMainTopic = allSubTopics.groupBy { it.parentTopicId }
                
                val megaExams = mutableListOf<MegaQuizExamEntity>()
                val mqQuestions = mutableListOf<MegaQuizQuestionEntity>()
                
                allMainTopics.forEachIndexed { index, mainTopic ->
                    val examId = "mq_${mainTopic.id}"
                    // Set the first as COMPLETED, second as LIVE, rest as UPCOMING
                    val status = when (index) {
                        0 -> "COMPLETED"
                        1 -> "LIVE"
                        else -> "UPCOMING"
                    }
                    // Space exams by 1 week each, starting from last week
                    val examDate = now + (index - 1) * 86400000L * 7 
                    
                    val examTitle = if (mainTopic.category == "BD") {
                        "মেগা কুইজ (বাংলাদেশ): ${mainTopic.title}"
                    } else {
                        "মেগা কুইজ (আন্তর্জাতিক): ${mainTopic.title}"
                    }

                    megaExams.add(
                        MegaQuizExamEntity(
                            id = examId,
                            title = examTitle,
                            examDate = examDate,
                            status = status
                        )
                    )
                    
                    val subTopicsForExam = subTopicsByMainTopic[mainTopic.id] ?: emptyList()
                    val mcqsForExam = mutableListOf<MCQQuestionEntity>()
                    subTopicsForExam.forEach { subTopic ->
                        val subTopicMcqs = mcqsBySubtopic[subTopic.id] ?: emptyList()
                        mcqsForExam.addAll(subTopicMcqs)
                    }
                    
                    // Shuffle and take up to 30 questions to keep the quiz length manageable
                    mcqsForExam.shuffle()
                    val selectedMcqs = mcqsForExam.take(30)
                    
                    selectedMcqs.forEachIndexed { qi, mcq ->
                        mqQuestions.add(
                            MegaQuizQuestionEntity(
                                id = "${examId}_q${qi + 1}",
                                examId = examId,
                                questionText = mcq.questionText,
                                options = mcq.options,
                                correctAnswer = mcq.correctAnswer,
                                explanation = mcq.explanation,
                                relatedSubTopicId = mcq.subTopicId
                            )
                        )
                    }
                }
                
                repository.insertMegaQuizExams(megaExams)
                repository.insertMegaQuizQuestions(mqQuestions)
                Log.d("SEED_DATA", "MegaQuiz Exams inserted: ${megaExams.size}")
                Log.d("SEED_DATA", "MegaQuiz Questions inserted: ${mqQuestions.size}")
                } // end if (!hasMegaQuizExams)

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
