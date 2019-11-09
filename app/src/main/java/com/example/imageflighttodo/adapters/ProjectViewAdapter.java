package com.example.imageflighttodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imageflighttodo.R;
import com.example.imageflighttodo.model.Project;
import com.example.imageflighttodo.model.ToDoItem;
import com.example.imageflighttodo.util.EmployeeTranslator;

import java.util.List;

public class ProjectViewAdapter extends RecyclerView.Adapter<ProjectViewAdapter.ViewHolder> {
    private Context ctx;
    private List<Project> projects;

    public ProjectViewAdapter(Context context, List<Project> list){
        this.ctx = context;
        this.projects = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.item_row_project, viewGroup, false);

        return new ViewHolder(view, this.ctx);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Project item = projects.get(i);
        viewHolder.title.setText(item.getTitle());
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        viewHolder.date.setText(df.format("dd.MM.yyyy 'at' HH:mm", item.getDate()));
        ArrayAdapter<String> adapter = new ArrayAdapter(ctx,android.R.layout.simple_list_item_1,item.getUsers());



        viewHolder.collaborators.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // TODO: 09.11.2019:  DELETE PROJECT METHOD


            }
        });

    }

    @Override
    public int getItemCount() {
        return this.projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date;
        ListView collaborators;
        Button delete;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            date = itemView.findViewById(R.id.card_date);
            delete=itemView.findViewById(R.id.button_delete);
            title = itemView.findViewById(R.id.card_title);
            collaborators =  itemView.findViewById(R.id.card_assignment_list);


        }
    }

    public void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}
