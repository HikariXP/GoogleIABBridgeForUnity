// SKU就是谷歌的唯一识别符号。比如你后台设定一个商品的UID是bababoi_1，那么这个就是SKU
// 谷歌没有从商品类型层面去区分可消耗还是不可消耗，所以理论上来讲所有商品都可以被"消耗掉"，需要小心处理。
package com.prime31;

import android.util.Log;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: IABPlugin.aar:classes.jar:com/prime31/Inventory.class */
public class Inventory {
    protected static final String TAG = "CharSuiVersion";

    Map<String, ProductDetails> mProductDetailMap = new HashMap();

    Map<String, Purchase> mPurchaseMap = new HashMap();

    public JSONObject getPurchaseAsJson(Purchase purchase) {
        try {
            JSONObject jsonObj = new JSONObject(purchase.getOriginalJson());
            jsonObj.put("signature", purchase.getSignature());
            jsonObj.put("originalJson", purchase.getOriginalJson());
            if (IABConstants.DEBUG) {
                Log.w("Unity", "---------json: " + jsonObj.toString());
            }
            return jsonObj;
        } catch (JSONException var3) {
            Log.i(TAG, "Error creating JSON from purchase" + var3.getMessage());
            return new JSONObject();
        }
    }

    public JSONArray getAllProductDetailAsJson() {
        JSONArray json = new JSONArray();
        for (ProductDetails productDetail : this.mProductDetailMap.values())
        {
            json.put(productDetail.zza());
        }
        return json;
    }

    public JSONArray getAllProductAsJson() {
        try {
            JSONArray json = new JSONArray();

            Log.i("Bababoi", "All Products count is  " + mProductDetailMap.size());

            for (ProductDetails details : this.mProductDetailMap.values()) {
                Log.i("Debug", "ProductDetails toString: " + details.toString());


                Matcher matcher = Pattern.compile("(?<=parsedJson=)(.+?)(?=,\\sproductId=)").matcher(details.toString());
                Log.i("Prime31", "getAllProductAsJson Ori:" + details);
                if (matcher.find()) {
                    Log.i("Prime31", "getAllProductAsJson Marcher:" + matcher.group(0));
                    json.put(new JSONObject((String) Objects.requireNonNull(matcher.group(0))));
                }
            }
            return json;
        } catch (JSONException exception) {
            Log.i(TAG, "Error creating JSON from product" + exception.getMessage());
            return new JSONArray();
        }
    }

    public JSONArray getAllPurchasesAsJson() {
        JSONArray json = new JSONArray();
        for (Purchase p : this.mPurchaseMap.values()) {
            json.put(getPurchaseAsJson(p));
        }
        return json;
    }

    public String getAllSkusAndPurchasesAsJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("purchases", getAllPurchasesAsJson());
            json.put("skus", getAllProductDetailAsJson());
            return json.toString();
        } catch (JSONException var2) {
            Log.i(TAG, "Error creating JSON from skus" + var2.getMessage());
            return "{}";
        }
    }

    public ProductDetails getProductDetails(String sku) {
        return this.mProductDetailMap.get(sku);
    }

    public Purchase getPurchase(String sku) {
        return this.mPurchaseMap.get(sku);
    }

    public boolean hasPurchase(String sku) {
        return this.mPurchaseMap.containsKey(sku);
    }

    public boolean hasDetails(String sku) {
        return this.mProductDetailMap.containsKey(sku);
    }

    public boolean hasProductDetails(String sku) {
        return this.mProductDetailMap.containsKey(sku);
    }

    public void erasePurchase(String sku) {
        if (this.mPurchaseMap.containsKey(sku)) {
            this.mPurchaseMap.remove(sku);
        }
    }

    public void addProductDetails(ProductDetails d)
    {

        this.mProductDetailMap.put(d.getProductId(), d);
    }


    /* JADX INFO: Access modifiers changed from: package-private */
    public void addPurchase(Purchase p) {
        this.mPurchaseMap.put((String) p.getProducts().get(0), p);
    }
}
