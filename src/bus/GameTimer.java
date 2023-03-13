package bus;

import java.io.Serializable;
import java.util.ArrayList;

public class GameTimer implements Serializable {

	private static final long serialVersionUID = 6235054743621828161L;
	private ArrayList<Long> timeList = new ArrayList<Long>();

	public void startStopTimer() {
		timeList.add(System.currentTimeMillis());
	}

	public void startTimer() {
		if (timeList.size() % 2 == 0) {
			timeList.add(System.currentTimeMillis());
		}

	}

	public void stopTimer() {
		if (timeList.size() % 2 != 0) {
			timeList.add(System.currentTimeMillis());
		}
	}

	public long getLastTimer() {
		if (timeList.size() == 0) {
			return 0;
		}
		return timeList.get(timeList.size() - 1);
	}

	public long getTime() {
		long total = 0;

		for (int i = 0; i < timeList.size(); i += 2) {
			if (i + 1 < timeList.size()) {
				total += (timeList.get(i + 1) - timeList.get(i));
			} else {
				total += (System.currentTimeMillis() - timeList.get(i));
			}

		}

		return total;
	}

	public static String timeToString(long time) {
		String rTime = "";
		if (time <= 0) {
			return "00:00";
		} else {
			int timeSec = (int) (time / 1000);
			int min = timeSec / 60;
			int sec = timeSec % 60;

			rTime += ((min == 0) ? ("0:") : (min + ":"));
			rTime += (sec < 10) ? ((sec == 0) ? ("00") : ("0" + sec)) : (sec + "");
		}

		return rTime;
	}

	public boolean isTiming() {
		if (timeList.isEmpty()) {
			return false;
		}
		if (timeList.size() % 2 == 0) {
			return false;
		}
		return true;
	}

}
