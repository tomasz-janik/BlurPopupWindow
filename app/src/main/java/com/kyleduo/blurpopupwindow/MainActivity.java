package com.kyleduo.blurpopupwindow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import static com.kyleduo.blurpopupwindow.R.id.container;

public class MainActivity extends AppCompatActivity {

    private static int[][] sPalettes = new int[][]{
            {0xFFF98989, 0xFFE03535},
            {0xFFC1E480, 0xFF67CC34},
            {0xFFEDF179, 0xFFFFB314},
            {0xFF80DDE4, 0xFF286EDC},
            {0xFFE480C6, 0xFFDC285E},
    };

    private static String[] sTitle = new String[]{
            "Bottom Menu",
            "Share Popup",
            "Dialog like"
    };

    BottomMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        EntranceAdapter adapter = new EntranceAdapter();
        adapter.setOnItemClickListener(new EntranceAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int index) {
                int pos = index % sTitle.length;

                switch (pos) {
                    case 0:
                        if (menu == null) {
                            menu = new BottomMenu.Builder(MainActivity.this).setBlurRadius(0).build();
                        }
                        menu.show();
                        break;
                    case 1:
                        new SharePopup.Builder(MainActivity.this).build().show();
                        break;
                    case 2:
                        new BlurPopupWindow.Builder(MainActivity.this)
                                .setContentView(R.layout.layout_dialog_like)
                                .bindClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(v.getContext(), "Click Button", Toast.LENGTH_SHORT).show();
                                    }
                                }, R.id.dialog_like_bt)
                                .setGravity(Gravity.CENTER)
                                .setScaleRatio(0.2f)
                                .setBlurRadius(10)
                                .setTintColor(0x30000000)
                                .build()
                                .show();
                        break;
                }
            }
        });
        rv.setAdapter(adapter);

    }

    private static class EntranceViewHolder extends RecyclerView.ViewHolder {
        ShadowContainer shadowContainer;
        TextView nameTv;

        public EntranceViewHolder(View itemView, final EntranceAdapter.OnItemClickListener listener) {
            super(itemView);
            shadowContainer = (ShadowContainer) itemView.findViewById(container);
            nameTv = (TextView) itemView.findViewById(R.id.entrance_name_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClicked(getAdapterPosition());
                    }
                }
            });
        }
    }

    private static class EntranceAdapter extends RecyclerView.Adapter<EntranceViewHolder> {

        interface OnItemClickListener {
            void onItemClicked(int index);
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public EntranceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entrance, parent, false);
            return new EntranceViewHolder(view, mOnItemClickListener);
        }

        @Override
        public void onBindViewHolder(EntranceViewHolder holder, int position) {
            holder.shadowContainer.setShadowColor(sPalettes[position][1]);
            holder.shadowContainer.setShadowRadius((int) (holder.shadowContainer.getResources().getDisplayMetrics().density * 6));

            ShadowContainer.ShadowDrawable shadowDrawable = holder.shadowContainer.getShadowDrawable();
            shadowDrawable.setCornerRadius((int) (holder.shadowContainer.getResources().getDisplayMetrics().density * 4));
            shadowDrawable.setColors(sPalettes[position]);

            holder.nameTv.setText(sTitle[position % sTitle.length]);
        }

        @Override
        public int getItemCount() {
            return sPalettes.length;
        }
    }
}
