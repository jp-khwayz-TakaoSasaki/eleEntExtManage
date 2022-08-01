package jp.co.khwayz.eleEntExtManage.casemark_print.task;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.casemark_print.CaseMarkPrintInfo;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import timber.log.Timber;
/**
 * ケースマーク印刷データ生成
 */
public class CaseMarkPrintDataCreateTask {
    // region [ private variable ]
    public static final String TAG = CaseMarkPrintDataCreateTask.class.getSimpleName();
    private final CaseMarkPrintDataCreateTask.Callback mCallback;
    private final String mInvoiceNo;    // All指定の際は空文字
    private final String mOutPutPDFPath;
    ArrayList<CaseMarkPrintInfo> mPrintList;

    // PDF
    private PdfDocument pdfDocument;
    private Paint paint;
    private int mPageCount = 0;

    // endregion

    public interface Callback<T> {
        void onPreExecute();
        void onTaskFinished(String invoiceNo);
        void onError();
    }

    private class AsyncRunnable implements Runnable {
        final Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void run() {
            int result = doInBackground();
            // 後処理
            handler.post(() -> onPostExecute(result));
        }
    }

    // region [ constructor ]
    public CaseMarkPrintDataCreateTask(@NonNull CaseMarkPrintDataCreateTask.Callback callback, String invoiceNo, String pdfPath) {
        mCallback = callback;
        mInvoiceNo = invoiceNo;
        mOutPutPDFPath = pdfPath;
    }
    // endregion

    public void execute() {
        // 事前処理
        mCallback.onPreExecute();
        // thread開始
        ExecutorService executors = Executors.newSingleThreadExecutor();
        executors.submit(new AsyncRunnable());
    }

    private int doInBackground() {
        try {
            // 印刷データ生成
            createPrintDataList(this.mInvoiceNo);

            // 処理終了
            return 0;
        } catch (Exception e) {
            Timber.e(e);
            return -1;
        }
    }

    private void onPostExecute(int response) {
        if (response == -1) {
            mCallback.onError();
        } else {
            mCallback.onTaskFinished(mInvoiceNo);
        }
    }
    // endregion

    /**
     * ケースマーク印刷データリスト生成
     * @param invoiceNo
     * @return
     */
    private void createPrintDataList(String invoiceNo){
        // 一括印刷の場合
        if(invoiceNo.isEmpty()){
            // 全情報取得
            mPrintList = new KonpoOuterDao().getAllPrintInfoList(Application.dbHelper.getWritableDatabase());
        } else {
            // 指定Invoice番号情報取得
            mPrintList = new KonpoOuterDao().getPrintInfoList(Application.dbHelper.getWritableDatabase(),
                    invoiceNo);
        }
        // QRコード文字列生成
        // QRコード文字列　：企業コード(9) + Invoice番号(15：左寄せ半角SPパディング) + ケースマーク番号(4：右寄せ0パディング)
        for(CaseMarkPrintInfo info : mPrintList) {
            Bitmap qrCodeImage = createQrCode(
                    Application.corporateCode
                            + String.format("%-15s", info.getInvoiceNo())
                            + String.format("%04d", Integer.parseInt(info.getCaseMarkNo())));
            info.setQrCodeImage(qrCodeImage);
        }

        // ケースマーク番号付加
        addCaseMarkNum();

        // PDFファイル生成
        createPDF();
    }

    /**
     * QRコード生成
     * @param qrCodeString
     * @return
     */
    private Bitmap createQrCode(String qrCodeString){
        Bitmap bitmap = null;
        try {
            //QRコード画像の大きさを指定(pixel)
            int size = Constants.QR_IMAGE_SIZE;

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            //QRコードをBitmapで作成
            bitmap = barcodeEncoder.encodeBitmap(qrCodeString, BarcodeFormat.QR_CODE, size, size);

        } catch (WriterException e) {
            Timber.e(e);
        }
        return bitmap;
    }

