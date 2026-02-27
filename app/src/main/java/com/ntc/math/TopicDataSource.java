package com.ntc.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicDataSource {

    public static Map<Integer, List<String>> getTopics() {
        Map<Integer, List<String>> topicsByClass = new HashMap<>();

        // Lớp 1
        List<String> class1Topics = new ArrayList<>();
        class1Topics.add("Các số đến 10");
        class1Topics.add("Phép cộng, trừ trong phạm vi 10");
        class1Topics.add("Bài tập tổng hợp");
        topicsByClass.put(1, class1Topics);

        // Lớp 2
        List<String> class2Topics = new ArrayList<>();
        class2Topics.add("Phép cộng, trừ có nhớ (phạm vi 100)");
        class2Topics.add("Các số đến 1000");
        class2Topics.add("Bảng nhân, chia 2, 3, 4, 5");
        class2Topics.add("Bài tập tổng hợp");
        topicsByClass.put(2, class2Topics);

        // Lớp 3
        List<String> class3Topics = new ArrayList<>();
        class3Topics.add("Phép nhân, chia ngoài bảng");
        class3Topics.add("Các số đến 100.000");
        class3Topics.add("Bài tập tổng hợp");
        topicsByClass.put(3, class3Topics);

        // Lớp 4
        List<String> class4Topics = new ArrayList<>();
        class4Topics.add("Phân số và các phép tính");
        class4Topics.add("Dấu hiệu chia hết");
        class4Topics.add("Bài tập tổng hợp");
        topicsByClass.put(4, class4Topics);

        // Lớp 5
        List<String> class5Topics = new ArrayList<>();
        class5Topics.add("Số thập phân và các phép tính");
        class5Topics.add("Tỉ số phần trăm");
        class5Topics.add("Toán chuyển động đều");
        class5Topics.add("Bài tập tổng hợp");
        topicsByClass.put(5, class5Topics);

        return topicsByClass;
    }
}