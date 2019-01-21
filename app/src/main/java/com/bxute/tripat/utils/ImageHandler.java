/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bxute.tripat.R;
import com.bxute.tripat.utils.PixelUtils;

public class ImageHandler {

  /**
   * loads image in circular shape.
   *
   * @param context   application context.
   * @param imageView Target ImageView
   * @param url       url of image.
   */
  public static void loadRoundedImage(final Context context, final ImageView imageView, String url) {
    try {
      int roundRadius = PixelUtils.convertDpToPixel(8, context);
      RequestOptions options = new RequestOptions()
       .placeholder(R.drawable.circular_image_placeholder)
       .error(R.drawable.circular_image_placeholder)
       .diskCacheStrategy(DiskCacheStrategy.ALL)
       .apply(RequestOptions.bitmapTransform(new RoundedCorners(roundRadius)))
       .priority(Priority.HIGH);

      Glide.with(context)
       .asBitmap()
       .load(url)
       .apply(options)
       .listener(new RequestListener<Bitmap>() {
         @Override
         public boolean onLoadFailed(@Nullable GlideException e,
                                     Object model,
                                     Target<Bitmap> target,
                                     boolean isFirstResource) {
           imageView.setImageResource(R.drawable.circular_image_placeholder);
           return false;
         }

         @Override
         public boolean onResourceReady(Bitmap resource,
                                        Object model,
                                        Target<Bitmap> target,
                                        DataSource dataSource,
                                        boolean isFirstResource) {
           imageView.setImageBitmap(resource);
           return true;
         }
       })
       .into(imageView);
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
      imageView.setImageResource(R.drawable.circular_image_placeholder);
    }
  }

  public static void loadCircularImage(final Context context, final ImageView imageView, String url) {
    try {
      RequestOptions options = new RequestOptions()
       .centerCrop()
       .placeholder(R.drawable.circular_image_placeholder)
       .diskCacheStrategy(DiskCacheStrategy.ALL)
       .priority(Priority.HIGH);
      Glide.with(context)
       .asBitmap()
       .load(url)
       .apply(options)
       .listener(new RequestListener<Bitmap>() {
         @Override
         public boolean onLoadFailed(@Nullable GlideException e,
                                     Object model,
                                     Target<Bitmap> target,
                                     boolean isFirstResource) {
           e.printStackTrace();
           return false;
         }

         @Override
         public boolean onResourceReady(Bitmap resource,
                                        Object model,
                                        Target<Bitmap> target,
                                        DataSource dataSource,
                                        boolean isFirstResource) {
           RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory
            .create(context.getResources(), resource);
           circularBitmapDrawable.setCircular(true);
           imageView.setImageDrawable(circularBitmapDrawable);
           return true;
         }
       })
       .into(imageView);
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }
}
