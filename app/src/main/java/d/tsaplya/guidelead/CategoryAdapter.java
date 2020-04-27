package d.tsaplya.guidelead;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<RequestsViewHolder>{

    private static HashMap<Integer,Boolean> isClicked = new HashMap<>();
   private Callback mCallback;
   private List<CategoryRepresentation> categoryList;

   public CategoryAdapter(List<CategoryRepresentation> categoryList){
       this.categoryList = categoryList;
   }

   public void setCallback(Callback callback){
       this.mCallback = callback;

   }

    @NonNull
    @Override
    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (categoryList != null && categoryList.size() > 0) {
            return categoryList.size();
        } else {
            return 1;
        }

    }

    public void addItems(List<CategoryRepresentation> sportList) {
        categoryList.addAll(sportList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RequestsViewHolder{
        @BindView(R.id.categoryIcon)
        ImageView coverImageView;

        @BindView(R.id.categotyTitle)
        TextView categoryTitle;
        @BindView(R.id.categoryDescription)
        TextView categoryDescr;
        @BindView(R.id.card_item)
        CardView cardView;
        @BindView(R.id.constlayout)
        ConstraintLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @SuppressLint("ResourceAsColor")
        public void onBind(int position){
            final CategoryRepresentation categoryRepresentation =categoryList.get(position);

            coverImageView.setImageResource(categoryRepresentation.getCategoryIcon());



            categoryTitle.setText(categoryRepresentation.getCategory().toString());

            categoryDescr.setText(categoryRepresentation.getDescription());


             layout.setOnClickListener(v->{
               boolean state  ;
                ConstraintLayout w = (ConstraintLayout)v;
                 if(isClicked.containsKey(position)) {
                    state = isClicked.get(position);
                    if(state){
                        isClicked.replace(position,false);
                    }else{
                        isClicked.replace(position,true);
                    }

                 }else{
                     state = true;
                     isClicked.put(position,false);
                 }

                if(state){
                    categoryTitle.setTextColor(Color.WHITE);
                    categoryDescr.setTextColor(Color.WHITE);
                    w.setBackgroundColor(0xFF2196F3);


                }else{
                    w.setBackgroundColor(Color.WHITE);
                    categoryTitle.setTextColor(Color.BLACK);
                    categoryDescr.setTextColor(Color.BLACK);
                }

            });


        }


        @Override
        protected void clear() {
            coverImageView.setImageDrawable(null);

            categoryTitle.setText("");
            categoryDescr.setText("");

        }
    }

}

