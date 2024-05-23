package Home.Comments_Frag;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.R;

import java.util.List;


//adapter for comments rec view
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comments> commentList;

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView commentText;
        public TextView timestamp;
        View row;

        public CommentViewHolder(View row) {
            super(row);
            this.row = row;

            userName = row.findViewById(R.id.CommentName);
            commentText = row.findViewById(R.id.CommentText);
            timestamp = row.findViewById(R.id.CommentTime);
        }
    }

    public CommentsAdapter(List<Comments> commentList) {
        this.commentList = commentList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comments_rec_view_act, parent, false);
        return new CommentsAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comments comment = commentList.get(position);
        holder.userName.setText(comment.getUserName());
        holder.commentText.setText(comment.getComment());
        holder.timestamp.setText(DateFormat.format("dd-MM-yyyy", comment.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
