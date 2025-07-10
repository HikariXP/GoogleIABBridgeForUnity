package com.android.vending.billing;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: IABPlugin.aar:classes.jar:com/android/vending/billing/IInAppBillingService.class */
public interface IInAppBillingService extends IInterface {
    int isBillingSupported(int i, String str, String str2) throws RemoteException;

    Bundle getSkuDetails(int i, String str, String str2, Bundle bundle) throws RemoteException;

    Bundle getBuyIntent(int i, String str, String str2, String str3, String str4) throws RemoteException;

    Bundle getPurchases(int i, String str, String str2, String str3) throws RemoteException;

    int consumePurchase(int i, String str, String str2) throws RemoteException;

    int stub(int i, String str, String str2) throws RemoteException;

    Bundle getBuyIntentToReplaceSkus(int i, String str, List<String> list, String str2, String str3, String str4) throws RemoteException;

    Bundle getBuyIntentExtraParams(int i, String str, String str2, String str3, String str4, Bundle bundle) throws RemoteException;

    Bundle getPurchaseHistory(int i, String str, String str2, String str3, Bundle bundle) throws RemoteException;

    int isBillingSupportedExtraParams(int i, String str, String str2, Bundle bundle) throws RemoteException;

    /* loaded from: IABPlugin.aar:classes.jar:com/android/vending/billing/IInAppBillingService$Default.class */
    public static class Default implements IInAppBillingService {
        @Override // com.android.vending.billing.IInAppBillingService
        public int isBillingSupported(int apiVersion, String packageName, String type) throws RemoteException {
            return 0;
        }

        @Override // com.android.vending.billing.IInAppBillingService
        public Bundle getSkuDetails(int apiVersion, String packageName, String type, Bundle skusBundle) throws RemoteException {
            return null;
        }

        @Override // com.android.vending.billing.IInAppBillingService
        public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type, String developerPayload) throws RemoteException {
            return null;
        }

        @Override // com.android.vending.billing.IInAppBillingService
        public Bundle getPurchases(int apiVersion, String packageName, String type, String continuationToken) throws RemoteException {
            return null;
        }

        @Override // com.android.vending.billing.IInAppBillingService
        public int consumePurchase(int apiVersion, String packageName, String purchaseToken) throws RemoteException {
            return 0;
        }

        @Override // com.android.vending.billing.IInAppBillingService
        public int stub(int apiVersion, String packageName, String type) throws RemoteException {
            return 0;
        }

        @Override // com.android.vending.billing.IInAppBillingService
        public Bundle getBuyIntentToReplaceSkus(int apiVersion, String packageName, List<String> oldSkus, String newSku, String type, String developerPayload) throws RemoteException {
            return null;
        }

        @Override // com.android.vending.billing.IInAppBillingService
        public Bundle getBuyIntentExtraParams(int apiVersion, String packageName, String sku, String type, String developerPayload, Bundle extraParams) throws RemoteException {
            return null;
        }

        @Override // com.android.vending.billing.IInAppBillingService
        public Bundle getPurchaseHistory(int apiVersion, String packageName, String type, String continuationToken, Bundle extraParams) throws RemoteException {
            return null;
        }

