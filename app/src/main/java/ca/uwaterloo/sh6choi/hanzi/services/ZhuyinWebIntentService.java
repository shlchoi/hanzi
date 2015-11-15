package ca.uwaterloo.sh6choi.hanzi.services;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.net.MalformedURLException;
import java.net.URL;

import ca.uwaterloo.sh6choi.hanzi.database.PinyinComponentDataSource;
import ca.uwaterloo.sh6choi.hanzi.model.PinyinComponent;

/**
 * Created by Samson on 2015-09-24.
 */
public class ZhuyinWebIntentService extends WebIntentService {

    private static final String TAG = ZhuyinWebIntentService.class.getCanonicalName();
    public static final String ACTION_SUCCESS = TAG + ".action.success";

    public ZhuyinWebIntentService() {
        super("ZhuyinWebIntentService");
    }

    @Override
    protected URL getUrl() throws MalformedURLException {
        return new URL("https://raw.githubusercontent.com/shlchoi/hanzi/master/zhuyin.json");
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, "Zhuyin retrieved");
        JsonArray array = new JsonParser().parse(response).getAsJsonArray();
        PinyinComponent[] pinyinComponents = new Gson().fromJson(array, PinyinComponent[].class);

        final PinyinComponentDataSource dataSource = new PinyinComponentDataSource(this);
        dataSource.open();
        for (int i = 0; i < pinyinComponents.length; i ++) {
            dataSource.update(pinyinComponents[i], null);
        }

        sendBroadcast(new Intent(ACTION_SUCCESS));
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, e.getMessage());
    }
}
