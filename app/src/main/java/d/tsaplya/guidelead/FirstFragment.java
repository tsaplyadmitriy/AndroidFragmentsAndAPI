package d.tsaplya.guidelead;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

public class FirstFragment extends Fragment {


    RecyclerView mRecyclerView;
    CategoryAdapter mCategoryAdapter;

    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        UserPreferences newUserPreferences = null;
        try {
             newUserPreferences = APIRequests.getUserPreference(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mRecyclerView = view.findViewById(R.id.categoryList);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this.getActivity(), R.drawable.divider_drawable);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mCategoryAdapter = new CategoryAdapter(new ArrayList<>());

        ArrayList<CategoryRepresentation> categoryRepresentations = new ArrayList<>();
        categoryRepresentations.add(new CategoryRepresentation(Category.ARCHITECTURE,
                R.drawable.icon,"Description of this category1"));
        categoryRepresentations.add(new CategoryRepresentation(Category.RELIGION,
                R.drawable.icon,"Description of this category2"));
        categoryRepresentations.add(new CategoryRepresentation(Category.ART,
                R.drawable.icon,"Description of this category3"));
        categoryRepresentations.add(new CategoryRepresentation(Category.LOCAL_CULTURE,
                R.drawable.icon,"Description of this category4"));
        categoryRepresentations.add(new CategoryRepresentation(Category.ARCHITECTURE,
                R.drawable.icon,"Description of this category1"));
        categoryRepresentations.add(new CategoryRepresentation(Category.RELIGION,
                R.drawable.icon,"Description of this category2"));
        categoryRepresentations.add(new CategoryRepresentation(Category.ART,
                R.drawable.icon,"Description of this category3"));
        categoryRepresentations.add(new CategoryRepresentation(Category.LOCAL_CULTURE,
                R.drawable.icon,"Description of this category4"));
        categoryRepresentations.add(new CategoryRepresentation(Category.ARCHITECTURE,
                R.drawable.icon,"Description of this category1"));
        categoryRepresentations.add(new CategoryRepresentation(Category.RELIGION,
                R.drawable.icon,"Description of this category2"));
        categoryRepresentations.add(new CategoryRepresentation(Category.ART,
                R.drawable.icon,"Description of this category3"));
        categoryRepresentations.add(new CategoryRepresentation(Category.LOCAL_CULTURE,
                R.drawable.icon,"Description of this category4"));

        mCategoryAdapter.addItems(categoryRepresentations);
        mRecyclerView.setAdapter(mCategoryAdapter);


        //TextView jsonContainer = view.findViewById(R.id.jsonRespond);
        //jsonContainer.setText(newUserPreferences.toString());
    }
}
