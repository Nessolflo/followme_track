package drive.tracker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class ReceiverService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        final Intent i = new Intent(context, ServiceLocation.class);
        if (null != ServiceLocation.INSTANCE)
            context.stopService(i);
        context.startService(i);
    }
}