    /**
     * ケースマーク番号付加
     *  各ケースマーク文字列に"C/NO" がある場合、末尾に半角スペース2個とケースマーク番号を付加する
      */
    private void addCaseMarkNum(){
        for(CaseMarkPrintInfo info : mPrintList){
            String caseMark1 = checkCaseMarkString(info.getCaseMarkRow01(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark1.isEmpty())info.setCaseMarkRow01(caseMark1);
            String caseMark2 = checkCaseMarkString(info.getCaseMarkRow02(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark2.isEmpty())info.setCaseMarkRow02(caseMark2);
            String caseMark3 = checkCaseMarkString(info.getCaseMarkRow03(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark3.isEmpty())info.setCaseMarkRow03(caseMark3);
            String caseMark4 = checkCaseMarkString(info.getCaseMarkRow04(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark4.isEmpty())info.setCaseMarkRow04(caseMark4);
            String caseMark5 = checkCaseMarkString(info.getCaseMarkRow05(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark5.isEmpty())info.setCaseMarkRow05(caseMark5);
            String caseMark6 = checkCaseMarkString(info.getCaseMarkRow06(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark6.isEmpty())info.setCaseMarkRow06(caseMark6);
            String caseMark7 = checkCaseMarkString(info.getCaseMarkRow07(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark7.isEmpty())info.setCaseMarkRow07(caseMark7);
            String caseMark8 = checkCaseMarkString(info.getCaseMarkRow08(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark8.isEmpty())info.setCaseMarkRow08(caseMark8);
            String caseMark9 = checkCaseMarkString(info.getCaseMarkRow09(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark9.isEmpty())info.setCaseMarkRow09(caseMark9);
            String caseMark10 = checkCaseMarkString(info.getCaseMarkRow10(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark10.isEmpty())info.setCaseMarkRow10(caseMark10);
            String caseMark11 = checkCaseMarkString(info.getCaseMarkRow11(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark11.isEmpty())info.setCaseMarkRow11(caseMark11);
            String caseMark12 = checkCaseMarkString(info.getCaseMarkRow12(), String.valueOf(info.getCaseMarkNo()));
            if(!caseMark12.isEmpty())info.setCaseMarkRow12(caseMark12);
        }
    }

    private String checkCaseMarkString(String caseMarkRowStr, String caseMarkNumStr){
        String retStr = "";
        String upperString = caseMarkRowStr.toUpperCase();
        if(upperString.indexOf(Constants.CHK_CASEMARK_STR) != -1){
            retStr = caseMarkRowStr + Constants.SP_STR + caseMarkNumStr;
        }
        return retStr;
    }

    /**
     * ケースマークPDFファイル生成
     */
    private void createPDF(){
        // Paint生成
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(Constants.CANVAS_TEXT_SIZE);

        // PDFドキュメント生成
        pdfDocument = new PdfDocument();
        FileOutputStream fos = null;

        // PDF生成
        try {
            // ページ生成
            // Invoce番号ごと、かつ、4ケースマーク番号ごとにページ生成する。
            ArrayList<CaseMarkPrintInfo> pdfPageDataList = new ArrayList<CaseMarkPrintInfo>();
            String chkInvoiceNo = mPrintList.get(0).getInvoiceNo();
            int caseMarkCount = 0;
            for (CaseMarkPrintInfo info : mPrintList){
                caseMarkCount++;
                // Invoice番号違い
                if(!chkInvoiceNo.equals(info.getInvoiceNo())){
                    // ページデータ追加
                    addPage(pdfPageDataList);
                    pdfPageDataList.clear();
                    // リスト追加
                    pdfPageDataList.add(info);
                    // ケースマーク番号更新
                    chkInvoiceNo=info.getInvoiceNo();
                    caseMarkCount = 1;
                    continue;
                }
                // 4ケースマーク目
                if(caseMarkCount == 4){
                    // リスト追加
                    pdfPageDataList.add(info);
                    // ページデータ追加
                    addPage(pdfPageDataList);
                    pdfPageDataList.clear();
                    caseMarkCount = 0;
                    continue;
                }
                // リスト追加
                pdfPageDataList.add(info);
            }

            // ページ未追加データあり
            if(pdfPageDataList.size() > 0) {
                // ページデータ追加
                addPage(pdfPageDataList);
            }

            //PDFの書き込みストリームを生成する。
            fos = new FileOutputStream(mOutPutPDFPath);
            //ストリームにPdfDocumentの内容を書き込む
            pdfDocument.writeTo(fos);
            //PdfDocumentを閉じる
            pdfDocument.close();
        } catch (FileNotFoundException e) {
            Timber.e(e);

        } catch (IOException e) {
            Timber.e(e);

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }
    }

    /**
     * ケースマークPDFファイルページ追加
     * @param pdfPageDataList
     */
    public void addPage(ArrayList<CaseMarkPrintInfo> pdfPageDataList){
        /**
         * PageInfoインスタンスを作成するためのビルダーをインスタンス化する。
         * A4１ページ
         *  幅:595point(72dpi)
         *  高さ:842point(72dpi)
         */
        mPageCount++;
        PdfDocument.PageInfo.Builder builder = new PdfDocument.PageInfo.Builder(595, 842, mPageCount);
        PdfDocument.PageInfo pageInfo = builder.create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Invoice番号
        paint.setTextAlign(Paint.Align.RIGHT);
        page.getCanvas().drawText(pdfPageDataList.get(0).getInvoiceNo() ,Constants.CANVAS_INVOICE_START ,Constants.CANVAS_INVOICE_TOP, paint);
        paint.setTextAlign(Paint.Align.LEFT);

        // Canvas 左上
        page.getCanvas().drawBitmap(pdfPageDataList.get(0).getQrCodeImage(),Constants.CANVAS_QR_LEFT_START, Constants.CANVAS_QR_UP_TOP, paint);
        float yPosition = Constants.CANVAS_UP_TOP;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow01() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow02() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow03() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow04() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow05() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow06() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow07() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow08() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow09() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow10() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow11() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        yPosition += Constants.CANVAS_TEXT_PADDING;
        page.getCanvas().drawText(pdfPageDataList.get(0).getCaseMarkRow12() ,Constants.CANVAS_LEFT_START ,yPosition, paint);

        // Canvas 右上
        if(pdfPageDataList.size() >= 2) {
            page.getCanvas().drawBitmap(pdfPageDataList.get(1).getQrCodeImage(),Constants.CANVAS_QR_RIGHT_START, Constants.CANVAS_QR_UP_TOP, paint);
            yPosition = Constants.CANVAS_UP_TOP;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow01() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow02() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow03() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow04() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow05() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow06() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow07() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow08() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow09() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow10() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow11() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(1).getCaseMarkRow12() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
        }

        // Canvas 左下
        if(pdfPageDataList.size() >= 3) {
            page.getCanvas().drawBitmap(pdfPageDataList.get(2).getQrCodeImage(),Constants.CANVAS_QR_LEFT_START, Constants.CANVAS_QR_UNDER_TOP, paint);
            yPosition = Constants.CANVAS_UNDER_TOP;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow01() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow02() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow03() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow04() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow05() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow06() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow07() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow08() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow09() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow10() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow11() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(2).getCaseMarkRow12() ,Constants.CANVAS_LEFT_START ,yPosition, paint);
        }

        // Canvas 右下
        if(pdfPageDataList.size() >= 4) {
            page.getCanvas().drawBitmap(pdfPageDataList.get(3).getQrCodeImage(),Constants.CANVAS_QR_RIGHT_START, Constants.CANVAS_QR_UNDER_TOP, paint);
            yPosition = Constants.CANVAS_UNDER_TOP;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow01() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow02() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow03() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow04() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow05() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow06() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow07() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow08() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow09() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow10() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow11() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
            yPosition += Constants.CANVAS_TEXT_PADDING;
            page.getCanvas().drawText(pdfPageDataList.get(3).getCaseMarkRow12() ,Constants.CANVAS_RIGHT_START ,yPosition, paint);
        }

        //Pageの編集を終了する。
        pdfDocument.finishPage(page);
    }
}
