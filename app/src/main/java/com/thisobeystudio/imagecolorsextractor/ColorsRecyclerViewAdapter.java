package com.thisobeystudio.imagecolorsextractor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by thisobeystudio on 7/11/17.
 * Copyright: (c) 2017 ThisObey Studio
 * Contact: thisobeystudio@gmail.com
 */

class ColorsRecyclerViewAdapter extends RecyclerView.Adapter<ColorsRecyclerViewAdapter.ColorsViewHolder> {

    private final String copyMessage = " HEX Color Copied To Clipboard";

    private final Context context;
    private final List<Integer> colors;

    class ColorsViewHolder extends RecyclerView.ViewHolder {

        final View colorView;
        final TextView colorValueTextView;

        ColorsViewHolder(View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.item_color_view);
            colorValueTextView = itemView.findViewById(R.id.item_color_value_text_view);
        }
    }

    ColorsRecyclerViewAdapter(Context context, List<Integer> colors) {
        this.context = context;
        this.colors = colors;
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    @Override
    public ColorsViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ColorsViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ColorsViewHolder viewHolder, int i) {

        int color = noAlpha(colors.get(i));
        viewHolder.colorView.setBackgroundColor(color);
        final String rgbVal = "#" + Integer.toHexString(color);

        viewHolder.colorValueTextView.setText(rgbVal);

        viewHolder.colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyColorTextToClipboard(rgbVal);
                showSnackBar(view, rgbVal + copyMessage);
            }
        });
    }

    private void copyColorTextToClipboard(String colorText) {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", colorText);
        clipboard.setPrimaryClip(clip);
    }

    private void showSnackBar(View view, String text) {
        Snackbar.make(view,
                text,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    // TODO using no alpha
    private int noAlpha(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(255, red, green, blue);
    }

    @SuppressWarnings("unused")
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

}