package hr.ferit.mdudjak.healthdiary;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by Mario on 7.6.2017..
 */

public abstract class WakeReminderIntentService extends IntentService {
    abstract void doReminderWork(Intent intent);

    public static final String LOCK_NAME_STATIC = "hr.ferit.mdudjak.healthdiary";
    private static PowerManager.WakeLock lockStatic = null;

    public static void acquireStaticLock(Context context) {
        getLock(context).acquire();
    }

    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic == null) {
            PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }
        return (lockStatic);
    }

    public WakeReminderIntentService(String name) {
        super(name);
    }

    @Override
    final protected void onHandleIntent(Intent intent) {
        try {
            doReminderWork(intent);
        } finally {
            getLock(this).release();
        }

    }
}