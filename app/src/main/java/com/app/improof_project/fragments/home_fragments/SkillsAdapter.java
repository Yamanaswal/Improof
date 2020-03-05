package com.app.improof_project.fragments.home_fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.improof_project.R;
import com.app.improof_project.fragments.connection_fragment.ConnectionAdapter;
import com.app.improof_project.models.homepage_model.home_response.HomePageResponseSkillsData;
import com.app.improof_project.models.homepage_model.home_response.HomePageResponseWrapper;
import com.app.improof_project.models.login_model.login_response.LoginResponseSkillsData;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.SkillsViewer> {

    private Context context;
    private List<HomePageResponseSkillsData> skillsList;
    DecimalFormat decimalFormat = new DecimalFormat("#.#");

    //getting the context and product list with constructor
    public SkillsAdapter(Context context, List<HomePageResponseSkillsData> skillsList) {
        this.context = context;
        this.skillsList = skillsList;
    }

    @NonNull
    @Override
    public SkillsViewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.skills_recycler_view, null);
        return new SkillsViewer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsViewer holder, int position) {
        //bind the data with recycler
        holder.skillTitle.setText(skillsList.get(position).getTitle());

        holder.countId.setText( "(" + String.valueOf(skillsList.get(position).getCount()) + ")");
        int people=skillsList.get(position).getCount();
        double rating=skillsList.get(position).getRate();
        double x= Double.parseDouble(rating(rating,people));
        holder.rateId.setText(String.valueOf(x));

    }

    @Override
    public int getItemCount() {
        return skillsList.size();
    }

    public static class SkillsViewer extends RecyclerView.ViewHolder {

        CardView skillsCard;
        TextView skillTitle, rateId, countId;


        public SkillsViewer(@NonNull View itemView) {
            super(itemView);
            skillTitle = itemView.findViewById(R.id.skillTitleId);
            skillsCard = itemView.findViewById(R.id.skillCardId);
            rateId = itemView.findViewById(R.id.rateId);
            countId = itemView.findViewById(R.id.countId);
        }
    }

    private String rating(double rating, int people) {
        double res = rating / people;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        nf.setGroupingUsed(false);
        return decimalFormat.format(res);
    }

}
