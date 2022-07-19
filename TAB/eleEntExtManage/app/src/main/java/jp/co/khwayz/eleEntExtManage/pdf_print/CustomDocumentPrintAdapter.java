package jp.co.khwayz.eleEntExtManage.pdf_print;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CustomDocumentPrintAdapter
        extends PrintDocumentAdapter {

    String outputFilePath;

    /**
     * コンストラクタ
     */
    public CustomDocumentPrintAdapter(String outputFilePath){
        this.outputFilePath = outputFilePath;
    }

    /**
     * 印刷ダイアログで、ページサイズや色などの印刷オプションを変更した時に呼び出される。
     * ページ数を計算する（このプログラムでは未実装）
     *
     * @param oldAttributes
     * @param newAttributes
     * @param cancellationSignal
     * @param callback
     * @param extras
     */
    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback, Bundle extras) {
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        //int pages = computePageCount(newAttributes);

        PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("file_name.pdf").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
        callback.onLayoutFinished(pdi, true);
    }

    /**
     * ページのレンダリング時に呼び出される。
     *
     * @param pageRange
     * @param parcelFileDescriptor
     * @param cancellationSignal
     * @param writeResultCallback
     */
    @Override
    public void onWrite(PageRange[] pageRange, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(outputFilePath);
            output = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(input != null){
                    input.close();
                }
                if(output != null){
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
    }
};