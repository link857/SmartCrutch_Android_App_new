package deltazero.smartcrutch.core;

import java.util.TimerTask;

import deltazero.smartcrutch.ui.MainActivity;

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

}
