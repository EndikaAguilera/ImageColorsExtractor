package com.thisobeystudio.imagecolorsextractor;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    private RecyclerView mRecyclerView;

    private int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.main_image_view);

        initRecyclerView();

        createPaletteAsync();

        Toast.makeText(MainActivity.this,
                "Click any color to copy it's value to clipboard",
                Toast.LENGTH_SHORT)
                .show();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setVisibility(View.VISIBLE);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        int columns = 3;
        RecyclerView.LayoutManager mLayoutManager =
                new GridLayoutManager(MainActivity.this, columns);

        int margin =
                (int) getResources().getDimension(R.dimen.recycler_view_items_separation);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(columns, margin));

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setNestedScrollingEnabled(false); // Make scroll smoothly
    }

    /**
     * Generate palette asynchronously and use it on a different thread using onGenerated()
     */
    private void createPaletteAsync() {

        mPosition++;

        if (mPosition == getImagesIds().length) {
            mPosition = 0;
        }

        mImageView.setImageResource(getImagesIds()[mPosition]);

        Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            public void onGenerated(Palette p) {

                // specify an adapter
                ColorsRecyclerViewAdapter adapter = new
                        ColorsRecyclerViewAdapter(MainActivity.this, getColors(p));
                mRecyclerView.setAdapter(adapter);
            }

        });

    }


    /**
     * @param palette palette
     * @return list of colors
     */
    private List<Integer> getColors(Palette palette) {
        List<Palette.Swatch> swatches = getSwatchesFromPalette(palette);

        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < swatches.size(); i++) {
            colors.add(swatches.get(i).getRgb());
        }
        return colors;
    }

    /**
     * @param p palette
     * @return list of palete swatches
     */
    private List<Palette.Swatch> getSwatchesFromPalette(Palette p) {
        if (p != null) {
            return p.getSwatches();
        } else {
            return null;
        }
    }

    /**
     * @param v next image button view
     */
    public void showNextImage(View v) {
        createPaletteAsync();
    }

    private int[] getImagesIds() {
        return new int[]{
                R.drawable.image_01,
                R.drawable.image_02,
                R.drawable.image_03,
                R.drawable.image_04,
                R.drawable.image_05,
                R.drawable.image_06,
                R.drawable.image_07,
                R.drawable.image_08,
                R.drawable.image_09,
                R.drawable.image_10};
    }

    @SuppressWarnings("unused")
    private List<Palette.Swatch> mGetAllAvailableSwatchesFromPalette(Palette p) {
        if (p != null) {
            // all available get swathes from palette
            List<Palette.Swatch> shortedSwatches = new ArrayList<>();
            shortedSwatches.add(p.getDominantSwatch());
            shortedSwatches.add(p.getVibrantSwatch());
            shortedSwatches.add(p.getLightVibrantSwatch());
            shortedSwatches.add(p.getDarkVibrantSwatch());
            shortedSwatches.add(p.getMutedSwatch());
            shortedSwatches.add(p.getLightMutedSwatch());
            shortedSwatches.add(p.getDarkMutedSwatch());
            return shortedSwatches;
        } else {
            return null;
        }
    }

}
