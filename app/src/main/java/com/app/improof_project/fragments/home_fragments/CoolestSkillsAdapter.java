package com.app.improof_project.fragments.home_fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.improof_project.R;
import com.app.improof_project.models.homepage_model.home_response.HomePageResponseSkillsData;

import java.util.ArrayList;
import java.util.List;

public class CoolestSkillsAdapter extends RecyclerView.Adapter<CoolestSkillsAdapter.CoolestSkillsViewHolder> {

    private Context context;
    private List<String> coolestskillsList;

    public CoolestSkillsAdapter(Context context,List<String> coolestskillsList){
        this.context = context;
        this.coolestskillsList = coolestskillsList;
    }

    @NonNull
    @Override
    public CoolestSkillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        Log.i("on create","yess");
        View view = inflater.inflate(R.layout.coolest_skills_view, null);
        return new CoolestSkillsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoolestSkillsViewHolder holder, int position) {
        String skill=coolestskillsList.get(position);
        holder.coolest_title.setText(skill);
    }

    @Override
    public int getItemCount() {
        return coolestskillsList.size();
    }

    public static class CoolestSkillsViewHolder extends RecyclerView.ViewHolder{

        TextView coolest_title;

        public CoolestSkillsViewHolder(@NonNull View itemView) {
            super(itemView);
            coolest_title = itemView.findViewById(R.id.coolest_title);
        }
    }

}
