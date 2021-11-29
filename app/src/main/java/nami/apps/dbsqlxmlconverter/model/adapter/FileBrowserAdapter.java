package nami.apps.dbsqlxmlconverter.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nami.apps.dbsqlxmlconverter.R;

 public class FileBrowserAdapter  extends RecyclerView.Adapter<FileBrowserAdapter.FileViewHolder> implements Filterable {
        private ArrayList<String> filteredData;
        private  ArrayList<String> originalData;
        private ArrayList<String> item;
        private int imageRes;
        private Context context ;
        private onFileBrowserListener onFileBrowserListener;
        private ItemFilter mFilter = new ItemFilter();

        public FileBrowserAdapter(Context context, FileBrowserAdapter.onFileBrowserListener onFileBrowserListener) {
            this.context = context;
            this.onFileBrowserListener = onFileBrowserListener;
        }

        @NonNull
        @Override
        public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.file_browser_recycler_view,parent,false);
            return  new FileViewHolder(view,onFileBrowserListener);
        }

        @Override
        public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
            holder.tv_item.setText(filteredData.get(position));
            if(!isAFile(filteredData.get(position)))
                holder.im_file_image.setBackgroundResource(R.drawable.folder_icon);
            else {
                setFileImage(holder,filteredData.get(position));
            }
        }

     private boolean isAFile(String item)
     {
         String lastChar = item.substring(item.length() - 1);
         if(lastChar.equals("/") )
             return false;
         if(lastChar.equals("0"))
             return false;
         return true;
     }

     private void setFileImage (FileViewHolder holder,String item)
     {
         String[] split = item.split("\\.");
         if(split[split.length-1].equals("db"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_database);
         else  if(split[split.length-1].equals("mp3"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_mp3);
         else  if(split[split.length-1].equals("doc"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_doc);
         else  if(split[split.length-1].equals("docx"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_doc);
         else  if(split[split.length-1].equals("zip"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_zip);
         else  if(split[split.length-1].equals("pdf"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_pdf);
         else  if(split[split.length-1].equals("png"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_png);
         else  if(split[split.length-1].equals("jpeg"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_jpg);
         else  if(split[split.length-1].equals("jpg"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_jpg);
         else  if(split[split.length-1].equals("apk"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_apk);
         else  if(split[split.length-1].equals("xml"))
             holder.im_file_image.setBackgroundResource(R.drawable.ic_xml);
         else
             holder.im_file_image.setBackgroundResource(R.drawable.ic_unknown_file);

     }

        @Override
        public int getItemCount() {
            if(filteredData==null)
                return 0;
            return filteredData.size();
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }

        public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            onFileBrowserListener onFileBrowserListener;
            TextView tv_item;
            ImageView im_file_image;

            public FileViewHolder(@NonNull View itemView, onFileBrowserListener onFileBrowserListener) {
                super(itemView);

                tv_item = (TextView) itemView.findViewById(R.id.tv_directory_list);
                im_file_image =( ImageView) itemView.findViewById(R.id.im_file_image);

                this.onFileBrowserListener = onFileBrowserListener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                onFileBrowserListener.onFileBrowserClick(getAdapterPosition());
            }
        }

        public interface onFileBrowserListener
        {
            void onFileBrowserClick(int position);
        }

        public void setItem(ArrayList<String> item)
        {
            if(this.item!=null)
                this.item.clear();

            this.item = item;
            this.filteredData = item ;
            this.originalData = item ;

            notifyDataSetChanged();
        }

        public void setImage(int imageRes)
        {
            this.imageRes = imageRes;
            notifyDataSetChanged();
        }
        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<String> list = item;

                int count = list.size();
                final ArrayList<String> nlist = new ArrayList<>(count);

                String filterableString ;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i);
                    if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(filterableString);
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }

        }

    }
