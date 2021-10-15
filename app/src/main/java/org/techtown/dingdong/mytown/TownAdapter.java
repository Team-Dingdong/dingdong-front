package org.techtown.dingdong.mytown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.techtown.dingdong.R;

import java.util.ArrayList;

public class TownAdapter extends RecyclerView.Adapter<TownAdapter.ViewHolder>
        implements Filterable
{
    private final boolean what;
    ArrayList<TownItem> townlist;
    ArrayList<TownItem> filteredList;

    int num;
    TownItem townItem;



    public TownAdapter(Context context, ArrayList<TownItem> townlist, boolean what){


        this.townlist = new ArrayList<>(townlist);
        this.what = what;
        this.filteredList = townlist;

    }


    public interface OnItemClickListener{

        void onItemClick(View view,TownItem townItem);
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener =listener;
    }

    public void dataSetChanged(ArrayList<TownItem> exampleList) {
        filteredList = exampleList;
        notifyDataSetChanged();
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemview = inflater.inflate(R.layout.item_town, viewGroup, false);
        ViewHolder holder = new ViewHolder(itemview);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull final ViewHolder viewholder,final int position) {
        TownItem currentItem = filteredList.get(position);
        viewholder.textView.setText(currentItem.getName());
        viewholder.tv_num.setText(currentItem.getId());

        if (mListener != null) {
            final int pos = position;
            //final ItemModel item = mItems.get(viewHolder.getAdapterPosition());
            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v, currentItem);
                    //mListener.onItemClicked(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    //검색 기능
   /* public void filterList(ArrayList<Town> filterList){
        items = filterList;
        notifyDataSetChanged();
    }
    */



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, tv_num;


        public ViewHolder(View itemview) {
            super(itemview);
            textView = itemView.findViewById(R.id.tv_townitem);
            tv_num = itemView.findViewById(R.id.tv_num);


        }


    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<TownItem> filteredList = new ArrayList<>();
                if(constraint == null || constraint.length() == 0) {
                    filteredList.addAll(townlist);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for(TownItem item : townlist) {
                        if(item.getName().toLowerCase().contains(filterPattern)) { //스트링 형식아니라 다른
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
               filteredList.clear();
                filteredList.addAll((ArrayList)filterResults.values);
                notifyDataSetChanged();


            }
        };
    }

