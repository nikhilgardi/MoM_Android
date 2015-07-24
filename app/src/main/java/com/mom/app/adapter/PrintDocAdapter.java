package com.mom.app.adapter;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;

/**
 * Created by Pbx12 on 6/15/2015.
 */
@TargetApi(19)
public class PrintDocAdapter extends PrintDocumentAdapter {


    private void drawPage(PdfDocument.Page page,
                          int pagenumber) {
        Canvas canvas = page.getCanvas();

        pagenumber++; // Make sure page numbers start at 1

        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        canvas.drawText(
                "Test Print Document Page " + pagenumber,
                leftMargin,
                titleBaseLine,
                paint);

        paint.setTextSize(14);
        canvas.drawText("This is some test content to verify that custom document printing works", leftMargin, titleBaseLine + 35, paint);

        if (pagenumber % 2 == 0)
            paint.setColor(Color.RED);
        else
            paint.setColor(Color.GREEN);

        PdfDocument.PageInfo pageInfo = page.getInfo();


        canvas.drawCircle(pageInfo.getPageWidth() / 2,
                pageInfo.getPageHeight() / 2,
                150,
                paint);
    }


    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, android.print.PrintDocumentAdapter.LayoutResultCallback callback, Bundle extras) {

    }


    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, android.print.PrintDocumentAdapter.WriteResultCallback callback) {

    }
}
