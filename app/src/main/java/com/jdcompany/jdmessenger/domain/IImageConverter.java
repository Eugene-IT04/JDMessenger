package com.jdcompany.jdmessenger.domain;

import android.graphics.Bitmap;

public interface IImageConverter {

    String bitmapToString(Bitmap image);

    Bitmap stringToBitmap(String str);
}
