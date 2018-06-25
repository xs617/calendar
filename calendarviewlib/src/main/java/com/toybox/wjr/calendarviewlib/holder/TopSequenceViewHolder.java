package com.toybox.wjr.calendarviewlib.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.toybox.wjr.calendarviewlib.CalendarUtil;
import com.toybox.wjr.calendarviewlib.R;

/**
 * Created by Administrator on 2018/6/21 0021.
 */

public class TopSequenceViewHolder {
    RecyclerView calendarGridLabel;
    View itemView;

    public TopSequenceViewHolder(View itemView) {
        this.itemView = itemView;
        calendarGridLabel = itemView.findViewById(R.id.top_sequence);

    }

    public void build() {
        final int count = 7;
        int wholeWidth = itemView.getResources().getDisplayMetrics().widthPixels;
        int padding = CalendarUtil.get2BaseAverageSpace(wholeWidth, 7);
        itemView.setPadding(padding / 2, itemView.getPaddingTop(), padding / 2, itemView.getPaddingBottom());
        final int width = (wholeWidth - padding) / count;

        calendarGridLabel.setLayoutManager(new GridLayoutManager(itemView.getContext(), count, LinearLayoutManager.VERTICAL, false));
        calendarGridLabel.setAdapter(new RecyclerView.Adapter<TopSequenceItemViewHolder>() {

            @NonNull
            @Override
            public TopSequenceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return TopSequenceItemViewHolder.build(parent, width);
            }

            @Override
            public void onBindViewHolder(@NonNull TopSequenceItemViewHolder holder, int position) {
                Context context = holder.itemView.getContext();
                String data = "";
                switch (position) {
                    case 0:
                        data = context.getString(R.string.Mon);
                        break;
                    case 1:
                        data = context.getString(R.string.Tue);
                        break;
                    case 2:
                        data = context.getString(R.string.Wed);
                        break;
                    case 3:
                        data = context.getString(R.string.Thu);
                        break;
                    case 4:
                        data = context.getString(R.string.Fri);
                        break;
                    case 5:
                        data = context.getString(R.string.Sat);
                        break;
                    case 6:
                        data = context.getString(R.string.Sun);
                        break;
                }
                holder.onBind(data);
            }

            @Override
            public int getItemCount() {
                return count;
            }
        });
    }
}
