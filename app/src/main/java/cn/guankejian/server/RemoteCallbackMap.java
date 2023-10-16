package cn.guankejian.server;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;

import java.io.PrintWriter;


public class RemoteCallbackMap<K, V extends IInterface> {
    private static final String TAG = "RemoteCallbackList";

    ArrayMap<IBinder, Callback> mCallbacks
            = new ArrayMap<IBinder, Callback>();
    private ArrayMap<K,V> mActiveBroadcast;
    private int mBroadcastCount = -1;
    private boolean mKilled = false;
    private StringBuilder mRecentCallers;

    private final class Callback implements IBinder.DeathRecipient {
        final V mCallback;
        final K mKey;

        Callback(K key, V callback) {
            mKey = key;
            mCallback = callback;
        }

        public void binderDied() {
            synchronized (mCallbacks) {
                mCallbacks.remove(mCallback.asBinder());
            }
            onCallbackDied(mCallback);
        }
    }



    public boolean register(K key, V callback) {
        synchronized (mCallbacks) {
            if (mKilled) {
                return false;
            }
            // Flag unusual case that could be caused by a leak. b/36778087
            logExcessiveCallbacks();
            IBinder binder = callback.asBinder();
            try {
                Callback cb = new Callback(key, callback);
                unregister(key,callback);
                binder.linkToDeath(cb, 0);
                mCallbacks.put(binder, cb);
                return true;
            } catch (RemoteException e) {
                return false;
            }
        }
    }


    public boolean unregister(K key, V callback) {
        synchronized (mCallbacks) {
            Callback cb = mCallbacks.remove(callback.asBinder());
            if (cb != null) {
                cb.mCallback.asBinder().unlinkToDeath(cb, 0);
                return true;
            }
            return false;
        }
    }


    public void kill() {
        synchronized (mCallbacks) {
            for (int cbi=mCallbacks.size()-1; cbi>=0; cbi--) {
                Callback cb = mCallbacks.valueAt(cbi);
                cb.mCallback.asBinder().unlinkToDeath(cb, 0);
            }
            mCallbacks.clear();
            mKilled = true;
        }
    }


    public void onCallbackDied(V callback) {
    }
    

    public void onCallbackDied(K key, V callback) {
        onCallbackDied(callback);
    }

    ThreadLocal<K> mKey = new ThreadLocal<>();

    public int beginBroadcast(K key) {
        synchronized (mCallbacks) {

            if (mBroadcastCount > 0) {
                throw new IllegalStateException(
                        "beginBroadcast() called while already in a broadcast");
            }
            mKey.set(key);

            final int N = mBroadcastCount = mCallbacks.size();
            if (N == 0) {
                return 0;
            }
            ArrayMap<K,V> active = mActiveBroadcast;
            if (active == null || active.size() < N) {
                mActiveBroadcast = active = new ArrayMap<>(N);
            }
            for (int i=0; i<N; i++) {
                Callback callback = mCallbacks.valueAt(i);
                active.put(callback.mKey,callback.mCallback);
            }
            return N;
        }
    }

    public V findBroadcastItem(K key) {
        return mActiveBroadcast.get(key);
    }


    public void finishBroadcast(K key) {
        synchronized (mCallbacks) {
            if (mBroadcastCount < 0) {
                throw new IllegalStateException(
                        "finishBroadcast() called outside of a broadcast");
            }

            ArrayMap<K,V> active = mActiveBroadcast;
            if (active != null) {
                final int N = mBroadcastCount;
                for (int i=0; i<N; i++) {
                    Callback callback = mCallbacks.valueAt(i);
                    active.remove(callback.mKey);
                }
            }

            mBroadcastCount = -1;
        }
    }

    public int getRegisteredCallbackCount() {
        synchronized (mCallbacks) {
            if (mKilled) {
                return 0;
            }
            return mCallbacks.size();
        }
    }


    public V getRegisteredCallbackItem(int index) {
        synchronized (mCallbacks) {
            if (mKilled) {
                return null;
            }
            return mCallbacks.valueAt(index).mCallback;
        }
    }


    public void dump(PrintWriter pw, String prefix) {
        synchronized (mCallbacks) {
            pw.print(prefix); pw.print("callbacks: "); pw.println(mCallbacks.size());
            pw.print(prefix); pw.print("killed: "); pw.println(mKilled);
            pw.print(prefix); pw.print("broadcasts count: "); pw.println(mBroadcastCount);
        }
    }

    private void logExcessiveCallbacks() {
        final long size = mCallbacks.size();
        final long TOO_MANY = 3000;
        final long MAX_CHARS = 1000;
        if (size >= TOO_MANY) {
            if (size == TOO_MANY && mRecentCallers == null) {
                mRecentCallers = new StringBuilder();
            }
            if (mRecentCallers != null && mRecentCallers.length() < MAX_CHARS) {
                mRecentCallers.append(getCallers(5));

                final StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 5; i++) {
                    sb.append(getCaller(callStack, i)).append(" ");
                }

                mRecentCallers.append('\n');
                if (mRecentCallers.length() >= MAX_CHARS) {
                    Log.e(TAG, "More than "
                            + TOO_MANY + " remote callbacks registered. Recent callers:\n"
                            + mRecentCallers.toString());
                    mRecentCallers = null;
                }
            }
        }
    }


    private static String getCaller(StackTraceElement callStack[], int depth) {
        // callStack[4] is the caller of the method that called getCallers()
        if (4 + depth >= callStack.length) {
            return "<bottom of call stack>";
        }
        StackTraceElement caller = callStack[4 + depth];
        return caller.getClassName() + "." + caller.getMethodName() + ":" + caller.getLineNumber();
    }

    public static String getCallers(final int depth) {
        final StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(getCaller(callStack, i)).append(" ");
        }
        return sb.toString();
    }
}
