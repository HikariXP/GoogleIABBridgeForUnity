package com.prime31;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryProductDetailsResult;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: IABPlugin.aar:classes.jar:com/prime31/GoogleIABPlugin.class */
public class GoogleIABPlugin extends GoogleIABPluginBase {
    private static String BILLING_NOT_RUNNING_ERROR = "The billing service is not running or billing is not supported. Aborting.";
    Inventory inventory;
    BillingClient billingClient;
    boolean serviceDisconnected;
    int queryInventoryResponseCount;
    int queryPurchaseResponseCount;
    int queryProductResponseCount;
    private List<Purchase> _purchases = new ArrayList();
    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() { // from class: com.prime31.GoogleIABPlugin.1
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            Log.i("Prime31", "------------------- onPurchasesUpdated. response code: " + billingResult.getResponseCode());
            if (billingResult.getResponseCode() == 0 && purchases != null) {
                for (Purchase purchase : purchases) {
                    GoogleIABPlugin.this.inventory.addPurchase(purchase);
                    Log.i("Prime31", "---------- total purchases in inventory: " + GoogleIABPlugin.this.inventory.mPurchaseMap.size());
                    GoogleIABPlugin.this.UnitySendMessage("purchaseSucceeded", GoogleIABPlugin.this.inventory.getPurchaseAsJson(purchase).toString());
                }
                return;
            }
            try {
                JSONObject json = new JSONObject();
                json.put("result", billingResult.getDebugMessage());
                json.put("response", billingResult.getResponseCode());
                GoogleIABPlugin.this.UnitySendMessage("purchaseFailed", json.toString());
            } catch (JSONException var5) {
                Log.i("Prime31", "failed to create JSON packet: " + var5.getMessage());
            }
        }
    };

    private ProductDetailsResponseListener pdrListener = new ProductDetailsResponseListener() {
        @Override
        public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull QueryProductDetailsResult queryProductDetailsResult) {
            if (billingResult.getResponseCode() == 0) {
                List<ProductDetails> productDetailsList = queryProductDetailsResult.getProductDetailsList();
                Log.i("Prime31", "------------------- onSkuDetailsResponse. total sku details: " + productDetailsList.size());
                for (ProductDetails skuDetails : productDetailsList) {
                    GoogleIABPlugin.this.inventory.addProductDetails(skuDetails);
                }
            } else {
                Log.i("Prime31", "queryInventoryFailed failed: " + billingResult.getDebugMessage());
                GoogleIABPlugin.this.UnitySendMessage("queryInventoryFailed", String.valueOf(billingResult.getResponseCode()));
            }
            GoogleIABPlugin.this.queryInventoryOperationCompleted();
        }
    };

    @Deprecated
    private PurchasesResponseListener purchaseListener = new PurchasesResponseListener() { // from class: com.prime31.GoogleIABPlugin.3
        public void onQueryPurchasesResponse(BillingResult billingResult, List<Purchase> list) {
            if (billingResult.getResponseCode() == 0) {
                Log.i("Prime31", "------------------- onQueryPurchasesResponse. total purchases: " + list.size());
                for (Purchase purchase : list) {
                    GoogleIABPlugin.this.inventory.addPurchase(purchase);
                }
            } else {
                Log.i("Prime31", "onQueryPurchasesResponse failed: " + billingResult.getDebugMessage());
                GoogleIABPlugin.this.UnitySendMessage("queryInventoryFailed", String.valueOf(billingResult.getResponseCode()));
            }
            GoogleIABPlugin.this.queryInventoryOperationCompleted();
        }
    };

    private final PurchasesResponseListener queryPurchaseListener = (billingResult, list) -> {
        if (billingResult.getResponseCode() == 0) {
            Log.i("Prime31", "------------------- onQueryPurchasesResponse. total purchases: " + list.size());
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Purchase purchase = (Purchase) it.next();
                this.inventory.addPurchase(purchase);
            }
        } else {
            Log.i("Prime31", "onQueryPurchasesResponse failed: " + billingResult.getDebugMessage());
            UnitySendMessage("queryPurchaseFailed", String.valueOf(billingResult.getResponseCode()));
        }
        this.queryPurchaseResponseCount--;
        if (this.queryPurchaseResponseCount == 0) {
            String purchaseJson = this.inventory.getAllPurchasesAsJson().toString();
            Log.i("Prime31", "queryPurchaseSucceeded: " + purchaseJson);
            UnitySendMessage("queryPurchaseSucceeded", purchaseJson);
        }
    };
    private final ProductDetailsResponseListener queryProductListener = (billingResult, productDetailsResult) -> {
        if (billingResult.getResponseCode() == 0)
        {
            // 8.0.0
            List<ProductDetails> productDetailsList = productDetailsResult.getProductDetailsList();

            for (ProductDetails productDetails : productDetailsList) {
                Log.i("Bababoi", "ProductDetails toString: " + productDetails.toString());
                this.inventory.addProductDetails(productDetails);
            }
        } else {
            Log.i("Bababoi", "queryProductFailed failed: " + billingResult.getDebugMessage());
            UnitySendMessage("queryProductFailed", String.valueOf(billingResult.getResponseCode()));
        }

        this.queryProductResponseCount--;

        if (this.queryProductResponseCount == 0) {
            String productJson = this.inventory.getAllProductAsJson().toString();
            UnitySendMessage("queryProductSucceeded", productJson);
        }
    };

    @Deprecated
    private void queryInventoryOperationCompleted() {
        this.queryInventoryResponseCount--;
        Log.i("Prime31", "------------------ queryInventoryResponseCount: " + this.queryInventoryResponseCount);
        if (this.queryInventoryResponseCount == 0) {
            UnitySendMessage("queryInventorySucceeded", this.inventory.getAllSkusAndPurchasesAsJson());
        }
    }

    public void enableLogging(boolean shouldEnable) {
        IABConstants.DEBUG = shouldEnable;
    }

    public void init(String publicKey) {
        IABConstants.logEntering(getClass().getSimpleName(), "init", publicKey);
        // 6.0.0
//        this.billingClient = BillingClient.newBuilder(getActivity()).setListener(this.purchasesUpdatedListener).enablePendingPurchases().build();

        // 8.0.0(这个内容在7.0.0被修改，EnablePendingPurchases需要有一个传参，这是官方的实现)
        this.billingClient = BillingClient.newBuilder(getActivity())  // 改用 getContext()
                .setListener(this.purchasesUpdatedListener).enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())  // 改用 setPurchasesUpdatedListener
                .build();

        connectBillingService();
        this.inventory = new Inventory();
    }

    private void connectBillingService() {
        this.billingClient.startConnection(new BillingClientStateListener() { // from class: com.prime31.GoogleIABPlugin.4
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == 0) {
                    GoogleIABPlugin.this.serviceDisconnected = false;
                    GoogleIABPlugin.this.UnitySendMessage("billingSupported", "");
                } else {
                    GoogleIABPlugin.this.UnitySendMessage("billingNotSupported", String.valueOf(billingResult.getResponseCode()));
                }
                Log.i("Prime31", "------------------- onBillingSetupFinished. response code: " + billingResult.getResponseCode());
            }

            public void onBillingServiceDisconnected() {
                Log.i("Prime31", "------------------- onBillingServiceDisconnected");
                GoogleIABPlugin.this.serviceDisconnected = true;
            }
        });
    }

    public void unbindService() {
        IABConstants.logEntering(getClass().getSimpleName(), "unbindService");
        this.billingClient.endConnection();
        this.serviceDisconnected = true;
    }

    public boolean areSubscriptionsSupported() {
        IABConstants.logEntering(getClass().getSimpleName(), "areSubscriptionsSupported");
        if (!this.serviceDisconnected) {
            return this.billingClient.isFeatureSupported("subscriptions").getResponseCode() == 0;
        }
        Log.i("Prime31", BILLING_NOT_RUNNING_ERROR);
        return false;
    }

    @Deprecated
    public void queryInventory(Object[] skus) {
        IABConstants.logEntering(getClass().getSimpleName(), "queryInventory", skus);
        IABConstants.logDebug("in queryInventory with Object[] parameter. Converting to String[] now.");
        queryInventory((String[]) Arrays.asList(skus).toArray(new String[skus.length]));
    }

    @Deprecated
    public void queryInventory(String[] skus) {
        IABConstants.logEntering(getClass().getSimpleName(), "queryInventory", (Object[]) skus);
        if (this.serviceDisconnected) {
            Log.i("Prime31", BILLING_NOT_RUNNING_ERROR);
            return;
        }
        this.queryInventoryResponseCount = 4;
        List<String> skuList = Arrays.asList(skus);
//        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
//        params.setSkusList(skuList).setType("subs");
//        this.billingClient.querySkuDetailsAsync(params.build(), this.skuListener);
//        SkuDetailsParams.Builder params2 = SkuDetailsParams.newBuilder();
//        params2.setSkusList(skuList).setType("inapp");
//        this.billingClient.querySkuDetailsAsync(params2.build(), this.skuListener);



        // 8.0.0移除了querySkuDetailsAsync方法，需要使用queryProductDetailsAsync方法取缔
        List<QueryProductDetailsParams.Product> products = new ArrayList<>(256);
        for(String productName : skuList)
        {
            QueryProductDetailsParams.Product currentProduct = QueryProductDetailsParams.Product.newBuilder().setProductId(productName).setProductType("subs").build();
            products.add(currentProduct);
        }

        QueryProductDetailsParams.Builder newParams1 = QueryProductDetailsParams.newBuilder();
        newParams1.setProductList(products);
        this.billingClient.queryProductDetailsAsync(newParams1.build(),this.pdrListener);

        products.clear();
        for(String productName : skuList)
        {
            QueryProductDetailsParams.Product currentProduct = QueryProductDetailsParams.Product.newBuilder().setProductId(productName).setProductType("inapp").build();
            products.add(currentProduct);
        }

        QueryProductDetailsParams.Builder newParams2 = QueryProductDetailsParams.newBuilder();
        newParams2.setProductList(products);
        this.billingClient.queryProductDetailsAsync(newParams2.build(),this.pdrListener);

        QueryPurchasesParams qpp1 = QueryPurchasesParams.newBuilder().setProductType("subs").build();
        QueryPurchasesParams qpp2 = QueryPurchasesParams.newBuilder().setProductType("inapp").build();

        // 旧代码
//        this.billingClient.queryPurchasesAsync("subs", this.purchaseListener);
//        this.billingClient.queryPurchasesAsync("inapp", this.purchaseListener);

        this.billingClient.queryPurchasesAsync(qpp1, this.purchaseListener);
        this.billingClient.queryPurchasesAsync(qpp2, this.purchaseListener);
    }

    public void queryPurchase() {
        IABConstants.logEntering(getClass().getSimpleName(), "queryPurchase");
        if (this.serviceDisconnected) {
            Log.i("Prime31", BILLING_NOT_RUNNING_ERROR);
            return;
        }
        this.queryPurchaseResponseCount = 2;
        this.billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType("subs").build(), this.queryPurchaseListener);
        this.billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType("inapp").build(), this.queryPurchaseListener);
    }

    public void queryProduct(Object[] products) {
        IABConstants.logDebug("in queryProduct with Object[] parameter. ");
        List<String> skuList = Arrays.asList((String[]) products.clone());
        IABConstants.logEntering(getClass().getSimpleName(), "queryProduct", skuList);
        if (this.serviceDisconnected) {
            Log.i("Prime31", BILLING_NOT_RUNNING_ERROR);
            return;
        }
        this.queryProductResponseCount = 2;
        queryProductDetailsAsync(skuList, "subs");
        queryProductDetailsAsync(skuList, "inapp");
    }

    private void queryProductDetailsAsync(List<String> products, String type) {
        QueryProductDetailsParams.Product.Builder builder = QueryProductDetailsParams.Product.newBuilder();
        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
        for (String product : products) {
            productList.add(builder.setProductId(product).setProductType(type).build());
        }
        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder().setProductList(productList).build();
        this.billingClient.queryProductDetailsAsync(params, this.queryProductListener);
    }

    public String getPurchaseHistory() {
        return this.inventory.getAllPurchasesAsJson().toString();
    }

    @Deprecated
    public void purchaseProduct(String sku) {
        IABConstants.logEntering(getClass().getSimpleName(), "purchaseProduct", sku);
        if (this.serviceDisconnected) {
            Log.i("Prime31", BILLING_NOT_RUNNING_ERROR);
        } else if (!sku.startsWith("android.test") && !this.inventory.hasDetails(sku)) {
            try {
                JSONObject json = new JSONObject();
                json.put("result", "developer error. Sku does not exist in the Inventory");
                json.put("response", -666);
                UnitySendMessage("purchaseFailed", json.toString());
            } catch (JSONException var4) {
                Log.i("Prime31", "failed to create JSON packet: " + var4.getMessage());
            }
        } else {
//            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(this.inventory.getSkuDetails(sku)).build();
            BillingFlowParams.ProductDetailsParams pdp = BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(this.inventory.getProductDetails(sku)).build();
            List<BillingFlowParams.ProductDetailsParams> selected = new ArrayList<BillingFlowParams.ProductDetailsParams>();
            selected.add(pdp);
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(selected).build();
            int responseCode = this.billingClient.launchBillingFlow(getActivity(), billingFlowParams).getResponseCode();
            Log.i("Prime31", "--------------- purchaseProduct Activity launch responseCode: " + responseCode);
        }
    }

    public void purchaseOfferProduct(String product, int offerSelectIndex) {
        IABConstants.logEntering(getClass().getSimpleName(), "purchaseOfferProduct", product);
        if (this.serviceDisconnected) {
            Log.i("Prime31", BILLING_NOT_RUNNING_ERROR);
        } else if (!product.startsWith("android.test") && !this.inventory.hasProductDetails(product)) {
            try {
                JSONObject json = new JSONObject();
                json.put("result", "developer error. Sku does not exist in the Inventory");
                json.put("response", -666);
                UnitySendMessage("purchaseFailed", json.toString());
            } catch (JSONException var4) {
                Log.i("Prime31", "failed to create JSON packet: " + var4.getMessage());
            }
        } else {
            ProductDetails details = this.inventory.getProductDetails(product);
            ArrayList<BillingFlowParams.ProductDetailsParams> params = new ArrayList<>();
            BillingFlowParams.ProductDetailsParams.Builder builder = BillingFlowParams.ProductDetailsParams.newBuilder();
            builder.setProductDetails(details);
            List<ProductDetails.SubscriptionOfferDetails> subs = details.getSubscriptionOfferDetails();
            if (details.getProductType().equals("subs")) {
                if (subs == null || subs.size() < offerSelectIndex) {
                    throw new IllegalArgumentException("Prime31 SubscriptionOfferDetails is null or offerSelectIndex error.");
                }
                builder.setOfferToken(((ProductDetails.SubscriptionOfferDetails) details.getSubscriptionOfferDetails().get(offerSelectIndex)).getOfferToken());
            }
            params.add(builder.build());
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(params).build();
            int responseCode = this.billingClient.launchBillingFlow(getActivity(), billingFlowParams).getResponseCode();
            Log.i("Prime31", "--------------- purchaseProduct Activity launch responseCode: " + responseCode);
        }
    }

    // 带着Pero的订单进行谷歌购买并填入订单
    public void purchaseOfferProduct(String obfuscatedAccountId, String obfuscatedProfileId, String product, int offerSelectIndex)
    {
        IABConstants.logEntering(getClass().getSimpleName(), "purchaseOfferProduct", product);

        // 如果订单为空则直接跳出
        if(obfuscatedProfileId == null)
        {
            Log.i("Prime31", "Order Input is null");
            return;
        }

        //
        if (this.serviceDisconnected) {
            Log.i("Prime31", BILLING_NOT_RUNNING_ERROR);
        } else if (!product.startsWith("android.test") && !this.inventory.hasProductDetails(product)) {
            try {
                JSONObject json = new JSONObject();
                json.put("result", "developer error. Sku does not exist in the Inventory");
                json.put("response", -666);
                UnitySendMessage("purchaseFailed", json.toString());
            } catch (JSONException var4) {
                Log.i("Prime31", "failed to create JSON packet: " + var4.getMessage());
            }
        } else {
            ProductDetails details = this.inventory.getProductDetails(product);
            ArrayList<BillingFlowParams.ProductDetailsParams> params = new ArrayList<>();
            BillingFlowParams.ProductDetailsParams.Builder builder = BillingFlowParams.ProductDetailsParams.newBuilder();
            builder.setProductDetails(details);
            List<ProductDetails.SubscriptionOfferDetails> subs = details.getSubscriptionOfferDetails();
            if (details.getProductType().equals("subs")) {
                if (subs == null || subs.size() < offerSelectIndex) {
                    throw new IllegalArgumentException("Prime31 SubscriptionOfferDetails is null or offerSelectIndex error.");
                }
                builder.setOfferToken(details.getSubscriptionOfferDetails().get(offerSelectIndex).getOfferToken());
            }
            params.add(builder.build());

            // 插入信息后发起购买
            BillingFlowParams.Builder billingFlowParamsFixed = BillingFlowParams.newBuilder();
            billingFlowParamsFixed.setProductDetailsParamsList(params);
            billingFlowParamsFixed.setObfuscatedProfileId(obfuscatedProfileId);
            billingFlowParamsFixed.setObfuscatedAccountId(obfuscatedAccountId);
            BillingFlowParams finalBillingFlowParams = billingFlowParamsFixed.build();

            Log.i("Prime31", MessageFormat.format("[GoogleIABPlugin]:obfuscatedProfileId:{0}}",obfuscatedProfileId));
            // 并且获取返回码
            int responseCode = this.billingClient.launchBillingFlow(getActivity(), finalBillingFlowParams).getResponseCode();
            Log.i("Prime31", "--------------- purchaseProduct Activity launch responseCode: " + responseCode);
        }
    }

    public void consumeProduct(final String sku) {
        IABConstants.logEntering(getClass().getSimpleName(), "consumeProduct", sku);
        if (this.serviceDisconnected) {
            Log.i("Prime31", BILLING_NOT_RUNNING_ERROR);
        } else if (!this.inventory.hasPurchase(sku)) {
            UnitySendMessage("consumePurchaseFailed", "sku does not have a purchase in the inventory");
        } else {
            final Purchase purchase = this.inventory.getPurchase(sku);
            ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
            ConsumeResponseListener listener = new ConsumeResponseListener() { // from class: com.prime31.GoogleIABPlugin.5
                public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                    if (billingResult.getResponseCode() == 0) {
                        GoogleIABPlugin.this.inventory.erasePurchase(sku);
                        GoogleIABPlugin.this.UnitySendMessage("consumePurchaseSucceeded", GoogleIABPlugin.this.inventory.getPurchaseAsJson(purchase).toString());
                        return;
                    }
                    String res = ((String) purchase.getProducts().get(0)) + ": " + billingResult.getDebugMessage();
                    GoogleIABPlugin.this.UnitySendMessage("consumePurchaseFailed", res);
                }
            };
            this.billingClient.consumeAsync(consumeParams, listener);
        }
    }

    public void acknowledgePurchase(final String sku) {
        IABConstants.logEntering(getClass().getSimpleName(), "acknowledgePurchase", sku);
        if (this.serviceDisconnected) {
            Log.i("Prime31", BILLING_NOT_RUNNING_ERROR);
        } else if (!this.inventory.hasPurchase(sku)) {
            UnitySendMessage("consumePurchaseFailed", "sku does not have a purchase in the inventory");
        } else {
            final Purchase purchase = this.inventory.getPurchase(sku);
            if (purchase.getPurchaseState() == 1 && !purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                this.billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() { // from class: com.prime31.GoogleIABPlugin.6
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                        if (billingResult.getResponseCode() == 0) {
                            GoogleIABPlugin.this.inventory.erasePurchase(sku);
                            GoogleIABPlugin.this.UnitySendMessage("acknowledgePurchaseSucceeded", GoogleIABPlugin.this.inventory.getPurchaseAsJson(purchase).toString());
                            return;
                        }
                        String res = ((String) purchase.getProducts().get(0)) + ": " + billingResult.getDebugMessage();
                        GoogleIABPlugin.this.UnitySendMessage("acknowledgePurchaseFailed", res);
                    }
                });
            }
        }
    }
}
