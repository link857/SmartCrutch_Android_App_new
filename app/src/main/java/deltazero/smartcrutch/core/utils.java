package deltazero.smartcrutch.core;

import androidx.annotation.NonNull;

import java.util.TimerTask;

import deltazero.smartcrutch.ui.MainActivity;
import deltazero.smartcrutch.ui.MapActivity;

public class utils {

    public static class GetStatusTimerTask extends TimerTask {
        MainActivity mainActivity;
        String uuid;

        public GetStatusTimerTask(MainActivity mainActivity, @NonNull String uuid) {
            this.mainActivity = mainActivity;
            this.uuid = uuid;
        }

        @Override
        public void run() {
            API.getStatus(mainActivity, uuid);
        }
    }

    public static class GetLocTimerTask extends TimerTask {
        MapActivity mapActivity;
        String uuid;

        public GetLocTimerTask(MapActivity mapActivity, @NonNull String uuid) {
            this.mapActivity = mapActivity;
            this.uuid = uuid;
        }

        @Override
        public void run() {
            API.getLoc(mapActivity, uuid);
        }
    }


}
