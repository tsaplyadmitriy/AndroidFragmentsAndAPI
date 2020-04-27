package d.tsaplya.guidelead;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.widget.ImageView;

public class CategoryRepresentation {
    private Category category;
    private int categoryIcon;
    private String description;

    public CategoryRepresentation(Category category, int categoryIcon, String  description){
        this.category = category;
        this.categoryIcon = categoryIcon;
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
