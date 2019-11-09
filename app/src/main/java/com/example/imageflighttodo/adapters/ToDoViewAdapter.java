package com.example.imageflighttodo.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.imageflighttodo.R;
import com.example.imageflighttodo.model.ToDoItem;
import com.example.imageflighttodo.util.EmployeeTranslator;

import java.util.List;

public class ToDoViewAdapter extends RecyclerView.Adapter<ToDoViewAdapter.ViewHolder> {
    private Context ctx;
    private List<ToDoItem> ToDos;

    public ToDoViewAdapter(Context context, List<ToDoItem> list){
        this.ctx = context;
        this.ToDos = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.item_row, viewGroup, false);

        return new ViewHolder(view, this.ctx);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ToDoItem item = ToDos.get(i);
        viewHolder.title.setText(item.getTitle());
        viewHolder.description.setText(item.getDescription());
        // TODO: 09.11.2019:  ADD USER ASSIGNED
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        viewHolder.date.setText(df.format("dd.MM.yyyy 'at' HH:mm", item.getDate()));

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: 09.11.2019:  ADD DELETE ITEM METHOD

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.ToDos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, assignment, date;
        Button delete;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            date = itemView.findViewById(R.id.card_date);
            delete=itemView.findViewById(R.id.button_delete);
            assignment = itemView.findViewById(R.id.card_assignment);
            title = itemView.findViewById(R.id.card_title);
            description = itemView.findViewById(R.id.card_description);


        }
    }
}
