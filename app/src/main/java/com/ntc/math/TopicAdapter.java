package com.ntc.math;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<String> topicList;
    private int userClass;

    public TopicAdapter(List<String> topicList, int userClass) {
        this.topicList = topicList;
        this.userClass = userClass;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        String topic = topicList.get(position);
        holder.topicTextView.setText(topic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), QuestionActivity.class);
            intent.putExtra("TOPIC_NAME", topic);
            intent.putExtra("USER_CLASS", userClass);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView topicTextView;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicTextView = itemView.findViewById(R.id.topicTextView);
        }
    }
}