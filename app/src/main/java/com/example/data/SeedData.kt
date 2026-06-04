package com.example.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object SeedData {

    fun populateDatabase(context: Context, repository: GKRepository) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val hasTopics = repository.getMainTopics("BANGLADESH").first().isNotEmpty()
                if (!hasTopics) {
                // Bangladesh Topics
                repository.insertMainTopics(listOf(
                    GKMainTopicEntity("bd_1", "BANGLADESH", "বাংলাদেশ পরিচিতি ও ভূ-প্রকৃতি", 1, "map"),
                    GKMainTopicEntity("bd_2", "BANGLADESH", "নদ-নদী ও জলাশয়", 2, "water_drop"),
                    GKMainTopicEntity("bd_3", "BANGLADESH", "আবহাওয়া, জলবায়ু ও পরিবেশ", 3, "wb_sunny"),
                    GKMainTopicEntity("bd_4", "BANGLADESH", "কৃষি, শিল্প, খনিজ ও বনজ সম্পদ", 4, "agriculture"),
                    GKMainTopicEntity("int_1", "INTERNATIONAL", "পৃথিবী পরিচিতি ও ভৌগোলিক বৈচিত্র্য", 1, "public"),
                    GKMainTopicEntity("int_2", "INTERNATIONAL", "আন্তর্জাতিক সংস্থা, সংগঠন ও জোট", 2, "language"),
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
                    GKMainTopicEntity("bd_15", "BANGLADESH", "শিল্প, সংস্কৃতি, ক্রীড়া ও বিবিধ", 15, "palette")
                ))

                // Bangladesh Subtopics
                repository.insertSubTopics(TrueBDTopic1Seed.getSubtopics() + BDTopic1Seed.getSubtopics() + IntlTopic1Seed.getSubtopics() + TrueIntlTopic2Seed.getSubtopics() + IntlTopic3Seed.getSubtopics() + IntlTopic4Seed.getSubtopics() + IntlTopic5Seed.getSubtopics() + IntlTopic6Seed.getSubtopics() + IntlTopic7Seed.getSubtopics() + IntlTopic8Seed.getSubtopics() + BDTopic2Seed.getSubtopics() + BDTopic3Seed.getSubtopics() + BDTopic4Seed.getSubtopics() + BDTopic5Seed.getSubtopics() + BDTopic6Seed.getSubtopics() + BDTopic7Seed.getSubtopics() + BDTopic8Seed.getSubtopics() + BDTopic9Seed.getSubtopics() + BDTopic10Seed.getSubtopics() + BDTopic11Seed.getSubtopics() + BDTopic12Seed.getSubtopics() + BDTopic13Seed.getSubtopics() + BDTopic14Seed.getSubtopics() + BDTopic15Seed.getSubtopics() + listOf(
                    GKSubTopicEntity("bd_1_3", "bd_1", "সীমান্তবর্তী চরম বিন্দুসমূহ (সর্ব-উত্তর, দক্ষিণ, পূর্ব, পশ্চিম)", 3, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_1_5", "bd_1", "সীমান্ত দৈর্ঘ্য এবং সমুদ্রসীমা জয় (ITLOS ও PCA এর রায়)", 5, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_1_7", "bd_1", "বাংলাদেশ-ভারত স্থল সীমান্ত চুক্তি (LBA) ও ছিটমহল বিনিময় ইতিহাস", 7, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_1_8", "bd_1", "বাংলাদেশের ভূ-প্রকৃতি (টারশিয়ারি পাহাড়, প্লাইস্টোসিন সোপান ও প্লাবন সমভূমি)", 8, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_3_1", "bd_3", "বাংলাদেশের জলবায়ুর বৈশিষ্ট্য ও বর্ষাকালীন মৌসুমী বায়ু", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_3_2", "bd_3", "প্রাকৃতিক দুর্যোগ (ঘূর্ণিঝড়, ভূমিকম্প, বন্যা, খরা) ও এর ইতিহাস", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_3_3", "bd_3", "পরিবেশ দূষণ (আর্সেনিক সমস্যা, নদী দূষণ, প্লাস্টিক বর্জ্য)", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_3_4", "bd_3", "প্রধান সেচ প্রকল্প ও পানি ব্যবস্থাপনা", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_4_1", "bd_4", "কৃষি সম্পদ (ধান, পাট, চা, গম ও অন্যান্য ফসলের জাত ও উৎপাদন)", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_4_2", "bd_4", "বনজ সম্পদ (সুন্দরবন ও অন্যান্য বনাঞ্চলের বৈশিষ্ট্য ও জীববৈচিত্র্য)", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_4_3", "bd_4", "মৎস্য ও প্রাণিসম্পদ (ইলিশ উৎপাদন, গবাদিপশু ও দুগ্ধ শিল্প)", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_4_4", "bd_4", "খনিজ সম্পদ (প্র প্রাকৃতিক গ্যাস, কয়লা, কঠিন শিলা ও অন্যান্য খনিজ ক্ষেত্র)", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_4_5", "bd_4", "বিদ্যুৎ ও জ্বালানি খাত (তাপবিদ্যুৎ, পারমাণবিক বিদ্যুৎ ও নবায়নযোগ্য জ্বালানি)", 5, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_4_6", "bd_4", "শিল্প সম্পদ (তৈরি পোশাক শিল্প, চামড়া শিল্প, কুটির ও ভারী শিল্প)", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_5_1", "bd_5", "বাংলাদেশের অর্থনৈতিক ব্যবস্থার রূপরেখা", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_5_2", "bd_5", "বাজেট প্রণয়ন প্রক্রিয়া ও জাতীয় রাজস্ব আয়-ব্যয়", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_5_3", "bd_5", "অর্থনৈতিক সমীক্ষা ও সামষ্টিক অর্থনৈতিক সূচকসমূহ", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_5_4", "bd_5", "মুদ্রানীতি, কেন্দ্রীয় ব্যাংক (বাংলাদেশ ব্যাংক) ও বাণিজ্যিক ব্যাংকিং ব্যবস্থা", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_5_5", "bd_5", "দেশের পুঁজিবাজার (ডিএসই ও সিএসই) এবং বীমা খাত", 5, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_5_6", "bd_5", "বৈদেশিক বাণিজ্য (আমদানি, রপ্তানি ও রেমিট্যান্স)", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_5_7", "bd_5", "রপ্তানি প্রক্রিয়াকরণ অঞ্চল (EPZ) ও বিশেষ অর্থনৈতিক অঞ্চল (EZ)", 7, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_6_1", "bd_6", "সড়ক পথ, মেরিন ড্রাইভ ও প্রধান মহাসড়কসমূহ", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_6_2", "bd_6", "মেগা সেতু (পদ্মা সেতু, বঙ্গবন্ধু সেতু) ও ফ্লাইওভারের ইতিহাস ও পরিমাপ", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_6_3", "bd_6", "নৌ-পথ, প্রধান সমুদ্রবন্দর (চট্টগ্রাম, মোংলা, পায়রা) ও স্থলবন্দরসমূহ", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_6_4", "bd_6", "আকাশ পথ ও বিমানবন্দরসমূহ (আন্তর্জাতিক ও অভ্যন্তরীণ)", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_6_5", "bd_6", "রেলপথের নেটওয়ার্ক ও আধুনিকায়ন", 5, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_6_6", "bd_6", "ডাক, টেলিযোগাযোগ, স্যাটেলাইট (বঙ্গবন্ধু স্যাটেলাইট-১) ও তথ্যপ্রযুক্তি অবকাঠামো", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_7_1", "bd_7", "প্রধান প্রধান উপজাতি (চাকমা, গারো, সাঁওতাল, মারমা, ত্রিপুরা)", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_7_2", "bd_7", "উপজাতিদের ভৌগোলিক বাসস্থান, ধর্ম, কৃষ্টি ও প্রধান উৎসবসমূহ", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_7_3", "bd_7", "ঐতিহাসিক উপজাতীয় বিদ্রোহ (সাঁওতাল বিদ্রোহ, চাকমা বিদ্রোহ)", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_7_4", "bd_7", "পার্বত্য চট্টগ্রাম শান্তি চুক্তি ও উপজাতীয় সাংস্কৃতিক কেন্দ্রসমূহ", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_8_1", "bd_8", "বাঙালি জাতির উদ্ভব, নৃ-তাত্ত্বিক পরিচয় ও বিকাশ", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_8_2", "bd_8", "প্রাচীন বাংলার বিখ্যাত জনপদসমূহ (পুণ্ড্র, গৌড়, বঙ্গ, সমতট, হরিকেল)", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_8_3", "bd_8", "বাংলায় ভ্রমণকারী বিখ্যাত পরিব্রাজক ও পর্যটকগণ (ফাহিয়েন, হিউয়েন সাং, ইবনে বতুতা)", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_8_4", "bd_8", "প্রাচীন রাজবংশ: মৌর্য, গুপ্ত, স্বাধীন বঙ্গ ও গৌড় রাজ্য (শশাঙ্ক)", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_8_5", "bd_8", "পাল বংশ ও সেন বংশের শাসন আমল", 5, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_8_6", "bd_8", "উপমহাদেশ ও বাংলায় ইসলামের আগমন এবং মুসলিম শাসন প্রতিষ্ঠা (বখতিয়ার খলজী)", 6, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_8_7", "bd_8", "দিল্লির সালতানাত এবং বাংলায় স্বাধীন সুলতানি আমল (ফখরুদ্দিন মোবারক শাহ, আলাউদ্দিন হোসেন শাহ)", 7, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_8_8", "bd_8", "মুঘল সাম্রাজ্য, বারো ভূঁইয়াদের ইতিহাস এবং সুবাদারি ও নবাবি আমল", 8, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_9_1", "bd_9", "ইউরোপীয় বণিকদের আগমন, পলাশীর যুদ্ধ ও ইংরেজ শাসন প্রতিষ্ঠা", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_9_2", "bd_9", "ব্রিটিশ বিরোধী আদিবাসী ও ধর্মীয় আন্দোলন (ফকির-সন্ন্যাসী, তিতুমীরের বাঁশের কেল্লা, ফরায়েজী আন্দোলন)", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_9_3", "bd_9", "নীল বিদ্রোহ, সাঁওতাল বিদ্রোহ ও সিপাহী বিদ্রোহ (১৮৫৭)", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_9_4", "bd_9", "সমাজ ও শিক্ষা সংস্কার আন্দোলন (রাজা রামমোহন রায়, ঈশ্বরচন্দ্র বিদ্যাসাগর, নবাব আব্দুল লতিফ, সৈয়দ আমীর আলী)", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_9_5", "bd_9", "সর্বভারতীয় কংগ্রেস প্রতিষ্ঠা, বঙ্গভঙ্গ (১৯০৫) ও মুসলিম লীগ প্রতিষ্ঠা (১৯০৬)", 5, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_9_6", "bd_9", "স্বদেশী আন্দোলন, খিলাফত ও অসহযোগ আন্দোলন", 6, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_9_7", "bd_9", "১৯৩৫ সালের ভারত শাসন আইন, লাহোর প্রস্তাব (১৯৪০) এবং ভারত বিভাগ (১৯৪৭)", 7, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_10_1", "bd_10", "দেশভাগ পরবর্তী রাজনৈতিক প্রেক্ষাপট ও আওয়ামী মুসলিম লীগ প্রতিষ্ঠা", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_10_2", "bd_10", "রাষ্ট্রভাষা আন্দোলন (১৯৪৮-১৯৫২), ভাষা শহীদ ও শহীদ মিনারের ইতিহাস", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_10_3", "bd_10", "ভাষা আন্দোলনভিত্তিক সাহিত্য, গান, চলচ্চিত্র ও বাংলা একাডেমির ভূমিকা", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_10_4", "bd_10", "১৯৫৪ সালের পূর্ব বাংলা প্রাদেশিক নির্বাচন ও যুক্তফ্রন্ট গঠন", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_10_5", "bd_10", "আইয়ুব খানের সামরিক শাসন, শিক্ষা আন্দোলন (১৯৬২) ও ঐতিহাসিক ৬-দফা দাবি (১৯৬৬)", 5, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_10_6", "bd_10", "আগরতলা ষড়যন্ত্র মামলা, '৬৯-এর গণঅভ্যুত্থান ও ১৯৭০ সালের সাধারণ নির্বাচন", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_11_1", "bd_11", "১৯৭১ সালের অসহযোগ আন্দোলন ও ৭ই মার্চের ঐতিহাসিক ভাষণ", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_11_2", "bd_11", "অপারেশন সার্চলাইট (২৫শে মার্চ) ও স্বাধীনতার আনুষ্ঠানিক ঘোষণা", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_11_3", "bd_11", "মুজিবনগর সরকার গঠন, শপথ গ্রহণ, কাঠামো ও এর কার্যাবলী", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_11_4", "bd_11", "মুক্তিযুদ্ধের রণকৌশল, ১১টি সেক্টর ও সেক্টর কমান্ডারদের পরিচিতি", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_11_5", "bd_11", "ক্র্যাক প্লাটুন ও মুক্তিযুদ্ধকালীন প্রধান অপারেশনসমূহ", 5, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_11_6", "bd_11", "বুদ্ধিজীবী হত্যাকাণ্ড, চূড়ান্ত বিজয় ও পাকিস্তানি সেনাবাহিনীর আত্মসমর্পণ", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_11_7", "bd_11", "বীরত্বসূচক খেতাব (বীরশ্রেষ্ঠ, বীর উত্তম, বীর বিক্রম, বীর প্রতীক) ও বীরশ্রেষ্ঠদের বিস্তারিত জীবনী", 7, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_11_8", "bd_11", "মুক্তিযুদ্ধে বিশ্বজনমত, বিদেশী বন্ধুদের অবদান ও 'কনসার্ট ফর বাংলাদেশ'", 8, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_11_9", "bd_11", "বঙ্গবন্ধু শেখ মুজিবুর রহমানের স্বদেশ প্রত্যাবর্তন ও রাষ্ট্র পুনর্গঠন", 9, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_11_10", "bd_11", "মুক্তিযুদ্ধ ভিত্তিক সাহিত্য, গান, চলচ্চিত্র, উপন্যাস, স্মৃতিস্তম্ভ ও ভাস্কর্য", 10, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_12_1", "bd_12", "শেখ মুজিবুর রহমানের শাসনামল (১৯৭২-১৯৭৫) ও ঐতিহাসিক ঘটনাবলি", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_12_2", "bd_12", "জিয়াউর রহমানের শাসনামল এবং হুসেইন মুহাম্মদ এরশাদের শাসনামল", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_12_3", "bd_12", "১৯৯১ পরবর্তী সংসদীয় পদ্ধতির শাসনআমল ও বিভিন্ন মেয়াদের সরকারসমূহ", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_13_1", "bd_13", "বাংলাদেশের সংবিধান প্রণয়নের ইতিহাস", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_13_2", "bd_13", "সংবিধানের মূল বৈশিষ্ট্য ও গুরুত্বপূর্ণ অনুচ্ছেদসমূহ", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_13_3", "bd_13", "সংবিধানের এযাবৎকালের সবকটি সংশোধনী ও এর প্রেক্ষাপট", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_13_4", "bd_13", "সরকারের ৩টি বিভাগ: নির্বাহী বিভাগ, আইন বিভাগ (জাতীয় সংসদ) ও বিচার বিভাগ", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_13_5", "bd_13", "সাংবিধানিক পদ ও সংস্থাসমূহ (পিএসসি, নির্বাচন কমিশন, দুদক, অ্যাটর্নি জেনারেল)", 5, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_13_6", "bd_13", "বাংলাদেশের নির্বাচন ব্যবস্থা, রাজনৈতিক দল ও সুশাসন", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_14_1", "bd_14", "উয়ারী-বটেশ্বর, মহাস্থানগড়, সোনারগাঁও ও ময়নামতি", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_14_2", "bd_14", "লালবাগ কেল্লা, আহসান মঞ্জিল, হোসনি দালান ও উত্তরা গণভবন", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_14_3", "bd_14", "ঐতিহাসিক জমিদার বাড়ি ও রাজবাড়িসমূহ (বালিয়াটি, তাজহাট)", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_15_1", "bd_15", "বাংলাদেশের বিখ্যাত ব্যক্তিবর্গ ও তাঁদের অবদান", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_15_2", "bd_15", "বাংলা সাহিত্য, লোক সংস্কৃতি ও বিখ্যাত উৎসবসমূহ", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_15_3", "bd_15", "প্রখ্যাত ভাস্কর ও তাঁদের নির্মিত বিখ্যাত ভাস্কর্যসমূহ", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_15_4", "bd_15", "বাংলাদেশের গণমাধ্যম, বিনোদন ও চলচ্চিত্র শিল্প", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("bd_15_5", "bd_15", "বাংলাদেশের ক্রীড়াঙ্গন (ক্রিকেট, ফুটবল ও জাতীয় খেলা কাবাডি এর ইতিহাস)", 5, "", "[]", "", "[]"),
                    GKSubTopicEntity("bd_15_6", "bd_15", "জাতীয় পদক, পুরস্কার ও বিভিন্ন সম্মাননা", 6, "", "[]", "", "[]")
                ))

                // BD MCQs
                val bdMcqs = mutableListOf<MCQQuestionEntity>()
                for(i in 1..5) {
                    bdMcqs.add(MCQQuestionEntity("bd_mcq_$i", "bd_1_1", "প্রশ্ন $i: প্রাচীন যুগে বাংলার রাজধানী কোথায় ছিল?", 
                        "[\"মহাস্থানগড়\", \"পাহাড়পুর\", \"ময়নামতি\", \"সোনারগাঁও\"]", "মহাস্থানগড়", "মহাস্থানগড় প্রাচীন পুণ্ড্রবর্ধনের রাজধানী ছিল।", 
                        "DU (2021-22)", "2021", "নবম-দশম শ্রেণির ইতিহাস বই", "APPROVED"))
                }
                bdMcqs.addAll(TrueBDTopic1Seed.getMCQs())
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
                bdMcqs.addAll(IntlTopic1Seed.getMCQs())
                bdMcqs.addAll(TrueIntlTopic2Seed.getMCQs())
                bdMcqs.addAll(IntlTopic3Seed.getMCQs())
                bdMcqs.addAll(IntlTopic4Seed.getMCQs())
                bdMcqs.addAll(IntlTopic5Seed.getMCQs())
                bdMcqs.addAll(IntlTopic6Seed.getMCQs())
                bdMcqs.addAll(IntlTopic7Seed.getMCQs())
                bdMcqs.addAll(IntlTopic8Seed.getMCQs())
                repository.insertMCQs(bdMcqs)

                // International Topics
                repository.insertMainTopics(listOf(
                    GKMainTopicEntity("bd_1", "BANGLADESH", "বাংলাদেশ পরিচিতি ও ভূ-প্রকৃতি", 1, "map"),
                    GKMainTopicEntity("bd_2", "BANGLADESH", "নদ-নদী ও জলাশয়", 2, "water_drop"),
                    GKMainTopicEntity("bd_3", "BANGLADESH", "আবহাওয়া, জলবায়ু ও পরিবেশ", 3, "wb_sunny"),
                    GKMainTopicEntity("bd_4", "BANGLADESH", "কৃষি, শিল্প, খনিজ ও বনজ সম্পদ", 4, "agriculture"),
                    GKMainTopicEntity("int_1", "INTERNATIONAL", "পৃথিবী পরিচিতি ও ভৌগোলিক বৈচিত্র্য", 1, "public"),
                    GKMainTopicEntity("int_2", "INTERNATIONAL", "আন্তর্জাতিক সংস্থা, সংগঠন ও জোট", 2, "language"),
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
                ))

                // International Subtopics
                repository.insertSubTopics(TrueBDTopic1Seed.getSubtopics() + BDTopic1Seed.getSubtopics() + IntlTopic1Seed.getSubtopics() + TrueIntlTopic2Seed.getSubtopics() + IntlTopic3Seed.getSubtopics() + IntlTopic4Seed.getSubtopics() + IntlTopic5Seed.getSubtopics() + IntlTopic6Seed.getSubtopics() + IntlTopic7Seed.getSubtopics() + IntlTopic8Seed.getSubtopics() + BDTopic2Seed.getSubtopics() + BDTopic3Seed.getSubtopics() + BDTopic4Seed.getSubtopics() + BDTopic5Seed.getSubtopics() + BDTopic6Seed.getSubtopics() + BDTopic7Seed.getSubtopics() + BDTopic8Seed.getSubtopics() + BDTopic9Seed.getSubtopics() + BDTopic10Seed.getSubtopics() + BDTopic11Seed.getSubtopics() + BDTopic12Seed.getSubtopics() + BDTopic13Seed.getSubtopics() + BDTopic14Seed.getSubtopics() + BDTopic15Seed.getSubtopics() + listOf(
                    // GKSubTopicEntity("int_1_1", "int_1", "বিশ্বের মৌলিক ধারণা, প্রাচীন বিশ্ব সভ্যতা, ঐতিহাসিক সাম্রাজ্য ও রাজবংশ [🌍]", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_1_2", "int_1", "বিশ্বের প্রধান প্রধান ভাষা, গুরুত্বপূর্ণ জাতি, উপজাতি ও নৃ-তাত্ত্বিক গোষ্ঠী", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_1_3", "int_1", "মহাদেশ পরিক্রমা ও ভৌগোলিক বৈশিষ্ট্য (এশিয়া, ইউরোপ, আফ্রিকা, উত্তর ও দক্ষিণ আমেরিকা, ওশেনিয়া ও অ্যান্টার্কটিকা)", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_1_4", "int_1", "বৈশ্বিক জলাশয়: মহাসাগর, সাগর, বিখ্যাত উপসাগর, দ্বীপ, দ্বীপপুঞ্জ, উপদ্বীপ ও বিখ্যাত সমুদ্রবন্দর", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_1_5", "int_1", "আন্তর্জাতিক নদী, হ্রদ (লেক), জলপ্রপাত, প্রণালি, কৃত্রিম খাল ও চ্যানেল", 5, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_1_6", "int_1", "ভূ-প্রকৃতির বৈচিত্র্য: বিখ্যাত অন্তরীপ, পর্বতমালা, মালভূমি, মরুভূমি ও সমভূমি", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_1_7", "int_1", "বিখ্যাত স্থানসমূহের ভৌগোলিক উপনাম এবং সীমারেখা (Border Lines)", 7, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_2_1", "int_2", "মধ্যযুগীয় ইতিহাস: আব্বাসীয় খিলাফত, মঙ্গোল সাম্রাজ্য ও ক্রুসেড বা ধর্মযুদ্ধ", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_2_2", "int_2", "প্রথম বিশ্বযুদ্ধ (১৯১৪-১৯১৮): প্রেক্ষাপট, প্রধান ঘটনাবলি, ফলাফল ও পতন হওয়া সাম্রাজ্যসমূহ", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_2_3", "int_2", "প্রথম বিশ্বযুদ্ধ পরবর্তী চুক্তি ও পদক্ষেপ: পিস ডিক্রি, বেলফোর ঘোষণা, উড্রো উইলসনের চৌদ্দ দফা, দ্বিতীয় ভার্সাই চুক্তি ও সেভ্রেস চুক্তি", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_2_4", "int_2", "জাতিপুঞ্জ (League of Nations) প্রতিষ্ঠা, এর গঠন, ব্যর্থতা এবং ১৯২৯ সালের বৈশ্বিক মহামন্দা ও নয়া নীতি (New Deal)", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_2_5", "int_2", "দ্বিতীয় বিশ্বযুদ্ধ (১৯৩৯-১৯৪৫): যুদ্ধের প্রেক্ষাপট, প্রধান অক্ষ ও মিত্র শক্তি, ঘটনাপঞ্জি ও প্রধান সামরিক অপারেশনসমূহ", 5, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_2_6", "int_2", "যুদ্ধোত্তর প্রতিক্রিয়া ও বিচার: হলোকাস্ট (ইহুদি নিধন), কাতিন গণহত্যা, ন্যুরেমবার্গ ট্রাইব্যুনাল ও জেনেভা কনভেনশন", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_2_7", "int_2", "ভূ-রাজনৈতিক রূপান্তর: উপনিবেশবাদ, নব্য উপনিবেশবাদ, বিশ্বের নিয়ন্ত্রণাধীন অঞ্চল ও দীর্ঘদিনের বিরোধপূর্ণ অঞ্চল", 7, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_3_1", "int_3", "জাতিসংঘ প্রতিষ্ঠার ইতিহাস: আটলান্টিক সনদ থেকে শুরু করে জাতিসংঘ গঠনে বিভিন্ন ঐতিহাসিক সম্মেলন ও পটভূমি", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_3_2", "int_3", "জাতিসংঘের গঠন কাঠামো: প্রতিষ্ঠা, সদস্যপদ লাভ ও বাতিলের নিয়ম, সদর দপ্তর, পতাকা, প্রতীক ও জাতিসংঘ ভবন", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_3_3", "int_3", "বাংলাদেশ ও জাতিসংঘ: সদস্যপদ লাভের ইতিহাস এবং জাতিসংঘে বাংলাদেশের ভূমিকা ও অবদান", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_3_4", "int_3", "৫টি সক্রিয় মূল অঙ্গ সংগঠন: সাধারণ পরিষদ, নিরাপত্তা পরিষদ, অর্থনৈতিক ও সামাজিক পরিষদ, সচিবালয় (মহাসচিবদের পরিচিতি) এবং আন্তর্জাতিক আদালত (ICJ)", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_3_5", "int_3", "জাতিসংঘের বিশেষায়িত সহযোগী সংস্থা, তহবিল, কর্মসূচি ও প্রকল্পসমূহ", 5, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_4_1", "int_4", "সহস্রাব্দ উন্নয়ন লক্ষ্যমাত্রা (MDG) এবং টেকসই উন্নয়ন লক্ষ্যমাত্রা (SDG/SDGs)-এর লক্ষ্য ও সূচকসমূহ", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_4_2", "int_4", "স্বল্পোন্নত দেশ (LDC) সংক্রান্ত আন্তর্জাতিক নিয়ম ও উত্তরণের শর্তাবলী", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_4_3", "int_4", "জাতিসংঘের স্বায়ত্তশাসিত বিশেষায়িত সংস্থাসমূহ: FAO, WHO, ILO, UNESCO, UNICEF, UNDP, IMF, World Bank, WTO", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_4_4", "int_4", "মানবাধিকার ও আন্তর্জাতিক আইন: Universal Declaration of Human Rights, International Bill of Human Rights এবং বৈশ্বিক মানবাধিকার সম্মেলন", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_4_5", "int_4", "নারী ও শিশু অধিকার: CEDAW, UNIFEM, UN Women এবং আন্তর্জাতিক শিশু অধিকার সনদ (UNCRC)", 5, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_4_6", "int_4", "আন্তর্জাতিক বিচার ব্যবস্থা: স্থায়ী সালিশি আদালত (PCA) এবং আন্তর্জাতিক অপরাধ আদালত (ICC)", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_4_7", "int_4", "বিশ্ব নিরাপত্তা ও অস্ত্র চুক্তি/কনভেনশন: রাসায়নিক অস্ত্র কনভেনশন, NPT, CTBT, JCPOA (ইরান পরমাণু চুক্তি), START-2", 7, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_5_1", "int_5", "ইসলামী ও আরব বিশ্বের সংগঠন: OIC, Arab League, GCC, AMU", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_5_2", "int_5", "ঔপনিবেশিক ও জোট নিরপেক্ষ আন্দোলন: Commonwealth, NAM (জোট নিরপেক্ষ আন্দোলন)", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_5_3", "int_5", "ইউরোপ ও এশিয়ার রাজনৈতিক জোট: EU (ইউরোপীয় ইউনিয়ন), CIS, ASEAN, SCO, AU (আফ্রিকান ইউনিয়ন), OAS", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_5_4", "int_5", "দক্ষিণ এশীয় ও আঞ্চলিক সহযোগিতা ফোরাম: SAARC, BIMSTEC, CIRDAP, BCIM, APA, APEC, ACU, ACP", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_6_1", "int_6", "বৈশ্বিক অর্থনৈতিক ব্যবস্থা ও সম্মেলন: ব্রেটন উডস সম্মেলন, বিশ্বব্যাংক (WB), আন্তর্জাতিক মুদ্রা তহবিল (IMF), ওয়ার্ল্ড ইকোনমিক ফোরাম (WEF)", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_6_2", "int_6", "আন্তর্জাতিক বাণিজ্য ও শুল্ক চুক্তি: GATT, WTO, ICC, BRICS, NDB, OPEC, OPEC Plus", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_6_3", "int_6", "আঞ্চলিক ও বৈশ্বিক উন্নয়ন ব্যাংক: ADB, IsDB, AIIB, AfDB, EBRD, EIB", 3, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_6_4", "int_6", "অন্যান্য অর্থনৈতিক সহযোগিতা সংস্থা: G-7, G-77, G-20, D-8, BENELUX, ECO, USMCA, OECD, IPEF", 4, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_6_5", "int_6", "বিশ্ব অর্থনীতি: বিভিন্ন দেশের অর্থনৈতিক মডেল, বিখ্যাত অর্থনীতিবিদদের তত্ত্ব, আন্তর্জাতিক পুঁজিবাজার ও সুনীল অর্থনীতি (Blue Economy)", 5, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_6_6", "int_6", "বৈশ্বিক শিল্প ও সম্পদ: প্রধান খনিজ ও প্রাকৃতিক সম্পদ, শিল্পপ্রধান দেশ, বিখ্যাত শিল্প নগরী এবং বৈশ্বিক অটোমোবাইল ও মোটরগাড়ি নির্মাতা প্রতিষ্ঠানসমূহ", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_7_1", "int_7", "আরব-ইসরায়েল সংঘাত: ১ম, ২য়, ৩য় ও ৪র্থ আরব-ইসরায়েল যুদ্ধ, ক্যাম্প ডেভিড চুক্তি, মিশর-ইসরায়েল শান্তি চুক্তি, অসলো চুক্তি ও উই রিভার চুক্তি", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_7_2", "int_7", "পারস্য উপসাগরীয় সংকট: ইরাক-ইরান যুদ্ধ, প্রথম উপসাগরীয় যুদ্ধ এবং আরব বসন্ত (Arab Spring)-এর ইতিহাস", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_7_3", "int_7", "স্নায়ু যুদ্ধ (Cold War): লৌহ পর্দা (Iron Curtain), উত্তর আটলান্টিক চুক্তি সংস্থা (NATO) এবং ওয়ারশ প্যাক্টের ইতিহাস", 3, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_7_4", "int_7", "স্নায়ুযুদ্ধকালীন বৈশ্বিক সংকট: কোরীয় যুদ্ধ, ভিয়েতনাম যুদ্ধ, কিউবা ক্ষেপণাস্ত্র সংকট, নক্ষত্র যুদ্ধ (Strategic Defense Initiative) এবং সোভিয়েত ইউনিয়নের (USSR) বিলুপ্তি", 4, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_7_5", "int_7", "আন্তর্জাতিক গোয়েন্দা ও পুলিশ সংস্থা: CIA, KGB, MI6, Mossad, INTERPOL", 5, "", "[]", "", "[]"),
                    GKSubTopicEntity("int_7_6", "int_7", "আন্তর্জাতিক সাহায্য ও মানবাধিকার সংস্থা: রেড ক্রস (ICRC), রোটারি ইন্টারন্যাশনাল, অক্সফাম, অ্যামনেস্টি ইন্টারন্যাশনাল, ট্রান্সপারেন্সি ইন্টারন্যাশনাল, CARE, USAID, হিউম্যান রাইটস ওয়াচ", 6, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_8_1", "int_8", "পরিবেশ ও জলবায়ু পরিবর্তন: বৈশ্বিক উষ্ণায়ন (Global Warming), গ্রিন হাউস প্রতিক্রিয়া, সমুদ্রপৃষ্ঠের উচ্চতা বৃদ্ধি ও এর প্রভাব", 1, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_8_2", "int_8", "পরিবেশগত চুক্তি ও কনভেনশন: UNEP, আইপিসিসি (IPCC), কিয়োটো প্রটোকল, প্যারিস জলবায়ু চুক্তি এবং ধরিত্রী সম্মেলন", 2, "", "[]", "", "[]"),
                    // GKSubTopicEntity("int_8_3", "int_8", "ভূ-রাজনীতি ও রাষ্ট্রীয় মতবাদ: বিখ্যাত ভূ-রাজনৈতিক তত্ত্ব, রাজনৈতিক মতবাদ (সমাজতন্ত্র, পুঁজিবাদ, ফ্যাসিবাদ), বিখ্যাত রাজনৈতিক হত্যাকাণ্ড এবং আন্তর্জাতিক রাজনৈতিক ও অর্থনৈতিক কেলেঙ্কারি", 3, "", "[]", "", "[]"),
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
                ))

                // Int MCQs
                val intMcqs = mutableListOf<MCQQuestionEntity>()
                for(i in 1..5) {
                    intMcqs.add(MCQQuestionEntity("int_mcq_$i", "int_1_1", "প্রশ্ন $i: প্রথম বিশ্বযুদ্ধ কবে শেষ হয়?", 
                        "[\"১৯১৪\", \"১৯১৬\", \"১৯১৮\", \"১৯২০\"]", "১৯১৮", "১৯১৮ সালের ১১ নভেম্বর যুদ্ধবিরতি চুক্তির মাধ্যমে প্রথম বিশ্বযুদ্ধ শেষ হয়।", 
                        "JU (2020-21)", "2020", "", "APPROVED"))
                }
                intMcqs.addAll(IntlTopic2Seed.getMCQs())
                intMcqs.addAll(BDTopic1Seed.getMCQs())
                intMcqs.addAll(IntlTopic2Seed.getMCQs())
                repository.insertMCQs(intMcqs)

                // Recent GK
                repository.insertRecentGK(listOf(
                    RecentGKEntity("rgk_1", "BD", "পদ্মা সেতু", "পদ্মা সেতুতে সর্বশেষ স্প্যান বসানো হয়েছে...", "দৈনিক ইত্তেফাক", "Rahim", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_2", "BD", "মেট্রোরেল", "মেট্রোরেলের নতুন লাইন সম্প্রসারণ...", "বাংলাদেশ প্রতিদিন", "", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_3", "INT", "COP 28", "COP 28 সম্মেলন দুবাইয়ে অনুষ্ঠিত...", "Daily Star", "", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis()),
                    RecentGKEntity("rgk_4", "INT", "G20 সম্মেলন", "G20 সম্মেলন ভারতে অনুষ্ঠিত...", "BBC", "Karim", "APPROVED", System.currentTimeMillis(), System.currentTimeMillis())
                ))

                // Mega Quiz Exams
                repository.insertMegaQuizExams(listOf(
                    MegaQuizExamEntity("mq_1", "Mega Quiz 1", System.currentTimeMillis() + 86400000, "UPCOMING"),
                    MegaQuizExamEntity("mq_2", "Mega Quiz 2", System.currentTimeMillis() - 86400000, "COMPLETED")
                ))

                // Mega Quiz Questions
                val mqQuestions = mutableListOf<MegaQuizQuestionEntity>()
                for(i in 1..5) {
                    mqQuestions.add(MegaQuizQuestionEntity("mq1_q$i", "mq_1", "মেগা কুইজ প্রশ্ন $i?", "[\"ক\", \"খ\", \"গ\", \"ঘ\"]", "ক", ""))
                    mqQuestions.add(MegaQuizQuestionEntity("mq2_q$i", "mq_2", "মেগা কুইজ প্রশ্ন $i?", "[\"ক\", \"খ\", \"গ\", \"ঘ\"]", "খ", ""))
                }
                repository.insertMegaQuizQuestions(mqQuestions)

                // Universities
                repository.insertUniversities(listOf(
                    UniversityEntity("u_1", "DU", "ঢাকা বিশ্ববিদ্যালয়", "", 1),
                    UniversityEntity("u_2", "JU", "জাহাঙ্গীরনগর বিশ্ববিদ্যালয়", "", 2),
                    UniversityEntity("u_3", "RU", "রাজশাহী বিশ্ববিদ্যালয়", "", 3),
                    UniversityEntity("u_4", "CU", "চট্টগ্রাম বিশ্ববিদ্যালয়", "", 4),
                    UniversityEntity("u_5", "KU", "খুলনা বিশ্ববিদ্যালয়", "", 5),
                    UniversityEntity("u_6", "IU", "ইসলামী বিশ্ববিদ্যালয়", "", 6),
                    UniversityEntity("u_7", "SUST", "শাহজালাল বিজ্ঞান ও প্রযুক্তি বিশ্ববিদ্যালয়", "", 7),
                    UniversityEntity("u_8", "BUET", "বাংলাদেশ প্রকৌশল বিশ্ববিদ্যালয়", "", 8),
                    UniversityEntity("u_9", "BUP", "বাংলাদেশ ইউনিভার্সিটি অব প্রফেশনালস", "", 9),
                    UniversityEntity("u_10", "JNU", "জগন্নাথ বিশ্ববিদ্যালয়", "", 10)
                ))

                // University Questions
                val uQuestions = mutableListOf<UniversityQuestionEntity>()
                for (year in listOf(2022, 2023)) {
                    for (i in 1..5) {
                        uQuestions.add(UniversityQuestionEntity("uq_du_${year}_$i", "u_1", year, "B Unit", "GK", "DU প্রশ্ন $i ($year)?", "[\"ক\", \"খ\", \"গ\", \"ঘ\"]", "ক", "", ""))
                    }
                }
                repository.insertUniversityQuestions(uQuestions)
            }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
