package deltazero.smartcrutch.core;

import java.util.TimerTask;

import deltazero.smartcrutch.ui.MainActivity;
import deltazero.smartcrutch.ui.MapActivity;

public class utils {

    public static class GetStatusTimerTask extends TimerTask {
        MainActivity mainActivity;

        public GetStatusTimerTask(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void run() {
            mainActivity.api.get_status(mainActivity);
        }
    }

    public static class GetLocTimerTask extends TimerTask {
        MapActivity mapActivity;

        public GetLocTimerTask(MapActivity mapActivity) {
            this.mapActivity = mapActivity;
        }

        @Override
        public void run() {
            mapActivity.api.getLoc(mapActivity);
        }
    }


}
