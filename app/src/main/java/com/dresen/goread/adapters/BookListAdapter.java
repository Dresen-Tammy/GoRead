package com.dresen.goread.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dresen.goread.R;
import com.dresen.goread.model.Book;

import java.util.ArrayList;

// BookListAdapter will bind each book to the view in the Recycler View
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    // class variables.  List of books and the MainActivity context.
    private ArrayList<Book> mBooks = new ArrayList<>();
    private Context mContext;

    // constructor
    public BookListAdapter(Context context, ArrayList<Book> restaurants) {
        mContext = context;
        mBooks = restaurants;
    }

    // inflates the layout and creates the ViewHolder object required for the adapter.
    @NonNull
    @Override
    public BookListAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
        BookViewHolder viewHolder = new BookViewHolder(view);
        return viewHolder;
    }

    // updates the contents of the ItemView to reflect the book in given position.
    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.BookViewHolder holder, int position) {
        holder.bindBook(mBooks.get(position));

    }

    // set the number of items the adapter will display.
    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    // find the views and set their values for the items in the list.
    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;
        TextView mFnameTextView;
        TextView mLnameTextView;
        TextView mDescTextView;

        private Context mContext;

        public BookViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mTitleTextView = itemView.findViewById(R.id.textView_title);
            mFnameTextView = itemView.findViewById(R.id.textView_fname);
            mLnameTextView = itemView.findViewById(R.id.textView_lname);
            mDescTextView = itemView.findViewById(R.id.textView_desc);
        }

        public void bindBook(Book book) {
            mTitleTextView.setText(book.getTitle());
            mFnameTextView.setText(book.getFirstName());
            mLnameTextView.setText(book.getLastName());
            mDescTextView.setText(book.getDescription());
        }
    }
}
