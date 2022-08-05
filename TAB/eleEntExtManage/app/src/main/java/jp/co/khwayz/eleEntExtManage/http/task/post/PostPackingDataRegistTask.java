package jp.co.khwayz.eleEntExtManage.http.task.post;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.InnerInfo;
import jp.co.khwayz.eleEntExtManage.common.models.IssueTagInfo;
import jp.co.khwayz.eleEntExtManage.common.models.OuterInfo;
import jp.co.khwayz.eleEntExtManage.common.models.OverPackKonpoShizaiInfo;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoInnerDao;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.OverpackKonpoShizaiDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.http.request.PackingDataRegistRequest;
import jp.co.khwayz.eleEntExtManage.http.response.SimpleResponse;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;
import jp.co.khwayz.eleEntExtManage.util.Util;

/**
 * 梱包情報登録Task
 */
public class PostPackingDataRegistTask extends HttpTaskBase<SimpleResponse> {
    // region [ private variable ]
    private final String mURL;
    private final String mInvoiceNo;
    private final String mPackingHoldFrag;

    // endregion

    // region [ constructor ]
    public PostPackingDataRegistTask(@NonNull Callback<SimpleResponse> callback, String url,
                                     String invoiceNo, String packingHoldFlag) {
        super(callback);
        mURL = url;
        mInvoiceNo = invoiceNo;
        mPackingHoldFrag = packingHoldFlag;
    }
    // endregion

