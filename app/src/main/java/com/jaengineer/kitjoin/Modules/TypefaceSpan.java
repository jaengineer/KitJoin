/*
 * This is the source code of Telegram for Android v. 1.7.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2014.
 */

package com.jaengineer.kitjoin.Modules;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.Log;

//MetricAffectingSpan: esta clase permite cambiar la anchura y altura de los carácteres
//Paint: contiene la información de estilo y color sobre cómo dibujar geometrías, texto y mapas de bits
//Paint.SUBPIXEL_TEXT_FLAG:  permite el posicionamiento de subpíxeles de texto
public class TypefaceSpan extends MetricAffectingSpan {

    private static String TAG = "TYPE_FACE_FACE";

    private Typeface mTypeface;

    public TypefaceSpan(Typeface typeface) {
        mTypeface = typeface;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
      //Paint.SUBPIXEL_TEXT_FLAG = 128
        p.setTypeface(mTypeface);
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
