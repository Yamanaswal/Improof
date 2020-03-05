package com.app.improof_project.activities.activities.SkillsAssessment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.improof_project.R;
import com.app.improof_project.models.homepage_model.home_response.HomePageResponseResult;
import com.app.improof_project.models.rating_model.rating_request.RatingList;
import com.app.improof_project.models.rating_model.rating_request.RatingRequest;
import com.app.improof_project.models.skills_assessment_model.skills_assessment_response.SkillsAssessmentResult;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SkillsAssessmentAdapter extends RecyclerView.Adapter<SkillsAssessmentAdapter.SkillsAssessmentViewHolder>  {

    Context context;
    List<SkillsAssessmentResult> assessmentList;
    RatingRequest ratingRequest ;
    SendData callBack;

    public SkillsAssessmentAdapter(Context context, List<SkillsAssessmentResult> assessmentList, RatingRequest ratingRequest,SendData callBack){
        this.context = context;
        this.assessmentList = assessmentList;
        this.ratingRequest = ratingRequest;
        this.callBack = (SendData) context;
    }

    @NonNull
    @Override
    public SkillsAssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.assessment_recyclerview, parent, false);
        return new SkillsAssessmentAdapter.SkillsAssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsAssessmentViewHolder holder, final int position) {
        holder.person_title.setText(assessmentList.get(position).getTitle());
        holder.person_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //rating
                ratingRequest.getRating().get(position).setRate(rating);
                callBack.onClickSubmitButton(ratingRequest.getRating());
            }
        });
    }

    public interface SendData{
       void onClickSubmitButton(List<RatingList> assessmentList);
    }

    @Override
    public int getItemCount() {
        return assessmentList.size();
    }



    public class SkillsAssessmentViewHolder extends RecyclerView.ViewHolder{

        TextView person_title,profileName,profileDesination;
        RatingBar person_rating;
        CardView ratingCard;

        public SkillsAssessmentViewHolder(@NonNull View itemView) {
            super(itemView);

            person_title = itemView.findViewById(R.id.person_title);
            person_rating = itemView.findViewById(R.id.person_rating);
            ratingCard = itemView.findViewById(R.id.ratingCard);
        }
    }
}
