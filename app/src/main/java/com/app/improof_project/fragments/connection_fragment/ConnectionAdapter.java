package com.app.improof_project.fragments.connection_fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.improof_project.R;
import com.app.improof_project.activities.activities.SkillsAssessment.SkillsAssessmentActivity;
import com.app.improof_project.models.connection_model.connection_response.ConnectionResponseResult;
import com.app.improof_project.models.skills_assessment_model.skills_assessment_request.SkillsAssessmentRequest;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ConnectionView> implements Serializable, Filterable {

    private Context context;
    private List<ConnectionResponseResult> connectionList;
    private List<ConnectionResponseResult> connectionListFull;
    private SkillsAssessmentRequest skillsAssessmentRequest;

    public ConnectionAdapter(FragmentActivity activity, List<ConnectionResponseResult> connectionList) {
        this.context = activity;
        this.connectionList = connectionList;
        connectionListFull = new ArrayList<>(connectionList);
    }

    @NonNull
    @Override
    public ConnectionView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.connection_recycler_view, parent, false);
        skillsAssessmentRequest = new SkillsAssessmentRequest();
        return new ConnectionAdapter.ConnectionView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionView holder, final int position) {
        holder.personName.setText(connectionList.get(position).getName());
        holder.desc.setText(connectionList.get(position).getDesignation());
        String current_pic = connectionList.get(position).getPic_path();

        Glide.with(context)
                .load("http://13.126.99.14/improof/storage/app/" + current_pic)
                .placeholder(R.mipmap.placeholder)
                .into(holder.profile_pic_id);
        skillsAssessmentRequest.setGiven_by(String.valueOf(position));

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(connectionListFull.get(position).getEvaluater()==2) {
                        if (connectionListFull.get(position).getEvaluated()==2)
                        {
                        Intent intent = new Intent(context, SkillsAssessmentActivity.class);
                        intent.putExtra("adapterdata", connectionList.get(position));
                        intent.putExtra("personName", connectionList.get(position).getName());
                        intent.putExtra("designation", connectionList.get(position).getDesignation());
                        intent.putExtra("ImageUrl", connectionList.get(position).getPic_path());
                        context.startActivity(intent);
                    }else
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                            dialog.setTitle("Improof");
                            dialog.setMessage("This Person can not evaluated");
                            dialog.setPositiveButton("ok",null).create().show();
                        }
                    }
                    else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Improof");
                        dialog.setMessage("This Person can not evaluated");
                        dialog.setPositiveButton("ok",null).create().show();
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return connectionList.size();
    }

    /*
    Search Function
     */
    @Override
    public Filter getFilter() {
        return searchResultFilter;
    }

    private Filter searchResultFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ConnectionResponseResult> filterList = new ArrayList<>();

            if(constraint.length() == 0){
                filterList.addAll(connectionListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ConnectionResponseResult item: connectionListFull) {
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                connectionList.clear();
                connectionList.addAll((List) results.values);
                notifyDataSetChanged();
        }
    };

    public static class ConnectionView extends RecyclerView.ViewHolder {

        CircleImageView profile_pic_id;
        TextView personName, desc;
        CardView cardView;

        public ConnectionView(@NonNull View itemView) {
            super(itemView);
            profile_pic_id = itemView.findViewById(R.id.profile_pic_id);
            personName = itemView.findViewById(R.id.person_name_id);
            desc = itemView.findViewById(R.id.desc_id);
            cardView = itemView.findViewById(R.id.connection_recycler_view);
        }
    }

}