        @Override // com.android.vending.billing.IInAppBillingService
        public int isBillingSupportedExtraParams(int apiVersion, String packageName, String type, Bundle extraParams) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: IABPlugin.aar:classes.jar:com/android/vending/billing/IInAppBillingService$Stub.class */
    public static abstract class Stub extends Binder implements IInAppBillingService {
        private static final String DESCRIPTOR = "com.android.vending.billing.IInAppBillingService";
        static final int TRANSACTION_isBillingSupported = 1;
        static final int TRANSACTION_getSkuDetails = 2;
        static final int TRANSACTION_getBuyIntent = 3;
        static final int TRANSACTION_getPurchases = 4;
        static final int TRANSACTION_consumePurchase = 5;
        static final int TRANSACTION_stub = 6;
        static final int TRANSACTION_getBuyIntentToReplaceSkus = 7;
        static final int TRANSACTION_getBuyIntentExtraParams = 8;
        static final int TRANSACTION_getPurchaseHistory = 9;
        static final int TRANSACTION_isBillingSupportedExtraParams = 10;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInAppBillingService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IInAppBillingService)) {
                return (IInAppBillingService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bundle _arg3;
            Bundle _arg4;
            Bundle _arg5;
            Bundle bundle1;
            switch (code) {
                case TRANSACTION_isBillingSupported /* 1 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String str1 = data.readString();
                    int i = isBillingSupported(_arg0, _arg1, str1);
                    reply.writeNoException();
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_getSkuDetails /* 2 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    String _arg12 = data.readString();
                    String str12 = data.readString();
                    if (0 != data.readInt()) {
                        bundle1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        bundle1 = null;
                    }
                    Bundle bundle3 = getSkuDetails(_arg02, _arg12, str12, bundle1);
                    reply.writeNoException();
                    if (bundle3 != null) {
                        reply.writeInt(TRANSACTION_isBillingSupported);
                        bundle3.writeToParcel(reply, TRANSACTION_isBillingSupported);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case TRANSACTION_getBuyIntent /* 3 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    String _arg13 = data.readString();
                    String str13 = data.readString();
                    String str3 = data.readString();
                    String str5 = data.readString();
                    Bundle bundle5 = getBuyIntent(_arg03, _arg13, str13, str3, str5);
                    reply.writeNoException();
                    if (bundle5 != null) {
                        reply.writeInt(TRANSACTION_isBillingSupported);
                        bundle5.writeToParcel(reply, TRANSACTION_isBillingSupported);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case TRANSACTION_getPurchases /* 4 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    String _arg14 = data.readString();
                    String str14 = data.readString();
                    String str32 = data.readString();
                    Bundle bundle2 = getPurchases(_arg04, _arg14, str14, str32);
                    reply.writeNoException();
                    if (bundle2 != null) {
                        reply.writeInt(TRANSACTION_isBillingSupported);
                        bundle2.writeToParcel(reply, TRANSACTION_isBillingSupported);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case TRANSACTION_consumePurchase /* 5 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    String _arg15 = data.readString();
                    String str15 = data.readString();
                    int _result = consumePurchase(_arg05, _arg15, str15);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case TRANSACTION_stub /* 6 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    String _arg16 = data.readString();
                    String str16 = data.readString();
                    int _result2 = stub(_arg06, _arg16, str16);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case TRANSACTION_getBuyIntentToReplaceSkus /* 7 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    String _arg17 = data.readString();
                    List<String> list = data.createStringArrayList();
                    String str2 = data.readString();
                    String str4 = data.readString();
                    String str6 = data.readString();
                    Bundle bundle6 = getBuyIntentToReplaceSkus(_arg07, _arg17, list, str2, str4, str6);
                    reply.writeNoException();
                    if (bundle6 != null) {
                        reply.writeInt(TRANSACTION_isBillingSupported);
                        bundle6.writeToParcel(reply, TRANSACTION_isBillingSupported);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case TRANSACTION_getBuyIntentExtraParams /* 8 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    String _arg18 = data.readString();
                    String _arg2 = data.readString();
                    String str22 = data.readString();
                    String str42 = data.readString();
                    if (0 != data.readInt()) {
                        _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    Bundle bundle62 = getBuyIntentExtraParams(_arg08, _arg18, _arg2, str22, str42, _arg5);
                    reply.writeNoException();
                    if (bundle62 != null) {
                        reply.writeInt(TRANSACTION_isBillingSupported);
                        bundle62.writeToParcel(reply, TRANSACTION_isBillingSupported);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case TRANSACTION_getPurchaseHistory /* 9 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    String _arg19 = data.readString();
                    String _arg22 = data.readString();
                    String str23 = data.readString();
                    if (0 != data.readInt()) {
                        _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    Bundle bundle4 = getPurchaseHistory(_arg09, _arg19, _arg22, str23, _arg4);
                    reply.writeNoException();
                    if (bundle4 != null) {
                        reply.writeInt(TRANSACTION_isBillingSupported);
                        bundle4.writeToParcel(reply, TRANSACTION_isBillingSupported);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case TRANSACTION_isBillingSupportedExtraParams /* 10 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    String _arg110 = data.readString();
                    String _arg23 = data.readString();
                    if (0 != data.readInt()) {
                        _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    int j = isBillingSupportedExtraParams(_arg010, _arg110, _arg23, _arg3);
                    reply.writeNoException();
                    reply.writeInt(j);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: IABPlugin.aar:classes.jar:com/android/vending/billing/IInAppBillingService$Stub$Proxy.class */
        public static class Proxy implements IInAppBillingService {
            private IBinder mRemote;
            public static IInAppBillingService sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.android.vending.billing.IInAppBillingService
            public int isBillingSupported(int apiVersion, String packageName, String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_isBillingSupported, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int isBillingSupported = Stub.getDefaultImpl().isBillingSupported(apiVersion, packageName, type);
                        _reply.recycle();
                        _data.recycle();
                        return isBillingSupported;
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.vending.billing.IInAppBillingService
            public Bundle getSkuDetails(int apiVersion, String packageName, String type, Bundle skusBundle) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    if (skusBundle != null) {
                        _data.writeInt(Stub.TRANSACTION_isBillingSupported);
                        skusBundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getSkuDetails, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Bundle skuDetails = Stub.getDefaultImpl().getSkuDetails(apiVersion, packageName, type, skusBundle);
                        _reply.recycle();
                        _data.recycle();
                        return skuDetails;
                    }
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.vending.billing.IInAppBillingService
            public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type, String developerPayload) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(sku);
                    _data.writeString(type);
                    _data.writeString(developerPayload);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getBuyIntent, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Bundle buyIntent = Stub.getDefaultImpl().getBuyIntent(apiVersion, packageName, sku, type, developerPayload);
                        _reply.recycle();
                        _data.recycle();
                        return buyIntent;
                    }
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.vending.billing.IInAppBillingService
            public Bundle getPurchases(int apiVersion, String packageName, String type, String continuationToken) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    _data.writeString(continuationToken);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getPurchases, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Bundle purchases = Stub.getDefaultImpl().getPurchases(apiVersion, packageName, type, continuationToken);
                        _reply.recycle();
                        _data.recycle();
                        return purchases;
                    }
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.vending.billing.IInAppBillingService
            public int consumePurchase(int apiVersion, String packageName, String purchaseToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(purchaseToken);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_consumePurchase, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int consumePurchase = Stub.getDefaultImpl().consumePurchase(apiVersion, packageName, purchaseToken);
                        _reply.recycle();
                        _data.recycle();
                        return consumePurchase;
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.vending.billing.IInAppBillingService
            public int stub(int apiVersion, String packageName, String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_stub, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int stub = Stub.getDefaultImpl().stub(apiVersion, packageName, type);
                        _reply.recycle();
                        _data.recycle();
                        return stub;
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.vending.billing.IInAppBillingService
            public Bundle getBuyIntentToReplaceSkus(int apiVersion, String packageName, List<String> oldSkus, String newSku, String type, String developerPayload) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeStringList(oldSkus);
                    _data.writeString(newSku);
                    _data.writeString(type);
                    _data.writeString(developerPayload);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getBuyIntentToReplaceSkus, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Bundle buyIntentToReplaceSkus = Stub.getDefaultImpl().getBuyIntentToReplaceSkus(apiVersion, packageName, oldSkus, newSku, type, developerPayload);
                        _reply.recycle();
                        _data.recycle();
                        return buyIntentToReplaceSkus;
                    }
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.vending.billing.IInAppBillingService
            public Bundle getBuyIntentExtraParams(int apiVersion, String packageName, String sku, String type, String developerPayload, Bundle extraParams) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(sku);
                    _data.writeString(type);
                    _data.writeString(developerPayload);
                    if (extraParams != null) {
                        _data.writeInt(Stub.TRANSACTION_isBillingSupported);
                        extraParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getBuyIntentExtraParams, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Bundle buyIntentExtraParams = Stub.getDefaultImpl().getBuyIntentExtraParams(apiVersion, packageName, sku, type, developerPayload, extraParams);
                        _reply.recycle();
                        _data.recycle();
                        return buyIntentExtraParams;
                    }
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.vending.billing.IInAppBillingService
            public Bundle getPurchaseHistory(int apiVersion, String packageName, String type, String continuationToken, Bundle extraParams) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    _data.writeString(continuationToken);
                    if (extraParams != null) {
                        _data.writeInt(Stub.TRANSACTION_isBillingSupported);
                        extraParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getPurchaseHistory, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Bundle purchaseHistory = Stub.getDefaultImpl().getPurchaseHistory(apiVersion, packageName, type, continuationToken, extraParams);
                        _reply.recycle();
                        _data.recycle();
                        return purchaseHistory;
                    }
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.vending.billing.IInAppBillingService
            public int isBillingSupportedExtraParams(int apiVersion, String packageName, String type, Bundle extraParams) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    if (extraParams != null) {
                        _data.writeInt(Stub.TRANSACTION_isBillingSupported);
                        extraParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_isBillingSupportedExtraParams, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int isBillingSupportedExtraParams = Stub.getDefaultImpl().isBillingSupportedExtraParams(apiVersion, packageName, type, extraParams);
                        _reply.recycle();
                        _data.recycle();
                        return isBillingSupportedExtraParams;
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }
        }

        public static boolean setDefaultImpl(IInAppBillingService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IInAppBillingService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
