package d.tsaplya.guidelead;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract  class RequestsViewHolder extends RecyclerView.ViewHolder {
    private int mCurrentPosition;

    public RequestsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position) {
        mCurrentPosition = position;
        clear();
    }
    public int getCurrentPosition() {
        return mCurrentPosition;
    }


}