    @Override
    protected SimpleResponse doInBackground() {
        SimpleResponse result;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().create();

        try {
            // リクエストパラメータ作成
            PackingDataRegistRequest requestParam = new PackingDataRegistRequest();
            // Invoice番号
            requestParam.setInvoiceNo(mInvoiceNo);
            // ピッキング保留フラグ
            requestParam.setKonpoHoldFlag(mPackingHoldFrag);

            // オーバーパック梱包資材リスト
            ArrayList<OverPackKonpoShizaiInfo> overPackList =
                    new OverpackKonpoShizaiDao().getOverPackKonpoShizaiList(Application.dbHelper.getWritableDatabase(), mInvoiceNo);
            // 明細
            for (OverPackKonpoShizaiInfo detail : overPackList) {
                PackingDataRegistRequest.OverPackRequestDetail req = new PackingDataRegistRequest.OverPackRequestDetail();
                req.setOverPackNo(detail.getOverPackNo());
                req.setPackingMaterialNo(detail.getPackingMaterialNo());
                req.setPackingMaterial(detail.getPackingMaterial());
                // リストに追加
                requestParam.getOverPacklist().add(req);
            }

            // 梱包アウターリスト
            ArrayList<OuterInfo> outerList =
                    new KonpoOuterDao().getOuterInfoList(Application.dbHelper.getWritableDatabase(), mInvoiceNo);
            // 明細
            for (OuterInfo detail : outerList) {
                PackingDataRegistRequest.OuterRequestDetail req = new PackingDataRegistRequest.OuterRequestDetail();
                req.setCsNumber(Util.getJsonInt(detail.getCsNumber()));
                req.setHyokiCsNumber(Util.getJsonStr(detail.getHyokiCsNumber()));
                req.setOuterSagyo1(Util.getJsonStr(detail.getOuterSagyo1()));
                req.setOuterSagyo1Siyo(Util.getJsonDbl(detail.getOuterSagyo1Siyo()));
                req.setOuterSagyo2(Util.getJsonStr(detail.getOuterSagyo2()));
                req.setOuterSagyo2Siyo(Util.getJsonDbl(detail.getOuterSagyo2Siyo()));
                req.setOuterSagyo3(Util.getJsonStr(detail.getOuterSagyo3()));
                req.setOuterSagyo3Siyo(Util.getJsonDbl(detail.getOuterSagyo3Siyo()));
                req.setOuterSagyo4(Util.getJsonStr(detail.getOuterSagyo4()));
                req.setOuterSagyo4Siyo(Util.getJsonDbl(detail.getOuterSagyo4Siyo()));
                req.setBlueiceSiyo(Util.getJsonDbl(detail.getBlueIceSiyo()));
                req.setDryiceSiyo(Util.getJsonDbl(detail.getDryIceSiyo()));
                req.setLabelSu(Util.getJsonInt(detail.getLabelSu()));
                req.setKonpoSu(Util.getJsonInt(detail.getKonpoSu()));
                req.setOuterLength(Util.getJsonDbl(detail.getOuterLength()));
                req.setOuterWidth(Util.getJsonDbl(detail.getOuterWidth()));
                req.setOuterHeight(Util.getJsonDbl(detail.getOuterHeight()));
                req.setNetWeight(Util.getJsonDbl(detail.getNetWeight()));
                req.setGrossWeight(Util.getJsonDbl(detail.getGrossWeight()));
                req.setSaisyuKonpoNisugata(Util.getJsonStr(detail.getSaisyuKonpoNisugata()));
                req.setBiko(Util.getJsonStr(detail.getBiko()));
                req.setPalettUchiwake(Util.getJsonStr(detail.getPalettUchiwake()));
                req.setCartonSu(Util.getJsonInt(detail.getCartonSu()));
                req.setNifudaSu(Util.getJsonInt(detail.getNifudaSu()));
                // リストに追加
                requestParam.getOuterList().add(req);
            }

            // 梱包インナーリスト
            ArrayList<InnerInfo> innerList =
                    new KonpoInnerDao().getInnerInfoList(Application.dbHelper.getWritableDatabase(), mInvoiceNo);
            // 明細
            for (InnerInfo detail : innerList) {
                PackingDataRegistRequest.InnerRequestDetail req = new PackingDataRegistRequest.InnerRequestDetail();
                req.setRenban(Util.getJsonInt(detail.getRenban()));
                req.setLineNo(Util.getJsonInt(detail.getLineNo()));
                req.setInnerSagyo1(Util.getJsonStr(detail.getInnerSagyo1()));
                req.setInnerSagyo1Siyo(Util.getJsonDbl(detail.getInnerSagyo1Siyo()));
                req.setInnerSagyo2(Util.getJsonStr(detail.getInnerSagyo2()));
                req.setInnerSagyo2Siyo(Util.getJsonDbl(detail.getInnerSagyo2Siyo()));
                req.setInnerSagyo3(Util.getJsonStr(detail.getInnerSagyo3()));
                req.setInnerSagyo3Siyo(Util.getJsonDbl(detail.getInnerSagyo3Siyo()));
                req.setInnerSagyo4(Util.getJsonStr(detail.getInnerSagyo4()));
                req.setInnerSagyo4Siyo(Util.getJsonDbl(detail.getInnerSagyo4Siyo()));
                req.setLabelSu(Util.getJsonInt(detail.getLabelSu()));
                req.setNetWeight(Util.getJsonDbl(detail.getNetWeight()));
                req.setDanNaiyoRyo(Util.getJsonDbl(detail.getDanNaiyoRyo()));
                req.setDanTani(Util.getJsonStr(detail.getDanTani()));
                req.setDanNaisoYoki(Util.getJsonStr(detail.getDanNaisoYoki()));
                req.setDanHonsu(Util.getJsonInt(detail.getDanHonsu()));
                req.setDanGaisoYoki(Util.getJsonStr(detail.getDanGaisoYoki()));
                req.setDanGaisoKosu(Util.getJsonInt(detail.getDanGaisoKosu()));
                req.setBiko(Util.getJsonStr(detail.getBiko()));
                // リストに追加
                requestParam.getInnerList().add(req);
            }

            // 出庫指示明細リスト
            ArrayList<IssueTagInfo> detailInfo =
                    new SyukkoShijiDetailDao().getSyukkoShijiList(Application.dbHelper.getWritableDatabase(), mInvoiceNo);
            // 明細
            for (IssueTagInfo detail : detailInfo) {
                PackingDataRegistRequest.SyukkoDetailRequestDetail req = new PackingDataRegistRequest.SyukkoDetailRequestDetail();
                req.setRenban(detail.getRenban());
                req.setLineNo(detail.getLineNo());
                req.setCsNumber(detail.getCsNumber());
                req.setOverPackNo(detail.getOverPackNo());
                req.setKonpozumiFlg(detail.getKonpozumiFlag());
                // リストに追加
                requestParam.getSyukkoList().add(req);
            }

            // jsonに変換
            String json = gson.toJson(requestParam);

            // Http Post
            ExecuteResponse executeResponse = httpPostExecute(mURL, json, true);
            // エラー
            if(executeResponse.isError()) {
                // エラーcallback起動
                raiseOnError(executeResponse.getHttpStatusCode(), executeResponse.getErrorMsgId());
                return null;
            }
            // JSONを変換
            result = gson.fromJson(executeResponse.getResponseJson(), SimpleResponse.class);
            return result;
        } catch (final Exception e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return null;
        }
    }

    @Override
    protected void onPostExecute(SimpleResponse response) {
        // エラー終了の場合
        if (response == null) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(response);
    }
}
